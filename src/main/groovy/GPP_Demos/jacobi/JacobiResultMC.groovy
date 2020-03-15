package GPP_Demos.jacobi

import groovyParallelPatterns.DataClass
import groovy.transform.CompileStatic

@CompileStatic
class JacobiResultMC extends DataClass {

  static String init = "initClass"
  static String collector = "collector"
  static String finalise = "finalise"

  int initClass(List d) {
    // no init data
    return completedOK
  }

  def solutionValues = null

  int collector(JacobiDataMC d) {
    String s = "$d.dataSetName, $d.n x $d.n, $d.iterations, "
    solutionValues = d.M.getByColumn(d.n + 1)
    def preSolution = d.solution.getEntries()
    if (solutionValues == preSolution) s = s + "OK" else s = s + "Not OK"
    print "$s, "
    return completedOK
  }

  int finalise(List d) {
    //solutionValues.each {println "$it"}  // for timing purposes removed
    return completedOK
  }


}
