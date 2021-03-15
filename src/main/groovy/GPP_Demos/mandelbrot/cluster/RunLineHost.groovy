package GPP_Demos.mandelbrot.cluster

import jcsp.net2.tcpip.TCPIPNodeAddress
import jcsp.net2.*
import jcsp.net2.mobile.*
import groovy_parallel_patterns.UniversalSignal
import groovy_parallel_patterns.cluster.NodeNetwork
import groovy_parallel_patterns.cluster.RequestNodeNetwork
import groovy_jcsp.*

int nodes = 2					// must be greater than 1
int width = 700					//700		350
int height = 400				//400		200
int maxIterations = 10
double pixelDelta = 0.005		//0.005		0.01
int workersPerNode = 2

String hostIP = "127.0.0.1"
def hostAddr = new TCPIPNodeAddress(hostIP, 1000)
//def hostAddr = new TCPIPNodeAddress(1000)
Node.getInstance().init(hostAddr)
println "Host running on ${hostAddr.getIpAddress()} for $nodes worker nodes"
// create request channel
def hostRequest = NetChannel.numberedNet2One(1)
// read requests from nodes
def loadChannels = new ChannelOutputList()
def nodeIPs = []
for ( w in 1 .. nodes ) {
  RequestNodeNetwork nodeRequest = hostRequest.read()
  def nodeLoadChannel = NetChannel.one2net(nodeRequest.loadLocation,
                      new CodeLoadingChannelFilter.FilterTX())
  loadChannels.append(nodeLoadChannel)
  nodeIPs << nodeRequest.nodeIP
}
println "Host has read request from all nodes $nodeIPs"
// construct nodeObject for each node
def nodeObjects = []
// and emitter in and out connections
def emitterInConnections = []
def emitterOutConnections = []
0.upto(nodes-1) { n->
  emitterInConnections << 100+n
  emitterOutConnections << [nodeIPs[n], 100]
  nodeObjects << new NodeNetwork ( nodeProcesses: new NodeLineWorker(workers: workersPerNode),
    inConnections : [100],
    outConnections: [[hostIP, 100 + n], [hostIP, 100+nodes]])
}
// send worker objects to each node
for ( w in 0 ..< nodes ) {
  loadChannels[w].write(nodeObjects[w])
}
println "Sent nodeObjects to nodes"
// read a signal form each node to indicate input channel have been created
for ( w in 0 ..< nodes ) {
  hostRequest.read()
}
println "Read in channel creation complete signals from nodes"
// create in channel lists for Emitter and Collector Networks
def collectorInConnections = [100 + nodes]
def inChanLists = NodeNetwork.BuildHostInChannels(emitterInConnections, collectorInConnections)

println "Sending signals to nodes to create out channel connections"

// write a signal to each node to indicate all input channels have been created
for ( w in 0 ..< nodes ) {
  loadChannels[w].write(new UniversalSignal())
}
println "Host creating Emitter and Collector out channel lists"
// create out channel lists for Emitter and Collector
def collectorOutConnections = []
def outChanLists = NodeNetwork.BuildHostOutChannels(emitterOutConnections, collectorOutConnections)
println "Host outChanLists created \n in: $inChanLists, \n out: $outChanLists"
// construct local processes on host node

def emit = new NodeNetwork(nodeProcesses: new NodeLineEmit(maxIterations: maxIterations,
                              width: width,
                              height: height,
                              pixelDelta: pixelDelta))
def collector = new NodeNetwork(nodeProcesses: new NodeLineCollect(nodes: nodes))
def processManagers = NodeNetwork.BuildHostNetwork( emit,
                          collector,
                          inChanLists,
                          outChanLists)
println "Built Host Network"
processManagers[0].start()
processManagers[1].start()
println "started processes running on Host"

processManagers[0].join()
processManagers[1].join()
println "Processes stopped running on Host"




