package GPP_Demos.goldbach.data

class Sieve extends GPP_Library.DataClass {

  List sieve = []		//sieve[i] holds prime state for i+1
  int N = 0

  static final String init = "initClass"

  int initClass ( List d){
    N = d[0]
    for ( i in 0..< N) sieve << true
    sieve [0] = false
    return completedOK
  }

  int ci = 0
  int createNextPrime (){
    ci = ci + 1
    while ((ci < N) && (sieve[ci] == false) ) {
      ci = ci + 1
    }
    if (ci == N)
      return -1
    else {
      int p = ci+1
      int np = ci
      np = np + p
      while (np < N){
        sieve[np] = false
        np = np + p
      }
      return p// because sieve[ci] has state for prime ci+1
    }
  }

}
