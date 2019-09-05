package GPP_Demos.concordance

import jcsp.lang.*
import groovyJCSP.*
 
import GPP_Library.DataDetails
import GPP_Library.ResultDetails
import GPP_Library.connectors.spreaders.OneFanAny
import GPP_Library.functionals.composites.AnyGroupOfPipelineCollects
import GPP_Library.terminals.Emit
 
import GPP_Demos.concordance.ConcordanceData as cd
import GPP_Demos.concordance.ConcordanceResults as cr
 
 

//usage runDemo concordance RunGoPConcordanceLog resultFile groups title N runNo
 
int groups
String title
int N
int minSeqLen = 2
boolean doFileOutput = false
String workingDirectory = System.getProperty('user.dir')
String fileName
String outFileName
 
if (args.size() == 0){
// assumed to be running form within Intellij
groups = 4
title = "bible"
N = 8
fileName = "./${title}.txt"
outFileName = "./${title}GoPLog"
}
else {
// assumed to be running via runDemo
String folder = args[0]
title = args[2]
fileName = workingDirectory + "/src/main/groovy/GPP_Demos/${folder}/${title}.txt"
outFileName = workingDirectory + "/src/main/groovy/GPP_Demos/${folder}/${title}GoPLog"
groups = Integer.parseInt(args[1])
N = Integer.parseInt(args[3])
}
 
 
def dDetails = new DataDetails( dName: cd.getName(),
dInitMethod: cd.init,
dInitData: [N, fileName, outFileName],
dCreateMethod: cd.create,
dCreateData: [null])
 
def rDetails = new ResultDetails( rName: cr.getName(),
rInitMethod: cr.init,
rInitData: [minSeqLen, doFileOutput],
rCollectMethod: cr.collector,
rFinaliseMethod: cr.finalise,
rFinaliseData: [null])
 
List <ResultDetails>  resultDetails = []
 
for ( g in 0..< groups) resultDetails << rDetails
 
System.gc()
print "RunGoPConcordanceLog, $doFileOutput, $title,  $groups, "
 
def startime = System.currentTimeMillis()
 
//@log groups "./GPPLogs/LogFile-2-"

import GPP_Library.Logger
import GPP_Library.LoggingVisualiser
import GPP_Library.gppVis.Visualiser
import GPP_Library.gppVis.Connector
import javafx.application.Platform

def logChan = Channel.any2one()
Logger.initLogChannel(logChan.out())
def logVis = new LoggingVisualiser ( logInput: logChan.in(), 
                     collectors: groups,
                     logFileName: "./GPPLogs/LogFile-2-" )

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

def emitter = new Emit(
    // input channel not required
    output: chan1.out(),
    eDetails: dDetails,
    logPhaseName: "0-emit",
    logPropertyName: "strLen")

    //gppVis command
    Visualiser.hb.getChildren().add(Visualiser.p.addWorker("0-emit")) 
 
def fanOut = new OneFanAny(
    input: chan1.in(),
    outputAny: chan2.out(),
    destinations: groups)

    //gppVis command
    Visualiser.hb.getChildren().add(new Connector(Connector.TYPE.SPREADER)) 
 
def poG = new AnyGroupOfPipelineCollects(
    inputAny: chan2.in(),
    visLogChan : logChan.out(),
    // no output channel required
    stages: 3,
    rDetails: resultDetails,
    stageOp: [cd.valueList, cd.indicesMap, cd.wordsMap],
    groups: groups,
    logPhaseNames: ["1-value", "2-indeces", "3-words", "4-collect"],
    logPropertyName: "strLen" )

    //gppVis command
    Visualiser.hb.getChildren().add(Visualiser.p.addGoP(groups, "1-value", "2-indeces", "3-words", "4-collect")) 

//gppVis command
//short delay to give JavaFx time to start up.
sleep(2000)
Platform.runLater(new Runnable() {
	@Override
	void run() {
		Visualiser.networkScene("./GPPLogs/LogFile-2-")
	}
}) 

//short delay to give JavaFx time to display.
sleep(3000) 

PAR network = new PAR()
 network = new PAR([logVis, emitter , fanOut , poG ])
 network.run()
 network.removeAllProcesses()
//END

//gppVis command
//Now that the network has completed, tell the vis where the log file is so it
//can access the data so it can replay it.
Platform.runLater(new Runnable() {
	@Override
	void run() {
		Visualiser.readLog("./GPPLogs/LogFile-2-log.csv")
	}
}) 
 
def endtime = System.currentTimeMillis()
println " ${endtime - startime}"
 
 
