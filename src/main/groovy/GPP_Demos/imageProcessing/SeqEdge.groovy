package GPP_Demos.imageProcessing

import groovyParallelPatterns.functionals.matrix.Matrix
import GPP_Demos.imageProcessing.CompositeGSImage as img
import GPP_Demos.imageProcessing.CompositeGSResult as imgRslt

//usage runDemo imageProcessing SeqEdge resultsFile jpgFileName

int nodes
String workingDirectory = System.getProperty('user.dir')
String inFileName
String outFileName
String inFile
String outFile
if (args.size() == 0){
    nodes = 1
    inFileName = "DSC_7610_4096.jpg"
    outFileName = "DSC_7610_4096_SeqEdge.jpg"
    inFile = "./$inFileName"
    outFile = "./$outFileName"
}
else {
    nodes = 1
    String folder = args[0]
    inFileName = args[1]
    outFileName = inFileName + "_Seq1_K1"
    inFile = workingDirectory + "/src/main/groovy/GPP_Demos/${folder}/${inFileName}.jpg"
    outFile = workingDirectory + "/src/main/groovy/GPP_Demos/${folder}/${outFileName}.jpg"
}

//edge 1
Matrix kernel1 = new Matrix(rows: 3, columns: 3)
kernel1.entries = new int[3][3]
kernel1.setByRow([-1, -1, -1], 0)
kernel1.setByRow([-1,  8, -1], 1)
kernel1.setByRow([-1, -1, -1], 2)
//edge 2
Matrix kernel2 = new Matrix(rows: 5, columns: 5)
kernel2.entries = new int[5][5]
kernel2.setByRow([ -1, -1, -1,  -1, -1], 0)
kernel2.setByRow([ -1, -1, -1,  -1, -1], 1)
kernel2.setByRow([ -1, -1, 24,  -1, -1], 2)
kernel2.setByRow([ -1, -1, -1,  -1, -1], 3)
kernel2.setByRow([ -1, -1, -1,  -1, -1], 4)

  
System.gc()
print "Edge Seq, $nodes, $inFileName, "
long startTime = System.currentTimeMillis()
 
def image = new img()
image.init(null)
image.create([inFile, outFile])
// greyscale
image.partition(nodes)
image.greyScale([0])
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
 
 
