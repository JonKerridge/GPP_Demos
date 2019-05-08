package GPP_Demos.concordance

import jcsp.lang.*
import groovyJCSP.*
 
import GPP_Library.DataDetails
import GPP_Library.ResultDetails
import GPP_Library.connectors.spreaders.OneFanAny
import GPP_Library.functionals.groups.AnyGroupCollect
import GPP_Library.functionals.pipelines.OnePipelineOne
import GPP_Library.terminals.Emit
 
import GPP_Demos.concordance.ConcordanceData as cd
import GPP_Demos.concordance.ConcordanceResults as cr
 
 

//usage runDemo concordance RunManyCollectConcordance resultsFile collectors title N
 
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
outFileName = "./${title}MCol"
}
else {
// assumed to be running via runDemo
String folder = args[0]
title = args[2]
fileName = workingDirectory + "/src/main/groovy/GPP_Demos/${folder}/${title}.txt"
outFileName = workingDirectory + "/src/main/groovy/GPP_Demos/${folder}/${title}MCol"
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
 
 
System.gc()
print "Many Collect, $doFileOutput, $title, $collectors, "
 
def startime = System.currentTimeMillis()
 
 

//NETWORK

def chan1 = Channel.one2one()
def chan2 = Channel.one2one()
def chan3 = Channel.one2any()

def emitter = new Emit(
    // input channel not required
    output: chan1.out(),
    eDetails: dDetails)
 
def pipe = new OnePipelineOne(
    input: chan1.in(),
    output: chan2.out(),
    stages : 3,
    stageOp: [cd.valueList, cd.indicesMap, cd.wordsMap])
 
def ofa = new OneFanAny(
    input: chan2.in(),
    outputAny: chan3.out(),
    destinations: collectors)
 
def collectGroup  = new AnyGroupCollect(
    inputAny: chan3.in(),
    // no output channel required
    rDetails: rDetails,
    collectors: collectors)

PAR network = new PAR()
 network = new PAR([emitter , pipe , ofa , collectGroup  ])
 network.run()
 network.removeAllProcesses()
//END

 
def endtime = System.currentTimeMillis()
println " ${endtime - startime} "
