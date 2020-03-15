package GPP_Demos.QuickSortIntegers

import jcsp.lang.*
import groovyJCSP.*
 
import GPP_Demos.QuickSortIntegers.QSData as qsd
import GPP_Demos.QuickSortIntegers.QSResult as qsr
import GPP_Demos.QuickSortIntegers.QSWorker as qsw
import groovyParallelPatterns.DataDetails
import groovyParallelPatterns.LocalDetails
import groovyParallelPatterns.ResultDetails
import groovyParallelPatterns.connectors.reducers.N_WayMerge
import groovyParallelPatterns.connectors.spreaders.OneDirectedList
import groovyParallelPatterns.functionals.groups.ListThreePhaseWorkerList
import groovyParallelPatterns.terminals.Collect
import groovyParallelPatterns.terminals.Emit
 

//usage runDemo QuickSort RunGroupQuickSort resultsFile workers instances
 
 
int workers
int instances
if (args.size() == 0) {
workers = 4
instances = 5000
} else {
//    String folder = args[0] not used
workers = Integer.parseInt(args[1])
instances = Integer.parseInt(args[2])
}
print "QuickSort $workers, $instances, "
 
System.gc()
def startime = System.currentTimeMillis()
 
def emitterDetails = new DataDetails(dName: qsd.getName(),
dInitMethod: qsd.init,
dInitData: [instances, workers],
dCreateMethod: qsd.create)
 
def resultDetails = new ResultDetails(rName: qsr.getName(),
rInitMethod: qsr.init,
rCollectMethod: qsr.collector,
rFinaliseMethod: qsr.finalise)
 
def workerDetails = new LocalDetails(lName: qsw.getName(),
lInitMethod: qsw.init)
 

//NETWORK

def chan1 = Channel.one2one()
def chan2 = Channel.one2oneArray(workers)
def chan2OutList = new ChannelOutputList(chan2)
def chan2InList = new ChannelInputList(chan2)
def chan3 = Channel.one2oneArray(workers)
def chan3OutList = new ChannelOutputList(chan3)
def chan3InList = new ChannelInputList(chan3)
def chan4 = Channel.one2one()

def emitter = new Emit(
    // input channel not required
    output: chan1.out(),
    eDetails: emitterDetails)
 
def fan = new OneDirectedList(
    input: chan1.in(),
    outputList: chan2OutList,
    indexProperty: "batch")
 
def tpws = new ListThreePhaseWorkerList(
    inputList: chan2InList,
    outputList: chan3OutList,
    inputMethod: qsw.inFunction,
    workMethod: qsw.workFunction,
    outFunction: qsw.outFunction,
    lDetails: workerDetails,
    workers: workers)
 
 
def merge = new N_WayMerge(
    inputList: chan3InList,
    output: chan4.out(),
    mergeChoice: qsd.mergeChoice,
    inClassName: qsd.getName())
 
def collector = new Collect(
    input: chan4.in(),
    // no output channel required
    rDetails: resultDetails)

PAR network = new PAR()
 network = new PAR([emitter , fan , tpws , merge , collector ])
 network.run()
 network.removeAllProcesses()
//END

 
 
def endtime = System.currentTimeMillis()
println " ${endtime - startime}"
 
 
