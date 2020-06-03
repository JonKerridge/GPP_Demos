package GPP_Demos.QuickSortRecords

import groovyParallelPatterns.DataClass

public class Record extends DataClass{
  String dataField
  String keyField

  static String initFile = "init"
  static String createRecord = "create"
  static String mergeChoice = "merger"

  static BufferedReader reader = null

  int init (List d){ //[input-file-name]
    File file  = new File((String)d[0])
    reader = file.newReader()
    return completedOK
  }

  int create(List d){
    String line = reader.readLine()
    if (line == null) return normalTermination
    dataField = line
    keyField = dataField[0..9]
    return normalContinuation
  }

  String toString(){
    String s = "$dataField"
    return s
  }

  static boolean keyCompare ( String s1, String s2){
    // assumes that all keys are unique!
    // returns true if s1 is less than s2
    // assumes keys are 10 chars long as per gensort
    int i = 0
    while ((s1[i].equals(s2[i])) && (i < 10) ) i = i + 1
    return (s1[i] < s2[i])
  }

  int merger (List <Record> buffers){
    // this is only called when it is known that  there is valid data
    // in at least one of the buffers elements
    int bSize = buffers.size()
    int i = 0
    while ((i < bSize) && (buffers[i] == null)) i = i + 1
    String currentMinimum
    currentMinimum = buffers[i].dataField
    int minimumLocation
    minimumLocation = i
    i = i + 1
    while (i < bSize){
      if ((buffers[i] != null) && (keyCompare(buffers[i].dataField, currentMinimum))){
        currentMinimum = buffers[i].dataField
        minimumLocation = i
      }
      else
        i = i + 1
    }
    return minimumLocation
  }
}
