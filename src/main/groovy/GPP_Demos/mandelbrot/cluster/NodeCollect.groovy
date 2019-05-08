package GPP_Demos.mandelbrot.cluster

import GPP_Library.cluster.*
import GPP_Library.connectors.reducers.AnyFanOne
import GPP_Library.terminals.Collect
import groovyJCSP.*
import jcsp.lang.*
import GPP_Library.*

import GPP_Demos.mandelbrot.data.MandelbrotCollect as mc

class NodeCollect implements NodeInterface {

  ChannelInput inputAny
  int nodes = 2

  @Override
  public void run() {
    println "NC: starting"
    def connect = Channel.one2one()

    def reduce = new AnyFanOne ( inputAny: inputAny,
                     output: connect.out(),
                     sources: nodes)

    def resultDetails = new ResultDetails(rName: mc.getName(),
                    rInitMethod: mc.init,
                    rCollectMethod: mc.collector,
                    rFinaliseMethod: mc.finalise)

    def collector = new Collect(input: connect.in(),
              rDetails: resultDetails)

    println "NC: about to invoke PAR"
    long startTime = System.currentTimeMillis()
    new PAR([reduce, collector]).run()
    long endTime = System.currentTimeMillis()
    println "NC: terminated ${endTime - startTime}"
  }

  @Override
  void connect(ChannelInputList inChannels, ChannelOutputList outChannels) {
    inputAny = inChannels[0]
  }


}
