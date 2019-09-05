package GPP_Demos.nbody

import GPP_Library.DataClass
import groovy.transform.CompileStatic
import GPP_Library.DataClassInterface as constants
import GPP_Library.functionals.matrix.Matrix

@CompileStatic
class NbodyData extends DataClass {

// based on code from http://physics.princeton.edu/~fpretori/Nbody/code.htm
    
    private static final double G = 6.673e-11 // gravitational constant

    Matrix planets = null
    static int N = 0
    static double dt = 1e11
    int reps = 0
    static String initMethod = "init"
    static String createMethod = "create"
    static String partitionMethod = "partition"
    static String calculationMethod = "doCalculation"
    static String updateMethod = "update"

    static BufferedReader reader = null   // the file reader that will used to read the data file
    List <Range> partitionRanges = []     // a list of 'nodes' partition ranges

  int init(List d) {  // [filename, N, dt ]
    File file = new File((String)d[0])
    reader = file.newReader()
    N = d[1]
    dt = d[2]
    return constants.completedOK
  } // init

  static boolean inputComplete = false

// usage of matric entries

  static final int rx = 0
  static final int ry = 1
  static final int vx = 2
  static final int vy = 3
  static final int mass = 4
  static final int id = 5
  static final int fx = 6
  static final int fy = 7

  int create(List d){ // d is null
     if (inputComplete) return normalTermination
     else {
         inputComplete = true   //we only read one file
         planets = new Matrix( rows: N, columns: 8)
         planets.entries = new double [N][8]
         for ( int r in 0 ..< N) {
             String line = reader.readLine()
//             println "Line: $r = $line"
             def tokens = line.split(" ")
             for ( int c in 0 ..< 6)
                 planets.entries[r][c] = Double.parseDouble(tokens[c])
         } // for each row
         reader.close()
         return normalContinuation
     }
  }// create

    void partition (int nodes) {
//        print "Range input $N, $nodes : "
      // now create the partitionRanges
      if (nodes == 1) {
        partitionRanges << (Range) ( 0 .. N-1)
      }
      else {
        int partSize = (int)(N / nodes)
        for ( p in 0 .. nodes-2)  {
            partitionRanges << (Range)( (p * partSize) .. ( ((p+1) * partSize)-1 ) )
        }
        partitionRanges << (Range)( ( (nodes-1) * partSize) .. (N-1) )
      }
//      println "Ranges $partitionRanges"
    } // partition

  double EPS = 3e4  // softening parameter (just to avoid infinities)

  void addForce (int a, int b){
      def planetA = planets.entries[a]
      def planetB = planets.entries[b]
      double dx = (double)planetB[rx] - (double)planetA[rx]
      double dy = (double)planetB[ry] - (double)planetA[ry]
      double d = Math.sqrt( (dx * dx) + (dy * dy) )
      double F = (G * (double)planetA[mass] * (double)planetB[mass]) / ((d * d) + (EPS * EPS))
      planetA[fx] = (double)planetA[fx] + ( F * dx / d )
      planetA[fy] = (double)planetA[fy] + ( F * dy / d )
  }

  void doCalculation(int node){
    Range range = partitionRanges[node]
    for ( r in range) {
        int ri = (int)r
        // set force cloumns 6 and 7 to zero
        planets.entries[ri][fx] = 0.0
        planets.entries[ri][fy] = 0.0
        for ( p in 0 ..< ri) addForce (ri, p)
        for ( p in ri+1 ..< N) addForce (ri, p)
//        for ( p in 0 ..< N) {
//            if ( p != r) addForce (r, p)
//        }
    }
    reps += 1
  }

  void update (){
      for ( p in 0..< N){
          def planet = planets.entries[(int)p]
          planet[vx] = (double)planet[vx] + (double)(dt * (double)planet[fx] / (double)planet[mass])
          planet[vy] = (double)planet[vy] + (double)(dt * (double)planet[fy] / (double)planet[mass])
          planet[rx] = (double)planet[rx] + (double)(dt * (double)planet[vx] / (double)planet[mass])
          planet[ry] = (double)planet[ry] + (double)(dt * (double)planet[vy] / (double)planet[mass])
      }
  }

}
