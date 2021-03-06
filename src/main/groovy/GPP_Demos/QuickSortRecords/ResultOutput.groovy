package GPP_Demos.QuickSortRecords

import groovy_parallel_patterns.DataClass

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

  String keyField

  int readRecord(Record r){
    String s = "${r.dataField}"
    writer.writeLine(s)
    keyField = s[0..9]
    return completedOK
  }

  int finalise(List d){
    writer.flush()
    writer.close()
    return completedOK
  }
}
