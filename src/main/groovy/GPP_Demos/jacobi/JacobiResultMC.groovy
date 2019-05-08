package GPP_Demos.jacobi

import groovy.transform.CompileStatic
import GPP_Library.DataClassInterface as Constants

@CompileStatic
class JacobiResultMC extends GPP_Library.DataClass {

  List results = []
  static String init = "initClass"
  static String collector = "collector"
  static String finalise = "finalise"

  int initClass ( List d){
    // no init data
    return Constants.completedOK
  }

  int collector ( JacobiDataMC d) {
	String s = "$d.dataSetName, $d.n x $d.n, $d.iterations, "
	def solutionValues = d.M.getByColumn(d.n+1)
    def preSolution = d.solution.getEntries()
    if ( solutionValues == preSolution)
	  s = s + "OK"
    else
	  s = s + "Not OK"
    results << s
    print "$s, "
    return Constants.completedOK
  }

  int finalise ( List d) {
    //results.each {println "$it"}  // for timing purposes removed
    return Constants.completedOK
  }


}
