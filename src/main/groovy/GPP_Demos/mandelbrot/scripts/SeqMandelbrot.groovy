package GPP_Demos.mandelbrot.scripts

import GPP_Demos.mandelbrot.data.MandelbrotLine
import GPP_Demos.mandelbrot.data.MandelbrotLineCollect
import groovyParallelPatterns.DataClass as Constants

// usage runDemo mandelbrot/scripts SeqMbrot resultsFile maxInterations width height pixeldelta

int maxIterations
int width                  //1400   700        350
int height                 //800    400        200
double pixelDelta         //0.0025 0.005      0.01


if (args.size() == 0){
    maxIterations = 100
    width = 700
    height = 400
    pixelDelta = 0.005
}
else {
    // assumed to be running via runDemo
//    String folder = args[0] not required
    maxIterations = Integer.parseInt(args[1])
    width = Integer.parseInt(args[2])
    height = Integer.parseInt(args[3])
    pixelDelta = Double.parseDouble(args[4])
}

System.gc()

print "Seq Mbrot, $maxIterations, $width, $height, $pixelDelta, "
long startTime = System.currentTimeMillis()

def ml = new MandelbrotLine()
def mlc = new MandelbrotLineCollect()
ml.initClass([width, height, pixelDelta, maxIterations])
def mbl = new MandelbrotLine()
int retcode = mbl.createInstance(null)
while  ( retcode ==  Constants.normalContinuation){
    mbl.calcColour(null)
    mlc.collector(mbl)
    mbl = new MandelbrotLine()
    retcode = mbl.createInstance(null)
}

print "W: ${mlc.whiteCount} B: ${mlc.blackCount} "

def endtime = System.currentTimeMillis()
println " ${endtime - startTime}"





