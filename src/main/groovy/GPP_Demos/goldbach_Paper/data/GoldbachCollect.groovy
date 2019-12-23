package GPP_Demos.goldbach_Paper.data

import groovy.transform.CompileStatic

@CompileStatic
class GoldbachCollect extends GPP_Library.DataClass {

  int minGoldbach = 6
  int maxGoldbach = 0

  static final String init = "initClass"
  static final String collector = "collector"
  static final String finalise = "finalise"

  int initClass(List p){
    return completedOK
  }

  int collector ( def o) {
    def rp = (ResultantPrimes)o
    List primes = rp.primes
    int primesSize = primes.size()
//		println "$primesSize: $primes"
    int p1 = 0
    int p2 = 0
    int gNext = 6
    int pMax = 0
    boolean searching = true
    boolean found = false
    int sum

    while (searching){
      // determine pMax
      while (( pMax < primesSize) && (((int) primes[pMax]) * 2 < gNext)) pMax = pMax + 1
      if (pMax == primesSize) searching = false
      else {
        p1 = pMax
        p2 = pMax
        sum = ((int) primes[p1]) + ((int) primes[p2])
        if ( sum == gNext) {
    //			println "\t\tFOUND $gNext at [$p1, $p2] and ${primes[p1]} + ${primes[p2]}"
          gNext = gNext + 2
        }
        else {
          while ((p2 >= 0) && ((sum = (((int) primes[p1]) + ((int) primes[p2]))) != gNext)) p2 = p2 - 1
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
              while ( (p1 < primesSize) && (p2 >= 0) && ((sum = (((int) primes[p1]) + ((int) primes[p2]))) != gNext) ) {
                p2 = p2 -1
    //						println "$gNext with $sum at $p1, $p2"
              }
              if (sum == gNext) {
    //						println "\t\tFOUND $gNext at [$p1, $p2] and ${primes[p1]} + ${primes[p2]}"
                gNext = gNext + 2
                found = true
              } else 	if (p1 == primesSize) {
                    searching = false
                    found = true // to break loop
                  }
            }
          }
        }
      } // end else
    //	Ask.Int("Continue with a 1" , 0, 2)
    } // end searching
    maxGoldbach = gNext - 2
    return completedOK
  }

  int finalise (List p) {
    print " $minGoldbach to $maxGoldbach, "
    return completedOK
  }

}
