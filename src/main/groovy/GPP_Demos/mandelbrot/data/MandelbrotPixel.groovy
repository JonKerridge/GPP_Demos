package GPP_Demos.mandelbrot.data

//import GPP_Demos.mandelbrot.cluster.SerializedMandelbrotPixel
import GPP_Demos.mandelbrot.cluster.*

class MandelbrotPixel  extends groovy_parallel_patterns.DataClass {
  int px, py, colour = -1
  double x, y
  static int maxIterations = 500
  // constants
  static final int WHITE = 1
  static final int BLACK = 0
  static final int mandelbrotOperation = 1
  // Mandelbrot region transformed to canvas coordinates
  static double minX = -2.5
  static double maxX = 1.0
  static double minY = 1.0
  static double maxY = -1.0
  // canvas size
  static int width = 350				//700
  static int height = 200				//400
  static double pixelDelta = 0.01		//0.005

    // current pixel coordinates used in createInstance
  static int pXc = 0
  static int pYc = 0

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

  int createInstance (List d){
    if ( pXc == width ){
      pXc = 0
      pYc = pYc + 1
      if (pYc == height)
        return normalTermination
    }
    px = pXc
    py = pYc
    x = minX + (pXc * pixelDelta)
    y = minY - (pYc * pixelDelta)  // for canvas coordinates
    pXc = pXc + 1
    return normalContinuation
  }

//  @Override  // removed with version groovy_parallel_patterns.1.0.9
//  def serialize(){
//    def smp = new SerializedMandelbrotPixel()
//    smp.px = this.px
//    smp.py = this.py
//    smp.x = this.x
//    smp.y = this.y
//    smp.colour = this.colour
//    smp.maxIterations = this.maxIterations
//    smp.calcColour = this.calcColour
//    return smp
//  }


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
    String s = "MP: [$px, $py] = $x, $y : $colour"
    return s
//		return null
  }
}
