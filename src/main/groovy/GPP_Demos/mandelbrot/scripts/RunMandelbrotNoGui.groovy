package GPP_Demos.mandelbrot.scripts

import jcsp.lang.*
import groovyJCSP.*
 
import groovyParallelPatterns.connectors.reducers.AnyFanOne
import groovyParallelPatterns.connectors.spreaders.OneFanAny
import groovyParallelPatterns.functionals.groups.AnyGroupAny
import groovyParallelPatterns.terminals.Collect
import groovyParallelPatterns.terminals.Emit
import groovyParallelPatterns.*
 
import GPP_Demos.mandelbrot.data.MandelbrotPixel as mp
import GPP_Demos.mandelbrot.data.MandelbrotCollect as mc
 

// usage runDemo mandelbrot/scripts RunMandelBrotNoGUI workers maxIterations width height delta
 
int workers
int maxIterations
int width                  //1400   700        350
int height                 //800    400        200
double pixelDelta         //0.0025 0.005      0.01
 
 
if (args.size() == 0){
workers = 4
maxIterations = 100
width = 700
height = 400
pixelDelta = 0.005
}
else {
// assumed to be running via runDemo
//    String folder = args[0] not required
workers = Integer.parseInt(args[1])
maxIterations = Integer.parseInt(args[2])
width = Integer.parseInt(args[3])
height = Integer.parseInt(args[4])
pixelDelta = Double.parseDouble(args[5])
}
 
System.gc()
 
print "Pixel noGUI, $width, $height, $maxIterations, $workers, "
long startTime = System.currentTimeMillis()
 
def emitDetails = new DataDetails(dName: mp.getName(),
dInitMethod: mp.init,
dInitData: [width, height, pixelDelta, maxIterations],
dCreateMethod: mp.create)
 
def resultDetails = new ResultDetails(rName: mc.getName(),
rInitMethod: mc.init,
rCollectMethod: mc.collector,
rFinaliseMethod: mc.finalise)
 

//NETWORK

def chan1 = Channel.one2one()
def chan2 = Channel.one2any()
def chan3 = Channel.any2any()
def chan4 = Channel.one2one()

def emit = new Emit(
    // input channel not required
    output: chan1.out(),
    eDetails: emitDetails)
 
def spread = new OneFanAny(
    input: chan1.in(),
    outputAny: chan2.out(),
    destinations: workers)
 
def group = new AnyGroupAny (
    inputAny: chan2.in(),
    outputAny: chan3.out(),
    function: mp.calcColour,
    workers: workers)
 
def reduce = new AnyFanOne (
    inputAny: chan3.in(),
    output: chan4.out(),
    sources: workers)
 
def collector = new Collect(
    input: chan4.in(),
    // no output channel required
    rDetails: resultDetails)

PAR network = new PAR()
 network = new PAR([emit , spread , group , reduce , collector ])
 network.run()
 network.removeAllProcesses()
//END

 
def endtime = System.currentTimeMillis()
println " ${endtime - startTime}"
