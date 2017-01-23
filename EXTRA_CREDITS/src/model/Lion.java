package model;

public class Lion {

	private String lionID;
	private double position_x;
	private double position_y;

	public String getLionID() {
		return lionID;
	}

	public void setLionID(String lionID) {
		this.lionID = lionID;
	}

	public double getPosition_x() {
		return position_x;
	}

	public void setPosition_x(double position_x) {
		this.position_x = position_x;
	}

	public double getPosition_y() {
		return position_y;
	}

	public void setPosition_y(double position_y) {
		this.position_y = position_y;
	}

	@Override
	public boolean equals(Object object) {
		Lion lion = (Lion) object;
		if (lion.getLionID().equals(this.getLionID())) {
			return true;
		}
		return false;
	}

}
