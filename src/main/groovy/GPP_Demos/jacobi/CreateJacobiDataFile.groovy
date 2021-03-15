package GPP_Demos.jacobi

import groovy_parallel_patterns.functionals.matrix.*
// before running bat files you need to create two additional Jacobi files for
// system sizes of 4096 and 8192  equations

int n = 8192  // need to run with 4096 and 8192
int range = 9		// range of values for non-diagonal elements
int diagRange = n * 10	//range of values for diagonal elements
String title = "Jacobi$n"
String fileName = "./${title}.txt"
def file = new File(fileName)
def writer = file.newPrintWriter()

writer.println" Jacobi Data Set $title\n1"

int iterations = 0
double margin = 1.0E-16		// works with float as well

/**
 * Solving Ax = b
 *  M holds the elements as follows:
 *  The columns 0 ..n-1 holds the array A[n][n]
 *  column n holds the value of the vector b
 *  column n+1 holds the currrent best guess values for vector x
 *  column n+2 holds the next iteration of the vector x
 */


def rng = new Random()

  // create an example matrix
  long startTime = System.currentTimeMillis()
  println "Starting: $startTime"
  Matrix M = new Matrix(rows: n, columns: n+3)
  M.entries = new double [n][n+3]

  Vector x = new Vector(elements: n)
  x.entries = new double[n]

  Vector b = new Vector(elements: n)
  b.entries = new double[n]

  String dataSetName = "$title"

  for ( r in 0 ..< n){
    for ( c in 0 ..< n ) {
      if (r == c)
        M.entries[r][c] = rng.nextInt(diagRange) + diagRange
      else
        M.entries[r][c] = rng.nextInt(range)
    } // c
  } // r
  println "Filled M ${ System.currentTimeMillis() - startTime}"
  // vector x which holds the expected solution
  def xVals = []
  for ( r in 0 ..< n) xVals << rng.nextInt(range) + 1
  x.setEntries(xVals)
  println "Filled X ${System.currentTimeMillis() - startTime}"
  // now calculate the vector b
//	def bVals = []
  for ( r in 0 ..< n){
    double B = 0.0
    for ( c in 0 ..< n ) {
      B = B + (M.entries[r][c] * x.entries[c])
    } //c
//		bVals <<  B
    M.entries[r][n] = B
		println "[$r, $n] = $B"
  } // r
//	M.setByColumn(bVals, n)
  println "Filled B ${System.currentTimeMillis() - startTime}"
  // check for diagonal dominance
  boolean dominant = true
  int rt = 0
  while ((rt < n) && (dominant)){
    def sum = 0
    for (c in 0 ..< rt) sum = sum + M.entries[rt][c]
    for (c in rt+1 ..< n) sum = sum + M.entries[rt][c]
    if (sum > M.entries[rt][rt]) dominant = false
    rt = rt + 1
  } //rt
  println "Diagonal is $dominant ${System.currentTimeMillis() - startTime}"
  // now insert the vector b into M if dominant true
  if (dominant){
//		for ( rr in 0 ..< n) M.entries[rr][n] = b.entries[rr]
//		M.setByColumn(b.getEntries(), n)
    // now print matrix to file
//		println "B values = ${b.getEntries()}"
    println "Construction OK, creating $dataSetName"
//		file.withPrintWriter { writer ->
    writer.println "$dataSetName"
    writer.println "$n"
    writer.println "${x.getEntries()}"
//		writer.println "${b.GetByColumn(0)}"
    0.upto(n-1) {row ->
      writer.println "${M.getByRow(row)}"
    }
    println "finished in  ${System.currentTimeMillis() - startTime}; doing the calculation"
    // now calculate the sequential time
    startTime = System.currentTimeMillis()
    boolean running = true
    while (running){
      for ( r in 0 ..< n) {
        double bVal = M.entries[r][n]
        for (c in 0 ..< r) bVal = bVal - (M.entries[r][c] * M.entries[c][n+1])
        for (c in r+1 ..< n) bVal = bVal - (M.entries[r][c] * M.entries[c][n+1])
        M.entries[r][n+2] = bVal / M.entries[r][r]
      } // r
      boolean checked = Math.abs(M.entries[0][n+1] - M.entries[0][n+2]) < margin
      int i = 1
      while ((i < n) && checked){
        checked = Math.abs(M.entries[i][n+1] - M.entries[i][n+2]) < margin
        i = i + 1
      } // i while
      if (checked)
        running = false
      else{
        M.setByColumn( M.getByColumn(n+2), n+1)
//				for ( r in 0 ..< n) M.entries[r][n+1] = M.entries[r][n+2]
        iterations += 1
      }
    } // while running
    long endTime = System.currentTimeMillis()
    println "Result for $n x $n matrix after $iterations iterations in ${endTime - startTime} milliSeconds"
    println "Vector x = ${x.getEntries()}"
    n = n * 2
    diagRange = n * 10
  }
  else
    println "Construct is non-dominant"

writer.flush()
writer.close()




