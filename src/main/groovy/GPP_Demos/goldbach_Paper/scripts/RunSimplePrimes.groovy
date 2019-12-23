package GPP_Demos.goldbach_Paper.scripts

import jcsp.lang.*
import groovyJCSP.*
 
import GPP_Library.DataDetails
import GPP_Library.ResultDetails
import GPP_Library.terminals.Collect
import GPP_Library.terminals.EmitWithLocal
 
import GPP_Demos.goldbach_Paper.data.Prime as p
import GPP_Demos.goldbach_Paper.data.PrimeListSeqSimple as pl
import GPP_Demos.goldbach_Paper.data.Sieve as s
 

//usage runDemo goldbach_Paper/scripts/RunSimplePrimes N
 
int N = 0
 
if (args.size() == 0){
N = 50000
}
else {
N = Integer.parseInt(args[0])
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
lInitData: [N])
print "Simple Primes, "
def startime = System.currentTimeMillis()
 

//NETWORK

def chan1 = Channel.one2one()

def emitter = new EmitWithLocal(
    // input channel not required
    output: chan1.out(),
    eDetails: eDetails)
 
def collector = new Collect(
    input: chan1.in(),
    // no output channel required
    rDetails: rDetails)

PAR network = new PAR()
 network = new PAR([emitter , collector ])
 network.run()
 network.removeAllProcesses()
//END

 
def endtime = System.currentTimeMillis()
def elapsedTime = endtime - startime
println "${elapsedTime} "
