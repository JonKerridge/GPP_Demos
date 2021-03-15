package GPP_Demos.QuickSortIntegers

import groovy.transform.CompileStatic

@CompileStatic
class QSWorker extends groovy_parallel_patterns.DataClass {

  List<QSData> qsBuffer = []
  static String init = "nullInitialise"
  static String inFunction = "iFunc"
  static String workFunction = "wFunc"
  static String outFunction = "oFunc"

  int nullInitialise (List d)	{
    return completedOK
  }

  int  iFunc (List d, QSData o){
    qsBuffer << o
    return completedOK
  }

  int wFunc(){
    // does the quicksort of qsBuffer
//		println "doing the quick sort"
//		qsBuffer.each{println "$it"}
    quickSortRun(qsBuffer, 0, qsBuffer.size()-1)
//		println "completed the quick sort"
//		qsBuffer.each{println "$it"}
    return completedOK
  }

  int currentIndex = 0
//	static int bufferSize = qsBuffer.size()

  QSData oFunc() {
//		println "QSW: oFunc : $currentIndex ${qsBuffer.size()}"
    // returns the element of qsBuffer to be output
    if (currentIndex < qsBuffer.size()){
//			QSData o = new QSData (index: qsBuffer[currentIndex].index,
//									dataValue:qsBuffer[currentIndex].dataValue,
//									batch: qsBuffer[currentIndex].batch)
      QSData o = qsBuffer[currentIndex]
      currentIndex = currentIndex + 1
      return o
    }
    else
      return null
  }

  int partition(List<QSData> m, int first, int last){
    int pivotValue = m[first].index
    int left = first+1
    int right = last
    boolean done = false
    while (!done){
      while ((left <= right) && (m[left].index<=pivotValue)) left = left + 1
      while ((m[right].index>= pivotValue) && (right >= left)) right = right - 1
      if (right < left)
        done = true
      else
        m.swap(left, right)
    }
    m.swap(first, right)
    return right

  }

  void quickSortRun(List<QSData> b, int first, int last){
    if (first < last) {
      int splitPoint = partition(b, first, last)
      quickSortRun(b, first, splitPoint-1)
      quickSortRun(b, splitPoint+1, last)
    }

  }
}
