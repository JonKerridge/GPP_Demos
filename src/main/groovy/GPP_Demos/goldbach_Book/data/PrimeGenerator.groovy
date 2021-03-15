package GPP_Demos.goldbach_Book.data

import groovy_parallel_patterns.DataClass

class PrimeGenerator extends DataClass{

  int maxPrime
  int filterMax
  BitSet filterSieve
  int currentPrime

  static String init = "init"
  static String getPrime = "getNextPrime"

  int init(List d) {
    maxPrime = d[0]
    filterMax = Math.sqrt(maxPrime) + 1
    filterSieve = new BitSet(filterMax)
    currentPrime = 0
    filterSieve.set(2, filterMax, true)
//    println "PG.init $maxPrime, $filterMax, $filterSieve"
    return completedOK
  }

  int getNextPrime(){
    // uses ideas from https://himsen.github.io/pdf/Project1_parallel_algorithms_Torben.pdf
    // find next prime in filterSieve; has bit set true
    while ((currentPrime <= filterMax) && !filterSieve.get(currentPrime))
      currentPrime += 1
    // now see if we have found another prime
    if (currentPrime < filterMax) {
      // clear multiples of new currentPrime
      int i = currentPrime * currentPrime
      while ( i < filterMax){
        filterSieve.clear(i)
        i += currentPrime
      }
      // increment current Prime but return prime found
      currentPrime += 1
      return currentPrime - 1
    }
    else // no more primes
      return -1
  }
}
