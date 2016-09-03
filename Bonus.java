import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.LinkedList;

/* Bonus class that is extended by 3 other bonuses implementing 
 * different modifyState methods. Since the Bonus objects only
 * ever fall down and only need to accout for collision with
 * the paddle or the bottom of the screen, they require less
 * methods and less instance variables than a regular game 
 * object. 
 */
public class Bonus {
	// time interval during which the Bonus should operate
    public final static int INTERVAL = 8000;
    public final static int MOTION_INTERVAL = 35;
    
    public static final String img_file = null; // image file 
    public static final int SIZE = 40; // width and height
    public static final int INIT_V_Y = 5;
    public static final int MOTION_V_X = 5;
    
    public boolean isDisplayed;
    public boolean isRestoredState;
    public boolean motionActivated = false;
    
    public String bonusName;
    public int pos_x;
    public int pos_y;
    public int init_x;
    public int init_y;
    public int v_x;
    public int v_y;
    public int max_y;
    
    public int width;
    public int height;
    public int countDown = INTERVAL;
    public static final int GRAVITY = 1;
    
    private Area objArea;
    
    // constructor
    public Bonus(int init_x, int init_y, int width, int height, int courtHeight) {
    	isDisplayed = true;
    	isRestoredState = true;
    	this.init_x = init_x;
    	this.init_y = init_y;
    	pos_x = init_x;
    	pos_y = init_y;
    	this.width = width;
    	this.height = height;
    	v_x = 0;
    	v_y = INIT_V_Y;
    	max_y = courtHeight - SIZE;   
    }
    
    /* Moves the bonus down and switches the display boolean
     * to false if it reaches the end of the screen. The fancy 
     * motion is randomly chosen among three options
     */
    public void move() {
    	if (!motionActivated) {
    		int r = (int) (100 * Math.random());
    		
    		if (r < 33) {
    	        upDownMotion();
    		} else if (r > 66) {
    		    rightLeftMotion();
    		} else {
    		    circularMotion();
    		}
    	}
    	
    	pos_x += v_x;
    	pos_y += v_y;
    }
    
    /* The following are helper functions that play with the physics
     * of the bonus object in order to make catching it more 
     * challenging (and more fun!)
     * 
     * upDownMotion() gives the Bonus an upward velocity for half a second, then
     * a downward velocity for 1 second, and repeats. This is accomplished 
     * using the modulo function
     * 
     *  rightLeftMotion() uses the same algorithm to periodically give the Bonus
     *  a motion toward the left or the right every 750ms
     *  
     *  circularMotion() is more subtle: it involves rotation around a central downward
     *  axis - once around the circle every 500 ms - as the bonus is accelerated downward,
     *  in order to allow for a spiral-like motion. Very cool.
     */
    
    private void upDownMotion() {
    	motionActivated = true;
    	
    	javax.swing.Timer motion_timer = 
    			new javax.swing.Timer(MOTION_INTERVAL, new ActionListener() {
			int timeCount = 0;
    		public void actionPerformed(ActionEvent e) {
    			if (isDisplayed) {
				    timeCount += MOTION_INTERVAL;
				    if (timeCount % 1500 < 500) {
					    v_y = -INIT_V_Y;
				    } else if (timeCount % 1500 > 500) {
				    	v_y = INIT_V_Y;
				    }
    			}
			}
		});
		motion_timer.start();    	
    }
    
    private void rightLeftMotion() {
    	motionActivated = true;
    	
    	javax.swing.Timer motion_timer = 
    			new javax.swing.Timer(MOTION_INTERVAL, new ActionListener() {
			int timeCount = 0;
    		public void actionPerformed(ActionEvent e) {
    			if (isDisplayed) {
				    timeCount += MOTION_INTERVAL;
				    if (timeCount % 1500 < 750) {
					    v_x = MOTION_V_X;
				    } else if (timeCount % 1500 > 750) {
				    	v_x = -MOTION_V_X;
				    }
    			}
			}
		});
		motion_timer.start();    	
    }
    
    private void circularMotion() {
        motionActivated = true;   
        
    	javax.swing.Timer motion_timer = 
    			new javax.swing.Timer(MOTION_INTERVAL, new ActionListener() {
			int timeCount = 35; // can't be zero for future calculations
			int grav_v_y = 0;
			double theta = 0;
			
    		public void actionPerformed(ActionEvent e) {
    			if (isDisplayed) {
    				timeCount +=  MOTION_INTERVAL;
    				
    				// action of gravity over y
    				if (timeCount % 300 < 40) {
    					grav_v_y += GRAVITY;
    				}
    				
    				int axis_y_pos = init_y + INIT_V_Y * (timeCount / MOTION_INTERVAL);
    				int axis_x_pos = init_x + (SIZE / 2); 
    				
    				double angle_increment = (2 * Math.PI) / (500 / MOTION_INTERVAL);
    				theta = (theta + angle_increment) % (2 * Math.PI);
    				
    				pos_x = axis_x_pos + (int) (Math.cos(theta) * 50);
    				pos_y = axis_y_pos + (int) (Math.sin(theta) * 50);
    				
    				v_y = INIT_V_Y + grav_v_y;
    			}
			}
		});
		motion_timer.start();   
    }
    
    
    /* Determines whether the bonus object intersects
     * with a given game object. Used to determine 
     * collision with the paddle. 
     */
    
    public boolean intersects(GameObj obj){
    	objArea = 
				new Area(new Ellipse2D.Double(pos_x, pos_y, SIZE, SIZE));
		return objArea.intersects(
			new Rectangle2D.Double(obj.pos_x + obj.v_x, obj.pos_y + obj.v_y,
					obj.width, obj.height));
	}
    
    /* Called within the tic() method in GameCourt. 
     * Decrements the local interval by a given int, 
     * and cancels the effect of the modification if the
     * time limit is reached 
     */
    public boolean reduceInterval(int reduceTime) {
    	if (countDown - reduceTime < 0) {
    		countDown = INTERVAL; // reinitialize for other bonuses
    		return true;
    	} else {
    		countDown -= reduceTime;
    		return false;
    	}
    }
    
	/* Method that affects the state of the provided game object for 
	 * a finite time shared by all the implementations of Bonus.
	 * Needs to be implemented locally by each subclass.
	 */
	public void modifyState(GameObj gameObj, LinkedList<Bonus> bonusList) {
	}
	
	/* Method that undoes the effect of modifyState(). Called
	 * by the reduceInterval method when the time interval is
	 * completely elapsed. 
	 */
	public void restoreState(GameObj gameObj) {
	}
	
	// Same draw method as in gameObj
	public void draw(Graphics g) {
	}
	
}
