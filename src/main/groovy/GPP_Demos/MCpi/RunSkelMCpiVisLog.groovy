package GPP_Demos.MCpi

import jcsp.lang.*
import groovyJCSP.*
 
import groovyParallelPatterns.*
import groovyParallelPatterns.connectors.reducers.*
import groovyParallelPatterns.connectors.spreaders.*
import groovyParallelPatterns.terminals.*
import groovyParallelPatterns.functionals.groups.*
import GPP_Demos.MCpi.MCpiData as piData
import GPP_Demos.MCpi.MCpiResults as piResults
 

//usage runDemo MCpi RunSkelMCpi resultsFile workers instances iterations
 
int workers
int instances
int iterations
 
if (args.size() == 0 ) {
workers = 4
instances =1024
iterations = 100000
}
else {
//    String folder = args[0] not required
workers = Integer.parseInt(args[1])
instances = Integer.parseInt(args[2])
iterations = Integer.parseInt(args[3])
}
 
DataDetails emitData = new DataDetails( dName: piData.getName(),
dInitMethod: piData.init,
dInitData: [instances],
dCreateMethod: piData.create,
dCreateData: [iterations])
 
 
ResultDetails resultDetails = new ResultDetails(rName: piResults.getName(),
rInitMethod: piResults.init,
rCollectMethod: piResults.collector,
rFinaliseMethod: piResults.finalise)
 
System.gc()
 
print """SkelMCpi, $workers, $instances, $iterations, """
def startime = System.currentTimeMillis()
 
//@log 1 "./LogFile-2-"

import groovyParallelPatterns.Logger
import groovyParallelPatterns.LoggingVisualiser
import groovyParallelPatterns.gppVis.Visualiser
import groovyParallelPatterns.gppVis.Connector
import javafx.application.Platform

def logChan = Channel.any2one()
Logger.initLogChannel(logChan.out())
def logVis = new LoggingVisualiser ( logInput: logChan.in(), 
                     collectors: 1,
                     logFileName: "./LogFile-2-" )

//gppVis command
new Thread() {
	@Override
	public void run() {
		Visualiser.main() 
	}
}.start() 
 

//NETWORK

def chan1 = Channel.one2one()
def chan2 = Channel.one2any()
def chan3 = Channel.any2any()
def chan4 = Channel.one2one()

def emit = new Emit (
    // input channel not required
    output: chan1.out(),
    eDetails: emitData,
    logPhaseName: "0-emit",
    logPropertyName: "instance")

    //gppVis command
    Visualiser.hb.getChildren().add(Visualiser.p.addWorker("0-emit")) 
 
def ofa = new OneFanAny (
    input: chan1.in(),
    outputAny: chan2.out(),
    destinations: workers)

    //gppVis command
    Visualiser.hb.getChildren().add(new Connector(Connector.TYPE.SPREADER)) 
 
def group = new AnyGroupAny (
    inputAny: chan2.in(),
    outputAny: chan3.out(),
    workers: workers,
    function: piData.withinOp,
    logPhaseName: "1-split",
    logPropertyName: "instance" )

    //gppVis command
    Visualiser.hb.getChildren().add(Visualiser.p.addGroup(workers, "1-split")) 
 
def afo = new AnyFanOne (
    inputAny: chan3.in(),
    output: chan4.out(),
    sources: workers)

    //gppVis command
    Visualiser.hb.getChildren().add(new Connector(Connector.TYPE.REDUCER)) 
 
def collector = new Collect (
    input: chan4.in(),
    visLogChan : logChan.out(),
    // no output channel required
    rDetails: resultDetails,
    logPhaseName: "1-value",
    logPropertyName: "instance" )

    //gppVis command
    Visualiser.hb.getChildren().add(Visualiser.p.addWorker("1-value")) 

//gppVis command
//short delay to give JavaFx time to start up.
sleep(2000)
Platform.runLater(new Runnable() {
	@Override
	void run() {
		Visualiser.networkScene("./LogFile-2-")
	}
}) 

//short delay to give JavaFx time to display.
sleep(3000) 

PAR network = new PAR()
 network = new PAR([logVis, emit , ofa , group , afo , collector ])
 network.run()
 network.removeAllProcesses()
//END

//gppVis command
//Now that the network has completed, tell the vis where the log file is so it
//can access the data so it can replay it.
Platform.runLater(new Runnable() {
	@Override
	void run() {
		Visualiser.readLog("./LogFile-2-log.csv")
	}
}) 
 
def endtime = System.currentTimeMillis()
println " ${endtime - startime} "
