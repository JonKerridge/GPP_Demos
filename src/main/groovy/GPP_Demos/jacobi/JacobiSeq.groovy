package GPP_Demos.jacobi

//usage runDemo jacobi JacobiSeq resultsFile title

import GPP_Demos.jacobi.JacobiDataMC as jd
import GPP_Demos.jacobi.JacobiResultMC as jr

String title
String workingDirectory = System.getProperty('user.dir')
String fileName

if (args.size() == 0){
    // running from within Intellij
    title = "Jacobi1024"
    fileName = "./${title}.txt"
}
else {
    // running through runDemo bat file
    String folder = args[0]
    title = args[1]
    fileName = workingDirectory + "/src/main/groovy/GPP_Demos/${folder}/${title}.txt"
}

double margin = 1.0E-16

System.gc()
print "SeqJacobi $title, "
long startTime = System.currentTimeMillis()

def data = new jd()
data.&"init"([fileName])
data.&"create"(null)
data.&"partition"(1)
data.&"doCalculation"(0)
while (data.&"repeat"(margin)){
    data.&"update"()
    data.&"doCalculation"(0)
}
def result = new jr()
result.&"initClass"(null)
result.&"collector"(data)
result.&"finalise"(null)

long endTime = System.currentTimeMillis()
println " ${endTime - startTime}"


