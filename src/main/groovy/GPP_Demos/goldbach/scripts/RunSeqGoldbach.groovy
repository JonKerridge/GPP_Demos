package GPP_Demos.goldbach.scripts

import jcsp.lang.*
import groovyJCSP.*
 
import GPP_Library.DataDetails
import GPP_Library.GroupDetails
import GPP_Library.LocalDetails
import GPP_Library.ResultDetails
import GPP_Library.connectors.reducers.ListSeqOne
import GPP_Library.connectors.spreaders.OneSeqCastList
import GPP_Library.functionals.groups.ListGroupList
import GPP_Library.terminals.Collect
import GPP_Library.terminals.EmitWithLocal
import GPP_Library.functionals.transformers.CombineNto1
 
import GPP_Demos.goldbach.data.Prime as p
import GPP_Demos.goldbach.data.InternalPrimeList as ipl
import GPP_Demos.goldbach.data.Sieve as s
import GPP_Demos.goldbach.data.ResultantPrimes as rp
import GPP_Demos.goldbach.data.PartitionedPrimeList as ppl
import GPP_Demos.goldbach.data.GoldbachCollect as gc
 

//usage runDemo goldbach/scripts RunSeqGoldbach resultFile maxN
 
int maxN
 
if (args.size() == 0){
maxN = 20000
}
else {
maxN = Integer.parseInt(args[1])
}
int pWorkers = 1	// pWorkers
 
System.gc()
print "SeqGB, $maxN, $pWorkers, "
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
 
 
def eDetails = new DataDetails( dName:  p.getName(),
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
 
def resDetails = new ResultDetails(rName: gc.getName(),
rInitMethod:gc.init,
rCollectMethod: gc.collector,
rFinaliseMethod: gc.finalise)
 
def combineLocal = new LocalDetails(lName: ipl.getName(),
lInitMethod: ipl.init,)
 
def combineOut = new LocalDetails(lName: rp.getName(),
lInitMethod: rp.init,
lInitData: [pWorkers],
lFinaliseMethod: rp.finalise)
 
 

//NETWORK

def chan1 = Channel.one2one()
def chan2 = Channel.one2oneArray(pWorkers)
def chan2OutList = new ChannelOutputList(chan2)
def chan2InList = new ChannelInputList(chan2)
def chan3 = Channel.one2oneArray(pWorkers)
def chan3OutList = new ChannelOutputList(chan3)
def chan3InList = new ChannelInputList(chan3)
def chan4 = Channel.one2one()
def chan5 = Channel.one2one()

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
 
def combine = new CombineNto1(
    input: chan4.in(),
    output: chan5.out(),
    localDetails: combineLocal,
    outDetails: combineOut,
    combineMethod: ipl.toIntegers)
 
 
def collector = new Collect (
    input: chan5.in(),
    // no output channel required
    rDetails: resDetails)

PAR network = new PAR()
 network = new PAR([emitter , spread , group , reduce , combine , collector ])
 network.run()
 network.removeAllProcesses()
//END

 
def endtime = System.currentTimeMillis()
println " ${endtime - startime} "
