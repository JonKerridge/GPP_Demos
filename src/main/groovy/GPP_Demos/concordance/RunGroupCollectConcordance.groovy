package GPP_Demos.concordance

import jcsp.lang.*
import groovyJCSP.*
 
import GPP_Library.DataDetails
import GPP_Library.ResultDetails
import GPP_Library.connectors.spreaders.OneIndexedList
import GPP_Library.functionals.groups.ListGroupCollect
import GPP_Library.functionals.pipelines.OnePipelineOne
import GPP_Library.terminals.Emit
 
import GPP_Demos.concordance.ConcordanceData as cd
import GPP_Demos.concordance.ConcordanceResults as cr
 
 

//usage runDemo concordance RunGroupCollectConcordance resultsFile collectors title N
 
int collectors
String title
int N
int minSeqLen = 2
boolean doFileOutput = false
String workingDirectory = System.getProperty('user.dir')
String fileName
String outFileName
 
if (args.size() == 0){
// assumed to be running form within Intellij
collectors = 4
title = "bible"
N = 8
fileName = "./${title}.txt"
outFileName = "./${title}GCol"
}
else {
// assumed to be running via runDemo
String folder = args[0]
title = args[2]
fileName = workingDirectory + "/src/main/groovy/GPP_Demos/${folder}/${title}.txt"
outFileName = workingDirectory + "/src/main/groovy/GPP_Demos/${folder}/${title}GCol"
collectors = Integer.parseInt(args[1])
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
 
for ( g in 0..< collectors) resultDetails << rDetails
 
System.gc()
print "GroupCollect, $doFileOutput, $title, $collectors, "
def startime = System.currentTimeMillis()
 
 

//NETWORK

def chan1 = Channel.one2one()
def chan2 = Channel.one2one()
def chan3 = Channel.one2oneArray(collectors)
def chan3OutList = new ChannelOutputList(chan3)
def chan3InList = new ChannelInputList(chan3)

def emitter = new Emit(
    // input channel not required
    output: chan1.out(),
    eDetails: dDetails)
 
def pipe = new OnePipelineOne(
    input: chan1.in(),
    output: chan2.out(),
    stages : 3,
    stageOp: [cd.valueList, cd.indicesMap, cd.wordsMap])
 
def oil = new OneIndexedList(
    input: chan2.in(),
    outputList: chan3OutList,
    indexFunction : cd.indexer,
    indexBounds: [0, collectors])
 
def lgc = new ListGroupCollect(
    inputList: chan3InList,
    // no output channel required
    workers: collectors,
    rDetails: resultDetails)

PAR network = new PAR()
 network = new PAR([emitter , pipe , oil , lgc ])
 network.run()
 network.removeAllProcesses()
//END

 
 
def endtime = System.currentTimeMillis()
println " ${endtime - startime} "
 
