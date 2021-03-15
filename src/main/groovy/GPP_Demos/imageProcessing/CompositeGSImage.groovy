package GPP_Demos.imageProcessing

import java.awt.Color
import java.awt.image.*
import javax.imageio.ImageIO

import groovy.transform.CompileStatic

import groovy_parallel_patterns.functionals.matrix.Matrix
import groovy_parallel_patterns.DataClass

@CompileStatic
class CompositeGSImage extends DataClass{
    String inFileName = ""
    String outFileName = ""
    BufferedImage image = null
    Matrix imageMatrix0 = null
    Matrix imageMatrix1 = null
//    Matrix kernel = null
    List partitionRanges = [] // a list of 'nodes' partition ranges

    int width = 0
    int height = 0
    int currentImage = 0
    int nodes

    static String initMethod = "init"
    static String createMethod = "create"
    static String partitionMethod = "partition"
    static String greyScaleMethod = "greyScale"
    static String convolutionMethod = "convolutionGreyScale"
    static String updateImageIndex = "updateImageIndex"

    /**
     * CAUTION images use the (x, y) notation with top left corner [0, 0]
     * Height refers to the y direction and Width the x direction
     *
     * Matrix uses the row and column notation. The elements of the matrix are
     * accessed by [r, c] with the top left corner of the matrix being [0, 0]
     * Height refers to the r index and width the c index
     */

    def extractImage () {  // always extracts into imageMatrix0
//        println "Extract h = $height, w = $width"
        // this could be parallelised over the nodes
        for ( r in 0 ..< height){
            for ( c in 0 ..< width){
                imageMatrix0.entries[r][c] = image.getRGB(c, r)
            }
        }
        currentImage = 0
//        println "extracted image $currentImage"
    } // extractImage

    // some function and kernel operations will
    // transform for image0 to image1 and vice versa
    // hence need to compress the matrix that has the final result

    def compressImage0 () {
        for ( r in 0 ..< height){
            for ( c in 0 ..< width){
                image.setRGB(c, r, (int)imageMatrix0.entries[r][c])
            }
        }
    } // compressImage0

    def compressImage1 () {
        for ( r in 0 ..< height){
            for ( c in 0 ..< width){
                image.setRGB(c, r, (int)imageMatrix1.entries[r][c])
            }
        }
    } // compressImage1

    def compressImage () {
//        println "Compressing image $currentImage"
        if (currentImage == 0) compressImage0()
        else compressImage1()
    }

    int init(List d) {  // d is null]
        return completedOK
    } // init

    static boolean inputComplete = false

    int create(List d){ // d = [fileName] assume only one file processed
//        println "Create inputComplete = $inputComplete"
      if (inputComplete) return normalTermination
      else {
        inputComplete = true   //we only read one file
        inFileName = d[0]
        outFileName = d[1]
        File inImageFile = new File(inFileName)
        image = ImageIO.read(inImageFile)
        width = image.getWidth()
        height = image.getHeight()
//        println " reading image file $fileName: H= $height, w= $width"
        imageMatrix0 = new Matrix(rows: height, columns: width)
        imageMatrix0.entries = new int[height][width]
        imageMatrix1 = new Matrix(rows: height, columns: width)
        imageMatrix1.entries = new int[height][width]
        // extract pixels into matrix
        extractImage()
        return normalContinuation
      }
    } // create

    void partition (int nodes) {
        this.nodes = nodes
        if (partitionRanges == []){
            // now create the partitionRanges over the columns or width of image
            if (nodes == 1) {
                partitionRanges << [ 0, width-1]
            }
            else {
                int partSize = (int)(width / nodes)
                for ( p in 0 .. nodes-2)  {
                    partitionRanges << [ (p * partSize), ( ((p+1) * partSize)-1 ) ]
                }
                partitionRanges << [ ((nodes-1) * partSize), (width-1) ]
            }
//            println "Partition: $partitionRanges"
        }
    } // partition

    // assumes columns are partitioned for parallelization
    int greyScale(List d){ // d = [node]
        int node = (int) d[0]
        Matrix useMatrix = currentImage == 0 ? imageMatrix0 : imageMatrix1
        for (  c in ((List)partitionRanges[node])[0] .. ((List)partitionRanges[node])[1]) {
            0 .upto(height-1){  row ->
                int p = (int)useMatrix.entries[(int)row][(int)c]
                int a = (p>>24)&0xff
                int r = (p>>16)&0xff
                int g = (p>>8)&0xff
                int b = p&0xff
                //calculate average
                int avg = (int)((r+g+b)/3)
                //replace RGB value with avg
                p = (a<<24) | (avg<<16) | (avg<<8) | avg
                useMatrix.entries[(int)row][(int)c] = p
            }
        }
        // currentImage is not altered
//        println "Grey scale $node finished using image $currentImage"
        return completedOK
    } // greyScale

    int constructGSColor ( int alpha, int gcColor){
        return (alpha<<24) | (gcColor<<16) | (gcColor<<8) | gcColor
    }

    int convolutionGreyScale (List d) { // d = [node, kernelMatrix, factor, bias]
        int node = (int)d[0]
        Matrix kernelMatrix = (Matrix)d[1]
        int factor = (int)d[2] // used to divide result if the kernel sum greater than 1
        int bias = (int)d[3] // used to brighten pixel
        Matrix sourceMatrix = currentImage == 0 ? imageMatrix0 : imageMatrix1
        Matrix resultMatrix = currentImage == 0 ? imageMatrix1 : imageMatrix0
        // convoluting from sourceMatrix to resultMatrix
        assert kernelMatrix.rows == kernelMatrix.columns : "kernel matrix MUST be square"
        int kernelSize = kernelMatrix.rows
        int ksMinus1 = kernelSize - 1
        int kernelSpan = (int)(kernelSize / 2)
        Matrix subImage = new Matrix(rows: kernelSize, columns: kernelSize)
        subImage.entries = new int [kernelSize][kernelSize]
        int lastRow = height - 1
        int currentImageTop, currentIndex, sum, row, getRow, alpha
        // work out the start and end column for each range depending on node
        int startColumn, endColumn
        if ( (node == 0)  && (nodes == 1)) {
            startColumn = kernelSpan
            endColumn = (int) ((List)partitionRanges[node])[1] - kernelSpan
        }
        else if  (node == 0){
            startColumn = kernelSpan
            endColumn =  (int)((List) partitionRanges[node])[1]
        }
        else if ( node == (partitionRanges.size() - 1)) {
            startColumn = (int)((List) partitionRanges[node])[0]
            endColumn = (int) ((List)partitionRanges[node])[1] - kernelSpan
        }
        else {
            startColumn = (int)((List) partitionRanges[node])[0]
            endColumn = (int)((List) partitionRanges[node])[1]
        }
        // now undertake the convolutions
        startColumn.upto(endColumn){ c ->
            // read in first kernelSize image rows
            for ( r in 0 ..< kernelSize) {
                int ir = ksMinus1 - r
                subImage.entries[r] = sourceMatrix.getImageRows( lastRow - ir, (int)c, kernelSize)
            }
            getRow = lastRow - kernelSize
            currentImageTop = 0
            kernelSpan.upto(height - ksMinus1){ rp ->
                currentIndex = currentImageTop
                row = lastRow - rp
                sum = 0
                0.upto(ksMinus1) { kr ->
                    0.upto(ksMinus1) {kc ->
                        Color color = new Color((int)subImage.entries[currentIndex][(int)kc])
                        alpha = color.getAlpha()
                        int col = color.getBlue()  // all colours components are the same
                        sum += col * (int)kernelMatrix.entries[(int)kr][(int)kc]
                    }//kc
                    if (currentIndex == ksMinus1) currentIndex = 0
                    else currentIndex += 1
                }//kr
                int newCol = ( (int)(sum / factor)) + bias
                if (newCol < 0) newCol = Math.abs(newCol)
                resultMatrix.entries[(int)row][(int)c] = constructGSColor( alpha, newCol)
                if (currentImageTop == 0) currentImageTop = ksMinus1
                else currentImageTop -= 1
                if (getRow >= 0) {
                    subImage.entries[currentImageTop] = sourceMatrix.getImageRows( getRow, (int)c, kernelSize)
                    getRow -= 1
                }
            } // rp
        }// c
        // now deal with the edges
        for (i in 0 ..< kernelSpan) {
            resultMatrix.setByRow(sourceMatrix.getByRow(i), i)
            resultMatrix.setByColumn(sourceMatrix.getByColumn(i), i)
        }
        for ( i in (width-kernelSpan) ..< width)
            resultMatrix.setByColumn(sourceMatrix.getByColumn(i), i)
        for ( i in (height-kernelSpan) ..< height)
            resultMatrix.setByRow(sourceMatrix.getByRow(i), i)
        return completedOK
    } // convolutionGreyScale


    int updateImageIndex(){
        // now update currentImage
        currentImage = 1 - currentImage
        return completedOK
    }

}
