package GPP_Demos.imageProcessing

import groovyParallelPatterns.functionals.matrix.Matrix
import GPP_Demos.imageProcessing.CompositeARGBImage as img
import GPP_Demos.imageProcessing.CompositeARGBResult as imgRslt

//usage runDemo imageProcessing SeqRGBImage resultsFile

int nodes
String workingDirectory = System.getProperty('user.dir')
String inFileName = "DSC_0120.jpg"
String outFileName = "DSC_0120_RGB_${nodes}_seq.jpg"
String inFile
String outFile
if (args.size() == 0){
    nodes = 1
    inFile = "./$inFileName"
    outFile = "./$outFileName"
}
else {
    nodes = 1
    String folder = args[0]
    inFile = workingDirectory + "/src/main/groovy/GPP_Demos/${folder}/$inFileName"
    outFile = workingDirectory + "/src/main/groovy/GPP_Demos/${folder}/$outFileName"
}
 
Matrix kernel1 = new Matrix(rows: 3, columns: 3)
kernel1.entries = new int[3][3]
kernel1.setByRow([-1, -1, -1], 0)
kernel1.setByRow([-1,  8, -1], 1)
kernel1.setByRow([-1, -1, -1], 2)
int factor1 = 1
 
Matrix kernel2 = new Matrix(rows: 3, columns: 3)
kernel2.entries = new int[3][3]
kernel2.setByRow([ 0, -1,  0], 0)
kernel2.setByRow([-1,  4, -1], 1)
kernel2.setByRow([ 0, -1,  0], 2)
int factor2 = 1
 
Matrix kernel3 = new Matrix(rows: 5, columns: 5)
kernel3.entries = new int[5][5]
kernel3.setByRow([ 0, 0, -1,  0, 0], 0)
kernel3.setByRow([ 0, 0, -1,  0, 0], 1)
kernel3.setByRow([ 0, 0,  4,  0, 0], 2)
kernel3.setByRow([ 0, 0, -1,  0, 0], 3)
kernel3.setByRow([ 0, 0, -1,  0, 0], 4)
int factor3 = 1
 
Matrix kernel4 = new Matrix(rows: 5, columns: 5)
kernel4.entries = new int[5][5]
kernel4.setByRow([ -1, 0,  0,  0, 0], 0)
kernel4.setByRow([ 0, -2,  0,  0, 0], 1)
kernel4.setByRow([ 0,  0,  6,  0, 0], 2)
kernel4.setByRow([ 0,  0,  0, -2, 0], 3)
kernel4.setByRow([ 0,  0,  0,  0, -1], 4)
int factor4 = 1
 
// sharpen
Matrix kernel5 = new Matrix(rows: 3, columns: 3)
kernel5.entries = new int[3][3]
kernel5.setByRow([-1, -1, -1], 0)
kernel5.setByRow([-1,  9, -1], 1)
kernel5.setByRow([-1, -1, -1], 2)
int factor5 = 1
 
// blur
Matrix kernel6 = new Matrix(rows: 3, columns: 3)
kernel6.entries = new int[3][3]
kernel6.setByRow([1, 1, 1], 0)
kernel6.setByRow([1, 1, 1], 1)
kernel6.setByRow([1, 1, 1], 2)
int factor6 = 9
 
System.gc()
print "RGBImage Seq, $nodes, "
long startTime = System.currentTimeMillis()
 

def image = new img()
image.init(null)
image.create([inFile, outFile])
//blur 1
image.partition(nodes)
image.convolution([0, kernel6, factor6, 0])
image.updateImageIndex()
//blue 2
image.partition(nodes)
image.convolution([0, kernel6, factor6, 0])
image.updateImageIndex()
//get result
def imageRslt = new imgRslt()
imageRslt.init(null)
imageRslt.collect(image)
imageRslt.finalise(null)
 
long endTime = System.currentTimeMillis()
println " ${endTime - startTime}"
 
 
