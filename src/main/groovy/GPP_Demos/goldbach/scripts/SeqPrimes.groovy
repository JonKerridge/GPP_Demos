package GPP_Demos.goldbach.scripts

import GPP_Demos.goldbach.data.*
import GPP_Library.DataClassInterface as Constants

def startime = System.currentTimeMillis()

def N = 50000
boolean morePrimes = true
def retCode = -1

Sieve sieve = new Sieve()
sieve.initClass([N])
PrimeListSeqSimple primeList = new PrimeListSeqSimple()
primeList.initClass(null)
while (morePrimes){
  def prime = new Prime()
  prime.initClass(null)
  retCode = prime.createInstance([sieve])
  if (retCode == Constants.normalTermination) morePrimes = false
  else if (retCode == Constants.normalContinuation ) primeList.collector(prime)
  else {
    morePrimes = false
    println "error"
  }
}
retCode = primeList.finalise(null)
def endtime = System.currentTimeMillis()
def elapsedTime = endtime - startime
println "Time taken = ${elapsedTime} milliseconds"
