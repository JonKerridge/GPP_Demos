package GPP_Demos.nbody

import jcsp.lang.*
import groovyJCSP.*
 
import GPP_Library.DataDetails
import GPP_Library.ResultDetails
import GPP_Library.functionals.matrix.MultiCoreEngine
import GPP_Library.terminals.Collect
import GPP_Library.terminals.Emit
import GPP_Demos.nbody.NbodyData as nd
import GPP_Demos.nbody.NbodyResults as nr
 

// usage runDemo nbody ParNbody outFile N nodes iterations
 
int nodes
int N
String workingDirectory = System.getProperty('user.dir')
int iterations
double dt = 1e11
String readPath, writePath
 
if (args.size() == 0){
// assumed to be running form within Intellij
N = 128
nodes = 4
readPath = "./planets_list.txt"
writePath = "./${N}_planets_${nodes}_Par.txt"
iterations = 100
}
else {
// assumed to be running via runDemo
String folder = args[0]
N = Integer.parseInt(args[1])
nodes = Integer.parseInt(args[2])
iterations  = Integer.parseInt(args[3])
writePath = workingDirectory + "/src/main/groovy/GPP_Demos/${folder}/${N}_planets_${nodes}_Par.txt"
readPath = workingDirectory + "/src/main/groovy/GPP_Demos/${folder}/planets_list.txt"
}
 
System.gc()
print "ParNbody (matrix of Planet data), $N , $nodes, "
long startTime = System.currentTimeMillis()
 
 
def eDetails = new DataDetails (dName: nd.getName(),
dCreateMethod: nd.createMethod,
dInitMethod: nd.initMethod,
dInitData: [readPath, N, dt])
 
def rDetails = new ResultDetails(rName: nr.getName(),
rInitMethod: nr.init,
rInitData: [writePath],
rCollectMethod: nr.collector,
rFinaliseMethod: nr.finalise)
 
//@log 1 "./NBody-"

import GPP_Library.Logger
import GPP_Library.LoggingVisualiser
import GPP_Library.gppVis.Visualiser
import GPP_Library.gppVis.Connector
import javafx.application.Platform

def logChan = Channel.any2one()
Logger.initLogChannel(logChan.out())
def logVis = new LoggingVisualiser ( logInput: logChan.in(), 
                     collectors: 1,
                     logFileName: "./NBody-" )

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
    logPropertyName: "reps",
    logPhaseName: "emit")

    //gppVis command
    Visualiser.hb.getChildren().add(Visualiser.p.addWorker("emit")) 
 
def mcEngine = new MultiCoreEngine (
    input: chan1.in(),
    output: chan2.out(),
    nodes: nodes,
    finalOut: true,
    iterations: iterations,
    partitionMethod: nd.partitionMethod,
    calculationMethod: nd.calculationMethod,
    updateMethod: nd.updateMethod ,
    logPropertyName: "reps")

    //gppVis command
    Visualiser.hb.getChildren().add(Visualiser.p.addMCEngine (nodes )) 
 
def collector = new Collect(
    input: chan2.in(),
    visLogChan : logChan.out(),
    // no output channel required
    rDetails: rDetails,
    logPropertyName: "reps",
    logPhaseName: "collect" )

    //gppVis command
    Visualiser.hb.getChildren().add(Visualiser.p.addWorker("collect")) 

//gppVis command
//short delay to give JavaFx time to start up.
sleep(2000)
Platform.runLater(new Runnable() {
	@Override
	void run() {
		Visualiser.networkScene("./NBody-")
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
		Visualiser.readLog("./NBody-log.csv")
	}
}) 
 
 
long endTime = System.currentTimeMillis()
println " ${endTime - startTime}"
 
 
