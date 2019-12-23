package GPP_Demos.goldbach_Book.data

import GPP_Library.DataClass

class Prime extends DataClass{
  int primeValue
  static String init = "initPrime"
  static String getPrime = "getNextPrime"
  static String addPrime = "addPrime"

  int initPrime(List d){
    return completedOK
  }

  int getNextPrime (List d){
    PrimeGenerator localSieve = d[0]
    int p = localSieve.getNextPrime()
    if ( p != -1) {
      primeValue = p
      return normalContinuation
    }
    else
      return normalTermination
  }

  int addPrime (List d){  // no datamodifier in d[0]
    PartitionedSieve sieve = d[1]
    sieve.addNextPrime(primeValue)
    return completedOK
  }

  Prime clone() {
    Prime clonedPrime = new Prime()
    clonedPrime.primeValue = this.primeValue
    return clonedPrime
  }

  String toString( ){
    String s = " $primeValue"
    return s
  }
}
