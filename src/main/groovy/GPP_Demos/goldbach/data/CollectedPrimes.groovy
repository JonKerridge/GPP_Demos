package GPP_Demos.goldbach.data

class CollectedPrimes extends GPP_Library.DataClass {
    
    List sieve = []
    List primes = []
    int N, leftHand, rightHand
  
    static final String init = "initClass"
    static final String inputMethod = "inputPrime"
    static final String workMethod = "createprimeList"
    static final String outFunction = "outputRP"
    
    int initClass ( List d){
    N = d[0]
    leftHand = d[1]
    rightHand = d[2]
    for ( i in 0..< N) sieve << true
    if ( leftHand == 1 ) sieve[0] = false
    return completedOK
  }
  
  int inputPrime (List parameters) {
      List modifer = parameters[0]
      Prime prime = parameters[1]
      int p = prime.p
      int index = -1
      def rem = leftHand % p
      if (leftHand == 1)
        index = p - 1
      else if (rem == 0)
        index = 0
      else
        index = p - rem
//      println "inP: $p :  ${index}"
      if ( leftHand != 1 ) sieve[index] = false
      index = index + p
      while ( index < rightHand) {
        sieve[index] = false
        index = index + p
      }
      return completedOK
  }
  
  int createprimeList(){
      int prime = 0
      for ( i in 0 ..< N){
          if (sieve[i]) {
            prime = i + leftHand
            primes << prime
          }
      }
      return completedOK      
  }
  
  static boolean doOutput = true
  
  def outputRP () {
      if (doOutput) {
          def outPrimes = new ResultantPrimes()
          primes.each{ p ->
              outPrimes.primes << p
          }
          //println "max prime = ${primes[primes.size() - 1]}"
          doOutput = false
          return outPrimes
      }
      else
          return null
  }
}
