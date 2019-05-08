package GPP_Demos.solarSystem

import groovy.transform.CompileStatic

import GPP_Library.DataClassInterface as constants


@CompileStatic
class PlanetrySystem extends GPP_Library.DataClass {

	List <Planet> planets = []
	List <Range> partitionRanges = [] // a list of 'nodes' partition ranges
	static BufferedReader reader = null           // the file reader that will used to read the data file
	static int N = 0
	static double dt = 1e11
	double EPS = 3e4  // softening parameter (just to avoid infinities)

	static String initMethod = "init"
	static String createMethod = "create"
	static String partitionMethod = "partition"
	static String calculationMethod = "doCalculation"
	static String updateMethod = "update"

	int init(List d) {  // d is null]
		return constants.completedOK
	  } // init

    static boolean inputComplete = false

	int create(List d){ // d [fileName, N, dt]
		File file = new File((String)d[0])
		reader = file.newReader()
		N = d[1]
		dt = d[2]
		if (inputComplete) return constants.normalTermination
		else {
			inputComplete = true   //we only read one file
			for ( int n in 0 ..< N) {
				Planet planet
				String sCurrentLine = reader.readLine()
				String[] result = sCurrentLine.split(" ");
				String rx = result[0];
				double rtx = Double.parseDouble(rx);
				String ry = result[1];
				double rty = Double.parseDouble(ry);
				String vx = result[2];
				double vtx = Double.parseDouble(vx);
				String vy = result[3];
				double vty = Double.parseDouble(vy);
				String mas = result[4];
//				System.out.println(rtx);
				double mass = Double.parseDouble(mas);
				String ide = result[5];
				int id = Integer.parseInt(ide);
				planets[n] = new Planet(rtx, rty, vtx, vty, mass, id);
//				System.out.println(rtx + " " + rty + " " + vtx + " " + vty + " " + mass + " " + id);
				Arrays.fill(result, null);
			} // for each planet
			reader.close()
			return constants.normalContinuation
		}
	} // create


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

  void doCalculation (int node ){
    Range range = partitionRanges[node]
    for ( i in range) {
		planets[(int)i].resetForce()
		// Notice-2 loops-->N^2 complexity
		for (int j = 0; j < N; j++) {
			if (i != j)
				planets[(int)i].addForce(planets[j])
		}
    }
  }

  void update (){
      for ( i in 0..< N){
		  planets[i].update(dt);
      }
  }
}
