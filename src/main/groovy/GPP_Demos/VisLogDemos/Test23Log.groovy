package GPP_Demos.VisLogDemos

import jcsp.lang.*
import groovyJCSP.*
 
import GPP_Library.*
import GPP_Library.connectors.reducers.*
import GPP_Library.connectors.spreaders.*
import GPP_Library.functionals.groups.*
import GPP_Library.terminals.*
 

int workers = 3
def er = new TestExtract()
 
 
def emitterDetails = new DataDetails(dName: TestData.getName(),
dInitMethod: TestData.totalInitialise,
dInitData: [20],
dCreateMethod: TestData.create)
 
def resultDetails = new ResultDetails(rName: TestResult.getName(),
rInitMethod: TestResult.init,
rCollectMethod: TestResult.collector,
rFinaliseMethod: TestResult.finalise,
rFinaliseData: [er])
 
def m1 = [[0], [0], [0]]             // for stage 1
def m2 = [[100], [100], [100]]       // for stage 2
def m3 = [[1000], [1000], [1000]]    // for stage 3
 
def group1Details = new GroupDetails(workers: workers,
groupDetails: new LocalDetails[workers])
def group2Details = new GroupDetails(workers: workers,
groupDetails: new LocalDetails[workers])
def group3Details = new GroupDetails(workers: workers,
groupDetails: new LocalDetails[workers])
 
for (w in 0..<workers) {
group1Details.groupDetails[w] = new LocalDetails(lName: TestWorker.getName(),
lInitMethod: TestWorker.init,
lFinaliseMethod: TestWorker.finalise)
group2Details.groupDetails[w] = new LocalDetails(lName: TestWorker.getName(),
lInitMethod: TestWorker.init,
lFinaliseMethod: TestWorker.finalise)
group3Details.groupDetails[w] = new LocalDetails(lName: TestWorker.getName(),
lInitMethod: TestWorker.init,
lFinaliseMethod: TestWorker.finalise)
}
group1Details.groupDetails[0].lInitData = [100, 0]
group1Details.groupDetails[1].lInitData = [100, 0]
group1Details.groupDetails[2].lInitData = [100, 0]
 
group2Details.groupDetails[0].lInitData = [100, 0]
group2Details.groupDetails[1].lInitData = [100, 0]
group2Details.groupDetails[2].lInitData = [100, 0]
 
group3Details.groupDetails[0].lInitData = [100, 0]
group3Details.groupDetails[1].lInitData = [100, 0]
group3Details.groupDetails[2].lInitData = [100, 0]
 
//@log 1 "./Vis23-"

import GPP_Library.Logger
import GPP_Library.LoggingVisualiser
import GPP_Library.gppVis.Visualiser
import GPP_Library.gppVis.Connector
import javafx.application.Platform

def logChan = Channel.any2one()
Logger.initLogChannel(logChan.out())
def logVis = new LoggingVisualiser ( logInput: logChan.in(), 
                     collectors: 1,
                     logFileName: "./Vis23-" )

//gppVis command
new Thread() {
	@Override
	public void run() {
		Visualiser.main();
	}
}.start();
 

//NETWORK

def chan1 = Channel.one2one()
def chan2 = Channel.one2oneArray(workers)
def chan2OutList = new ChannelOutputList(chan2)
def chan2InList = new ChannelInputList(chan2)
def chan3 = Channel.one2oneArray(workers)
def chan3OutList = new ChannelOutputList(chan3)
def chan3InList = new ChannelInputList(chan3)
def chan4 = Channel.one2oneArray(workers)
def chan4OutList = new ChannelOutputList(chan4)
def chan4InList = new ChannelInputList(chan4)
def chan5 = Channel.one2oneArray(workers)
def chan5OutList = new ChannelOutputList(chan5)
def chan5InList = new ChannelInputList(chan5)
def chan6 = Channel.one2one()

def emitter = new Emit(
    // input channel not required
    output: chan1.out(),
    eDetails: emitterDetails,
    logPhaseName: "emit",
    logPropertyName: "w1")

    //gppVis command
    Visualiser.hb.getChildren().add(Visualiser.p.addWorker("emit"));
 
def outFan = new OneSeqCastList(
    input: chan1.in(),
    outputList: chan2OutList )

    //gppVis command
    Visualiser.hb.getChildren().add(new Connector(Connector.TYPE.SPREADER));
 
def stage1 = new ListGroupList(
    inputList: chan2InList,
    outputList: chan3OutList,
    gDetails: group1Details,
    function: TestData.func1,
    modifier: m1,
    workers: workers,
    logPhaseName: "stage1",
    logPropertyName: "w1")

    //gppVis command
    Visualiser.hb.getChildren().add(Visualiser.p.addGroup(workers, "stage1" ));
 
def stage2 = new ListGroupList(
    inputList: chan3InList,
    outputList: chan4OutList,
    gDetails: group2Details,
    function: TestData.func2,
    modifier: m2,
    workers: workers,
    logPhaseName: "stage2",
    logPropertyName: "w1")

    //gppVis command
    Visualiser.hb.getChildren().add(Visualiser.p.addGroup(workers, "stage2" ));
 
def stage3 = new ListGroupList(
    inputList: chan4InList,
    outputList: chan5OutList,
    gDetails: group3Details,
    function: TestData.func3,
    modifier: m3,
    workers: workers,
    logPhaseName: "stage3",
    logPropertyName: "w1")

    //gppVis command
    Visualiser.hb.getChildren().add(Visualiser.p.addGroup(workers, "stage3" ));
 
def inFan = new ListFanOne(
    inputList: chan5InList,
    output: chan6.out(),
    )

    //gppVis command
    Visualiser.hb.getChildren().add(new Connector(Connector.TYPE.REDUCER));
 
 
def collector = new Collect(
    input: chan6.in(),
    visLogChan : logChan.out(),
    // no output channel required
    rDetails: resultDetails,
    logPhaseName: "collect",
    logPropertyName: "w1")

    //gppVis command
    Visualiser.hb.getChildren().add(Visualiser.p.addWorker("collect"));

//gppVis command
//short delay to give JavaFx time to start up.
sleep(2000)
Platform.runLater(new Runnable() {
	@Override
	void run() {
		Visualiser.networkScene()
	}
});

//short delay to give JavaFx time to display.
sleep(3000);

PAR network = new PAR()
 network = new PAR([logVis, emitter , outFan , stage1 , stage2 , stage3 , inFan , collector ])
 network.run()
 network.removeAllProcesses()
//END

//gppVis command
//Now that the network has completed, tell the vis where the log file is so it
//can access the data so it can replay it.
Platform.runLater(new Runnable() {
	@Override
	void run() {
		Visualiser.readLog("./Vis23-log.csv")
	}
});
 
println "23Log: $er"
