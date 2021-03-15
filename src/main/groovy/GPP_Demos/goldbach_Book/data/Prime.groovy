package GPP_Demos.goldbach_Book.data

import groovy_parallel_patterns.DataClass

class Prime extends DataClass{
  int primeValue
  static String init = "initPrime"        // does nothing except return
  static String getPrime = "getNextPrime" // used by emitter
  static String addPrime = "addPrime"     // function in primeGroup

  int initPrime(List d){
    return completedOK
  }

  int getNextPrime (List d){  //[local_worker, [dCreateData = null]]
    PrimeGenerator localSieve = d[0]
    int p = localSieve.getNextPrime()
    if ( p != -1) {
      primeValue = p
      return normalContinuation
    }
    else
      return normalTermination
  }

  int addPrime (List d){  // no data modifier in d[0]
    PartitionedSieve sieve = d[1]
    sieve.addNextPrime(primeValue)
    return completedOK
  }

  Prime clone() {  //required because we use OneSeqCastList spreader
    Prime clonedPrime = new Prime()
    clonedPrime.primeValue = this.primeValue
    return clonedPrime
  }

  String toString( ){
    String s = " $primeValue"
    return s
  }
}
