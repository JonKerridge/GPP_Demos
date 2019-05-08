package GPP_Demos.nbody.java;


import java.io.IOException;

public class generate_file {

	 public static Planet planets[]= new Planet[10000];
	 static String file_name ="C:/Users/Sean/Documents/HP_docs/planets_list.txt";

	 public static double circlev(double rx, double ry) {
		    double solarmass=1.98892e30;
		    double r2=Math.sqrt(rx*rx+ry*ry);
		    double numerator=(6.67e-11)*1e6*solarmass;
		    return Math.sqrt(numerator/r2);
		  }
	 public static double exp(double lambda) {
	        return -Math.log(1 - Math.random()) / lambda;
	    }

	public static void main(String[] args) {

		    double solarmass=1.98892e30;
		    for (int i = 0; i < 9999; i++) {
		      double px = 1e18*exp(-1.8)*(0.5-Math.random());
		      double py = 1e18*exp(-1.8)*(0.5-Math.random());
		      double magv = circlev(px,py);

		      double absangle = Math.atan(Math.abs(py/px));
		      double thetav= Math.PI/2-absangle;
		      double vx   = -1*Math.signum(py)*Math.cos(thetav)*magv;
		      double vy   = Math.signum(px)*Math.sin(thetav)*magv;
		      //Orient a random 2D circular orbit/

		           if (Math.random() <=.5) {
		              vx=-vx;
		              vy=-vy;
		            }

		      double mass = Math.random()*solarmass*10+1e20;
		      //Colour the masses in green gradients by mass
		   /*   int red     = (int) Math.floor(mass*254/(solarmass*10+1e20));
		      int blue   = (int) Math.floor(mass*254/(solarmass*10+1e20));
		      int green    = 255;
		      Color color = new Color(red, green, blue);*/
		      int id = i;
		      planets[i]   = new Planet(px, py, vx, vy, mass, id);
		    }
		    //Put the central mass in
		    planets[0]= new Planet(0,0,0,0,1e6*solarmass,0);//put a heavy body in the center

		    WriteFile data = new WriteFile(file_name, true);

		    for(int t=0; t<planets.length-1; t++)
		    {
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
					data.writeToFile(fst+" "+fsr+" "+vst+" "+vsr+" "+mase+" "+id);
				} catch (IOException e) {
					e.printStackTrace();
				}
		    	System.out.println(fst+" "+fsr+" "+vst+" "+vsr+" "+mase+" "+id);
		    }
	}

}
