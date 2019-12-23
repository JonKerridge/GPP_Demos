package GPP_Demos.QuickSortIntegers


//usage runDemo QuickSort SeqQickSort resultsFile instances

int workers = 1
int instances
if (args.size() == 0){
    instances = 5000
}
else {
//    String folder = args[0] not used
    instances = Integer.parseInt(args[1])
}

print "SeqSort $instances, "

System.gc()
def startime = System.currentTimeMillis()

def qsd = new QSData()
qsd.initClass([instances, workers])
def qsw = new QSWorker()
def qsr = new QSResult()
for ( i in 0..< instances) {
    qsd = new QSData()
    qsd.createInstance(null)
    qsw.iFunc(null, qsd)
}
qsw.wFunc()
def sortedBuffer = qsw.qsBuffer
for ( i in 0..< instances) qsr.collector(sortedBuffer[i])
qsr.finalise(null)

def endtime = System.currentTimeMillis()
println " ${endtime - startime}"
