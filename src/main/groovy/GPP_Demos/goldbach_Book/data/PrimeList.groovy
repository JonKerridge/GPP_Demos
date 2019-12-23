package GPP_Demos.goldbach_Book.data

import GPP_Library.DataClass

class PrimeList extends DataClass{

  List <Integer> listOfPrimes

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
    return completedOK
  }

  String toString(){
//    String s = "$listOfPrimes \n"
    String s = " "
//    s = s + "${listOfPrimes.size()} primes"
    return s
  }
}
