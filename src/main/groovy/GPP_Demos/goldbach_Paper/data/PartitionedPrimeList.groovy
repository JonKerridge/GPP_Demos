package GPP_Demos.goldbach_Paper.data

import groovy.transform.CompileStatic

@CompileStatic
class PartitionedPrimeList extends groovyParallelPatterns.DataClass {

  List sieve = []
  int N, leftHand, rightHand

  static final String init = "initClass"
  static final String finalise = "finalise"

    int initClass ( List d){
    N = d[0]
    leftHand = d[1]
    rightHand = d[2]
    for ( i in 0..< N) sieve << true
    if ( leftHand == 1 ) sieve[0] = false
    return completedOK
  }

  void sieveNextPrime (int p){
//		print "SNP: $p"
    int index = -1
    def rem = leftHand % p
    if (leftHand == 1)
      index = p - 1
    else if (rem == 0)
      index = 0
    else
      index = p - rem
//		println "SNP: $p :  ${index}"
    if ( leftHand != 1 ) sieve[index] = false
    index = index + p
    while ( index < rightHand) {
      sieve[index] = false
      index = index + p
    }
  }

  int finalise (List d){
    return completedOK
  }
  

}
