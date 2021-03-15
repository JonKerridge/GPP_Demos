package GPP_Demos.mandelbrot.cluster

class SerializedMandelbrotPixel  extends groovy_parallel_patterns.DataClass {
  int px, py, colour = -1
  double x, y
  int maxIterations
  // constants
  int WHITE = 1
  int BLACK = 0
  String calcColour = "calcColour"

/*
  @Override
  int invoke (int fn, List d){
    if ( fn != mandelbrotOperation) return unexpectedReturnCode("SMP: Incorrect operation code $fn",-1)
    else {
      operation ()
      return completedOK
    }
  }
*/
  int calcColour (List d) {
    double xl = 0.0, yl = 0.0, xtemp = 0.0
    int iterations = 0
    while (((xl * xl)+(yl * yl) < 4) && iterations < maxIterations) {
      xtemp = (xl * xl) - (yl * yl) + x
      yl = (2 * xl * yl) + y
      xl = xtemp
      iterations = iterations + 1
    }
    colour = (iterations < maxIterations) ? WHITE : BLACK
    return completedOK
  }

  String toString(){
    String s = "SMP: [$px, $py] = $x, $y : $colour <> $WHITE"
    return s
//		return null
  }
}
