package GPP_Demos.solarSystem

import groovy.transform.CompileStatic
import groovyParallelPatterns.DataClassInterface as constants

@CompileStatic
class PlanetryResult extends groovyParallelPatterns.DataClass {

    String printFileName = ""
    PrintWriter printWriter = null
    File printFile = null

    static final String init = "initClass"
    static final String collector = "collector"
    static final String finalise = "finalise"

    int initClass (List d){
        printFileName = d[0]
        printFile = new File(printFileName)
        printWriter = printFile.newPrintWriter()
        return constants.completedOK
      }

  int collector(PlanetrySystem o) {
    int N = o.N
    for ( p in 0..<N ) {
        printWriter.println "${o.planets[p]} "
    }
    printWriter.flush()
    printWriter.close()
    return constants.completedOK
  }
  int finalise(List p) {
      return constants.completedOK
    }

}
