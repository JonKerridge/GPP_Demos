package GPP_Demos.goldbach.scripts

import jcsp.lang.*
import groovyJCSP.*
 
import GPP_Library.DataDetails
import GPP_Library.LocalDetails
import GPP_Library.ResultDetails
import GPP_Library.functionals.workers.Worker
import GPP_Library.terminals.Collect
import GPP_Library.terminals.EmitWithLocal
import GPP_Demos.goldbach.data.Prime as p
import GPP_Demos.goldbach.data.Sieve as s
import GPP_Demos.goldbach.data.PartitionedPrimeList as ppl
import GPP_Demos.goldbach.data.PrimeList as pl
 

//usage runDemo goldbach/scripts/RunPrimes resultsFile maxN pWorkers
 
int maxN = 0
int pWorkers = 1    // number of prime workers
 
if (args.size() == 0){
maxN = 100000
}
else {
maxN = Integer.parseInt(args[0])
}
 
System.gc()
print "Primes, $maxN, $pWorkers, "
def startime = System.currentTimeMillis()
 
int N = maxN
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
 
def workerLocal = new LocalDetails(lName: ppl.getName(),
lInitMethod: ppl.init,
lInitData: primeInitData[0],
lFinaliseMethod: ppl.finalise)
 

//NETWORK

def chan1 = Channel.one2one()
def chan2 = Channel.one2one()

def emitter = new EmitWithLocal(
    // input channel not required
    output: chan1.out(),
    eDetails: eDetails)
 
def worker = new Worker(
    input: chan1.in(),
    output: chan2.out(),
    outData: false,
    lDetails: workerLocal,
    function: p.sievePrime)
 
def collector = new Collect(
    input: chan2.in(),
    // no output channel required
    rDetails: rDetails)

PAR network = new PAR()
 network = new PAR([emitter , worker , collector ])
 network.run()
 network.removeAllProcesses()
//END

 
def endtime = System.currentTimeMillis()
println " ${endtime - startime} "
