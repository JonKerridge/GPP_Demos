package GPP_Demos.nbody.java;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class Run_Sequential {

	public static int N = 100;									// number of planets
	public static Planet planets[] = new Planet[100000];		// the default planet list contains 9999 planets
	static int n = 0;
	static int h = 100;											// number of iterations

	private static String path = "src/planets_list.txt";
	private static String writepath = "src/result_list_"+h+"_"+N+"_planets";
	static BufferedReader br = null;
	static FileReader fr = null;
	private static String sCurrentLine;

	public static void main(String[] args) {
//		double radius = 1e18; // radius of universe
	    System.gc();
	    System.out.print("SeqJava  " + h + ", " + N + " -> ");
	    long startTime = System.currentTimeMillis();


		try {
			fr = new FileReader(path);
			br = new BufferedReader(fr);

			br = new BufferedReader(new FileReader(path));

			while ((sCurrentLine = br.readLine()) != null) {
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
				n = n + 1;
//				System.out.println(n);

			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
				if (fr != null)
					fr.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		for (int l = 0; l < h; l++) {
			for (int i = 0; i < N; i++) {
				planets[i].resetForce();
				// Notice-2 loops-->N^2 complexity
				for (int j = 0; j < N; j++) {
					if (i != j)
						planets[i].addForce(planets[j]);
				}
			}
			// Then, loop again and update the bodies using timestep dt
			for (int i = 0; i < N; i++) {
				planets[i].update(1e11);
			}

		}
		WriteFile WF = new WriteFile(writepath, true);      // write to the filer
		for (int t = 0; t < N; t++) {
			double ft = planets[t].rx;
			String fst = String.valueOf(ft);
			double fr = planets[t].ry;
			String fsr = String.valueOf(fr);
			double vt = planets[t].vx;
			String vst = String.valueOf(vt);
			double vr = planets[t].vy;
			String vsr = String.valueOf(vr);
			double mas = planets[t].mass;
			String mase = String.valueOf(mas);
			int ide = planets[t].id;
			String id = String.valueOf(ide);
			try {
				WF.writeToFile(fst + " " + fsr + " " + vst + " " + vsr + " " + mase + " " + id);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//			System.out.println(fst + " " + fsr + " " + vst + " " + vsr + " " + mase + " " + id);
		}
	    long endTime = System.currentTimeMillis();
	    System.out.print (" " + (endTime - startTime) + " \n");

	} // main

}
