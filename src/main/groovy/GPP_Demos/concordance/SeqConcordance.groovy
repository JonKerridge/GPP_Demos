package GPP_Demos.concordance

//usage runDemo concordance SeqConcordance resultFile title N

String title
int N
int minSeqLen = 2
boolean doFileOutput = false
String workingDirectory = System.getProperty('user.dir')
String fileName
String outFileName

if (args.size() == 0){
    // assumed to be running form within Intellij
    title = "bible"
    N = 8
    fileName = "./${title}.txt"
    outFileName = "./${title}Seq"
}
else {
    // assumed to be running via runDemo
    String folder = args[0]
    title = args[1]
    fileName = workingDirectory + "/src/main/groovy/GPP_Demos/${folder}/${title}.txt"
    outFileName = workingDirectory + "/src/main/groovy/GPP_Demos/${folder}/${title}Seq"
    N = Integer.parseInt(args[2])
}

print "Seq, $doFileOutput, $title, $N, $minSeqLen, "
System.gc()
def startime = System.currentTimeMillis()

def cd = new ConcordanceData()
cd.initClass([N, fileName, outFileName])
for ( n in 1 .. N){
  cd.createInstance(null)
  cd.createIntValueList(null)
  cd.createValueIndicesMap(null)
  cd.createWordMap(null)
  def cr = new ConcordanceResults(minSeqLen: 2)
  cr.initClass([minSeqLen, doFileOutput])
  cr.collector(cd)
  cr.finalise(null)
}


def endtime = System.currentTimeMillis()
println " ${endtime - startime} "


