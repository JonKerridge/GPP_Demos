package GPP_Demos.mandelbrot.data

import GPP_Demos.mandelbrot.cluster.SerializedMandelbrotLine

class MandelbrotLine extends GPP_Library.DataClass {

  int []colour		// array of colour values for this line
  double [][] line 	// array of [x,y] values for this line
  int ly = 0			// y coordinate of line
  static int maxIterations = 500
  // constants
  static final int WHITE = 1
  static final int BLACK = 0
  // Mandelbrot region transformed to canvas coordinates
  static double minX = -2.5
  static double maxX = 1.0
  static double minY = 1.0
  static double maxY = -1.0
  // canvas size
  static int width = 350				//700
  static int height = 200				//400
  static double pixelDelta = 0.01		//0.005

  static final String init = "initClass"
  static final String create = "createInstance"
  static final String calcColour = "calcColour"

  int initClass ( List d){
    width = d[0]
    height = d[1]
    pixelDelta = d[2]
    maxIterations = d[3]
    return completedOK
  }

  static int lineY = 0

  int createInstance (List d){
    if (lineY == height) return normalTermination
    ly = lineY
    line = new double[width][2]
    0.upto(width-1){ w ->
      line[w][0] = minX + ( w * pixelDelta)
      line[w][1] = minY - (lineY * pixelDelta)
    }
    lineY = lineY + 1
    return normalContinuation
  }

  @Override
  def serialize(){
    def sml = new SerializedMandelbrotLine()
    sml.colour = this.colour
    sml.line = this.line
    sml.ly = this.ly
    sml.width = this.width
    sml.WHITE = this.WHITE
    sml.BLACK = this.BLACK
    sml.maxIterations = this.maxIterations
    return sml
  }
  
  // based on algorithm at https://en.wikipedia.org/wiki/Mandelbrot_set

  int calcColour (List d){
    colour = new int[width]
    0.upto(width-1){ w->
      double xl = 0.0, yl = 0.0, xtemp = 0.0
      int iterations = 0
      while (((xl * xl)+(yl * yl) < 4) && iterations < maxIterations) {
        xtemp = (xl * xl) - (yl * yl) + line[w][0]
        yl = (2 * xl * yl) + line[w][1]
        xl = xtemp
        iterations = iterations + 1
      }
      colour[w] = (iterations < maxIterations) ? WHITE : BLACK
    }
//		println "ML: $colour"
    return completedOK
  }

  String toString(){
    String s = "ML: $ly = "
    0.upto(width-1){ w->
      s = s + "${colour[w]}"
    }
    return s
//		return null
  }
}
