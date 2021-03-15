package GPP_Demos.nbody

import groovy_parallel_patterns.DataClassInterface as constants
import groovy.transform.CompileStatic

@CompileStatic
class NbodyResults extends groovy_parallel_patterns.DataClass {

  String printFileName = ""
  PrintWriter printWriter = null
  File printFile = null

  static final String init = "initClass"
  static final String collector = "collector"
  static final String finalise = "finalise"

// usage of matric entries

  static final int rx = 0
  static final int ry = 1
  static final int vx = 2
  static final int vy = 3
  static final int mass = 4
  static final int id = 5
  static final int fx = 6
  static final int fy = 7

  int initClass (List d){
    printFileName = d[0]
//        print "WriteFile = $printFileName, "
    printFile = new File(printFileName)
//        printFile.createNewFile()
    printWriter = printFile.newPrintWriter()
    return completedOK
  }

  int collector(NbodyData o) {
    int N = o.N
    for ( p in 0..<N ) {
      printWriter.print "${o.planets.entries[p][rx]} "
      printWriter.print "${o.planets.entries[p][ry]} "
      printWriter.print "${o.planets.entries[p][vx]} "
      printWriter.print "${o.planets.entries[p][vy]} "
      printWriter.print "${o.planets.entries[p][mass]} "
      printWriter.println "${o.planets.entries[p][id]} "
    }
    printWriter.flush()
    printWriter.close()
    return completedOK
  }

  int finalise(List p) {
      return completedOK
    }

}
