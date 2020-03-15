package GPP_Demos.goldbach_Book.data

import groovyParallelPatterns.DataClass

class MCEResult extends DataClass{

  List outcome
  int partitionSize
  List partitionData
  int maxGoldbachNumber = 0

  static String init = "init"
  static String collector = "collect"
  static String finalise = "determineMaxGoldbach"

  int init(List d){
    return completedOK
  }

  int collect(PrimeListMCE p){
    outcome = p.goldbachOutcome
    partitionSize = p.partitionSize
    partitionData = p.partitionData
    return completedOK
  }

  int determineMaxGoldbach(List d){
    int nodes = outcome.size()
    for ( n in 1..<nodes){
      if (outcome[n-1] + 2 != partitionData[n]) {
        maxGoldbachNumber = -1
        break
      }
    }
    if (maxGoldbachNumber != -1) maxGoldbachNumber = outcome[nodes-1]
    print "${maxGoldbachNumber - 2}, "
    return completedOK
  }
}
