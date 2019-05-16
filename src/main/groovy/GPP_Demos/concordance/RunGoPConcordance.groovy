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
 
 

//usage runDemo concordance RunGoPConcordance resultFile workers title N
 
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
outFileName = "./${title}GoP"
}
else {
// assumed to be running via runDemo
String folder = args[0]
title = args[2]
fileName = workingDirectory + "/src/main/groovy/GPP_Demos/${folder}/${title}.txt"
outFileName = workingDirectory + "/src/main/groovy/GPP_Demos/${folder}/${title}GoP"
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
print "GoP, $doFileOutput, $title, $groups, $N, "
 
def startime = System.currentTimeMillis()
 
 

//NETWORK

def chan1 = Channel.one2one()
def chan2 = Channel.one2any()

def emitter = new Emit(
    // input channel not required
    output: chan1.out(),
    eDetails: dDetails)
 
def fanOut = new OneFanAny(
    input: chan1.in(),
    outputAny: chan2.out(),
    destinations: groups)
 
def GoPconcordance = new AnyGroupOfPipelineCollects(
    inputAny: chan2.in(),
    // no output channel required
    stages: 3,
    rDetails: resultDetails,
    stageOp: [cd.valueList, cd.indicesMap, cd.wordsMap],
    groups: groups)

PAR network = new PAR()
 network = new PAR([emitter , fanOut , GoPconcordance ])
 network.run()
 network.removeAllProcesses()
//END

 
 
def endtime = System.currentTimeMillis()
println " ${endtime - startime}"
 
 
