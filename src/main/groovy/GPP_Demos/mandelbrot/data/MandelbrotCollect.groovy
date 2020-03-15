package GPP_Demos.mandelbrot.data

class MandelbrotCollect extends groovyParallelPatterns.DataClass {

  int blackCount = 0
  int whiteCount = 0
  int points = 0

  static final String init = "initClass"
  static final String collector = "collector"
  static final String finalise = "finalise"

  int initClass ( List d){
    return completedOK
  }

  int finalise( List d){
    //println "$points points processed with $whiteCount white and $blackCount black"
    return completedOK
  }

  int collector( def o){
    def mp = o
    int colour = mp.colour
    points = points + 1
    if (colour == mp.WHITE) whiteCount = whiteCount + 1
    else blackCount = blackCount + 1
    return completedOK
  }
}
