package GPP_Demos.mceSort

import jcsp.lang.*
import groovyJCSP.*
 
import groovyParallelPatterns.DataDetails
import groovyParallelPatterns.ResultDetails
import groovyParallelPatterns.functionals.matrix.MultiCoreEngine
import groovyParallelPatterns.terminals.Collect
import groovyParallelPatterns.terminals.EmitSingle
 

def fileInPath = "D:\\QuickSortInputs\\"
def fileOutPath = "D:\\QuickSortOutputs\\"
 
int nodes
String inFileName, outFileName
if (args.size() == 0) {
nodes = 8
inFileName = fileInPath + "1m-input.txt"
outFileName = fileOutPath + "1m-outputMCE.txt"
} else {
//    String folder = args[0] not used
nodes = Integer.parseInt(args[1])
inFileName = fileInPath + args[2]
outFileName = fileOutPath + args[3]
}
 
def eDetails = new DataDetails(
dName: MCEsortData.getName(),
dInitMethod: MCEsortData.initMethod,
dInitData: [inFileName, outFileName],
dCreateMethod: MCEsortData.createMethod
)
 
def rDetails = new ResultDetails(
rName: MCEsortResult.getName(),
rInitMethod: MCEsortResult.init,
rCollectMethod: MCEsortResult.process,
rFinaliseMethod: MCEsortResult.finalise
)
 
System.gc()
long startTime = System.currentTimeMillis()
print "mceParSort, $nodes, $inFileName, $outFileName, "
 

//NETWORK

def chan1 = Channel.one2one()
def chan2 = Channel.one2one()

def emit = new EmitSingle(
    // input channel not required
    output: chan1.out(),
    eDetails: eDetails
    )
 
def mce = new MultiCoreEngine(
    input: chan1.in(),
    output: chan2.out(),
    nodes: nodes,
    iterations: 1,
    partitionMethod: MCEsortData.partitionMethod,
    calculationMethod: MCEsortData.calculationMethod,
    updateMethod: MCEsortData.updateMethod
    )
 
def collect = new Collect(
    input: chan2.in(),
    // no output channel required
    rDetails: rDetails
    )

PAR network = new PAR()
 network = new PAR([emit , mce , collect ])
 network.run()
 network.removeAllProcesses()
//END

 
long endTime = System.currentTimeMillis()
println " ${endTime-startTime}"
