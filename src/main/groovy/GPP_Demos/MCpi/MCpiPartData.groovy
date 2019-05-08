/**
 *
 */
package GPP_Demos.MCpi
/**
 *
 */

class MCpiPartData extends GPP_Library.DataClass {
  int iterations = 0
  static int instance = 1
  static int  instances
  static final int errorState = -1
  static final String initClass = "initClass"
  static final String createInstance = "createInstance"
  static final String doCalc = "operation"

  public int initClass (List p) {
    instances = p[0]
    return completedOK

  }

  int createInstance (List d){
    if ( instance > instances) return normalTermination
    else {
      iterations = d[0]
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
  def operation(List d){
    MCpiPartWorker wc = d[1]
    wc.iterations = wc.iterations + iterations
    def rng = new Random()
    float x, y
    for ( i in 1 ..iterations){
      x = rng.nextFloat()
      y = rng.nextFloat()
      if ( ((x*x) + (y*y)) <= 1.0 ) wc.within = wc.within + 1
    }
    return completedOK
  }
}
