package a7;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

public class FramePuzzleWidget extends JPanel implements MouseListener, KeyListener {

	private PictureView blank;
	private PictureView[][] pieces;
	private ObservablePicture[][] pictures;
	private Picture picture;
	private String caption;

	public FramePuzzleWidget(Picture picture) {
		this.picture = picture;
		this.caption = "caption";

		pieces = new PictureView[5][5];
		pictures = new ObservablePictureImpl[5][5];

		// split image
		Picture[][] splitted = split();

		// Change color
		Pixel[][] pixels = new Pixel[splitted[4][4].getWidth()][splitted[4][4].getHeight()];
		for (int i = 0; i < splitted[4][4].getWidth(); i++) {
			for (int j = 0; j < splitted[4][4].getHeight(); j++) {
				pixels[i][j] = splitted[4][4].getPixel(i, j).darken(1.0);
			}
		}
		// Reassign to right
		splitted[4][4] = new MutablePixelArrayPicture(pixels, this.caption);

		setLayout(new GridLayout(5, 5));

		// Add listener
		for (int j = 0; j < 5; j++) {
			for (int i = 0; i < 5; i++) {
				pictures[i][j] = splitted[i][j].createObservable();
				pieces[i][j] = new PictureView(pictures[i][j]);
				pieces[i][j].addMouseListener(this);
				pieces[i][j].addKeyListener(this);
				add(pieces[i][j]);
			}
		}
		blank = pieces[4][4];
		this.addKeyListener(this);
		this.setFocusable(true);

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		int blank_col = getColumn(getBlank());
		int blank_row = getRow(getBlank());

		// Process different executions
		switch (e.getKeyCode()) {

		case KeyEvent.VK_UP:
			if (blank_row - 1 < 0) {
				break;
			}
			ObservablePicture t_down = pictures[blank_col][blank_row];
			pictures[blank_col][blank_row] = pictures[blank_col][blank_row - 1];
			pieces[blank_col][blank_row].setPicture(pictures[blank_col][blank_row]);
			pictures[blank_col][blank_row - 1] = t_down;
			pieces[blank_col][blank_row - 1].setPicture(t_down);
			blank = pieces[blank_col][blank_row - 1];
			break;

		case KeyEvent.VK_DOWN:

			if (blank_row + 1 >= 5) {
				break;
			}
			ObservablePicture t_up = pictures[blank_col][blank_row];
			pictures[blank_col][blank_row] = pictures[blank_col][blank_row + 1];
			pieces[blank_col][blank_row].setPicture(pictures[blank_col][blank_row]);
			pictures[blank_col][blank_row + 1] = t_up;
			pieces[blank_col][blank_row + 1].setPicture(t_up);
			blank = pieces[blank_col][blank_row + 1];
			break;

		case KeyEvent.VK_LEFT:

			if (blank_col - 1 < 0) {
				break;
			}
			ObservablePicture t_right = pictures[blank_col][blank_row];
			pictures[blank_col][blank_row] = pictures[blank_col - 1][blank_row];
			pieces[blank_col][blank_row].setPicture(pictures[blank_col][blank_row]);

			pictures[blank_col - 1][blank_row] = t_right;
			pieces[blank_col - 1][blank_row].setPicture(t_right);
			blank = pieces[blank_col - 1][blank_row];

			break;

		case KeyEvent.VK_RIGHT:
			if (blank_col + 1 >= 5) {
				break;
			}
			ObservablePicture t_left = pictures[blank_col][blank_row];
			pictures[blank_col][blank_row] = pictures[blank_col + 1][blank_row];
			pieces[blank_col][blank_row].setPicture(pictures[blank_col][blank_row]);

			pictures[blank_col + 1][blank_row] = t_left;
			pieces[blank_col + 1][blank_row].setPicture(t_left);
			blank = pieces[blank_col + 1][blank_row];

			break;

		}

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int clicked_col = getColumn((PictureView) e.getSource());
		int clicked_row = getRow((PictureView) e.getSource());

		int blank_col = getColumn(getBlank());
		int blank_row = getRow(getBlank());

		// Catch all and swap
		if (clicked_col < blank_col && clicked_row == blank_row) {
			ObservablePicture temp_pic = pictures[blank_col][blank_row];

			for (int i = 0; i < blank_col - clicked_col; i++) {
				// Swap and set
				pictures[blank_col - i][blank_row] = pictures[blank_col - i - 1][blank_row];
				pieces[blank_col - i][blank_row].setPicture(pictures[blank_col - i][blank_row]);
			}
			pictures[clicked_col][clicked_row] = temp_pic;
			pieces[clicked_col][clicked_row].setPicture(temp_pic);
			blank = pieces[clicked_col][clicked_row];

		} else if (clicked_col > blank_col && clicked_row == blank_row) {
			ObservablePicture temp_pic = pictures[blank_col][blank_row];

			for (int i = 0; i < clicked_col - blank_col; i++) {
				pictures[blank_col + i][blank_row] = pictures[blank_col + i + 1][blank_row];
				pieces[blank_col + i][blank_row].setPicture(pictures[blank_col + i][blank_row]);
			}
			pictures[clicked_col][clicked_row] = temp_pic;
			pieces[clicked_col][clicked_row].setPicture(temp_pic);
			blank = pieces[clicked_col][clicked_row];

		} else if (clicked_col == blank_col && clicked_row < blank_row) {
			ObservablePicture temp_pic = pictures[blank_col][blank_row];

			for (int i = 0; i < blank_row - clicked_row; i++) {
				pictures[blank_col][blank_row - i] = pictures[blank_col][blank_row - i - 1];
				pieces[blank_col][blank_row - i].setPicture(pictures[blank_col][blank_row - i]);
			}
			pictures[clicked_col][clicked_row] = temp_pic;
			pieces[clicked_col][clicked_row].setPicture(temp_pic);
			blank = pieces[clicked_col][clicked_row];

		} else if (clicked_col == blank_col && clicked_row > blank_row) {
			ObservablePicture temp_pic = pictures[blank_col][blank_row];

			for (int i = 0; i < clicked_row - blank_row; i++) {
				pictures[blank_col][blank_row + i] = pictures[blank_col][blank_row + i + 1];
				pieces[blank_col][blank_row + i].setPicture(pictures[blank_col][blank_row + i]);
			}
			pictures[clicked_col][clicked_row] = temp_pic;
			pieces[clicked_col][clicked_row].setPicture(temp_pic);
			blank = pieces[clicked_col][clicked_row];
		}
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

	private Picture[][] split() {
		Picture[][] split_pic = new Picture[5][5];
		int width_len = picture.getWidth() / 5;
		int height_len = picture.getHeight() / 5;
		int row_count = 0;
		for (int y = 0; y < picture.getHeight(); y += height_len) {
			// Reset column
			int col_count = 0;
			for (int x = 0; x < picture.getWidth(); x += width_len) {
				split_pic[col_count][row_count] = picture.extract(x, y, width_len, height_len);
				col_count++;
			}
			row_count++;
		}
		return split_pic;
	}

	// Getters

	private int getRow(PictureView piece) {
		int row = -1;
		for (int x = 0; x < 5; x++) {
			for (int y = 0; y < 5; y++) {
				if (piece == pieces[x][y]) {
					row = y;
					break;
				}
			}
		}
		return row;
	}

	private int getColumn(PictureView piece) {
		int col = -1;
		for (int x = 0; x < 5; x++) {
			for (int y = 0; y < 5; y++) {
				if (piece == pieces[x][y]) {
					col = x;
					break;
				}
			}
		}
		return col;
	}

	private PictureView getBlank() {
		PictureView piece = null;
		for (int x = 0; x < 5; x++) {
			for (int y = 0; y < 5; y++) {
				if (pieces[x][y] == blank) {
					piece = blank;
					break;
				}
			}
		}
		return piece;
	}
}
