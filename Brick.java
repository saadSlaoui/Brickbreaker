import java.awt.Color;
import java.awt.Graphics;

/* The Brick interface lists the instance variables that every Brick
 * should possess as well as the methods which modify the state
 * of a Brick 
 * 
 */

public class Brick extends GameObj {
	
	public static final int WIDTH = 40;
	public static final int HEIGHT = 20;
	public static final int INIT_VEL_X = 0;
	public static final int INIT_VEL_Y = 0;

	
	/* Position in the corresponding 2D array. Should always
	 * be within bounds with regard to the BrickPanel array
	 * 0 <= row <= NUMROWS - 1
	 * 0 <= col <= NUMCOL - 1 
	 */ 
	public int row;
	public int col;
	
	/* Score that is added to the current total score when the 
	 * Brick is broken 
	 */	
	public final static int VALUE_PER_HIT = 500;
	public int value;
	
	/* Color of the brick, depending on resistance */
	public Color c;
	
	/* Current number of hits necessary to break the Brick */ 
	public int hits;
	
	/* Indicates whether the brick should be displayed. Becomes 
	 * false when the Brick is broken
	 */
	public boolean isDisplayed;
	
	/* Existence and nature of Bonus released when Brick is broken, 
	 * determined at random by the constructor.
	 */
	public boolean containsBonus;
	public Bonus bonus;
	
	
	/* Constructor for the Brick class. 
	 * Strength is an int that varies between 1 and 3 and determines
	 * what type of Brick
	 */
	public Brick(int courtWidth, int courtHeight, int resistance, int row, int col) {
		super(INIT_VEL_X, INIT_VEL_Y, row * HEIGHT, col * WIDTH, 
				WIDTH, HEIGHT, courtWidth, courtHeight);
		
		isDisplayed = true;
		this.row = row;
		this.col = col;
		hits = resistance;
		value = hits * VALUE_PER_HIT;
		
		// set color
		if (resistance == 1) {
			c = Color.GREEN;
		} else if (resistance == 2) {
			c = Color.ORANGE;
		} else if (resistance == 3) {
			c = Color.RED;
		}
		
		this.setBonus(resistance);
	}
	
	/* Alternative constructor for the brick class which randomly
	 * generates a brick of resistance 1, 2 or 3
	 * 
	 */
	public Brick(int courtWidth, int courtHeight, int row, int col) {
		super(INIT_VEL_X, INIT_VEL_Y, col * WIDTH, row * HEIGHT, 
				WIDTH, HEIGHT, courtWidth, courtHeight);
		isDisplayed = true;
		this.row = row;
		this.col = col;
		
		int resistance = 0;
		// randomly determine resistance
		double resistanceType = 100 * Math.random();
		if (resistanceType < 33) {
			resistance = 1;
		} else if (resistanceType > 66) {
			resistance = 3;
		} else {
			resistance = 2;
		}
		
		hits = resistance;
		value = hits * VALUE_PER_HIT;
		
		// set color
		if (resistance == 1) {
			c = Color.GREEN;
		} else if (resistance == 2) {
			c = Color.ORANGE;
		} else if (resistance == 3) {
			c = Color.RED;
		}
		
		this.setBonus(resistance);
	}
	
    private void setBonus(int resistance) {
    	String b = this.randomBonus(resistance);
		if (b == null) {
			containsBonus = false;
			bonus = null;
		} else if (b.equals("QuickBall")) {
			containsBonus = true;
			bonus = new QuickBall(this.pos_x, this.pos_y, max_y + height);
		} else if (b.equals("QuickBall")) {
			containsBonus = true;
			bonus = new QuickBall(this.pos_x, this.pos_y, max_y + height);
		} else if (b.equals("LargePaddle")) {
			containsBonus = true;
			bonus = new LargePaddle(this.pos_x, this.pos_y, max_y + height);
		}
    }
	
	/* Helper method that randomly determines whether a given brick 
	 * contains a bonus. The chance of obtaining a bonus increases
	 * with the resistance of the ball. Specifically, the chances of 
	 * getting a bonus are 10%, 20% and 30% for a resistance of 
	 * 1, 2, 3 respectively.
	 * Returns null if no bonus, or else a String representative of the
	 * bonus.
	 */
	private String randomBonus(int resistance) {
		double r = Math.random();
		double bonusRange = 0.12 * resistance;
		
		if (r < bonusRange) {
			double bonusType = 100 * Math.random();
			if (bonusType < 33) {
				return "BigBall";
			} else if (bonusType > 66) {
				return "QuickBall";
			} else {
				return "LargePaddle";
			}
		} else {
			return null;
		}		
	}	

	@Override
	public void draw(Graphics g) {
	}
	
}
