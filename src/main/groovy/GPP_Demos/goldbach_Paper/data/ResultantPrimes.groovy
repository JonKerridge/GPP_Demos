package GPP_Demos.goldbach_Paper.data

import groovy.transform.CompileStatic

@CompileStatic
class ResultantPrimes extends GPP_Library.DataClass {

  List primes = [] // holds prime value from 2 upwards in index 0
  Map goldbachs = [:]   // holds the even numbers as key and a value comprising pairs of primes that sum to the key value
  int workers = -1
//	static final String buildList = "buildList"
//  static  String getRange = "rangeGoldbach"
  static  String getRange = "rangeGoldbach"  // "goldbachFactors"
  static  int errorCode = -10
  static  int partitionSizeMismatch = -100
  static  String init = "initClass"
  static  String finalise = "finalise"

  int initClass(List p){
    workers = p[0]		// contains the number of Goldbach workers
    return completedOK
  }

  int finalise (List p){
    def ipl = (InternalPrimeList) p[0]
    this.primes = ipl.primes.sort()
    return completedOK
  }

  int rangeGoldbach(List p ){
    // p[0] contains the worker's dataModifer list
    // p[1] is a the local worker class
    List modifier = p[0]
    int gWorkers = modifier[0]	// this is also the number of partitions
    GoldbachRange gr = (GoldbachRange)p[1]
    int partition = gr.partition
    int primesTotalSize = primes.size()
    int partitionSpan = (  primesTotalSize /  gWorkers ).intValue()
    int factor = gWorkers == 4 ? 1 : 4
    int overlap = (Math.sqrt(partitionSpan) / 2).intValue() + factor
    int partitionStart = 0
    int partitionEnd = 0
    if (partition == 0) {
      partitionStart = 1
      partitionEnd = partitionSpan + overlap
    }
    else if (partition == (gWorkers - 1)){
      partitionStart = (partitionSpan * partition) - overlap
      partitionEnd = primesTotalSize - 1
    }
    else {
      partitionStart = (partitionSpan * partition) - overlap
      partitionEnd = (partitionSpan * (partition + 1)) + overlap
    }
    gr.rangeStart = 2 * ( (int) (primes[partitionStart]))
//		println "RP: $partition, $primesTotalSize, $partitionSpan, $partitionStart, $partitionEnd, $overlap"
    int p1 = partitionStart
    int p2 = partitionStart
    int gNext = ((int) primes[p1]) + ((int) primes[p2])
    int pMax = partitionStart
    boolean searching = true
    boolean found = false
    int sum

    while (searching){
      // determine pMax
      while (( pMax < partitionEnd) && (((int) primes[pMax]) * 2 < gNext)) pMax = pMax + 1
      if (pMax == partitionEnd) searching = false
      else {
        p1 = pMax
        p2 = pMax
        sum = ((int)primes[p1]) + ((int) primes[p2])
        if ( sum == gNext) {
    //			println "\t\tFOUND $gNext at [$p1, $p2] and ${primes[p1]} + ${primes[p2]}"
          gNext = gNext + 2
        }
        else {
          while ((p2 >= 0) && ((sum = ((int)primes[p1]) + ((int) primes[p2])) != gNext) ) p2 = p2 - 1
          if (sum == gNext) {
    //				println "\t\tFOUND $gNext at [$p1, $p2] and ${primes[p1]} + ${primes[p2]}"
            gNext = gNext + 2
          }
          else {
            // p2 = -1 and gNext has not been found
            found = false
            while (!found) {
              p1 = p1 + 1
              p2 = pMax
              while ( (p1 < partitionEnd) && (p2 >= 0) && ( (sum = ((int)primes[p1]) + ((int) primes[p2])) != gNext) ) {
                p2 = p2 -1
    //						println "$gNext with $sum at $p1, $p2"
              }
              if (sum == gNext) {
    //						println "\t\tFOUND $gNext at [$p1, $p2] and ${primes[p1]} + ${primes[p2]}"
                gNext = gNext + 2
                found = true
              } else 	if (p1 == partitionEnd) {
                    searching = false
                    found = true // to break loop
                  }
            }
          }
        }
      } // end else
    } // end searching
//		println "Goldbach checked partition $partition up from $gMin to ${gNext - 2}"
    gr.rangeEnd = gNext - 2
    return completedOK

  }
  
  int goldbachFactors(List p) { // determines all the factors of each goldbach number
      // p[0] contains the worker's dataModifer list
      // p[1] is a the local worker class
      List modifier = p[0]
      int gWorkers = modifier[0]  // this is also the number of partitions
      GoldbachRange gr = (GoldbachRange)p[1]
      int partitionId = gr.partition
      int primesSize = primes.size()
      int primesSpan = (primesSize / gWorkers ).intValue()
      int maxPrime = primes[primesSize - 1]
      int maxGoldbach = 2 * maxPrime
      int partitionStart = 0
      int partitionEnd = 0
      if (partitionId == 0) {
          partitionStart = 1
          partitionEnd = primesSpan - 1
      }
      else if (partitionId == (gWorkers - 1)) {
          partitionStart = (primesSpan * partitionId)
          partitionEnd = primesSize - 1
    
      }
      else {
          partitionStart = (primesSpan * partitionId)
          partitionEnd = (primesSpan * (partitionId + 1)) - 1    
      }
 //     println "$partitionId, $primesSize, $primesSpan, [ $partitionStart, $partitionEnd ]"
      // now create all the goldbach numbers in the partition
      for ( t in partitionStart .. partitionEnd)    // iterate over the partition
          for ( v in 1 .. (primesSize-1)) {           // iterate over the complete range
              int a = primes[t]
              int b = primes[v]
              int goldbachNumber = a + b
              List goldbachNumberList = gr.goldbachs.get(goldbachNumber) as List
              List goldbachPair = null
              if (a < b) goldbachPair = [a, b]
              else goldbachPair = [b, a]
              if (goldbachNumberList == null) {  // first pair for this number
                  gr.goldbachs.put(goldbachNumber, [goldbachPair])
              }
              else { // have to check if list already contains goldbachPair
                  if ( !(goldbachNumberList.contains(goldbachPair))) {  // have to add goldbachPair to list
                      goldbachNumberList = goldbachNumberList << goldbachPair
                      gr.goldbachs.put(goldbachNumber, goldbachNumberList)
                  }
                  // otherwise the pair is already in the list
              }
          }
//      println "modified goldbachs $partitionId"    
//      gr.goldbachs.each {println "$it"}
      return completedOK
  }

  @Override
  Object clone () {
//		println "RP:clone"
    def rpl = new ResultantPrimes()
    this.primes.each { rpl.primes << it}
    rpl.workers = this.workers
    return rpl
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
