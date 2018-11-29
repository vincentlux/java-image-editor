package a7;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ImageAdjusterWidget extends JPanel implements ChangeListener {

	private PictureView picture_view;
	private JSlider blur, satu, bright;
	private Picture adjusted;
	JLabel blur_label, satu_label, bright_label;
	private String caption;

	public ImageAdjusterWidget(Picture picture) {
		setLayout(new BorderLayout());

		// Initialize a JPanel
		JPanel full_slider = new JPanel();
		full_slider.setLayout(new GridLayout(3, 1));

		// Save original picture
		adjusted = picture;
		picture_view = new PictureView(adjusted.createObservable());
		add(picture_view, BorderLayout.CENTER);

		// set label
		blur_label = new JLabel("Blur: ");
		satu_label = new JLabel("Saturation: ");
		bright_label = new JLabel("Brightness: ");

		// Set slider
		blur = new JSlider(0, 5, 0);
		satu = new JSlider(-100, 100, 0);
		bright = new JSlider(-100, 100, 0);

		// Set size
		blur.setPreferredSize(new Dimension(600, 40));
		satu.setPreferredSize(new Dimension(560, 40));
		bright.setPreferredSize(new Dimension(560, 40));

		// Set tick and label
		blur.setPaintTicks(true);
		blur.setMajorTickSpacing(1);
		blur.setPaintLabels(true);

		satu.setPaintTicks(true);
		satu.setMajorTickSpacing(25);
		satu.setPaintLabels(true);

		bright.setPaintTicks(true);
		bright.setMajorTickSpacing(25);
		bright.setPaintLabels(true);

		// Initialize three sub JPanel
		JPanel blur_slider = new JPanel();
		blur_slider.add(blur_label);
		blur_slider.add(blur);

		JPanel satu_slider = new JPanel();
		satu_slider.add(satu_label);
		satu_slider.add(satu);

		JPanel bright_slider = new JPanel();
		bright_slider.add(bright_label);
		bright_slider.add(bright);

		// combine
		full_slider.add(blur_slider);
		full_slider.add(satu_slider);
		full_slider.add(bright_slider);

		blur.addChangeListener(this);
		satu.addChangeListener(this);
		bright.addChangeListener(this);

		add(full_slider, BorderLayout.SOUTH);
		caption = "caption";
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		Picture newpic = this.adjust(blur.getValue(), satu.getValue(), bright.getValue());
		picture_view.setPicture(newpic.createObservable());
	}

	private Picture adjust(int blur, double satu, double bright) {
		Picture new_pic = this.adjusted;
		Pixel[][] image_array = new Pixel[new_pic.getWidth()][new_pic.getHeight()];
		// Blur
		for (int x = 0; x < new_pic.getWidth(); x++) {
			for (int y = 0; y < new_pic.getHeight(); y++) {
				// Initiate
				double sum_r = 0;
				double sum_g = 0;
				double sum_b = 0;
				int min_x = x - blur;
				int min_y = y - blur;
				int max_x = x + blur;
				int max_y = y + blur;

				// Follow the rule
				if (min_x < 0) {
					min_x = 0;
				}
				if (min_y < 0) {
					min_y = 0;
				}
				if (max_x >= new_pic.getWidth()) {
					max_x = new_pic.getWidth() - 1;
				}
				if (max_y >= new_pic.getHeight()) {
					max_y = new_pic.getHeight() - 1;
				}

				// Loop and update
				for (int i = min_x; i <= max_x; i++) {
					for (int j = min_y; j <= max_y; j++) {
						sum_r += new_pic.getPixel(i, j).getRed();
						sum_g += new_pic.getPixel(i, j).getGreen();
						sum_b += new_pic.getPixel(i, j).getBlue();
					}
				}
				int total = (max_x - min_x + 1) * (max_y - min_y + 1);
				image_array[x][y] = new ColorPixel(sum_r / total, sum_g / total, sum_b / total);
			}
		}

		// Saturation
		for (int x = 0; x < new_pic.getWidth(); x++) {
			for (int y = 0; y < new_pic.getHeight(); y++) {
				Pixel currentPixel = image_array[x][y];
				double intensity = currentPixel.getIntensity();
				double r = currentPixel.getRed();
				double g = currentPixel.getGreen();
				double b = currentPixel.getBlue();
				if (r == 0 && b == 0 && g == 0) {
					image_array[x][y] = new ColorPixel(r, g, b);
					continue;
				}
				if (satu <= 0) {
					r = r * (1.0 + (satu / 100.0)) - (intensity * satu / 100.0);
					g = g * (1.0 + (satu / 100.0)) - (intensity * satu / 100.0);
					b = b * (1.0 + (satu / 100.0)) - (intensity * satu / 100.0);
				} else {
					// Get max
					double max = Math.max(Math.max(r, g), b);
					r = r * ((max + ((1.0 - max) * (satu / 100.0))) / max);
					g = g * ((max + ((1.0 - max) * (satu / 100.0))) / max);
					b = b * ((max + ((1.0 - max) * (satu / 100.0))) / max);
				}
				image_array[x][y] = new ColorPixel(r, g, b);
			}
		}

		// Brightness
		for (int x = 0; x < new_pic.getWidth(); x++) {
			for (int y = 0; y < new_pic.getHeight(); y++) {
				Pixel currentPixel = image_array[x][y];
				double r = currentPixel.getRed();
				double g = currentPixel.getGreen();
				double b = currentPixel.getBlue();
				if (bright >= 0) {
					r += ((1 - r) * (bright / 100));
					g += ((1 - g) * (bright / 100));
					b += ((1 - b) * (bright / 100));
				} else {
					r += (r * (bright / 100));
					g += (g * (bright / 100));
					b += (b * (bright / 100));
				}
				image_array[x][y] = new ColorPixel(r, g, b);
			}
		}

		return new MutablePixelArrayPicture(image_array, caption);
	}
}
