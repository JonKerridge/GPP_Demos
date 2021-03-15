package GPP_Demos.mandelbrot.cluster

import GPP_Demos.mandelbrot.data.MandelbrotLine as ml
import groovy_parallel_patterns.*
import groovy_parallel_patterns.cluster.*
import groovy_parallel_patterns.cluster.connectors.*
import groovy_parallel_patterns.terminals.Emit
import groovy_jcsp.*
import jcsp.lang.*

class NodeLineEmit implements NodeInterface {

  ChannelInputList requests
  ChannelOutputList responses
  int width = 350
  int height = 200
  int maxIterations = 100
  double pixelDelta = 0.01


  @Override
  public void run() {
    def connect = Channel.one2one()
    def emitDetails = new DataDetails(dName: ml.getName(),
                  dInitMethod: ml.init,
                  dInitData: [width, height, pixelDelta, maxIterations],
                  dCreateMethod: ml.create)

    def emit = new Emit(output : connect.out(),
              eDetails: emitDetails)

    def spreader = new OneNodeRequestedList(input: connect.in(),
                        request: requests,
                        response: responses)
    println "NE: starting Mandelbrot for $width x $height and maxIterations = $maxIterations"
    new PAR([emit, spreader]).run()
    println "NE: terminated"
  }

  @Override
  void connect(ChannelInputList inChannels, ChannelOutputList outChannels) {
    requests = inChannels
    responses = outChannels

  }

}
