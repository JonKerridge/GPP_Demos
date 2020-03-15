package GPP_Demos.concordance

import groovyParallelPatterns.DataClass

class ConcordanceCombine extends DataClass{
  def wordBufferAll = []
  def intValuesAll = []
  String outFileName
  def maxN = -1
  static final int errorState = -1
  static final String appendBuff = "appendBuffers"

  static final String init = "initClass"
  static final String create = "createInstance"
  static final String finalise = "finalise"

  int initClass( List d) {
    maxN = d[0]
    outFileName = d[1]
    return completedOK
  }

  int appendBuffers (ConcordanceWords inClass) {
    wordBufferAll << inClass.unPunctuatedWords
    intValuesAll << inClass.intValues
    return completedOK
  }

  int finalise(List p) {
    return completedOK
  }


}
