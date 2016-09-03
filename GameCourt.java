import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import javax.imageio.ImageIO;
import javax.swing.*;


/**
 * GameCourt
 * 
 * This class holds the primary game logic for how different objects interact
 * with one another. Take time to understand how the timer interacts with the
 * different methods and how it repaints the GUI on every tick().
 * 
 */
@SuppressWarnings("serial")
public class GameCourt extends JPanel {

	// the state of the game logic
	private String img_file = "garage_background1.png"; // the background
	private BufferedImage img;
	private Paddle paddle;
	private Ball ball;
	
	
	// The game variables and their associated JLabels
    private int numLives;
    private JLabel numLives_label;
    private int score;
    private JLabel score_label;
    
    // the username input passed in by the user at the beginning
    private JLabel username;
    public static boolean usernameWasEntered = false;
    
    // the object in charge of the high scores file
    HighScores highScores;
    JDialog highScores_window;
    JLabel highScores_label;
    
    private Brick[][] brickArray = 
    		new Brick[NUM_ROWS_BRICKARR][NUM_COLS_BRICKARR];
    private LinkedList<Bonus> bonusList = new LinkedList<Bonus>();
	
	public boolean playing = false;

	// Game constants
	public static final int COURT_WIDTH = 600;
	public static final int COURT_HEIGHT = 600;
	public static final int PADDLE_VELOCITY = 16;
	public static final int NUM_ROWS_BRICKARR = 12;
	public static final int NUM_COLS_BRICKARR = 15;
	// Update interval for timer, in milliseconds
	public static final int INTERVAL = 35;
	public static final int INIT_LIVES = 3;

	public GameCourt(JLabel numLives_label, 
			JLabel score_label, JLabel username_label, JDialog highScores_window) {
		// creates border around the court area, JComponent method
		setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		// initialize background
		try {
			img = ImageIO.read(new File(img_file));
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
		
		/* Initialize the brick array, whose design depends on the 
		 * input display.  
		 */
		fillBrickArray(brickArray, getLevel1List());
		
		// The timer is an object which triggers an action periodically
		// with the given INTERVAL. One registers an ActionListener with
		// this timer, whose actionPerformed() method will be called
		// each time the timer triggers. We define a helper method
		// called tick() that actually does everything that should
		// be done in a single timestep.
		javax.swing.Timer timer = new javax.swing.Timer(INTERVAL, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tick();
			}
		});
		timer.start(); 
		
		// Enable keyboard focus on the court area.
		// When this component has the keyboard focus, key
		// events will be handled by its key listener.
		setFocusable(true);
		
		addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_LEFT)
					paddle.v_x = -PADDLE_VELOCITY;
				else if (e.getKeyCode() == KeyEvent.VK_RIGHT)
					paddle.v_x = PADDLE_VELOCITY;
			}

			public void keyReleased(KeyEvent e) {
				paddle.v_x = 0;
			}
		});
        
		// game state initialization
		this.numLives = INIT_LIVES;
		this.numLives_label = numLives_label;
		this.score_label = score_label;
		score_label.setText("Score: " + score);
		numLives_label.setText("Lives: " + numLives);
		
		// high-score initialization
		this.username = username_label;
		this.highScores_window = highScores_window;
		this.highScores_label = new JLabel();
		this.highScores_window.add(highScores_label);
		this.highScores = new HighScores();
	}
	
	// BRICK ARRAY RELATED CODE 

	/* Helper function that fills up the provided Brick array using
	 * an input linked list of int arrays.
	 * Format of documentation for filling the 2D array with bricks:
     * say we want to fill up columns 5 to 8 and 10 to 12 in row 4
     * then we would pass in a linked list of int[] arrays of the 
     * form:
     * ({4, 5, 9}, {4, 10, 13})
     * Specifically, each int array is of size 3 and of the form
     * {row, first_col, last_col + 1}
     * 
     * Then, we can fill in the associated spots with bricks using a
     * for each loop through the list
	 */
	private void fillBrickArray(Brick[][] bricksArr, 
			               LinkedList<int[]> brickList) {
		// first empty every case of the 2D array
		resetBrickArray(bricksArr);
		
		for (int[] a : brickList) {
            for (int j = a[1]; j < a[2]; j++) {
		        bricksArr[a[0]][j] = 
		        		new Brick(COURT_WIDTH, COURT_HEIGHT, a[0], j);
		    }
		}	
	}
	
	/* Simple helper method that takes in a 2D bricks array
	 * and resets every one of its cases to null 
	 */
	private void resetBrickArray(Brick[][] bricksArr) {
		for (int i = 0; i < NUM_ROWS_BRICKARR; i++) {
			for (int j = 0; j < NUM_COLS_BRICKARR; j++) {
				bricksArr[i][j] = null;
			}
		}
	}
	
	/* Generates the linked list corresponding
	 * to level 1 in the format described in the documentation
	 * of fillBrickArray above.
	 */
	private LinkedList<int[]> getLevel1List() {
		LinkedList<int[]> l = new LinkedList<int[]>();
		int[][] vals = {{0, 0, 3}, {0, 4, 7}, {0, 8, 11},
				        {1, 0, 1}, {1, 5, 6}, {1, 8, 9},
				        {2, 0, 1}, {2, 5, 6}, {2, 8, 11},
				        {3, 0, 1}, {3, 5, 6}, {3, 10, 11},
				        {4, 0, 3}, {4, 4, 7}, {4, 8, 11},
				        
				        {6, 1, 2}, {6, 3, 6}, {6, 7, 10}, 
				        {7, 1, 2}, {7, 5, 6}, {7, 7, 8}, {7, 9, 10}, 
				        {8, 1, 2}, {8, 4, 5}, {8, 7, 8}, {8, 9, 10}, 
				        {9, 1, 2}, {9, 3, 4}, {9, 7, 8}, {9, 9, 10},
				        {10, 1, 2}, {10, 3, 6}, {10, 7, 10}, 
				        };
		for (int i = 0; i < vals.length; i++) {
		    l.add(vals[i]);
		}
		
		return l;	
	}
	
	@SuppressWarnings("unused")
	private LinkedList<int[]> getLevel2List() {
		LinkedList<int[]> l = new LinkedList<int[]>();
		int[][] vals = {{1, 4, 9}, 
				        {2, 3, 4}, {2, 9, 10}, 
				        {3, 2, 3}, {3, 5, 6}, {3, 7, 8}, {3, 10, 11},
				        {4, 1, 2}, {4, 5, 6}, {4, 7, 8}, {4, 11, 12},
				        {5, 1, 2}, {5, 11, 12},
				        {6, 1, 2}, {6, 4, 5}, {6, 8, 9}, {6, 11, 12},
				        {7, 2, 3}, {7, 5, 8}, {7, 10, 11},
				        {8, 3, 4}, {8, 9, 10},
				        {9, 4, 8}};
		
		for (int i = 0; i < vals.length; i++) {
		    l.add(vals[i]);
		}
		
		return l;		
	}
	
	/* Helper method that goes through a 2D array of bricks and paints
	 * every one of the input brick with its associated color and 
	 * position on the window
	 */
	private void drawBrickArray(Brick[][] bricksArr, Graphics g) {
		for (int row = 0; row < NUM_ROWS_BRICKARR; row++) {
			for (int col = 0; col < NUM_COLS_BRICKARR; col++) {
				int pos_x = (col) * Brick.WIDTH;
				int pos_y = (row) * Brick.HEIGHT;
				if (bricksArr[row][col] != null) {
					if (bricksArr[row][col].isDisplayed) {
				        g.setColor(bricksArr[row][col].c);
				        g.fillRect(pos_x, pos_y, Brick.WIDTH, Brick.HEIGHT);
					}
				}
			}
		}		
	}
	
	private Set<Brick> hitBrick(Brick[][] brickArr) {
		Set<Brick> brickSet = new HashSet<Brick>();
		
		for (int row = 0; row < NUM_ROWS_BRICKARR; row++) {
			for (int col = 0; col < NUM_COLS_BRICKARR; col++) {
			    Brick b = brickArr[row][col];
		        if (b != null) {
		        	if (b.isDisplayed && ball.willIntersect(b)) {
		        		brickSet.add(b);
		        	}
		        }
			}
		}
		return brickSet;		
	}
	
	// END GAME STATE RELATED CODE
	
	/* Goes through the 2D brickArray to determine if they have
	 * all been broken. If so, returns true to indicate that the 
	 * level is over. Else, returns false. 
	 */
	
	public boolean LevelOver() {
		boolean isOver = true;
		for (int i = 0; i < HEIGHT; i++) {
			for (int j = 0; j < WIDTH; j++) {
				if (brickArray[i][j] == null) {
					continue;
				} else if (brickArray[i][j].isDisplayed) {
					isOver = false;
					break;
				}
			}
		}
		return isOver;
	}
	
	/* Method that returns true if the end of the game was reached,
	 * either by emptying the brickArray 2D array or by getting the
	 * number of lives down to zero. 
	 */
	
	public boolean endState() {
		return (LevelOver() ||
				getNumLives() == 0);
	}
	
	// STATE ACCESS METHODS
	
	public int getScore() {
		return score;
	}
		
	public int getNumLives() {
		return numLives;		
	}
		
		
	/**
	 * (Re-)set the level to its initial state.
	 */
	public void reset() {
		numLives = INIT_LIVES;
		score = 0;
        ball = new Ball(COURT_WIDTH, COURT_HEIGHT);
        paddle = new Paddle(COURT_WIDTH, COURT_HEIGHT);
        fillBrickArray(brickArray, getLevel1List());
        askUsername();
        
        // update labels
        numLives_label.setText("Lives: " + numLives);
        score_label.setText("Score: " + score);
    
		requestFocusInWindow();
	}
	
	public void askUsername() {
		if (!usernameWasEntered) {
			JTextField text_field = new JTextField("Enter Username, then press enter: ");
			// ask for username
			JDialog username_window = new JDialog();
			
			text_field.addKeyListener(new KeyAdapter() {
				public void keyPressed(KeyEvent e) {
					if (e.getKeyCode() == KeyEvent.VK_ENTER) {
						// read text
						username.setText(text_field.getText().substring(34));
						username_window.dispose();
						usernameWasEntered = true;
						playing = true;
					}
			    }
			});
			
			username_window.add(text_field);
			username_window.setPreferredSize(new Dimension(250, 120));
			username_window.setAlwaysOnTop(true);
			
			username_window.pack();
			username_window.setLocation(300, 140);	
			username_window.setVisible(true);
		}
	}
	
	

	/**
	 * This method is called every time the timer defined in the constructor
	 * triggers.
	 */
	void tick() {
		if (playing) {
			ball.move();
			paddle.move();
			
			// Check for ball motion
			ballLost();
			
			// check for ball collision
			for (Brick b : hitBrick(brickArray)) {
				ball.bounceBrick(b);
				// update brick state and score				    
			    score += brickBreak(b);
				score_label.setText("Score: " + score);
			}			
			
			if (ball.willIntersect(paddle)) {
				ball.bounce(Direction.DOWN); // change y velocity
				ball.bouncePaddle(paddle);   // change x velocity
			}
			ball.bounce(ball.hitWall());
			
			if (!bonusList.isEmpty()) {
				for (Bonus bonus : bonusList) {
					bonus.move();
					
					if (bonus.intersects(paddle) && bonus.isDisplayed) {
						/* The bonus was caught. We modify the state accordingly
						 * and set a timer which restores the state and removes
						 * the bonus from the bonus list as soon as the original
						 * time interval is elapsed.
					     */
						bonus.isDisplayed = false;
						
						if (bonus.bonusName.equals("LargePaddle")) { 
						    bonus.modifyState(paddle, bonusList);
						} else if (bonus.bonusName.equals("QuickBall") || 
								   bonus.bonusName.equals("BigBall")) {
							bonus.modifyState(ball, bonusList);
						}
					} else if (bonus.pos_x + bonus.v_x > bonus.max_y) {
					    // the bonus fell to the bottom of the screen and wasn't caught
						bonus.isDisplayed = false;
						bonusList.remove(bonus);
					}
				}
			}
			
			// check for end game
			if (endState()) {
				playing = false;
				usernameWasEntered = false;
				
				highScores.addHighScore(username.getText(), score);
				highScores.displayHighScores(highScores_window, highScores_label);
			}
			repaint();
		}		
	}
	
	
	/* Helper method that checks whether the ball has reached 
	 * the bottom wall and decreases the numLives instance variables
	 * by one in that case. Also updates the associated JLabel 
	 * accordingly and launches the end state when appropriate.
	 */
	private void ballLost() {
		if (ball.pos_y + ball.v_y > ball.max_y) {
		    ball.pos_x = Ball.INIT_POS_X;
		    ball.pos_y = Ball.INIT_POS_Y;
		    ball.v_x = Ball.INIT_VEL_X;
		    ball.v_y = Ball.INIT_VEL_Y;
		    if (numLives - 1 == 0) {
				numLives = 0;
				numLives_label.setText("Lives: " + numLives);
				playing = false;
				bonusList = new LinkedList<Bonus>();
			} else {
				numLives--;		
				numLives_label.setText("Lives: " + numLives);
			}
		    // cancel any bonus modification
			if (!bonusList.isEmpty()) {
			    for (Bonus b : bonusList) {
				    if (!b.isRestoredState) {
					    if (b.bonusName.equals("LargePaddle")) {
				            b.restoreState(paddle);
					    } else {
						    b.restoreState(ball);
					    }
				    }			
			    }
			}
		}
	}
	
	/* Takes in a brick and decrements its hit instance variable
	 * by 1. If it reaches zero, makes the necessary changes to the
	 * local score and to the state of the associated brick. Also
	 * adds the associated bonus to the bonus list if the brick 
	 * contained one.
	 */
	
	private int brickBreak(Brick b) {
		if (b.hits - 1 == 0) {
			b.hits = 0;
			b.isDisplayed = false;
			if (b.containsBonus) {
				bonusList.add(b.bonus);
			}
			return b.value; 
		} else {
			b.hits--;
			return 0;
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		// display background
		g.drawImage(img, 0, 0, 600, 600, null);
		
		ball.draw(g);
		paddle.draw(g);
		// draw every brick in the brick array
		drawBrickArray(brickArray, g);
		// draw every bonus in the list
		if (!bonusList.isEmpty()) {
			for (Bonus bonus : bonusList) {
			    if (bonus.isDisplayed) {
				    bonus.draw(g);
			    }
			}
		}
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(COURT_WIDTH, COURT_HEIGHT);
	}
}
