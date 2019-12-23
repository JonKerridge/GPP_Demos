package GPP_Demos.goldbach_Book.data

import GPP_Library.DataClass

class PartitionedSieve extends DataClass{
  int maxPrime
  BitSet primes
  int offset
  int partitionSize

  static String init = "initSieve"
  static String finalise = "finalise"

  int initSieve(List d){
    maxPrime = (int)d[0]
    offset = (int)d[1]
    partitionSize = (int)d[2]
    assert (partitionSize >= Math.sqrt(maxPrime)) :
        "PartitionedSieve : $partitionSize too small for $maxPrime"
    primes = new BitSet(partitionSize)
    if ( offset == 0)
      primes.set(2, partitionSize, true)
    else
      primes.set(1, partitionSize, true)
    // because the partition size is such that bit[0] in each partition
    // is bound not to be prime
    return completedOK
  }

  void addNextPrime(int p) {
    int bit
    int limit
    limit = offset + partitionSize - 1

    if ( offset == 0)
      bit = p + p   // ensures that the bit corresponding to p is true
    else
      bit = p - (offset % p)
    // now go through the primes sieve removing multiples
    while ( bit <= limit) {
      primes.clear(bit)
      bit = bit + p
    }
  }

  int finalise(List d){
    return completedOK
  }

  String toString(){
    int primeCount = 0
    for ( i in 0..primes.length())
      if (primes.get(i))
        primeCount += 1
    String s = "$offset has $primes \n $primeCount primes"
    return s
  }
}
