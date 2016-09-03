import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import javax.imageio.ImageIO;

public class BigBall extends Bonus {
	public static final String img_file = "BigBall.png";
	private static BufferedImage img;
	
	public BigBall(int pos_x, int pos_y, int courtHeight) {
		super(pos_x, pos_y, SIZE, SIZE, courtHeight);
		bonusName = "BigBall";
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
		    gameObj.width = (int) (1.5 * gameObj.width);
		    gameObj.height = (int) (1.5 * gameObj.height);
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
		gameObj.width = Ball.SIZE;
		gameObj.height = Ball.SIZE;
		isRestoredState = true;
	}
	
	@Override
	public void draw(Graphics g) {
		if (isDisplayed) {
		    g.drawImage(img, pos_x, pos_y, width , height, null);
		}
	}
	
}
