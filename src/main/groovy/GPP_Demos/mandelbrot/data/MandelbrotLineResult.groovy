package GPP_Demos.mandelbrot.data

import java.awt.*
import java.util.List;
import jcsp.awt.*


class MandelbrotLineResult extends groovy_parallel_patterns.DataClass {

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
    for ( y in 0 ..< height){
      for ( x in 0 ..< width){
        int p = ((x * height + y) * 2) + 1
        graphics[p] = new GraphicsCommand.SetColor(colour)
        graphics[p+1] = new GraphicsCommand.FillRect( x,y,1,1)
      }
    }
    // now set the graphics
    dList.set(graphics)
    return completedOK
  }

  int updateDisplayList (MandelbrotLine ml, DisplayList dList){
    GraphicsCommand [] modifier= new GraphicsCommand [width * 2]
    int lineY = ml.ly
//		println "udl: $ml"
    for ( x in 0 ..< width){
      if ( ml.colour[x] == MandelbrotLine.WHITE){
        modifier[x*2] = new GraphicsCommand.SetColor(Color.WHITE)
      }
      else {
        modifier[x*2] = new GraphicsCommand.SetColor(Color.BLACK)
      }
      modifier[x*2 + 1]= new GraphicsCommand.FillRect( x,lineY,1,1)
    }
    int offset = 1 + lineY * width * 2
    dList.change(modifier, offset)
    return completedOK
  }

  int finalise( List d){
    return completedOK
  }
}
