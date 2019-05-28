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
 
//@log 1 "./Jacobi-"

import GPP_Library.Logger
import GPP_Library.LoggingVisualiser
import GPP_Library.gppVis.Visualiser
import GPP_Library.gppVis.Connector
import javafx.application.Platform

def logChan = Channel.any2one()
Logger.initLogChannel(logChan.out())
def logVis = new LoggingVisualiser ( logInput: logChan.in(), 
                     collectors: 1,
                     logFileName: "./Jacobi-" )

//gppVis command
new Thread() {
	@Override
	public void run() {
		Visualiser.main() 
	}
}.start() 
 

//NETWORK

def chan1 = Channel.one2one()
def chan2 = Channel.one2one()

def emit = new Emit(
    // input channel not required
    output: chan1.out(),
    eDetails: eDetails,
    logPropertyName: "iterations",
    logPhaseName: "emit")

    //gppVis command
    Visualiser.hb.getChildren().add(Visualiser.p.addWorker("emit")) 
 
def mcEngine = new MultiCoreEngine (
    input: chan1.in(),
    output: chan2.out(),
    nodes: nodes,
    errorMargin: margin,
    finalOut: true,
    partitionMethod: jd.partitionMethod,
    calculationMethod: jd.calculationMethod,
    errorMethod: jd.errorMethod,
    updateMethod: jd.updateMethod,
    logPropertyName: "iterations")

    //gppVis command
    Visualiser.hb.getChildren().add(Visualiser.p.addMCEngine (nodes )) 
 
def collector = new Collect(
    input: chan2.in(),
    visLogChan : logChan.out(),
    // no output channel required
    rDetails: rDetails,
    logPropertyName: "iterations",
    logPhaseName: "collect" )

    //gppVis command
    Visualiser.hb.getChildren().add(Visualiser.p.addWorker("collect")) 

//gppVis command
//short delay to give JavaFx time to start up.
sleep(2000)
Platform.runLater(new Runnable() {
	@Override
	void run() {
		Visualiser.networkScene("./Jacobi-")
	}
}) 

//short delay to give JavaFx time to display.
sleep(3000) 

PAR network = new PAR()
 network = new PAR([logVis, emit , mcEngine , collector ])
 network.run()
 network.removeAllProcesses()
//END

//gppVis command
//Now that the network has completed, tell the vis where the log file is so it
//can access the data so it can replay it.
Platform.runLater(new Runnable() {
	@Override
	void run() {
		Visualiser.readLog("./Jacobi-log.csv")
	}
}) 
 
long endTime = System.currentTimeMillis()
println " ${endTime - startTime}"
 
