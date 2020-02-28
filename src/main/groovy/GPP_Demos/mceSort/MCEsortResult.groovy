package GPP_Demos.mceSort

import GPP_Library.DataClass

class MCEsortResult extends DataClass{
  static String init = "init"
  static String process = "doMerge"
  static String finalise = "finalise"

  int init (List d){
    return completedOK
  }

  int doMerge (MCEsortData records){
    records.mergeParts()
    return completedOK
  }

  int finalise(List d){
    return completedOK
  }
}