package GPP_Demos.mandelbrot.data

import java.awt.*
import java.util.List;
import jcsp.awt.*

class MandelbrotResult extends groovy_parallel_patterns.DataClass {

  static int width
  static int height

  static final String init = "initClass"
  static final String updateDList = "updateDisplayList"
  static final String finalise = "finalise"

  int initClass ( List d){
    // could change size of canvas
    width = d[0]
    height = d[1]
    int pixels = width * height
    Color colour = d[2]
    DisplayList dList = d[3]
    GraphicsCommand[] graphics = new GraphicsCommand[(pixels * 2) + 1]
    graphics[0] = new GraphicsCommand.ClearRect(0,0, width, height)
    // populate the rest of the graphics with initial colour
    for ( x in 0 ..< width){
      for ( y in 0 ..< height){
        int p = ((x * height + y) * 2) + 1
        graphics[p] = new GraphicsCommand.SetColor(colour)
        graphics[p+1] = new GraphicsCommand.FillRect( x,y,1,1)
      }
    }
    // now set the graphics
    dList.set(graphics)
    return completedOK
  }

  int updateDisplayList (MandelbrotPixel mp, DisplayList dList){
    GraphicsCommand [] modifier= new GraphicsCommand [1]
//		def mp = (MandelbrotPixel)o
//		int x = mp.px
//		int y = mp.py
//		int c = mp.colour
    int p = ((mp.px * height + mp.py) * 2) + 1
//		println "UDL: $mp - height: $height modifying $p"

    if ( mp.colour == MandelbrotPixel.WHITE){
      modifier[0] = new GraphicsCommand.SetColor(Color.WHITE)
    }
    else {
      modifier[0] = new GraphicsCommand.SetColor(Color.BLACK)
    }
    dList.change(modifier, p)
//		println "UDL: modified $p"
    return completedOK
  }

  int finalise( List d){
    return completedOK
  }
}
