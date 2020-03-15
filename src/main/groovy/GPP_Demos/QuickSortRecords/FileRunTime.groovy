package GPP_Demos.QuickSortRecords

import jcsp.lang.*
import groovyJCSP.*
 
import groovyParallelPatterns.DataDetails
import groovyParallelPatterns.ResultDetails
import groovyParallelPatterns.terminals.Collect
import groovyParallelPatterns.terminals.Emit
 

def fileInPath = "D:\\QuickSortInputs\\"
def fileOutPath = "D:\\QuickSortOutputs\\"
 
String inFileName
if (args.size() == 0) {
inFileName = fileInPath + "1m-input.txt"
} else {
//    String folder = args[0] not used
inFileName = fileInPath + args[1]
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
rInitData: [fileOutPath + "transfer.txt"], // overwritten each run
rCollectMethod: ResultOutput.readRecord,
rFinaliseMethod: ResultOutput.finalise
)
 
 
long startTime = System.currentTimeMillis()

//NETWORK

def chan1 = Channel.one2one()

def emit = new Emit(
    // input channel not required
    output: chan1.out(),
    eDetails: eDetails
    )
 
def collect = new Collect(
    input: chan1.in(),
    // no output channel required
    rDetails: rDetails
    )

PAR network = new PAR()
 network = new PAR([emit , collect ])
 network.run()
 network.removeAllProcesses()
//END

long endTime = System.currentTimeMillis()
println "Transfer, $inFileName, ${endTime-startTime}"
