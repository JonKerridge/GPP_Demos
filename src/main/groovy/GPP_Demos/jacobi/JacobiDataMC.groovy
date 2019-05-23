package GPP_Demos.jacobi

import GPP_Library.DataClass
import groovy.transform.CompileStatic
import GPP_Library.functionals.matrix.Matrix
import GPP_Library.functionals.matrix.Vector

@CompileStatic
class JacobiDataMC extends DataClass {

  Matrix M = null
  Vector solution = null
  static String initMethod = "init"
  static String createMethod = "create"
  static String partitionMethod = "partition"
  static String errorMethod = "repeat"
  static String calculationMethod = "doCalculation"
  static String updateMethod = "update"

  double margin = 1.0E-16		// works with float as well

  static int dataSets = 0 	// the number of data sets in the file
  int n = 0					// the number of rows in the matrix
  String dataIdentifier = ""
  String dataSetName = ""
  List <Range> partitionRanges = []	// a list of 'nodes' partition ranges
  static BufferedReader reader = null			// the file reader that will used to read the data file
  int iterations = 0


  int init(List d) {  // [filename]
    def file = new File((String)d[0])
    reader = file.newReader()
    dataIdentifier = reader.readLine()
    dataSets = Integer.parseInt(reader.readLine())
    //println """Processing file ${d[0]} containing $dataIdentifier  comprising $dataSets datasets\n"""
    return completedOK
  } // init

  static int currentDataSet = 0
  int thisDataSet

  int create(List d){ // d is null

    if ( currentDataSet == dataSets) return normalTermination
    else {
      currentDataSet = currentDataSet + 1
      thisDataSet = currentDataSet
      def line = reader.readLine()
      dataSetName = line
      line = reader.readLine()
      n = Integer.parseInt(line)
      M = new Matrix(rows: n, columns: n+3)
      M.entries = new double [n][n+3]
      solution = new Vector(elements: n)

      // now read in the x Values line
      line = reader.readLine()
      def tokens = line.split(',')
      def solutionValues = []
      solutionValues << Double.parseDouble(tokens[0].substring(1))
      for ( i in 1 ..< n-1) solutionValues << Double.parseDouble(tokens[i])  //.toDouble()
      solutionValues << Double.parseDouble(tokens[n-1][0 .. tokens[n-1].size()-2])
      solution.setEntries(solutionValues)

      // now read in the lines for the rows of Matrix M
      for ( r in 0 ..< n){
        line = reader.readLine()
        tokens = line.split(',')
        M.entries[r][0] = Double.parseDouble(tokens[0].substring(1))
        for ( i in 1 ..< n+2) M.entries[r][i] = Double.parseDouble(tokens[i])
        M.entries[r][n+2] = Double.parseDouble(tokens[n+2][0 .. tokens[n+2].size()-2])
      }
    }
    return normalContinuation
  } // create

    void partition (int nodes) {
      // now create the partitionRanges
      if (nodes == 1) {
        partitionRanges << (Range) ( 0 .. n-1)
      }
      else {
        int partSize = (int)(n / nodes)
        for ( p in 0 .. n-2)  {
            partitionRanges << (Range)( (p * partSize) .. ( ((p+1) * partSize)-1 ) )
        }
        partitionRanges << (Range)( ( (n-1) * partSize) .. (n-1) )
      }
      iterations = 0
    } // partition


  boolean repeat(def margin){
//		println "R n+1: ${M.getByColumn(n+1)}"
//		println "R n+2: ${M.getByColumn(n+2)}"
    double difference = Math.abs((double)M.entries[0][n+1] - (double)M.entries[0][n+2])
    int checked = Double.compare(difference, (double)margin)
//    checked less than zero implies difference < margin
    int i = 1
    while ((i < n) && (checked < 0)){
        difference = Math.abs((double)M.entries[i][n+1] - (double)M.entries[i][n+2])
        checked = Double.compare(difference, (double)margin)
      i = i + 1
    } // i while
    return (checked >= 0)
  }

  void update (){
      // column n+1 is the current solution
      // column n+2 is the next guess
      // copy all of column n+2 into column n+1
//      for ( r in 0..< n) M.entries[r, n+1] = M.entries[r, n+2]
      M.setByColumn(M.getByColumn(n+2), n+1)
  }

  void doCalculation(int node){
      // based on https://en.wikipedia.org/wiki/Jacobi_method
    Range range = partitionRanges[node]
    for ( r in range) {
      double bVal = (double)M.entries[(int)r][n]
      for (c in 0 ..< (int)r) bVal = bVal - ((double)M.entries[(int)r][c] * (double)M.entries[c][n+1])
      for (c in (int)r+1 ..< n) bVal = bVal - ((double)M.entries[(int)r][c] * (double)M.entries[c][n+1])
      M.entries[(int)r][n+2] = (double)(bVal / (double)M.entries[(int)r][(int)r])
    }
    iterations  += 1
  }

  String toString() {
    String s = "Set $thisDataSet is $dataSetName size $n x $n and ranges $partitionRanges\n"
    s = s + "with $iterations iterations\n"
    s = s + "${M.getByColumn(n+1)}\n"
    return s
  }
}
