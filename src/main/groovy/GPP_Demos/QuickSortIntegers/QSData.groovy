package GPP_Demos.QuickSortIntegers

import groovy.transform.CompileStatic

@CompileStatic
class QSData extends GPP_Library.DataClass {

  int index
  String dataValue
  int batch
  static int instance = 1
  static int instances = 1024
  static int workers = 2
  static String init = "initClass"
  static String create = "createInstance"
  static String mergeChoice = "merger"
  static int [] randomNumbers

  int initClass (List d){
    instances = d[0]
    workers = d[1]
    Random r = new Random()
    randomNumbers = r.ints(instances + 1, 0, 2048).toArray();
//		println "random indexes ($instances for $workers) = $randomNumbers"
    return completedOK
  }

  int myInstance
  int createInstance (List d){
    if ( instance > instances) return normalTermination
    else {
      index = randomNumbers[instance]
      myInstance = instance
      dataValue = "Instance $myInstance"
      batch = index % workers
      instance = instance + 1
      return normalContinuation
    }
  }

  static int merger (List<QSData> buffers){
//		buffers.each { println "merger: $it"}
//		println "$buffers"
    // this is only called when it is known that  there is valid data
    // in at least one of the buffers elements
    int bSize = buffers.size()
    int i = 0
    while ((i < bSize) && (buffers[i] == null)) i = i + 1
    int currentMinimum = buffers[i].index
    int minimumLocation = i
    i = i + 1
    while (i < bSize){
      if ((buffers[i] != null) && (buffers[i].index < currentMinimum)){
        currentMinimum = buffers[i].index
        minimumLocation = i
      }
      else
        i = i + 1
    }
    return minimumLocation
  }

  String toString(){
    String s = "QSData: $index, $dataValue, $batch"
    return s
  }

}
