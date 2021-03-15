package GPP_Demos.mandelbrot.cluster

import groovy_parallel_patterns.cluster.NodeInterface
import groovy_parallel_patterns.cluster.connectors.NodeRequestingFanAny
import groovy_parallel_patterns.connectors.reducers.AnyFanOne
import groovy_parallel_patterns.functionals.groups.AnyGroupAny
import groovy_jcsp.*
import jcsp.lang.*

//import SerializedMandelbrotPixel as smp
import GPP_Demos.mandelbrot.data.MandelbrotLine as ml

class NodeLineWorker  implements NodeInterface {

  ChannelOutput request
  ChannelInput response
  ChannelOutput output
  int workers = 4

  @Override
  void connect(ChannelInputList inChannels, ChannelOutputList outChannels) {
    response = inChannels[0]
    request = outChannels[0]
    output = outChannels[1]
  }

  @Override
  public void run() {
    def toGroup = Channel.one2any()
    def fromGroup = Channel.any2one()
    def requester = new NodeRequestingFanAny (request: request,
                        response: response,
                        outputAny: toGroup.out(),
                        destinations: workers)
    def group = new AnyGroupAny(inputAny: toGroup.in(),
                  outputAny: fromGroup.out(),
                  workers: workers,
                  function: ml.calcColour)
    def compressor = new AnyFanOne(inputAny: fromGroup.in(),
                    output: output,
                    sources: workers)
    println "NW: about to run PAR"
    new PAR ([requester, group, compressor]).run()
    println "NW: terminated"
  }

}
