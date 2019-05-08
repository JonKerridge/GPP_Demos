package GPP_Demos.MCpi

import GPP_Demos.MCpi.MCpiData as piData

/**
 * MCpiResults is used to create the final totals of the number of iterations and the number of
 * points within the unit circle.<p>
 *
 * @param iterationSum the total number of iterations generated by the Farm
 * @param withinSum the total sum of the number of random points that are within the unit circle
 * @return completedOK
 */
class MCpiResults extends GPP_Library.DataClass {
  int iterationSum = 0
  int withinSum = 0
  static String init = "initClass"
  static String collector = "collector"
  static String finalise = "finalise"

  /** Finalise is used to calculate the value of pi and then print out the result
   * @param p null
   * @return completedOK
   */
  int finalise(List p) {
    double pi = 4.0 * ((double) withinSum / (double) iterationSum)
//    println """Total Iterations: $iterationSum Points Within : $withinSum pi Value :$pi"""
//    println "Math value of pi is ${Math.PI}"
    print "$pi, "
    return completedOK
  }
  /**
   * Implements the collector method and is used to create the required running sums of total iterations
   * and the number of points within the unit circle
   * @param o this parameter will be passed  as a reference to a MCpiData object
   * @return completedOK
   */
  int collector(Object o){
    iterationSum = iterationSum + ((piData) o).iterations
    withinSum = withinSum + ((piData) o).within
    return completedOK
  }

  /**
   * Initialises the class but in this case nothing needs to be done
   * @param d null
   * @return completedOK
   */
  int initClass (List d){
    return completedOK
  }

}
