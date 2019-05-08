package GPP_Demos.MCpi

//usage runDemo MCpi SeqMCpi resultsFile instances iterations

import GPP_Demos.MCpi.MCpiData as piData
import GPP_Demos.MCpi.MCpiResults as piResults

int instances
int iterations

if (args.size() == 0 ) {
    instances =1024
    iterations = 100000
}
else {
//    String folder = args[0] not required
    instances = Integer.parseInt(args[1])
    iterations = Integer.parseInt(args[2])
}

print "SeqMCpi,  $instances, $iterations, "

  System.gc()
  def startime = System.currentTimeMillis()

  def mcpires = new piResults()
  for ( i in 1 .. instances){
    def mcpi = new piData()
    mcpi.initClass([instances])
    mcpi.createInstance([iterations])
    mcpi.getWithin(null)
    mcpires.collector(mcpi)
  }
  mcpires.finalise(null)

  def endtime = System.currentTimeMillis()
  println " ${endtime - startime}"

