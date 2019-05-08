package GPP_Demos.mandelbrot.cluster

import GPP_Library.cluster.NodeInterface
import GPP_Library.cluster.connectors.NodeRequestingFanAny
import GPP_Library.connectors.reducers.AnyFanOne
import GPP_Library.functionals.groups.AnyGroupAny
import groovyJCSP.*
import jcsp.lang.*

//import SerializedMandelbrotPixel as smp
import GPP_Demos.mandelbrot.data.MandelbrotPixel as mp

class NodeWorker  implements NodeInterface {

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
                  function: mp.calcColour)
    def compressor = new AnyFanOne(inputAny: fromGroup.in(),
                    output: output,
                    sources: workers)
    println "NW: about to run PAR"
    new PAR ([requester, group, compressor]).run()
    println "NW: terminated"
  }

}
