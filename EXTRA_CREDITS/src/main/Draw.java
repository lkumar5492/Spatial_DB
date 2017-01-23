package main;

import java.awt.BorderLayout;
import java.awt.Checkbox;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JApplet;
import javax.swing.JFrame;

import model.Lion;
import model.Pond;
import model.Region;
import dao.FetchData;

public class Draw extends JApplet implements MouseListener {

	private static final long serialVersionUID = 1L;
	final static Color bg = Color.white;
	final static Color fg = Color.black;
	Checkbox checkbox;
	List<Region> regionList = null;
	List<Lion> lionList = null;
	List<Pond> pondList = null;
	private boolean isChecked = Boolean.FALSE;
	List<Lion> selectedLions = new ArrayList<Lion>();// initializing selected
														// lions
	List<Pond> selectedPonds = new ArrayList<Pond>();// initializing selected
														// ponds

	public void init() {
		// Initialize drawing colors
		setBackground(bg);
		setForeground(fg);
		checkbox = new Checkbox();
		checkbox.setLabel("Show lions and ponds in selected region");
		checkbox.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.DESELECTED) {
					System.out.println("CHECKBOX DESELECTED!!!");
					isChecked = Boolean.FALSE;
					selectedLions.clear();// reset selected lions
					selectedPonds.clear();// reset selected ponds
					repaint();
					return;
				} else if (e.getStateChange() == ItemEvent.SELECTED) {
					System.out.println("CHECKBOX SELECTED!!!");
					isChecked = Boolean.TRUE;
				}
			}
		});
		add(checkbox, BorderLayout.EAST);
		addMouseListener(this);

	}

	public void paint(Graphics g) {
		Graphics2D g2D = (Graphics2D) g;

		FetchData fetchData = new FetchData();

		/* FETCHING REGIONS AND DRAWING POLYGONS */
		regionList = fetchData.fetchRegions();

		if (!regionList.isEmpty()) {
			for (Region reg : regionList) {
				GeneralPath polygon = new GeneralPath(
						GeneralPath.WIND_EVEN_ODD, reg.getX().length);
				/*
				 * System.out.println("X:" + reg.getX()[0]);
				 * System.out.println("Y:" + reg.getY()[0]);
				 */
				polygon.moveTo(reg.getX()[0], reg.getY()[0]);

				for (int index = 1; index < reg.getX().length; index++) {
					/*
					 * System.out.println("X:" + reg.getX()[index]);
					 * System.out.println("Y:" + reg.getY()[index]);
					 */
					polygon.lineTo(reg.getX()[index], reg.getY()[index]);
				}
				polygon.closePath();
				g2D.draw(polygon);
			}
		}
		/* FETCHING LIONS AND DRAWING POINTS */
		lionList = fetchData.fetchLions();

		if (!lionList.isEmpty()) {
			for (Lion lion : lionList) {
				if (selectedLions.contains(lion)) {
					g2D.setColor(Color.RED);
					g2D.drawLine((int) lion.getPosition_x(),
							(int) lion.getPosition_y(),
							(int) lion.getPosition_x(),
							(int) lion.getPosition_y());
				} else {
					g2D.setColor(Color.GREEN);
					g2D.drawLine((int) lion.getPosition_x(),
							(int) lion.getPosition_y(),
							(int) lion.getPosition_x(),
							(int) lion.getPosition_y());
				}
			}
		}
		/* FETCHING PONDS AND DRAWING CIRCLE */
		pondList = fetchData.fetchPond();

		if (!pondList.isEmpty()) {

			for (Pond pond : pondList) {
				if (selectedPonds.contains(pond)) {
					g2D.setPaint(Color.RED);
					Ellipse2D.Double shape = new Ellipse2D.Double(
							pond.getX_coord(), pond.getY_coord(),
							pond.getRadius() * 2.0, pond.getRadius() * 2.0);
					g2D.fill(shape);
				} else {
					g2D.setPaint(Color.BLUE);
					Ellipse2D.Double shape = new Ellipse2D.Double(
							pond.getX_coord(), pond.getY_coord(),
							pond.getRadius() * 2.0, pond.getRadius() * 2.0);
					g2D.fill(shape);
					/*
					 * g2D.fillOval(Math.round(pond.getX_coord()),
					 * Math.round(pond.getY_coord()),
					 * Math.round(pond.getRadius() * 2.0),
					 * Math.round(pond.getRadius() * 2.0));
					 */
				}
			}
		}

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (isChecked) {
			selectedLions.clear();
			selectedPonds.clear();

			int xpos = e.getX();
			int ypos = e.getY();
			System.out.println("X POSITION: " + xpos + "\t Y POSITION: " + ypos
					+ "\n--------------------");

			if (!regionList.isEmpty()) {
				for (Region region : regionList) {
					GeneralPath polygon = new GeneralPath(
							GeneralPath.WIND_EVEN_ODD, region.getX().length);
					/*
					 * System.out.println("X:" + reg.getX()[0]);
					 * System.out.println("Y:" + reg.getY()[0]);
					 */
					polygon.moveTo(region.getX()[0], region.getY()[0]);

					for (int index = 1; index < region.getX().length; index++) {
						/*
						 * System.out.println("X:" + reg.getX()[index]);
						 * System.out.println("Y:" + reg.getY()[index]);
						 */
						polygon.lineTo(region.getX()[index],
								region.getY()[index]);
					}
					polygon.closePath();
					if (polygon.contains(xpos, ypos)) {
						System.out.println("INSIDE REGION:"
								+ region.getRegionID());
						System.out.println("LIONS INSIDE REGION:");
						/* LIONS INSIDE CLICKED REGION */
						if (!lionList.isEmpty()) {
							for (Lion lion : lionList) {
								if (polygon.contains(lion.getPosition_x(),
										lion.getPosition_y())) {
									System.out.println("LION: "
											+ lion.getLionID());
									selectedLions.add(lion);
								}
							}
						}
						System.out.println("PONDS INSIDE REGION:");
						/* PONDS INSIDE CLICKED REGION */
						if (!pondList.isEmpty()) {
							for (Pond pond : pondList) {
								Ellipse2D.Double shape = new Ellipse2D.Double(
										pond.getX_coord(), pond.getY_coord(),
										pond.getRadius() * 2.0,
										pond.getRadius() * 2.0);
								if (polygon.contains(shape.getBounds2D())) {
									System.out.println("POND: "
											+ pond.getPondID()
											+ "\n------------------");
									selectedPonds.add(pond);
								}
							}
						}

					}
				}
			}
			repaint();
		}

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public static void main(String args[]) {
		System.out.println("welcome");

		JFrame jf = new JFrame("EXTRA_CREDIT");
		jf.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		JApplet jApplet = new Draw();
		jf.getContentPane().add(BorderLayout.CENTER, jApplet);
		jApplet.init();
		jf.pack();
		jf.setSize(new Dimension(1000, 1000));
		jf.setVisible(true);
	}

}
