import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

/* Ball class that creates a red ball which is thrown around by the paddle
 * and collisions with the bricks. Inspired by the Circle class, with its
 * instance variables slightly modified. 
 * Starts right above the Paddle.
 */

public class Ball extends GameObj {
	public static final int SIZE = 30;
	public static final int INIT_POS_X = 230;
	public static final int INIT_POS_Y = 350;
	public static final int INIT_VEL_X = 7;
	public static final int INIT_VEL_Y = 9;
	
	private Area objArea;

	public Ball(int courtWidth, int courtHeight) {
		super(INIT_VEL_X, INIT_VEL_Y, INIT_POS_X, INIT_POS_Y, SIZE, SIZE,
				courtWidth, courtHeight);
	}
	
	/* Called when ball hits paddle.  Determines the future direction of the 
	 * ball depending on where in the paddle happened the collision: if 
	 * the ball hit the left half of the paddle, its new x velocity is more or 
	 * less positive depending on the distance from the center, and vice-versa.
	 */
	
	public void bouncePaddle(GameObj paddle) {
		if (this.willIntersect(paddle)) {
			int paddle_center = paddle.pos_x + (paddle.width / 2);
			if (this.pos_x >= paddle_center) {
				int dx = pos_x - paddle_center;
				double change_factor = 
						(dx * 1.0) / (paddle.width / 2);
				this.v_x = (int)
					Math.abs(INIT_VEL_X * change_factor);
			} else {
				int dx = paddle_center - pos_x;
				double change_factor = 
						(dx * 1.0) / (paddle_center - paddle.pos_x);
				this.v_x = (int)
					-Math.abs(INIT_VEL_X * change_factor);
			}
		}
	}
	
	
	
	@Override
	public boolean willIntersect(GameObj other) {
		objArea = 
				new Area(new Ellipse2D.Double(pos_x, pos_y, SIZE, SIZE));
		return objArea.intersects(
			new Rectangle2D.Double(other.pos_x + other.v_x, other.pos_y + other.v_y,
					other.width, other.height));
	}
	
	public void bounceBrick(Brick b) {
		// if null do nothing
		if (b == null) {
			return;
		}
		// if something check if intersects
		if (this.willIntersect(b)) {
			// if intersects check if (1) bottom, (2) top, (3) left, (4) right
			// (1) || (2)
			if ((pos_y <= b.pos_y + b.height &&
				 pos_y + height / 2 > b.pos_y + b.height) ||
				(pos_y + height >= b.pos_y &&
				 pos_y + height / 2 < b.pos_y)) {
				this.v_y = - v_y;	
			}
			// (3) || (4)
			else {
				this.v_x = -v_x;
			}
		}
	}

	@Override
	public void draw(Graphics g) {
		g.setColor(Color.RED);
		g.fillOval(pos_x, pos_y, width, height);
	}
}
