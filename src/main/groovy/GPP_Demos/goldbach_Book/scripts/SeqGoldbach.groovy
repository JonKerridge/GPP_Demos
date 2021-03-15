package GPP_Demos.goldbach_Book.scripts

import GPP_Demos.goldbach_Book.data.CombinePartitionedSieves
import GPP_Demos.goldbach_Book.data.PartitionedSieve
import GPP_Demos.goldbach_Book.data.Prime
import GPP_Demos.goldbach_Book.data.PrimeGenerator
import groovy_parallel_patterns.DataClass
import GPP_Demos.goldbach_Book.data.PrimeListSeq

//usage runDemo goldbach_Book/scripts SeqGoldbach resultsFile scale

int scale

if (args.size() == 0){
  // assumed to be running form within Intellij
  scale = 16
}
else {
  // assumed to be running via runDemo
  // working directory folder assumed to be in args[0]
  scale = Integer.parseInt(args[1])
}

int k = 1024
int maxPrime = scale * k

long startTime = System.currentTimeMillis()

def partitionedSieve = new PartitionedSieve()
partitionedSieve.initSieve([maxPrime, 0, maxPrime])
def cps = new CombinePartitionedSieves()
cps.init()
def primeList = new PrimeListSeq()
primeList.initPrimes()
def primeGenerator = new PrimeGenerator()
primeGenerator.init([maxPrime])
def prime = new Prime()
int rc = prime.getNextPrime([primeGenerator])
while (rc != DataClass.normalTermination){
//  int p = prime.primeValue
//  println "$p"
  prime.addPrime([null, partitionedSieve])
  prime = new Prime()
  rc = prime.getNextPrime([primeGenerator])
}
cps.combiner(partitionedSieve)
primeList.createPrimes(cps)
primeList.findMaxGoldbach() // direct call equivalent to MC Engine
long endTime = System.currentTimeMillis()
println "SeqGoldbach, $maxPrime, ${primeList.lastGoldbach}, ${endTime - startTime} msecs"
