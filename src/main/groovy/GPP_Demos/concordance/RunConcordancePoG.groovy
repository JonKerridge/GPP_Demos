package GPP_Demos.concordance

import jcsp.lang.*
import groovyJCSP.*
 
import GPP_Library.DataDetails
import GPP_Library.ResultDetails
import GPP_Library.patterns.TaskParallelOfGroupCollects
import GPP_Demos.concordance.ConcordanceData as cd
import GPP_Demos.concordance.ConcordanceResults as cr
 
 

//usage runDemo concordance RunConcordancePoG resultFile workers title N
 
int workers
String title
int N
int minSeqLen = 2
boolean doFileOutput = false
String workingDirectory = System.getProperty('user.dir')
String fileName
String outFileName
 
if (args.size() == 0){
// assumed to be running form within Intellij
workers = 4
title = "bible"
N = 8
fileName = "./${title}.txt"
outFileName = "./${title}PoG"
}
else {
// assumed to be running via runDemo
String folder = args[0]
title = args[2]
fileName = workingDirectory + "/src/main/groovy/GPP_Demos/${folder}/${title}.txt"
outFileName = workingDirectory + "/src/main/groovy/GPP_Demos/${folder}/${title}PoG"
workers = Integer.parseInt(args[1])
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
 
for ( g in 0..< workers) resultDetails << rDetails
 
System.gc()
print "PoG, $doFileOutput, $title, $workers, $N, "
def startime = System.currentTimeMillis()
 

//NETWORK


def concordancePoG = new TaskParallelOfGroupCollects(
    eDetails: dDetails,
    rDetails: resultDetails,
    stages: 3,
    stageOp: [cd.valueList, cd.indicesMap, cd.wordsMap],
    workers: workers,
    stageModifier: null
    )

concordancePoG .run()

//END

 
 
def endtime = System.currentTimeMillis()
println " ${endtime - startime} "
 
 
