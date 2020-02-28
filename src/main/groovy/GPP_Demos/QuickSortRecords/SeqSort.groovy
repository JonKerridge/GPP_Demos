package GPP_Demos.QuickSortRecords

import GPP_Library.DataClass as dc
import GPP_Demos.QuickSortRecords.Record
import GPP_Demos.QuickSortRecords.SortWorker

long startTime = System.currentTimeMillis()

def record = new Record()
def sortWorker = new SortWorker()

def fileInPath = "D:\\QuickSortInputs\\"
def fileOutPath = "D:\\QuickSortOutputs\\"

String inFileName, outFileName

if (args.size() == 0) {
  inFileName = fileInPath + "1m-input.txt"
  outFileName = fileOutPath + "1m-output-seq.txt"
} else {
//    String folder = args[0] not used
  inFileName = fileInPath + args[1]
  outFileName = fileOutPath + args[2]
}

record.init([inFileName])
sortWorker.initialise([])
def rec = new Record()
while ( rec.create([]) != dc.normalTermination){
  sortWorker.iFunc([], rec)
  rec = new Record()
}
sortWorker.wFunc()
// no need to merge as we have a single buffer of sorted records
def resultOutput = new ResultOutput()
resultOutput.initialiseOutFile([outFileName])
Record r = sortWorker.oFunc()
while ( r != null) {
  resultOutput.readRecord((r))
  r = sortWorker.oFunc()
}
resultOutput.finalise([])
long endTime = System.currentTimeMillis()
println "QuickSort, SEQ, $inFileName, $outFileName,  ${endTime-startTime}"

