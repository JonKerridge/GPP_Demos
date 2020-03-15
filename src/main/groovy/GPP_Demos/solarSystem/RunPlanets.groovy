package GPP_Demos.solarSystem

import jcsp.lang.*
import groovyJCSP.*
 
import GPP_Demos.solarSystem.PlanetrySystem as ps
import groovyParallelPatterns.DataDetails
import groovyParallelPatterns.ResultDetails
import groovyParallelPatterns.functionals.matrix.MultiCoreEngine
import groovyParallelPatterns.terminals.Collect
import groovyParallelPatterns.terminals.Emit
import GPP_Demos.solarSystem.PlanetryResult as pr
 

// usage runDemo solarSystem RunPlanets resultFile N nodes iterations
 
int nodes
int N
String workingDirectory = System.getProperty('user.dir')
int iterations
double dt = 1e11
String readPath, writePath
 
if (args.size() == 0){
// assumed to be running form within Intellij
N = 128
nodes = 4
readPath = "./planets_list.txt"
writePath = "./${N}_solar_${nodes}_Par.txt"
iterations = 100
}
else {
// assumed to be running via runDemo
String folder = args[0]
N = Integer.parseInt(args[1])
nodes = Integer.parseInt(args[2])
iterations  = Integer.parseInt(args[3])
writePath = workingDirectory + "/src/main/groovy/GPP_Demos/${folder}/${N}_solar_${nodes}_Par.txt"
readPath = workingDirectory + "/src/main/groovy/GPP_Demos/${folder}/planets_list.txt"
}
 
System.gc()
print "RunPlanets (arrayList of planets solarSystem) $iterations, $N , $nodes, "
long startTime = System.currentTimeMillis()
 
 
def eDetails = new DataDetails (dName: ps.getName(),
dCreateMethod: ps.createMethod,
dInitMethod: ps.initMethod,
dCreateData: [readPath, N, dt])
 
def rDetails = new ResultDetails(rName: pr.getName(),
rInitMethod: pr.init,
rInitData: [writePath,],
rCollectMethod: pr.collector,
rFinaliseMethod: pr.finalise)
 

//NETWORK

def chan1 = Channel.one2one()
def chan2 = Channel.one2one()

def emit = new Emit(
    // input channel not required
    output: chan1.out(),
    eDetails: eDetails)
 
def mcEngine = new MultiCoreEngine (
    input: chan1.in(),
    output: chan2.out(),
    nodes: nodes,
    finalOut: true,
    iterations: iterations,
    partitionMethod: ps.partitionMethod,
    calculationMethod: ps.calculationMethod,
    updateMethod: ps.updateMethod )
 
def collector = new Collect(
    input: chan2.in(),
    // no output channel required
    rDetails: rDetails)

PAR network = new PAR()
 network = new PAR([emit , mcEngine , collector ])
 network.run()
 network.removeAllProcesses()
//END

 
 
long endTime = System.currentTimeMillis()
println " ${endTime - startTime}"
 
 
 
 
 
 
 
 
 
