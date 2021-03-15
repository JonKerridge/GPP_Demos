package GPP_Demos.goldbach_Book.data

import groovy_parallel_patterns.DataClass

class CombinePartitionedSieves extends DataClass{

  List <PartitionedSieve> sieves

  static String init = "init"
  static String combineMethod = "combiner"

  int init(List d){
    sieves = []
    return completedOK
  }

  int combiner(PartitionedSieve ps) {
    sieves << ps
//    println "CPS $ps"
    return completedOK
  }

  String toString(){
    String s = ""
    sieves.each {ps ->
      s = s + "${ps.offset}\n"
      s = s + "${ps.primes}"
      return s
    }
  }

}
