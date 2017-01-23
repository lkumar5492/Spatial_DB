package model;

public class Pond {

	private String pondID;
	private double x_coord;
	private double y_coord;
	private double radius;
	private double x[];
	private double y[];

	public String getPondID() {
		return pondID;
	}

	public void setPondID(String pondID) {
		this.pondID = pondID;
	}

	public double[] getX() {
		return x;
	}

	public void setX(double[] x) {
		this.x = x;
	}

	public double[] getY() {
		return y;
	}

	public void setY(double[] y) {
		this.y = y;
	}

	public double getX_coord() {
		return x_coord;
	}

	public void setX_coord(double x_coord) {
		this.x_coord = x_coord;
	}

	public double getY_coord() {
		return y_coord;
	}

	public void setY_coord(double y_coord) {
		this.y_coord = y_coord;
	}

	public double getRadius() {
		return radius;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}

	@Override
	public boolean equals(Object object) {
		Pond pond = (Pond) object;
		if (pond.getPondID().equals(this.getPondID())) {
			return true;
		}
		return false;
	}
}
