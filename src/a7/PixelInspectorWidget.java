package a7;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import java.text.DecimalFormat;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class PixelInspectorWidget extends JPanel implements MouseListener {

	private PictureView picture_view;
	JLabel x, y, red, green, blue, bright;

	// Constructor
	public PixelInspectorWidget(Picture picture) {
		setLayout(new BorderLayout());

		picture_view = new PictureView(picture.createObservable());
		picture_view.addMouseListener(this);
		add(picture_view, BorderLayout.CENTER);

		JPanel left_layout = new JPanel();
		left_layout.setLayout(new GridLayout(6, 1));

		// Initialize labels
		x = new JLabel("X: ");
		y = new JLabel("Y: ");
		red = new JLabel("Red: ");
		green = new JLabel("Green: ");
		blue = new JLabel("Blue: ");
		bright = new JLabel("Brightness: ");

		// Add listener
		x.addMouseListener(this);
		y.addMouseListener(this);
		red.addMouseListener(this);
		green.addMouseListener(this);
		blue.addMouseListener(this);
		bright.addMouseListener(this);

		// Add label to left_layout
		left_layout.add(x);
		left_layout.add(y);
		left_layout.add(red);
		left_layout.add(green);
		left_layout.add(blue);
		left_layout.add(bright);

		// Add left_layout to left
		add(left_layout, BorderLayout.WEST);
	}

	// Change mouse click behavior
	@Override
	public void mouseClicked(MouseEvent e) {
		DecimalFormat format = new DecimalFormat("0.00");
		x.setText("X: " + e.getX());
		y.setText("Y: " + e.getY());
		red.setText("Red: " + format.format(picture_view.getPicture().getPixel(e.getX(), e.getY()).getRed()));
		green.setText("Green: " + format.format(picture_view.getPicture().getPixel(e.getX(), e.getY()).getGreen()));
		blue.setText("Blue: " + format.format(picture_view.getPicture().getPixel(e.getX(), e.getY()).getBlue()));
		bright.setText(
				"Brightness: " + format.format(picture_view.getPicture().getPixel(e.getX(), e.getY()).getIntensity()));
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

}
