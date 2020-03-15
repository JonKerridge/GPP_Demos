package GPP_Demos.goldbach_Paper.scripts

import jcsp.lang.*
import groovyJCSP.*
 
import groovyParallelPatterns.DataDetails
import groovyParallelPatterns.GroupDetails
import groovyParallelPatterns.LocalDetails
import groovyParallelPatterns.ResultDetails
import groovyParallelPatterns.connectors.reducers.ListSeqOne
import groovyParallelPatterns.connectors.spreaders.OneSeqCastList
import groovyParallelPatterns.functionals.groups.ListGroupList
import groovyParallelPatterns.terminals.Collect
import groovyParallelPatterns.terminals.EmitWithLocal
import GPP_Demos.goldbach_Paper.data.Prime as p
import GPP_Demos.goldbach_Paper.data.Sieve as s
import GPP_Demos.goldbach_Paper.data.PartitionedPrimeList as ppl
import GPP_Demos.goldbach_Paper.data.PrimeList as pl
 

//usage runDemo goldbach_Paper/scripts/RunCombiningPrimes resultsFile maxN pWorkers
 
int maxN = 0
int pWorkers = 0    // number of prime workers
 
if (args.size() == 0){
maxN = 100000
pWorkers = 2
}
else {
maxN = Integer.parseInt(args[0])
pWorkers = Integer.parseInt(args[1])
}
 
System.gc()
print "Combining Primes, $maxN, $pWorkers, "
def startime = System.currentTimeMillis()
 
assert((maxN % pWorkers) == 0 )
 
int N = maxN / pWorkers
int filter = Math.sqrt(maxN) + 1
def primeInitData = []
int start = 1
int end = 0
for ( i in 1.. pWorkers){
end = i * N
primeInitData << [ N, start, end]
start = end + 1
}
 
def rDetails = new ResultDetails(rName: pl.getName(),
rInitMethod:pl.init,
rCollectMethod: pl.collector,
rFinaliseMethod: pl.finalise)
 
def eDetails = new DataDetails(dName:  p.getName(),
dInitMethod: p.init,
dCreateMethod: p.create,
lName: s.getName(),
lInitMethod: s.init,
lInitData: [filter])
 
def gDetails = new GroupDetails(workers: pWorkers,
groupDetails: new LocalDetails [pWorkers])
 
for (w in 0 ..< pWorkers) {
gDetails.groupDetails[w] = new LocalDetails(lName: ppl.getName(),
lInitMethod: ppl.init,
lInitData: primeInitData[w],
lFinaliseMethod: ppl.finalise)
}
 

//NETWORK

def chan1 = Channel.one2one()
def chan2 = Channel.one2oneArray(pWorkers)
def chan2OutList = new ChannelOutputList(chan2)
def chan2InList = new ChannelInputList(chan2)
def chan3 = Channel.one2oneArray(pWorkers)
def chan3OutList = new ChannelOutputList(chan3)
def chan3InList = new ChannelInputList(chan3)
def chan4 = Channel.one2one()

def emitter = new EmitWithLocal(
    // input channel not required
    output: chan1.out(),
    eDetails: eDetails)
 
 
def spread = new OneSeqCastList(
    input: chan1.in(),
    outputList: chan2OutList )
 
def group = new ListGroupList (
    inputList: chan2InList,
    outputList: chan3OutList,
    gDetails: gDetails,
    workers: pWorkers,
    outData: false,
    function: p.sievePrime
    )
 
def reduce = new ListSeqOne (
    inputList: chan3InList,
    output: chan4.out(),
    )
 
def collector = new Collect(
    input: chan4.in(),
    // no output channel required
    rDetails: rDetails)

PAR network = new PAR()
 network = new PAR([emitter , spread , group , reduce , collector ])
 network.run()
 network.removeAllProcesses()
//END

 
def endtime = System.currentTimeMillis()
println " ${endtime - startime} "
