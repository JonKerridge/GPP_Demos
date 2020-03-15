package GPP_Demos.goldbach_Book.data

import groovyParallelPatterns.DataClass

class PrimeListMCE extends DataClass{

  List <Integer> listOfPrimes
  // these variables are used for the Goldbach analysis
  int maxPrime
  int goldbachLimit
  int maxGoldbach
  int partitionSize
  List partitionData
  List goldbachOutcome

  static String initPrimeList = "initPrimes"
  static String createPrimeList = "createPrimes"
  static String partitionGoldbachSpace = "partition"
  static String goldbachDetermination = "findGoldbach"
  static String goldbachUpdate = "update" //required by MCE but just returns

  int initPrimes(List d){
    listOfPrimes = []
    partitionData = []
    goldbachLimit = d[0]
    return completedOK
  }

  int createPrimes (CombinePartitionedSieves cps){
//    println "CreatePrimes ${cps.toString()}"
    cps.sieves.each {s ->
      for ( i in 0 ..< s.primes.length())
        if (s.primes.get(i))
          listOfPrimes << i + s.offset
    }
    maxPrime = listOfPrimes[listOfPrimes.size() - 1]
//    maxGoldbach = maxPrime * 2
    return completedOK
  }

  void partition(int nodes){
    assert (goldbachLimit % nodes == 0) :
      "nodes (%nodes) must divide maxGoldbach ($goldbachLimit) precisely"
    partitionSize = goldbachLimit / nodes // simple mechanism
    goldbachOutcome = []
    for ( n in 0 ..< nodes) goldbachOutcome << -1
    // simple partitioning of equal size
    for ( n in 0 ..< nodes){
      partitionData << n*partitionSize //[offset]
    }
//    println "partitionData = $partitionData"
  }

  boolean isGoldbach(int x){
    int p = 0
    while (listOfPrimes[p] < x/2 ) p += 1
    for ( i in p ..< listOfPrimes.size()){
      if (listOfPrimes.contains(x - listOfPrimes[i]))
        return true
    }
    return false
  }// isGoldbach

  void findGoldbach(int nodeId) {
    int startVal
    int gMax = 0
    startVal = partitionData[nodeId]
//    println "$nodeId startVal is $startVal of size $partitionSize"
    int maxPossibleGValue = startVal + partitionSize - 2
    if (nodeId == 0) startVal = 6 // adjust for initial partition
//    println "$nodeId starts at $startVal up to $maxPossibleGValue"
    for ( int g = startVal; g <= maxPossibleGValue; g = g + 2) {
      if (!isGoldbach(g)) {
//        println "$nodeId failed on $g with $maxPrime"
        gMax = g
        break
      }
      else
        gMax = g
    }
//    println "Node $nodeId has $gMax"
    goldbachOutcome[nodeId] = gMax
  } // findGoldbach

  int update(){
    return completedOK
  }

  String toString(){
//    String s = "$listOfPrimes \n"
    String s = " "
    s = s + "${listOfPrimes.size()} primes"
    return s
  }
}
