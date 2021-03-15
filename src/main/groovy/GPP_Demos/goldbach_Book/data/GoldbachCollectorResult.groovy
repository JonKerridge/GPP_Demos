package GPP_Demos.goldbach_Book.data

import groovy_parallel_patterns.DataClass

class GoldbachCollectorResult extends DataClass{

  // this version simply does the Goldbach number determination in a Collector process
  // it cannot be made parallel easily. It is based on the SeqGoldbach script and
  // is thus equivalent to it.  The only performance difference will be in
  // the parallel determination of the primes.

  List <Integer> primes
  int maxPrime
  int maxGoldbach
  BitSet gNumbers
  int lastGoldbach

  static String init = "init"
  static String collector = "collect"
  static String finalise = "findMaxGoldbach"

  int init(List d){
    return completedOK
  }

  int collect(PrimeList p){
    primes = p.listOfPrimes
    maxPrime = primes[primes.size() - 1]
    maxGoldbach = maxPrime * 2
    gNumbers = new BitSet(maxGoldbach)
    return completedOK
  }

  boolean isGoldbach(int x){
    int p = 0
    while (primes[p] < x/2 ) p += 1
    for ( i in p ..< primes.size()){
      if (primes.contains(x - primes[i]))
        return true
    }
    return false
  }// isGoldbach


  int findMaxGoldbach(List d) {
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
    print "$lastGoldbach, "
    return completedOK
  } // findMaxGoldbach

}
