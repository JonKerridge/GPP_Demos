package GPP_Demos.goldbach_Paper.data

import groovy.transform.CompileStatic

@CompileStatic
class InternalPrimeList extends groovy_parallel_patterns.DataClass {

  List primes = []

  static final String init = "initClass"
  static final String toIntegers = "toIntegers"

  int initClass(List p){
    return completedOK
  }

  int toIntegers (PartitionedPrimeList ppl){
//		def ppl = (PartitionedPrimeList) p[0]
    int n = ppl.N
    int lh = ppl.leftHand
    List sieve = ppl.sieve
    int prime = 0
    for ( i in 0 ..< n){
      if (sieve[i]) {
        prime = i + lh
        primes << prime
      }
    }
    return completedOK
  }

  String toString() {
    String s = "RP: \n"
    int c = 0
    primes.each { p ->
      s = s + "$p\t"
      c = c + 1
      if ( c == 10) {
        s = s + "\n"
        c = 0
      }
    }
    return s
  }

}
