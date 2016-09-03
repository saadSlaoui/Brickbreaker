import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import javax.imageio.ImageIO;

public class QuickBall extends Bonus {
	public static final String img_file = "QuickBall.png";
	private static BufferedImage img;
	
	public QuickBall(int pos_x, int pos_y, int courtHeight) {
		super(pos_x, pos_y, SIZE, SIZE, courtHeight);
		bonusName = "QuickBall";
		try {
			if (img == null) {
				img = ImageIO.read(new File(img_file));
			}
		} catch (IOException e) {
			System.out.println("Internal Error:" + e.getMessage());
		}
	}
	
	@Override
	public void modifyState(GameObj gameObj, LinkedList<Bonus> bonusList) {
		if (isRestoredState) {
		    gameObj.v_x *= 2;
		    gameObj.v_y *= 2;
		    isRestoredState = false;
		    
		    javax.swing.Timer bonus_timer = new javax.swing.Timer(INTERVAL, 
		    		new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (reduceInterval(INTERVAL) && !isRestoredState) {
				        restoreState(gameObj);  
				        bonusList.remove(this);
					}
				}
			});
			bonus_timer.start();
		}
	}
	
	@Override
	public void restoreState(GameObj gameObj) {
		gameObj.v_x = Ball.INIT_VEL_X;
		gameObj.v_y = Ball.INIT_VEL_Y;
		isRestoredState = true;
	}
	
	@Override
	public void draw(Graphics g) {
		if (isDisplayed) {
		    g.drawImage(img, pos_x, pos_y, width , height, null);
		}
	}
	
}