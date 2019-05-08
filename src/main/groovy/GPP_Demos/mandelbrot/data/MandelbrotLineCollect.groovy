package GPP_Demos.mandelbrot.data

class MandelbrotLineCollect extends GPP_Library.DataClass {

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
    def ml = o
    def colour = ml.colour
    int width = colour.size()
    0.upto(width-1){ w->
      points = points + 1
      if (colour[w] == ml.WHITE) whiteCount = whiteCount + 1
      else blackCount = blackCount + 1
    }
    return completedOK
  }
}
