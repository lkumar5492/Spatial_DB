package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Lion;
import model.Pond;
import model.Region;
import oracle.spatial.geometry.JGeometry;
import oracle.sql.STRUCT;

public class FetchData {

	private Connection con;

	public List<Region> fetchRegions() {
		DbConnect dbConnect = new DbConnect();
		con = dbConnect.connect();
		List<Region> regionList = new ArrayList<Region>();
		try {
			PreparedStatement preparedStatement = con
					.prepareStatement("select * from region");
			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				Region region = new Region();
				region.setRegionID(rs.getString("regionID"));// fetch region ID
																// from db
				// fetch sdo_geometry from db
				STRUCT regionStruct = (STRUCT) rs.getObject("region");
				JGeometry jGeometry = JGeometry.load(regionStruct);
				double ordinates[] = jGeometry.getOrdinatesArray();
				int n = jGeometry.getNumPoints();
				int x = 0, y = 0;
				double x_coord[] = new double[n];
				double y_coord[] = new double[n];

				for (int i = 0; i < ordinates.length; i++) {
					if (i % 2 == 0) {
						x_coord[x++] = ordinates[i];
					} else {
						y_coord[y++] = ordinates[i];
					}
				}
				region.setX(x_coord);
				region.setY(y_coord);
				regionList.add(region);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		dbConnect.close(con);
		if (regionList.isEmpty()) {
			regionList = null;
		}

		return regionList;
	}

	public List<Lion> fetchLions() {
		DbConnect dbConnect = new DbConnect();
		con = dbConnect.connect();
		List<Lion> lionList = new ArrayList<Lion>();
		try {
			PreparedStatement preparedStatement = con
					.prepareStatement("select * from lion");
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				Lion lion = new Lion();
				lion.setLionID(rs.getString("lionID"));

				STRUCT lionStruct = (STRUCT) rs.getObject("position");
				JGeometry jGeometry = JGeometry.load(lionStruct);
				double position[] = jGeometry.getPoint();
				lion.setPosition_x(position[0]);
				lion.setPosition_y(position[1]);
				lionList.add(lion);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		dbConnect.close(con);
		if (lionList.isEmpty()) {
			lionList = null;
		}
		return lionList;
	}

	public List<Pond> fetchPond() {

		DbConnect dbConnect = new DbConnect();
		con = dbConnect.connect();
		List<Pond> pondList = new ArrayList<Pond>();
		try {
			PreparedStatement preparedStatement = con
					.prepareStatement("select * from pond");
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				Pond pond = new Pond();
				pond.setPondID(rs.getString("pondID"));
				STRUCT pondStruct = (STRUCT) rs.getObject("pondShape");
				JGeometry jGeometry = JGeometry.load(pondStruct);

				double mbr[] = jGeometry.getMBR();
				pond.setX_coord(mbr[0]);
				pond.setY_coord(mbr[1]);
				pond.setRadius((mbr[2] - mbr[0]) / 2.0);

				/*
				 * double pondShape[] = jGeometry.getOrdinatesArray(); int n =
				 * jGeometry.getNumPoints(); int x = 0, y = 0; double x_coord[]
				 * = new double[n]; double y_coord[] = new double[n];
				 * 
				 * for (int i = 0; i < pondShape.length; i++) { if (i % 2 == 0)
				 * { x_coord[x++] = pondShape[i]; } else { y_coord[y++] =
				 * pondShape[i]; } } pond.setX(x_coord); pond.setY(y_coord);
				 */
				pondList.add(pond);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		dbConnect.close(con);
		if (pondList.isEmpty()) {
			pondList = null;
		}
		return pondList;
	}
}
