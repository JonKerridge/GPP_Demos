package GPP_Demos.mceSort

def fileInPath = "D:\\QuickSortInputs\\"
def fileOutPath = "D:\\QuickSortOutputs\\"

int nodes
String inFileName, outFileName
if (args.size() == 0) {
  nodes = 1
  inFileName = fileInPath + "1m-input.txt"
  outFileName = fileOutPath + "1m-outputSeqMCE.txt"
} else {
//    String folder = args[0] not used
  nodes = 1
  inFileName = fileInPath + args[1]
  outFileName = fileOutPath + args[2]
}

System.gc()
long startTime = System.currentTimeMillis()
print "seqMCESort, $nodes, $inFileName, $outFileName, "

def outcome = new MCEsortResult()
def data = new MCEsortData()
data.init([inFileName, outFileName])
data.create(null)
data.partitionRecords(nodes)
data.quickSortInit(0)
data.checkPartsSorted()
outcome.init(null)
outcome.doMerge(data)
outcome.finalise(null)

long endTime = System.currentTimeMillis()
println " ${endTime-startTime}"
