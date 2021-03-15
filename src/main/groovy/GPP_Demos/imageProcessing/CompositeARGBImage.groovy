package GPP_Demos.imageProcessing

import groovy.transform.CompileStatic
import java.awt.Color
import java.awt.image.*

import javax.imageio.ImageIO

import groovy_parallel_patterns.functionals.matrix.Matrix
import groovy_parallel_patterns.DataClass

@CompileStatic
class CompositeARGBImage extends DataClass{
    String inFileName = ""
    String outFileName = ""
    BufferedImage image = null
    Matrix imageMatrix0 = null
    Matrix imageMatrix1 = null
    List partitionRanges = [] // a list of 'nodes' partition ranges

    int width = 0
    int height = 0
    int currentImage = 0
    int nodes

    static String initMethod = "init"
    static String createMethod = "create"
    static String partitionMethod = "partition"
    static String convolutionRGBMethod = "convolution"
    static String updateImageIndex = "updateImageIndex"

    /**
     * CAUTION images use the (x, y) notation with top left corner [0, 0]
     * Height refers to the y direction and Width the x direction
     *
     * Matrix uses the row and column notation. The elements of the matrix are
     * accessed by [r, c] with the top left corner of the matrix being [0, 0]
     * Height refers to the r index and width the c index
     */

    void extractImage () {  // always extracts into imageMatrix0
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

    void compressImage0 () {
        for ( r in 0 ..< height){
            for ( c in 0 ..< width){
                image.setRGB(c, r, (int)imageMatrix0.entries[r][c])
            }
        }
    } // compressImage0

    void compressImage1 () {
        for ( r in 0 ..< height){
            for ( c in 0 ..< width){
                image.setRGB(c, r, (int)imageMatrix1.entries[r][c])
            }
        }
    } // compressImage1

    void compressImage () {
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
//    println " reading image file $inImageFile: H= $height, w= $width"
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

    int constructRGBColor ( int alpha, int red, int green, int blue){
        return (alpha<<24) | (red<<16) | (green<<8 | blue )
    }

    int convolution (List d) { // d = [node, kernelMatrix, factor, bias]
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
        Matrix [] argbSubImage = new Matrix[4]
        for ( c in 0 .. 3) { // c: 0 - blue, 1 - green, 2 - red 3 - alpha
            argbSubImage[c] = new Matrix(rows: kernelSize, columns: kernelSize)
            argbSubImage[c].entries = new int [kernelSize][kernelSize]
        }
        int lastRow = height - 1
        int currentImageTop, currentIndex, row, getRow
        int sumR, sumG, sumB, alpha
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
                // extract argb from the image row values
                int [] rowValues = new int[kernelSize]
                rowValues = sourceMatrix.getImageRows( lastRow - ir, (int)c, kernelSize)
                for ( k in 0..ksMinus1 ) { // for each value in rowValues
                    Color color =new Color(rowValues[k])
                    argbSubImage[0].entries[r][k] = color.getBlue()
                    argbSubImage[1].entries[r][k] = color.getGreen()
                    argbSubImage[2].entries[r][k] = color.getRed()
                    argbSubImage[3].entries[r][k] = color.getAlpha()
                } //k
            } //r
            getRow = lastRow - kernelSize
            currentImageTop = 0
            kernelSpan.upto(height - ksMinus1){ rp ->
                currentIndex = currentImageTop
                row = lastRow - rp
                sumR = 0
                sumB = 0
                sumG = 0
                alpha = 0
                0.upto(ksMinus1) { kr ->
                    0.upto(ksMinus1) {kc ->
                        sumB = sumB + (int)((int)argbSubImage[0].entries[currentIndex][(int)kc] * (int)kernelMatrix.entries[(int)kr][(int)kc])
                        sumG = sumG + (int)((int)argbSubImage[1].entries[currentIndex][(int)kc] * (int)kernelMatrix.entries[(int)kr][(int)kc])
                        sumR = sumR + (int)((int)argbSubImage[2].entries[currentIndex][(int)kc] * (int)kernelMatrix.entries[(int)kr][(int)kc])
                        alpha = alpha + ((int)argbSubImage[3].entries[currentIndex][(int)kc])
                    }//kc
                    if (currentIndex == ksMinus1) currentIndex = 0
                    else currentIndex += 1
                }//kr
                alpha = (int)(alpha / kernelSize)
                sumB = ( (int)(sumB / factor)) + bias
                sumG = ( (int)(sumG / factor)) + bias
                sumR = ( (int)(sumR / factor)) + bias
                if (sumB < 0) sumB = (int)Math.abs(sumB)
                if (sumG < 0) sumG = (int)Math.abs(sumG)
                if (sumR < 0) sumR = (int)Math.abs(sumR)
                resultMatrix.entries[(int)row][(int)c] = constructRGBColor( (int)alpha, sumR, sumG, sumB)
                if (currentImageTop == 0) currentImageTop = ksMinus1
                else currentImageTop -= 1
                if (getRow >= 0) {
                    int [] rowValues = new int[kernelSize]
                    rowValues = sourceMatrix.getImageRows( getRow, (int)c, kernelSize)
                    for ( k in 0..ksMinus1 ) { // for each value in rowValues
                        Color color =new Color(rowValues[k])
                        argbSubImage[0].entries[currentImageTop][k] = color.getBlue()
                        argbSubImage[1].entries[currentImageTop][k] = color.getGreen()
                        argbSubImage[2].entries[currentImageTop][k] = color.getRed()
                        argbSubImage[3].entries[currentImageTop][k] = color.getAlpha()
                    } //k
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
    } // convolution

    int updateImageIndex(){
        // now update currentImage
        currentImage = 1 - currentImage
        return completedOK
    }

}
