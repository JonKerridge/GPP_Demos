package GPP_Demos.concordance

import jcsp.lang.*
import groovyJCSP.*
 
import GPP_Library.DataDetails
import GPP_Library.LocalDetails
import GPP_Library.ResultDetails
import GPP_Library.connectors.reducers.ListMergeOne
import GPP_Library.connectors.spreaders.OneFanAny
import GPP_Library.connectors.spreaders.OneFanList
import GPP_Library.functionals.composites.AnyGroupOfPipelineCollects
import GPP_Library.functionals.groups.ListGroupList
import GPP_Library.terminals.*
import GPP_Library.functionals.transformers.CombineNto1
 
import GPP_Demos.concordance.ConcordanceWords as cw
import GPP_Demos.concordance.ConcordanceCombine as cc
import GPP_Demos.concordance.ConcordanceResults as cr
import GPP_Demos.concordance.ConcordanceData as cd
 

//usage runDemo concordance RunExtendedConcordanceVisLog resultsFile title blockWorkers blockSize pogWorkers N
 
int blockWorkers
int pogWorkers
int blockSize
String title
int N
int minSeqLen = 2
boolean doFileOutput = false
String workingDirectory = System.getProperty('user.dir')
String fileName
String outFileName
 
if (args.size() == 0){
// assumed to be running form within Intellij
blockWorkers = 4
pogWorkers = 2
blockSize = 64000
title = "bible"
N = 8
fileName = "./${title}.txt"
outFileName = "./${title}Ext"
}
else {
// assumed to be running via runDemo
String folder = args[0]
title = args[1]
fileName = workingDirectory + "/src/main/groovy/GPP_Demos/${folder}/${title}.txt"
outFileName = workingDirectory + "/src/main/groovy/GPP_Demos/${folder}/${title}ExtLog"
blockWorkers = Integer.parseInt(args[2])
blockSize = Integer.parseInt(args[3])
pogWorkers = Integer.parseInt(args[4])
N = Integer.parseInt(args[5])
}
 
def dDetails = new DataDetails( dName: cw.getName(),
dInitMethod: cw.init,
dInitData: [fileName, blockSize],
dCreateMethod: cw.create,
dCreateData: [null])
 
def localData = new LocalDetails(lName: cc.getName(),
lInitData: [N, outFileName],
lInitMethod: cc.init,
lFinaliseMethod: cc.finalise)
 
def outData = new LocalDetails( lName:cd.getName(),
lInitMethod: cd.initLocal,
lInitData: null,
lCreateMethod: cd.create,
lFinaliseMethod: cd.finalise)
 
def rDetails = new ResultDetails( rName: cr.getName(),
rInitMethod: cr.init,
rInitData: [minSeqLen, doFileOutput],
rCollectMethod: cr.collector,
rFinaliseMethod: cr.finalise,
rFinaliseData: [null])
 
List <ResultDetails>  resultDetails = []
 
for ( g in 0..< pogWorkers) resultDetails << rDetails
 
print "RunExtendedConcordanceLog, $doFileOutput, $title, $blockWorkers, $pogWorkers, $blockSize, "
System.gc()
def startime = System.currentTimeMillis()
 
//@log pogWorkers "./LogFileExt-1-"

import GPP_Library.Logger
import GPP_Library.LoggingVisualiser
import GPP_Library.gppVis.Visualiser
import GPP_Library.gppVis.Connector
import javafx.application.Platform

def logChan = Channel.any2one()
Logger.initLogChannel(logChan.out())
def logVis = new LoggingVisualiser ( logInput: logChan.in(), 
                     collectors: pogWorkers,
                     logFileName: "./LogFileExt-1-" )

//gppVis command
new Thread() {
	@Override
	public void run() {
		Visualiser.main() 
	}
}.start() 
 

//NETWORK

def chan1 = Channel.one2one()
def chan2 = Channel.one2oneArray(blockWorkers)
def chan2OutList = new ChannelOutputList(chan2)
def chan2InList = new ChannelInputList(chan2)
def chan3 = Channel.one2oneArray(blockWorkers)
def chan3OutList = new ChannelOutputList(chan3)
def chan3InList = new ChannelInputList(chan3)
def chan4 = Channel.one2one()
def chan5 = Channel.one2one()
def chan6 = Channel.one2one()
def chan7 = Channel.one2any()

def emit = new Emit (
    // input channel not required
    output: chan1.out(),
    eDetails: dDetails,
    logPhaseName: "emit",
    logPropertyName: "bufferInstance")

    //gppVis command
    Visualiser.hb.getChildren().add(Visualiser.p.addWorker("emit")) 
 
def fos = new OneFanList(
    input: chan1.in(),
    outputList: chan2OutList )

    //gppVis command
    Visualiser.hb.getChildren().add(new Connector(Connector.TYPE.SPREADER)) 
 
def group = new ListGroupList(
    inputList: chan2InList,
    outputList: chan3OutList,
    function: cw.processBuffer,
    workers: blockWorkers,
    logPhaseName: "split",
    logPropertyName: "bufferInstance")

    //gppVis command
    Visualiser.hb.getChildren().add(Visualiser.p.addGroup(blockWorkers, "split" )) 
 
def fis = new ListMergeOne(
    inputList: chan3InList,
    output: chan4.out(),
    )

    //gppVis command
    Visualiser.hb.getChildren().add(new Connector(Connector.TYPE.REDUCER)) 
 
 
def combine = new CombineNto1(
    input: chan4.in(),
    output: chan5.out(),
    localDetails: localData,
    outDetails: outData,
    combineMethod: cc.appendBuff,
    logPhaseName: "combine",
    inputLogPropertyName: "bufferInstance",
    outputLogPropertyName: "strLen")

    //gppVis command
    Visualiser.hb.getChildren().add(Visualiser.p.addWorker("combine")) 
 
def emitInstances = new EmitFromInput(
    input: chan5.in(),
    output: chan6.out(),
    eDetails: outData,
    logPhaseName: "emitter",
    logPropertyName: "strLen" )

    //gppVis command
    Visualiser.hb.getChildren().add(Visualiser.p.addWorker("emitter")) 
 
def fanOut = new OneFanAny(
    input: chan6.in(),
    outputAny: chan7.out(),
    destinations: pogWorkers)

    //gppVis command
    Visualiser.hb.getChildren().add(new Connector(Connector.TYPE.SPREADER)) 
 
def poG = new AnyGroupOfPipelineCollects(
    inputAny: chan7.in(),
    visLogChan : logChan.out(),
    // no output channel required
    stages: 3,
    rDetails: resultDetails,
    stageOp: [cd.valueList, cd.indicesMap, cd.wordsMap],
    groups: pogWorkers,
    logPhaseNames: ["value", "index", "words", "collect"],
    logPropertyName: "strLen" )

    //gppVis command
    Visualiser.hb.getChildren().add(Visualiser.p.addGoP(pogWorkers, "value", "index", "words", "collect")) 

//gppVis command
//short delay to give JavaFx time to start up.
sleep(2000)
Platform.runLater(new Runnable() {
	@Override
	void run() {
		Visualiser.networkScene()
	}
}) 

//short delay to give JavaFx time to display.
sleep(3000) 

PAR network = new PAR()
 network = new PAR([logVis, emit , fos , group , fis , combine , emitInstances , fanOut , poG ])
 network.run()
 network.removeAllProcesses()
//END

//gppVis command
//Now that the network has completed, tell the vis where the log file is so it
//can access the data so it can replay it.
Platform.runLater(new Runnable() {
	@Override
	void run() {
		Visualiser.readLog("./LogFileExt-1-log.csv")
	}
}) 
 
def endtime = System.currentTimeMillis()
println " ${endtime - startime} "
 
