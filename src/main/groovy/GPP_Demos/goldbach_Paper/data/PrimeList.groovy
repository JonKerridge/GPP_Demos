package GPP_Demos.goldbach_Paper.data

import groovy.transform.CompileStatic

@CompileStatic
class PrimeList extends GPP_Library.DataClass {

  List primes = []

  static final String init = "initClass"
  static final String collector = "collector"
  static final String finalise = "finalise"


  int initClass (List d){
    return completedOK
  }
/* simple version
  @Override
  int collector(def o){
    primes << o.p
    return completedOK
  }
*/
  int collector(PartitionedPrimeList o){
//		println "C: ${o.N}, ${o.leftHand}, ${o.rightHand} "
    PartitionedPrimeList ppl =  o
//    int n = ppl.N
    int lh = ppl.leftHand
    List sieve = ppl.sieve
    for ( i in 0 ..< sieve.size()){
      if (sieve[i]) {
        int p = i + lh
//				print "$p "
        primes << p
      }
    }
//		println " "
    return completedOK
  }

  int finalise(List p) {

//    println "primes are: "
//    int c = 0
//    for ( i in 0..< primes.size()){
//       print "${primes[i]}\t"
//       c = c + 1
//       if ( c == 10) {
//         println " "
//         c = 0
//       }
//    }
//    println "\nfinished"

    print " ${primes.size()} "
    return completedOK
  }

}

