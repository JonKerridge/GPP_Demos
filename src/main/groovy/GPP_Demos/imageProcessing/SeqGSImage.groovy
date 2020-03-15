package GPP_Demos.imageProcessing

import groovyParallelPatterns.functionals.matrix.Matrix
import GPP_Demos.imageProcessing.CompositeGSImage as img
import GPP_Demos.imageProcessing.CompositeGSResult as imgRslt

//usage runDemo imageProcessing SeqGSImage resultsFile

int nodes
String workingDirectory = System.getProperty('user.dir')
String inFileName = "DSC_0120.jpg"
String outFileName = "DSC_0120_GS_${nodes}_seq.jpg"
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

//edge
Matrix kernel1 = new Matrix(rows: 3, columns: 3)
kernel1.entries = new int[3][3]
kernel1.setByRow([-1, -1, -1], 0)
kernel1.setByRow([-1,  8, -1], 1)
kernel1.setByRow([-1, -1, -1], 2)
//edge
Matrix kernel2 = new Matrix(rows: 3, columns: 3)
kernel2.entries = new int[3][3]
kernel2.setByRow([ 0, -1,  0], 0)
kernel2.setByRow([-1,  4, -1], 1)
kernel2.setByRow([ 0, -1,  0], 2)
//edge
Matrix kernel3 = new Matrix(rows: 5, columns: 5)
kernel3.entries = new int[5][5]
kernel3.setByRow([ -1, -1, -1,  -1, -1], 0)
kernel3.setByRow([ -1, -1, -1,  -1, -1], 1)
kernel3.setByRow([ -1, -1, 24,  -1, -1], 2)
kernel3.setByRow([ -1, -1, -1,  -1, -1], 3)
kernel3.setByRow([ -1, -1, -1,  -1, -1], 4)
//blur
Matrix kernel4 = new Matrix(rows: 5, columns: 5)
kernel4.entries = new int[5][5]
kernel4.setByRow([ 0, 0,  1,  0, 0], 0)
kernel4.setByRow([ 0, 1,  1,  1, 0], 1)
kernel4.setByRow([ 1, 1,  1,  1, 1], 2)
kernel4.setByRow([ 0, 1,  1,  1, 0], 3)
kernel4.setByRow([ 0, 0,  1,  0, 0], 4)
//box blur
Matrix kernel5 = new Matrix(rows: 3, columns: 3)
kernel5.entries = new int[3][3]
kernel5.setByRow([1, 1, 1], 0)
kernel5.setByRow([1, 1, 1], 1)
kernel5.setByRow([1, 1, 1], 2)
 
  
System.gc()
print "GSImage Seq, $nodes, "
long startTime = System.currentTimeMillis()
 
def image = new img()
image.init(null)
image.create([inFile, outFile])
// greyscale
image.partition(nodes)
image.greyScale([0])
// blur 1
image.convolutionGreyScale([0, kernel4, 13, 0])
image.updateImageIndex()
//blur 2
image.convolutionGreyScale([0, kernel4, 13, 0])
image.updateImageIndex()
//edge detect
image.convolutionGreyScale([0, kernel1, 1, 0])
image.updateImageIndex()
//get result 
def imageRslt = new imgRslt()
imageRslt.init(null)
imageRslt.collect(image)
imageRslt.finalise(null) 
 
long endTime = System.currentTimeMillis()
println " ${endTime - startTime}"
 
 
