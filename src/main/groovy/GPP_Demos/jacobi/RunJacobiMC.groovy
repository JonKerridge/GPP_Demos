package GPP_Demos.jacobi

import jcsp.lang.*
import groovyJCSP.*
 
import GPP_Library.DataDetails
import GPP_Library.ResultDetails
import GPP_Library.functionals.matrix.MultiCoreEngine
import GPP_Library.terminals.Collect
import GPP_Library.terminals.Emit
import GPP_Demos.jacobi.JacobiDataMC as jd
import GPP_Demos.jacobi.JacobiResultMC as jr
 

/**
* A demonstration of the matrix.MultiCoreEngine process to evaluate the solution to a matrix
* containing a set of simultaneous equations.  The process network assumes that the input file will
* contain a single matrix or a number of matrices of different sizes.  The file Jacobi.txt containes
* four matrices and the remaining files Jacobinnn contain a single matrix of dimension nnn.  Scripts
* are provided to create the required input file.
*/
 
//usage runDemo jacobi RunJacobiMC resultsFile title nodes
 
int nodes
String title
String workingDirectory = System.getProperty('user.dir')
String fileName
 
if (args.size() == 0){
nodes = 4
title = "Jacobi1024"
fileName = "./${title}.txt"
}
else {
nodes = Integer.parseInt(args[2])
String folder = args[0]
title = args[1]
fileName = workingDirectory + "/src/main/groovy/GPP_Demos/${folder}/${title}.txt"
}
 
double margin = 1.0E-16
 
System.gc()
print "ParJacobi, $nodes, "
long startTime = System.currentTimeMillis()
 
def eDetails = new DataDetails (dName: jd.getName(),
dCreateMethod: jd.createMethod,
dInitMethod: jd.initMethod,
dInitData: [fileName])
 
def rDetails = new ResultDetails(rName: jr.getName(),
rInitMethod: jr.init,
rCollectMethod: jr.collector,
rFinaliseMethod: jr.finalise)
 

//NETWORK

def chan1 = Channel.one2one()
def chan2 = Channel.one2one()

def emit = new Emit(
    // input channel not required
    output: chan1.out(),
    eDetails: eDetails)
 
def mcEngine = new MultiCoreEngine (
    input: chan1.in(),
    output: chan2.out(),
    nodes: nodes,
    errorMargin: margin,
    finalOut: true,
    partitionMethod: jd.partitionMethod,
    calculationMethod: jd.calculationMethod,
    errorMethod: jd.errorMethod,
    updateMethod: jd.updateMethod )
 
def collector = new Collect(
    input: chan2.in(),
    // no output channel required
    rDetails: rDetails)

PAR network = new PAR()
 network = new PAR([emit , mcEngine , collector ])
 network.run()
 network.removeAllProcesses()
//END

 
 
long endTime = System.currentTimeMillis()
println " ${endTime - startTime}"
 
