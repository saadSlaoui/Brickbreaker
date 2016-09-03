/**
 * CIS 120 Game HW
 * (c) University of Pennsylvania
 * @version 2.0, Mar 2013
 */

// imports necessary libraries for Java swing
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * Game Main class that specifies the frame and widgets of the GUI
 */
public class Game implements Runnable {
	public void run() {
		// NOTE : recall that the 'final' keyword notes immutability
		// even for local variables.

		// Top-level frame in which game components live
		final JFrame frame = new JFrame("Gravity Brick Breaker");
		frame.setLocation(300, 100);
		
		try {
			displayMain(frame);
		} catch (IOException e) {
			e.printStackTrace();
		}		
		
		// Put the frame on the screen
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	public static void displayMain(JFrame frame) throws IOException {
		
		// Main menu image (top of the screen)	
		JLabel image_label = new JLabel();
		try {
			image_label.setIcon(
				new ImageIcon(ImageIO.read(new File("main_background.png"))));
    		frame.add(image_label, BorderLayout.NORTH);
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
		
		// Interaction panel (bottom of the screen)
		JPanel inter_panel = new JPanel();
		frame.add(inter_panel, BorderLayout.SOUTH);
		
		final JButton start_game = new JButton("Start Game");
		inter_panel.add(start_game);
		final JButton infos = new JButton("Infos");
		inter_panel.add(infos);
		//final JButton highScores = new JButton("High-Scores");
		//inter_panel.add(highScores);
		
		// add info screen using a JDialog
		infos.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JDialog info_window = new JDialog();
				
				// add info text to the JDialog using a JTextArea
				LinkedList<String> textLines = new LinkedList<String>();
				try {
					Scanner r = new Scanner(new File("infos.txt"));
					while (r.hasNextLine()) {
						textLines.add(r.nextLine());
					}
					r.close();
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
				
				
				JTextArea jta = new JTextArea();
				jta.setFont(new Font(Font.SERIF, Font.PLAIN, 20));
	            jta.setEditable(false);
	            jta.setLineWrap(true);
	            jta.setWrapStyleWord(true);
	            for (String line : textLines) {
	            	jta.append(line);
	            }
	            
	            info_window.add(jta);
				
	            info_window.setPreferredSize(new Dimension(400, 450));
				info_window.pack();
				info_window.setLocation(300, 140);	
				info_window.setVisible(true);
			}
		});
		
		// transition from main screen to level 1 screen
		start_game.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				frame.remove(inter_panel);
				frame.remove(image_label);
				displayLevel1(frame);
			}
		});
	}
	
	
	public static void displayLevel1(JFrame frame) {			
		JLabel usernameLabel = new JLabel();
		
		// prepare highscore screen
		JDialog highScores_window = new JDialog();
		
		// add GameCourt
		JLabel score = new JLabel();
		JLabel lives = new JLabel();
		JButton reset_button = new JButton("Reset");
		
		GameCourt court = 
			new GameCourt(score, lives, 
					usernameLabel, highScores_window);
		
		frame.add(court, BorderLayout.SOUTH);
		
		court.reset();
		
		reset_button.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				court.reset();
			}
		});
		
		// Add score, level and lives JLabel to top of screen		
		
		JPanel top_panel = gridLayoutTopPanel(court, score, lives, reset_button);
					
		frame.add(top_panel, BorderLayout.NORTH);
		
		highScores_window.setAlwaysOnTop(true);
		highScores_window.setPreferredSize(new Dimension(100, 200));
		highScores_window.pack();
		highScores_window.setLocation(300, 140);
		
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);		
		
		
	}
	
	public static JPanel gridLayoutTopPanel(GameCourt court,
			JLabel score, JLabel lives, JButton reset_button) {
		JPanel top_panel = new JPanel();
		top_panel.setLayout(new GridLayout(1, 4));
		
		top_panel.add(score);
		JLabel level1_label = new JLabel("Level: 1");
		top_panel.add(level1_label);
		top_panel.add(lives);
		top_panel.add(reset_button);
		
		return top_panel;		
	}	
	

	/*
	 * Main method run to start and run the game Initializes the GUI elements
	 * specified in Game and runs it IMPORTANT: Do NOT delete! You MUST include
	 * this in the final submission of your game.
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Game());
	}
}
