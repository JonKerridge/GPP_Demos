package GPP_Demos.goldbach.data

import groovy.transform.CompileStatic

@CompileStatic
class GoldbachRange extends GPP_Library.DataClass {

  int rangeStart = 0
  int rangeEnd = 0
  int partition = -1
  Map goldbachs = [:]   // only required for version that determines all the goldbach factors

  static final String init = "initClass"
  static final String finalise = "finalise"

  int initClass ( List d){
    partition = d[0]
    return completedOK

  }

  int finalise (List d){
    return completedOK
  }
  
  String toString(){
    def s = "Range: $partition, $rangeStart, $rangeEnd"
    return s
  }


}
