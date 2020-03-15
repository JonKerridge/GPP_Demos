package GPP_Demos.QuickSortRecords

import jcsp.lang.*
import groovyJCSP.*
 
import GPP_Demos.QuickSortIntegers.QSData
import GPP_Demos.QuickSortIntegers.QSWorker
import groovyParallelPatterns.DataDetails
import groovyParallelPatterns.LocalDetails
import groovyParallelPatterns.ResultDetails
import groovyParallelPatterns.connectors.reducers.N_WayMerge
import groovyParallelPatterns.connectors.spreaders.OneFanList
import groovyParallelPatterns.functionals.groups.ListThreePhaseWorkerList
import groovyParallelPatterns.terminals.Collect
import groovyParallelPatterns.terminals.Emit
 

//usage runDemo QuickSortRecords RunFileSort resultsFile workers inFile, outFile
 
/*
web sites for relevant information
http://sortbenchmark.org/  results from others
http://www.ordinal.com/gensort.html gensort download page
http://sortbenchmark.org/FAQ-2014.html basic description of sort benchmark
*/
 
def fileInPath = "D:\\QuickSortInputs\\"
def fileOutPath = "D:\\QuickSortOutputs\\"
 
int workers
String inFileName, outFileName
if (args.size() == 0) {
workers = 16
inFileName = fileInPath + "1m-input.txt"
outFileName = fileOutPath + "1m-output.txt"
} else {
//    String folder = args[0] not used
workers = Integer.parseInt(args[1])
inFileName = fileInPath + args[2]
outFileName = fileOutPath + args[3]
}
 
def eDetails = new DataDetails(
dName: Record.getName(),
dInitMethod: Record.initFile,
dInitData: [inFileName],
dCreateMethod: Record.createRecord
)
 
def rDetails = new ResultDetails(
rName: ResultOutput.getName(),
rInitMethod: ResultOutput.initialise,
rInitData: [outFileName],
rCollectMethod: ResultOutput.readRecord,
rFinaliseMethod: ResultOutput.finalise
)
 
def workerDetails = new LocalDetails(
lName: SortWorker.getName(),
lInitMethod: SortWorker.init
)
 
long startTime = System.currentTimeMillis()
 

//NETWORK

def chan1 = Channel.one2one()
def chan2 = Channel.one2oneArray(workers)
def chan2OutList = new ChannelOutputList(chan2)
def chan2InList = new ChannelInputList(chan2)
def chan3 = Channel.one2oneArray(workers)
def chan3OutList = new ChannelOutputList(chan3)
def chan3InList = new ChannelInputList(chan3)
def chan4 = Channel.one2one()

def emit = new Emit(
    // input channel not required
    output: chan1.out(),
    eDetails: eDetails
    )
 
def fan = new OneFanList(
    input: chan1.in(),
    outputList: chan2OutList )
 
def tpws = new ListThreePhaseWorkerList(
    inputList: chan2InList,
    outputList: chan3OutList,
    inputMethod: SortWorker.inFunction,
    workMethod: SortWorker.workFunction,
    outFunction: SortWorker.outFunction,
    lDetails: workerDetails,
    workers: workers)
 
 
def merge = new N_WayMerge(
    inputList: chan3InList,
    output: chan4.out(),
    mergeChoice: Record.mergeChoice,
    inClassName: Record.getName())
 
def collect = new Collect(
    input: chan4.in(),
    // no output channel required
    rDetails: rDetails
    )

PAR network = new PAR()
 network = new PAR([emit , fan , tpws , merge , collect ])
 network.run()
 network.removeAllProcesses()
//END

long endTime = System.currentTimeMillis()
println "QuickSort, $workers, $inFileName, $outFileName,  ${endTime-startTime}"
