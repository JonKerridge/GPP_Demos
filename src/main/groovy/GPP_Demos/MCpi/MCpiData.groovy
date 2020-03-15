package GPP_Demos.MCpi


/**MCpiData is the object that is written to the Farm to tell the processes the operation they are to undertake.
 * It has a property instances that indicates the number of objects of this class
 * that are to be generated in an Emit process.  It has a further two properties , iterations and within
 * that are used to indicate the number of iterations this object created and then count the number of
 * points thereby created that are within the unit circle.<p>
 *
 * @param instances the number of objects of this type to be generated and emitted into the process Farm
 * @param iterations the number of iterations used, each one generating a new point
 * @param within the number of such points that are within the unit circle
 * @param instance the running count of the instances generated so far
 * @param errorState the error code returned by this class should an error occur
 * @param opcode the operation code used in the invoke method
 *
 *
 */

class MCpiData extends groovyParallelPatterns.DataClass {
  int iterations = 0
  int within = 0
  static int instance = 0
  static int  instances
  static String withinOp = "getWithin"
  static String init = "initClass"
  static String create = "createInstance"


  /**
   * Initialises the class such that instances is set equal to the value of p
   * @param p a list containing one element with the number of instances
   * @return completedOK
   */
  int initClass (List p) {
    instances = p[0]
    return completedOK
  }

  /**
   * Create an instance of this class and increments instance
   * @param d the number of iteration to be undertaken
   * @return normalTermination when instance > instances and normalContinuation otherwise
   */
  int createInstance (List d){
    if ( instance > instances) return normalTermination
    else {
      iterations = d[0]
      within = 0
      instance = instance + 1
      return normalContinuation
    }
  }

  /**
   * Calculates for each iteration an x and y random value 0.0 <= v < 1.0
   * Then determines if the sum of the squares of x and y are <= 1.0 and adds
   * 1 to within if so.
   *
   * @return completedOK
   */
  int getWithin(List d){
    def rng = new Random()
    float x, y
    for ( i in 1 ..iterations){
      x = rng.nextFloat()
      y = rng.nextFloat()
      if ( ((x*x) + (y*y)) <= 1.0 ) within = within + 1
    }
    return completedOK
  }
}
