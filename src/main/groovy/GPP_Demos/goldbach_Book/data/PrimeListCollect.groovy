package GPP_Demos.goldbach_Book.data

import groovyParallelPatterns.DataClass


class PrimeListCollect extends DataClass{

  static String init = "init"
  static String collector = "collector"
  static String finalise = "finaliseMethod"

  List <Integer> primes

  int init(List d){
    primes = []
    return completedOK
  }

  int collector (PrimeList pl){
    primes = pl.listOfPrimes
    return completedOK
  }

  int finaliseMethod(List d){
    print "${primes.size()}, ${primes[primes.size() - 1]},"
    return completedOK
  }
}
