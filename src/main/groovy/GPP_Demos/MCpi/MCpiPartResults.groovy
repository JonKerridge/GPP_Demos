package GPP_Demos.MCpi


class MCpiPartResults extends GPP_Library.DataClass {
  def piValue
  static int iterationSum = 0
  static int withinSum = 0
  static final String finalise = "finalise"
  static final String collector = "collector"
  static final String initClass = "initClass"

  int errorState = -1

  int finalise(List p) {
    def pi = 4.0 * ((float) withinSum / (float) iterationSum)
//    print "$iterationSum\t$withinSum\t$pi"

//		println "Math value of pi is ${Math.PI}"
    return completedOK
  }
  int collector(def o){
    iterationSum = iterationSum + o.iterations
    withinSum = withinSum + o.within
    return completedOK
  }

  int initClass (List d){
    return completedOK
  }

}
