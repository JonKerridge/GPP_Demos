package GPP_Demos.nbody.java;

import java.awt.Color;

public class Planet {

	private static final double G = 6.673e-11; // gravitational constant

	public double rx, ry; // holds the cartesian positions
	public double vx, vy; // velocity components
	public double fx, fy; // force components
	public double mass; // mass
	public int id;
	public Color color; // color (for fun)

	// create and initialize a new Body
	public Planet(double rx, double ry, double vx, double vy, double mass,int id) {
		this.rx = rx;
		this.ry = ry;
		this.vx = vx;
		this.vy = vy;
		this.mass = mass;
		this.id = id;
	}

	// update the velocity and position using a timestep dt
	public void update(double dt) {
		vx += dt * fx / mass;
		vy += dt * fy / mass;
		rx += dt * vx;
		ry += dt * vy;
	}

	// returns the distance between two bodies
	public double distanceTo(Planet p) {
		double dx = rx - p.rx;
		double dy = ry - p.ry;
		return Math.sqrt(dx * dx + dy * dy);
	}

	// set the force to 0 for the next iteration
	public void resetForce() {
		fx = 0.0;
		fy = 0.0;
	}

	// compute the net force acting between the body a and b, and
	// add to the net force acting on a
	public void addForce(Planet b) {
		Planet a = this;
		double EPS = 3E4; // softening parameter (just to avoid infinities)
		double dx = b.rx - a.rx;
		double dy = b.ry - a.ry;
		double dist = Math.sqrt(dx * dx + dy * dy);
		double F = (G * a.mass * b.mass) / (dist * dist + EPS * EPS);
		a.fx += F * dx / dist;
		a.fy += F * dy / dist;
	}

	// convert to string representation formatted nicely
	public String toString() {
		return "" + rx + ", " + ry + ", " + vx + ", " + vy + ", " + mass;
	}

	/*public boolean in(Quadrant q) {
		return q.contains(this.rx, this.ry);
	}*/
}
