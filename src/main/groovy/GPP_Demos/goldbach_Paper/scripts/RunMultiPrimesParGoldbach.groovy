package GPP_Demos.goldbach_Paper.scripts

import jcsp.lang.*
import groovyJCSP.*
 
import GPP_Library.DataDetails
import GPP_Library.GroupDetails
import GPP_Library.LocalDetails
import GPP_Library.ResultDetails
import GPP_Library.connectors.reducers.ListSeqOne
import GPP_Library.connectors.spreaders.OneParCastList
import GPP_Library.connectors.spreaders.OneSeqCastList
import GPP_Library.functionals.groups.ListGroupList
import GPP_Library.terminals.Collect
import GPP_Library.terminals.EmitWithLocal
import GPP_Library.functionals.transformers.CombineNto1
 
import GPP_Demos.goldbach_Paper.data.Prime as p
import GPP_Demos.goldbach_Paper.data.InternalPrimeList as ipl
import GPP_Demos.goldbach_Paper.data.ResultantPrimes as rp
import GPP_Demos.goldbach_Paper.data.Sieve as s
import GPP_Demos.goldbach_Paper.data.PartitionedPrimeList as ppl
import GPP_Demos.goldbach_Paper.data.GoldbachRange as gr
import GPP_Demos.goldbach_Paper.data.GoldbachParCollect as gpc
 

//usage runDemo goldbach_Paper/scripts RunMultiPrimesParGoldbach resultsFile maxN gWorkers pWorkers
 
int maxN
int pWorkers	// pWorkers must divide maxN exactly checked by assertion!
// number of parallel prime sieves
int gWorkers	// number of Goldbach partitions
 
if (args.size() == 0){
// assumed to be running form within Intellij
maxN = 20000
gWorkers = 4
}
else {
// assumed to be running via runDemo
// working directory folder assumed to be in args[0]
maxN = Integer.parseInt(args[1])
gWorkers = Integer.parseInt(args[2])
pWorkers = Integer.parseInt(args[3])
}
 
System.gc()
print "MultiParGB $maxN, $pWorkers, $gWorkers, "
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
 
def g1Details = new GroupDetails(workers: pWorkers,
groupDetails: new LocalDetails [pWorkers])
 
for (w in 0 ..< pWorkers) {
g1Details.groupDetails[w] = new LocalDetails(lName: ppl.getName(),
lInitMethod: ppl.init,
lInitData: primeInitData[w],
lFinaliseMethod: ppl.finalise)
}
 
def g2Details = new GroupDetails(workers: gWorkers,
groupDetails: new LocalDetails [gWorkers])
 
for (w in 0 ..< gWorkers) {
g2Details.groupDetails[w] = new LocalDetails(lName: gr.getName(),
lInitMethod: gr.init,
lFinaliseMethod: ppl.finalise)
g2Details.groupDetails[w].lInitData = [w]
}
 
def combineLocal = new LocalDetails(lName: ipl.getName(),
lInitMethod: ipl.init,)
 
def combineOut = new LocalDetails(lName: rp.getName(),
lInitMethod: rp.init,
lInitData: [pWorkers],
lFinaliseMethod: rp.finalise)
 
def resDetails = new ResultDetails(rName: gpc.getName(),
rInitMethod:gpc.init,
rCollectMethod: gpc.collector,
rFinaliseMethod: gpc.finalise)
 
 

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
def chan6 = Channel.one2oneArray(gWorkers)
def chan6OutList = new ChannelOutputList(chan6)
def chan6InList = new ChannelInputList(chan6)
def chan7 = Channel.one2oneArray(gWorkers)
def chan7OutList = new ChannelOutputList(chan7)
def chan7InList = new ChannelInputList(chan7)
def chan8 = Channel.one2one()

def emitter = new EmitWithLocal(
    // input channel not required
    output: chan1.out(),
    eDetails: eDetails)
 
def spread1 = new OneSeqCastList(
    input: chan1.in(),
    outputList: chan2OutList )
 
def group1 = new ListGroupList(
    inputList: chan2InList,
    outputList: chan3OutList,
    gDetails: g1Details,
    workers: pWorkers,
    outData: false,
    function: p.sievePrime
    )
 
def reduce1 = new ListSeqOne (
    inputList: chan3InList,
    output: chan4.out(),
    )
 
def combine = new CombineNto1(
    input: chan4.in(),
    output: chan5.out(),
    localDetails: combineLocal,
    outDetails: combineOut,
    combineMethod: ipl.toIntegers)
 
def spread2 = new OneParCastList(
    input: chan5.in(),
    outputList: chan6OutList )
 
 
def group2 = new ListGroupList (
    inputList: chan6InList,
    outputList: chan7OutList,
    gDetails: g2Details,
    workers: gWorkers,
    modifier:[[gWorkers], [gWorkers], [gWorkers], [gWorkers] ],
    outData: false,
    function: rp.getRange)
 
def reduce2 = new ListSeqOne (
    inputList: chan7InList,
    output: chan8.out(),
    )
 
def collector = new Collect (
    input: chan8.in(),
    // no output channel required
    rDetails: resDetails)

PAR network = new PAR()
 network = new PAR([emitter , spread1 , group1 , reduce1 , combine , spread2 , group2 , reduce2 , collector ])
 network.run()
 network.removeAllProcesses()
//END

 
 
def endtime = System.currentTimeMillis()
def elapsedTime = endtime - startime
println " ${elapsedTime}"
