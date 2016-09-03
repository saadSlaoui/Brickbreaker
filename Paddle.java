import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/* Paddle object displayed using an image. Class inspired from
 * the provided Poison class, with different instance variables.
 */

public class Paddle extends GameObj {
	public static final String img_file = "paddle.png";
	public static final int WIDTH = 120;
	public static final int HEIGHT = 30;
	public static final int INIT_X = 210;
	public static final int INIT_Y = 560;
	public static final int INIT_VEL_X = 0;
	public static final int INIT_VEL_Y = 0;

	private static BufferedImage img;

	public Paddle(int courtWidth, int courtHeight) {
		super(INIT_VEL_X, INIT_VEL_Y, INIT_X, INIT_Y,
			   WIDTH, HEIGHT, courtWidth, courtHeight);
		try {
			if (img == null) {
				img = ImageIO.read(new File(img_file));
			}
		} catch (IOException e) {
			System.out.println("Internal Error:" + e.getMessage());
		}
		
	}		

	@Override
	public void draw(Graphics g) {
	    g.drawImage(img, pos_x, pos_y, width , height, null);
	}
}
