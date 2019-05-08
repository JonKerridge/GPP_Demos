package GPP_Demos.MCpi

//usage runDemo MCpi/SeqMCpiUnrolled resultsFile instances iterations

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

print "SeqMCpiUnrolled, $instances, $iterations, "

System.gc()
def startime = System.currentTimeMillis()

  def mcpi = new MCpiData(iterations: instances * iterations)
  def mcpires = new MCpiResults()

  mcpi.getWithin(null)
  mcpires.collector(mcpi)
  mcpires.finalise(null)

  def endtime = System.currentTimeMillis()
  def elapsedTime = endtime - startime
  println "\t${elapsedTime}"


