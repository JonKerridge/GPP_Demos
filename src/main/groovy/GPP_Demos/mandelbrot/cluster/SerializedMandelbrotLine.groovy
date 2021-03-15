package GPP_Demos.mandelbrot.cluster

class SerializedMandelbrotLine  extends groovy_parallel_patterns.DataClass {
  int []colour		// array of colour values for this line
  double [][] line 	// array of [x,y] values for this line
  int ly = 0			// y coordinate of line
  int width
  int maxIterations
  // constants
  int WHITE = 1
  int BLACK = 0
  String calcColour = "calcColour"

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
