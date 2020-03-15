package GPP_Demos.goldbach_Book.scripts

import jcsp.lang.*
import groovyJCSP.*
 
import GPP_Demos.goldbach_Book.data.PrimeListCollect
import groovyParallelPatterns.DataDetails
import groovyParallelPatterns.GroupDetails
import groovyParallelPatterns.LocalDetails
import groovyParallelPatterns.ResultDetails
import groovyParallelPatterns.connectors.reducers.ListMergeOne
import groovyParallelPatterns.connectors.spreaders.OneSeqCastList
import groovyParallelPatterns.functionals.groups.ListGroupList
import groovyParallelPatterns.functionals.transformers.CombineNto1
import groovyParallelPatterns.terminals.Collect
import groovyParallelPatterns.terminals.EmitWithLocal
import groovyParallelPatterns.terminals.TestPoint
import GPP_Demos.goldbach_Book.data.Prime
import GPP_Demos.goldbach_Book.data.PrimeGenerator
import GPP_Demos.goldbach_Book.data.PartitionedSieve as ps
import GPP_Demos.goldbach_Book.data.CombinePartitionedSieves as cps
import GPP_Demos.goldbach_Book.data.PrimeList
 

//usage runDemo goldbach_Book/scripts ParPrimes resultsFile scale primeWorkers
 
int scale
int primeWorkers
 
if (args.size() == 0){
// assumed to be running form within Intellij
scale = 16
primeWorkers = 2
}
else {
// assumed to be running via runDemo
// working directory folder assumed to be in args[0]
scale = Integer.parseInt(args[1])
primeWorkers = Integer.parseInt(args[2])
}
 
int k = 1024
int maxPrime = scale * k
 
assert (maxPrime % primeWorkers == 0):
"primeWorkers ($primeWorkers) MUST divide maxPrime ($maxPrime) exactly"
 
def eDetails = new DataDetails(
dName: Prime.getName(),
dInitMethod: Prime.init,
dCreateMethod: Prime.getPrime,
lName: PrimeGenerator.getName(),
lInitMethod: PrimeGenerator.init,
lInitData: [maxPrime]
)
 
List groupInitialData = []
int partitionSize = maxPrime / primeWorkers
int offset = 0
for (i in 0 ..< primeWorkers){
groupInitialData << [maxPrime, offset, partitionSize]
offset = offset + partitionSize
}
 
List < LocalDetails > primeGroupDetails = []
 
for ( i in 0..< primeWorkers){
primeGroupDetails << new LocalDetails(
lName: ps.getName(),
lInitMethod: ps.init,
lInitData: groupInitialData[i],
lFinaliseMethod: ps.finalise
)
}
GroupDetails gDetails = new GroupDetails(
workers: primeWorkers,
groupDetails: primeGroupDetails
)
 
def combineDetails = new LocalDetails(
lName: cps.getName(),
lInitMethod: cps.init,
)
 
def combineOutput = new LocalDetails(
lName: PrimeList.getName(),
lInitMethod: PrimeList.initPrimeList,
lFinaliseMethod: PrimeList.createPrimeList
)
 
def rDetails = new ResultDetails(
rName: PrimeListCollect.getName(),
rInitMethod: PrimeListCollect.init,
rCollectMethod: PrimeListCollect.collector,
rFinaliseMethod: PrimeListCollect.finalise
)
 
long startTime = System.currentTimeMillis()
 

//NETWORK

def chan1 = Channel.one2one()
def chan2 = Channel.one2oneArray(primeWorkers)
def chan2OutList = new ChannelOutputList(chan2)
def chan2InList = new ChannelInputList(chan2)
def chan3 = Channel.one2oneArray(primeWorkers)
def chan3OutList = new ChannelOutputList(chan3)
def chan3InList = new ChannelInputList(chan3)
def chan4 = Channel.one2one()
def chan5 = Channel.one2one()

def emitter = new EmitWithLocal(
    // input channel not required
    output: chan1.out(),
    eDetails: eDetails
    )
 
def spread = new OneSeqCastList(
    input: chan1.in(),
    outputList: chan2OutList )
 
def primeGroup = new ListGroupList(
    inputList: chan2InList,
    outputList: chan3OutList,
    workers: primeWorkers,
    gDetails: gDetails,
    function: Prime.addPrime,
    outData: false
    )
 
def merger = new ListMergeOne(
    inputList: chan3InList,
    output: chan4.out(),
    )
 
def combiner = new CombineNto1(
    input: chan4.in(),
    output: chan5.out(),
    localDetails: combineDetails,
    outDetails: combineOutput,
    combineMethod: cps.combineMethod
    )
 
def collector = new Collect(
    input: chan5.in(),
    // no output channel required
    rDetails: rDetails
    )

PAR network = new PAR()
 network = new PAR([emitter , spread , primeGroup , merger , combiner , collector ])
 network.run()
 network.removeAllProcesses()
//END

 
long endTime = System.currentTimeMillis()
println "ParPrimes, $maxPrime, $primeWorkers, ${endTime - startTime}"
 
