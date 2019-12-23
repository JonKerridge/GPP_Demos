package GPP_Demos.QuickSortIntegers

import groovy.transform.CompileStatic

@CompileStatic
class QSResult extends GPP_Library.DataClass {

  boolean overall = true
  static String init = "initClass"
  static String collector = "collector"
  static String finalise = "finalise"

  int initClass (List d){
    return completedOK
  }
  int previous = -1 // less than minimum key
  boolean stillTesting = true
  static int count = 0

  int collector (QSData d) {
//		println "Result: $d"
    count = count + 1
    if (stillTesting)
      if (d.index >= previous)
        previous = d.index
      else {
        stillTesting = false
        overall = false
      }
    return completedOK
  }

  int finalise (List d){
    print " $overall, $count "
    return completedOK
  }

}
