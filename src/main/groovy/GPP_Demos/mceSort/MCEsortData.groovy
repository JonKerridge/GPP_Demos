package GPP_Demos.mceSort

import GPP_Library.DataClass

class MCEsortData extends DataClass {
  String inFileName = ""
  String outFileName = ""
  List<String> records = []
  static String initMethod = "init"
  static String createMethod = "create"
  static String calculationMethod = "quickSortInit"
  static String partitionMethod = "partitionRecords"
  static String updateMethod = "checkPartsSorted"

  static BufferedReader reader = null
  static BufferedWriter writer = null
  int nodes
  int recordSize
  List<List<Integer>> partitionRanges = []  // a list of 'nodes' partition starts and ends

  int init(List d) { //[input-file-name (include path), output-file-name]
    File inFile = new File((String) d[0])
    reader = inFile.newReader()
    File outFile = new File((String) d[1])
    if (outFile.exists()) outFile.delete()
    writer = outFile.newWriter()
    return completedOK
  } //init

  int create(List d) {
    // assumes we shall use EmitSingle process
    String line = reader.readLine()
    while (line != null) {
//      println "read: $line"
      records << line
      line = reader.readLine()
    }
//    println "read in: ${records.size()}"
    return normalContinuation
  } // create

  void partitionRecords(int nodes) {
    // each element comprises [first record index, last record index] in the partition
    this.nodes = nodes
    this.recordSize = records.size()
//    println "records: $recordSize"
    if (nodes == 1) {
      partitionRanges << [0, (recordSize - 1)]
    } else {
      int partSize = (int) (recordSize / nodes)
      for (p in 0..nodes - 2) {
        partitionRanges << [(p * partSize), (((p + 1) * partSize) - 1)]
      }
      partitionRanges << [((nodes - 1) * partSize), (recordSize - 1)]
    }
//    println "partitions: $partitionRanges"
  } // partitionRecords

  static boolean keyCompare(String s1, String s2) {
    // assumes that all keys are unique!
    // returns true if s1 is less than s2
    // false otherwise
    // assumes keys are 10 chars long as per gensort
    int i = 0
    while ((s1[i].equals(s2[i])) && (i < 10)) i = i + 1
    return (s1[i] < s2[i])
  } // keyCompare

  void quickSortInit(int node) {
    int first = partitionRanges[node][0]
    int last = partitionRanges[node][1]
//      println "running $node; $first, $last"
    quickSortRun(records, first, last)
  } // quickSortInit

  void quickSortRun(List<String> b, int first, int last) {
//    println "QSR1: $first, $last"
    if (first < last) {
      int splitPoint = partition(b, first, last)
//      println "QSR2: $first, $last, $splitPoint"
      quickSortRun(b, first, splitPoint - 1)
      quickSortRun(b, splitPoint + 1, last)
    }
  } // quickSortRun

  int partition(List<String> m, int first, int last) {
    String pivotValue
    pivotValue = m[first]
//    println "P1: $first, $last, $pivotValue"
    int left, right
    left = first + 1
    right = last
    boolean done
    done = false
    while (!done) {
//      println "P2: $left, $right $pivotValue"
      while ((left <= right) && (keyCompare(m[left], pivotValue))) left = left + 1
//      println "P3: $left, $right $pivotValue"
      while ((!keyCompare(m[right], pivotValue)) && (right >= left)) right = right - 1
//      println "P4: $left, $right $pivotValue"
      if (right < left) done = true else {
        m.swap(left, right)
//        println "swap $left with $right for $pivotValue"
      }
    }
    m.swap(first, right)
    return right
  } // partition

  void checkPartsSorted() {
//    int i = 0
//    records.each{rec ->
//      println "$i: $rec"
//      i = i + 1
//    }
    for (n in 0..<nodes) {
      int start = partitionRanges[n][0]
      int last = partitionRanges[n][1] - 1
      for (r in start..last) {
        if (!keyCompare(records[r], records[r + 1])) println "records not sorted at $r and ${r + 1}"
      }
    }
  }
  // called from collector method in MCEsortResult
  void mergeParts() {
    if (nodes == 1) {
      for (i in 0 ..< recordSize) writer.writeLine(records[i])
      writer.flush()
      writer.close()
    }
    else {
      int[] starts = new int[nodes]
      int[] ends = new int[nodes]
      for (n in 0..<nodes) {
        starts[n] = partitionRanges[n][0]
        ends[n] = partitionRanges[n][1] + 1  // to include last record
      }
//    println " starts: $starts; ends $ends"
      boolean merging
      merging = true
      int currentMin

      while (merging) {
        int p
        p = 0
        while ((p < nodes) && (starts[p] == -1)) p = p + 1
        if (p == nodes) merging = false
        else { // still records to merge
          currentMin = starts[p]
          int partition
          partition = p
          int n
          n = p + 1
          boolean foundNewMin
          foundNewMin = false
          while (!foundNewMin && ( n < nodes)) {
            if (starts[n] != -1) {
              int testRec = starts[n]
              if (keyCompare((String) records[testRec], (String) records[currentMin])) {
                currentMin = testRec
                partition = n
              }
            }
            n = n + 1
            if (n == nodes) foundNewMin = true
          } // not found new min
          // currentMin contains the record number to be written in partition
          writer.writeLine(records[currentMin])
          starts[partition] = starts[partition] + 1
          if (starts[partition] == ends[partition]) starts[partition] = -1
//        println "written: $currentMin, $partition, ${starts[partition]}"
        } // end else block
      } // end merging loop
      writer.flush()
      writer.close()
    } // else part
  } // mergeParts
}
