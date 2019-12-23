package GPP_Demos.goldbach_Book.data

import GPP_Library.DataClass

class PrimeListSeq extends DataClass{

  // this version of PrimeList is used solely in the SeqGoldbach version

  List <Integer> listOfPrimes
  // these variables are used for the Goldbach analysis
  int maxPrime
  int maxGoldbach
  BitSet gNumbers
  int lastGoldbach

  static String initPrimeList = "initPrimes"
  static String createPrimeList = "createPrimes"

  int initPrimes(List d){
    listOfPrimes = []
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
    maxGoldbach = maxPrime * 2
    gNumbers = new BitSet(maxGoldbach)
    return completedOK
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

  void findMaxGoldbach() {
    int gn
    for ( int g = 6; g <= maxGoldbach; g = g + 2){
      if (isGoldbach(g))
        gNumbers.set(g) // in this case do not need to set but in parallel will need to
      else {
        gn = g
        break
      }
    }
    lastGoldbach = gn - 2
  } // findMaxGoldbach



  String toString(){
//    String s = "$listOfPrimes \n"
    String s = " "
    s = s + "${listOfPrimes.size()} primes"
    return s
  }
}
