package GPP_Demos.mandelbrot.cluster

import groovy_parallel_patterns.cluster.*
import groovy_parallel_patterns.connectors.reducers.AnyFanOne
import groovy_parallel_patterns.terminals.Collect
import groovy_jcsp.*
import jcsp.lang.*
import groovy_parallel_patterns.*

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
