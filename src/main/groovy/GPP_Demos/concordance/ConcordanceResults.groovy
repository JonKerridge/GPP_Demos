package GPP_Demos.concordance


class ConcordanceResults extends groovyParallelPatterns.DataClass {

  String printFileName = ""
  def printWriter = null
  static int minSeqLen = 1

  int errorState = -1

  static final String init = "initClass"
  static final String collector = "collector"
  static final String finalise = "finalise"
  
  boolean doFileOutput = false

  int finalise(List p) {
    return completedOK
  }

  int collector(def o){
    if (doFileOutput) {  
        printFileName = o.outFileName + "_${o.strLen}.txt"
        def printFile = new File(printFileName)
        printWriter = printFile.newPrintWriter()
        def concordanceEntry = " "
        o.wordMap.each {
          concordanceEntry = " "
          if (it.value.size() >= minSeqLen) {
            concordanceEntry = concordanceEntry + it.key + ", "
            concordanceEntry = concordanceEntry + it.value.size() + ", "
            concordanceEntry = concordanceEntry + it.value
            printWriter.println "$concordanceEntry"
          }
        }
        printWriter.flush()
        printWriter.close()
    }
    return completedOK
  }

  int initClass (List d){
    minSeqLen = d[0]
    doFileOutput = d[1]
    return completedOK
  }
}
