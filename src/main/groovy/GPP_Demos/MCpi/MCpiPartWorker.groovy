/**
 *
 */
package GPP_Demos.MCpi
/**
 *
 *
 */

class MCpiPartWorker extends GPP_Library.DataClass {
  int iterations = -1000000
  int within = -1000000
  static final int errorState = -1
  static final String init = "initClass"
  static final String finalise = "finalise"

  public int initClass (List p) {
    iterations = p[0]
    within = p[1]
    return completedOK
  }

  int finalise (List p ){
    return completedOK
  }
}
