package GPP_Demos.goldbach_Paper.data

import groovy.transform.CompileStatic

@CompileStatic
class GoldbachParCollect extends GPP_Library.DataClass {

  Map rangeMap = [:]
  static  int noOverlap = -100

  static  String init = "initClass"
  static  String collector = "collector"    // "collectorGoldbachs"
  static  String finalise = "finalise"     // "finaliseGoldbachs"

  int initClass ( List d){
    return completedOK

  }
  
  int collectorGoldbachs (def o){
      def gr = (GoldbachRange)o
//      println "modified goldbachs ${gr.partition}"    
//      gr.goldbachs.each {println "$it"}
      gr.goldbachs.each{k, v ->
          List currentValue = rangeMap.get(k)
          if ( currentValue == null) { // first time this golbach number has occured
              rangeMap.put(k, v)
          }
          else rangeMap.put(k, currentValue << v)
      }
      return completedOK
  }
  
  int finaliseGoldbachs(List d) {
      List keys = rangeMap.keySet().toSorted()
//      keys.each{ k ->
//          println "$k -> ${rangeMap.get(k)}"
//      }
      int keySize = keys.size()
      int current = (int)keys[0]
      int i = 1
      while (((int)keys[i] - current) == 2) {
          current = (int)keys[i]
          i = i + 1
      }   
//      println "last Goldbach number is $current"
      return completedOK
  }

  int collector (def o){
    def gr = (GoldbachRange)o
    int partition = gr.partition
    int rangeMin = gr.rangeStart
    int rangeMax = gr.rangeEnd
    rangeMap.put(partition, [rangeMin, rangeMax])
    return completedOK
  }

  int finalise (List d){
//		rangeMap.each {
//			println " Partition: ${it.key} is $it.value"
//		}
    int gMin = ((List) rangeMap.get(0))[0]
    int gMax = ((List) rangeMap.get(0))[1]
    int mapSize = rangeMap.size()
    if (mapSize == 2) {
      int test = ((List) rangeMap.get(1))[0]
      if ( gMax <  test ) gMax = -1
      else gMax = ((List) rangeMap.get(1))[1]
    }
    else {
      for ( r in 1 ..< (mapSize-1)){
        int test = ((List) rangeMap.get(r))[0]
        if ( gMax < test) {
          gMax = -1
          break
        }
        gMax = ((List) rangeMap.get(r+1))[1]
      }
    }
    print " $gMin to $gMax, "
    return completedOK
  }
  
}
