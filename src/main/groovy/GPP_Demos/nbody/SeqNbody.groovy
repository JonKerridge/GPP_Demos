package GPP_Demos.nbody

// usage runDemo nbody SeqNbody outFile N iterations

int N
String workingDirectory = System.getProperty('user.dir')
int iterations
double dt = 1e11
String readPath, writePath

if (args.size() == 0){
    // assumed to be running form within Intellij
    N = 128
    readPath = "./planets_list.txt"
    writePath = "./${N}_planets_Seq.txt"
    iterations = 100
}
else {
    // assumed to be running via runDemo
    String folder = args[0]
    N = Integer.parseInt(args[1])
    iterations  = Integer.parseInt(args[2])
    writePath = workingDirectory + "/src/main/groovy/GPP_Demos/${folder}/${N}_planets_Seq.txt"
    readPath = workingDirectory + "/src/main/groovy/GPP_Demos/${folder}/planets_list.txt"
}

System.gc()
print "SeqNbody, $N, "
long startTime = System.currentTimeMillis()

NbodyData planets = new NbodyData()


planets.&"${planets.initMethod}"([readPath, N, dt])
planets.&"${planets.createMethod}"(null)

// there is only 1 node
planets.&"${planets.partitionMethod}"(1)

// now simulate the iterations

for ( i in 0 ..< iterations){
    planets.&"${planets.calculationMethod}"(0)
    planets.&"${planets.updateMethod}"()
}

// now collect the results
NbodyResults results = new NbodyResults()

results.&"${results.init}"([writePath])
results.&"${results.collector}"(planets)
results.&"${results.finalise}"(null)

long endTime = System.currentTimeMillis()
println " ${endTime - startTime}"

