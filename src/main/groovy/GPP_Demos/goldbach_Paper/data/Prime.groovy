package GPP_Demos.goldbach_Paper.data

import groovy.transform.CompileStatic

@CompileStatic
class Prime extends groovy_parallel_patterns.DataClass {

  int p
  static final String sievePrime = "sievePrime"
  static final String init = "initClass"
  static final String create = "createInstance"

  int initClass (List d){
    return completedOK
  }

  int createInstance (List d){
    Sieve s = d[0]
    int r = s.createNextPrime()
    if ( r == -1)
      return normalTermination
    else {
      p = r
      return normalContinuation
    }
  }

  int sievePrime ( List d){
    // there is only one function hence no need to check fn
    PartitionedPrimeList ppl = d[1]
    ppl.sieveNextPrime(p)
    return completedOK
  }

  @Override
  def clone(){
    Prime np = new Prime()
    np.p = this.p
    return np
  }
}
