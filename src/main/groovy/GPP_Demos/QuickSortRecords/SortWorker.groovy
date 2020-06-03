package GPP_Demos.QuickSortRecords


import groovyParallelPatterns.DataClass

public class SortWorker extends DataClass{

  List <Record> recordBuffer

  static String init = "initialise"
  static String inFunction = "iFunc"
  static String workFunction = "wFunc"
  static String outFunction = "oFunc"

  int initialise (List d)	{
    recordBuffer = []
    return completedOK
  }

  int  iFunc (List d, Record o){
    recordBuffer << o
    return completedOK
  }

  int wFunc(){
//    recordBuffer.each {println "$it"}
    quickSortRun(recordBuffer, 0, recordBuffer.size()-1)
    return completedOK
  }

  int currentIndex = 0
  String keyfield

  Record oFunc() {
    if (currentIndex < recordBuffer.size()){
      Record o = recordBuffer[currentIndex]
      this.keyfield = o.keyField
      currentIndex = currentIndex + 1
      return o
    }
    else
      return null
  }

  int partition(List<Record> m, int first, int last){
    String pivotValue
    pivotValue = m[first].dataField
//    println "P1: $first, $last, $pivotValue"
    int left, right
    left = first+1
    right = last
    boolean done
    done = false
    while (!done){
//      println "P2: $left, $right $pivotValue"
      while ((left <= right) && (Record.keyCompare(m[left].dataField, pivotValue))) left = left + 1
//      println "P3: $left, $right $pivotValue"
      while (!Record.keyCompare(m[right].dataField, pivotValue) && (right >= left)) right = right - 1
//      println "P4: $left, $right $pivotValue"
      if (right < left)
        done = true
      else {
        m.swap(left, right)
//        println "swap $left with $right for $pivotValue"
      }
    }
    m.swap(first, right)
    return right

  }

  void quickSortRun(List<Record> b, int first, int last){
//    println "QSR1: $first, $last"
    if (first < last) {
      int splitPoint = partition(b, first, last)
//      println "QSR2: $first, $last, $splitPoint"
      quickSortRun(b, first, splitPoint-1)
      quickSortRun(b, splitPoint+1, last)
    }

  }

}
