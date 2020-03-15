package GPP_Demos.QuickSortRecords

import groovyParallelPatterns.DataClass

class ResultOutput extends DataClass{

  static BufferedWriter writer = null
  static String initialise = "initialiseOutFile"
  static String readRecord = "readRecord"
  static String finalise = "finalise"

  int initialiseOutFile(List d){
    File file  = new File((String)d[0])
    if (file.exists()) file.delete()
    writer = file.newWriter()
    return completedOK
  }

  int readRecord(Record r){
    String s = "${r.dataField}"
    writer.writeLine(s)
    return completedOK
  }

  int finalise(List d){
    writer.flush()
    writer.close()
    return completedOK
  }
}
