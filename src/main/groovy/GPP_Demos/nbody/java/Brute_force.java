package GPP_Demos.nbody.java;

// tell the compiler where to find the methods you will use.
// required when you create an applet
import java.applet.*;
// required to paint on screen
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class Brute_force extends Applet {
	/**
     * 
     */
    private static final long serialVersionUID = 1L;
    public int N = 100;													//decides how many planets will be iterated over
	public Planet planets[] = new Planet[100000];
	public TextField t1;
	public Label l1;
	public Button b1;
	public Button b2;
	public boolean shouldrun = false;
	int n = 0;
	int h;

	private String path = "C:/Users/Sean/Documents/HP_docs/planets_list.txt";
	BufferedReader br = null;
	FileReader fr = null;
	private String sCurrentLine;

	// The first time we call the applet, this function will start
	public void init() {
		startthebodies(N);
		t1 = new TextField("100", 5);
		b2 = new Button("Restart");
		b1 = new Button("Stop");
		l1 = new Label("Number of bodies:");
		ButtonListener myButtonListener = new ButtonListener();
		b1.addActionListener(myButtonListener);
		b2.addActionListener(myButtonListener);
		add(l1);
		add(t1);
		add(b2);
		add(b1);
	}

	// This method gets called when the applet is terminated. It stops the code
	public void stop() {
		shouldrun = false;
	}

	// Called by the applet initally. It can be executed again by calling
	// repaint();
	public void paint(Graphics g) {
		g.translate(250, 250); // Originally the origin is in the top right. Put
								// it in its normal place

		// check if we stopped the applet, and if not, draw the particles where
		// they are
		if (shouldrun) {
			if (h == 100) {									// h decides how many iterations the applet does
				WriteFile WF = new WriteFile("C:/Users/Sean/Documents/HP_docs/result_list_100its_500plans", true);
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
					System.out.println(fst + " " + fsr + " " + vst + " " + vsr + " " + mase + " " + id);
				}
				stop();
			}
			for (int i = 0; i < N; i++) {
				g.setColor(Color.BLUE); // bug with colour;
				g.fillOval((int) Math.round(planets[i].rx * 250 / 1e18), (int) Math.round(planets[i].ry * 250 / 1e18),
						8, 8);
			}
			// go through the Brute Force algorithm (see the function below)
			addforces(N);
			// go through the same process again until applet is stopped
			repaint();
			h = h + 1;
		}
	}

	// the bodies are initialized in circular orbits around the central mass.
	// This is just some physics to do that
	public static double circlev(double rx, double ry) {
		double solarmass = 1.98892e30;
		double r2 = Math.sqrt(rx * rx + ry * ry);
		double numerator = (6.67e-11) * 1e6 * solarmass;
		return Math.sqrt(numerator / r2);
	}

	// Initialize N bodies with random positions and circular velocities
	public void startthebodies(int N) {
		try { /**/
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
				System.out.println(rtx);
				double mass = Double.parseDouble(mas);
				String ide = result[5];
				int id = Integer.parseInt(ide);
				planets[n] = new Planet(rtx, rty, vtx, vty, mass, id);
				System.out.println(rtx + " " + rty + " " + vtx + " " + vty + " " + mass + " " + id);
				Arrays.fill(result, null);
				n = n + 1;
				System.out.println(n);

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

	} /**/

	// Use the method in Body to reset the forces, then add all the new forces
	public void addforces(int N) {
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

	public static double exp(double lambda) {
		return -Math.log(1 - Math.random()) / lambda;
	}

	public boolean action(Event e, Object o) {
		N = Integer.parseInt(t1.getText());
		if (N > 5000) {
			t1.setText("5000");
			N = 5000;
		}

		startthebodies(N);
		repaint();

		return true;
	}

	public class ButtonListener implements ActionListener {

		public void actionPerformed(ActionEvent evt) {
			// Get label of the button clicked in event passed in
			String arg = evt.getActionCommand();
			if (arg.equals("Restart")) {
				shouldrun = true;
				N = Integer.parseInt(t1.getText());
				if (N > 5000) {
					t1.setText("5000");
					N = 5000;
				}

				startthebodies(N);
				repaint();
			} else if (arg.equals("Stop"))
				stop();
		}
	}

}
