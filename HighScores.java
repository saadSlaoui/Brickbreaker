import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;

import javax.swing.JDialog;
import javax.swing.JLabel;

/* Class that maintains a high-score file which is updated every time
 * a user reaches the end state of the game.
 * Consists of 5 lines of the format <Username> <Score>, sorted in
 * descending order. When no games have been played, the 5 lines 
 * all have username "XXX" and score 000 
 * 
 */


public class HighScores {
    
	/* Tree Map to keep track of the highScores in a sorted order.
	 * Requires us to maintain the invariant: map of length 5 with
	 * keys sorted in descending order 
	 */
	private Map<Integer, String> highScores;
	// Array that is gradually updated to have the score in a sorted order
	private int[] sortedScores;
	
	public HighScores() {
		sortedScores = new int[5];
		highScores = new HashMap<Integer, String>();
		for (int i = 0; i < sortedScores.length; i++) {
			highScores.put(0, "XXX");
		}
	}
	
	
	/* Constructor for the highScores class: resets all the values of the 
	 * highScores instance variables to 
	 */
	
	public void addHighScore(String username, int newScore) {
		// check if score is in top 5, react accordingly
		for (int i = 0; i < sortedScores.length; i++) {
			if (newScore > sortedScores[i]) {
				int lastScore = sortedScores[4]; // needs to be deleted from map
				int[] newScoreArr = new int[5];
				for (int j = 0; j < i; j++) {
					newScoreArr[j] = sortedScores[j];
				}
				newScoreArr[i] = newScore;
				for (int j = i + 1; j < sortedScores.length; j++) {
					newScoreArr[j] = sortedScores[j - 1];					
				}
				
				sortedScores = newScoreArr;
				
				highScores.remove(lastScore);
				highScores.put(newScore, username);
				
				break;
			}
		}
	}
	
	public void displayHighScores(JDialog highScores_window, JLabel highScores_label) {
		// update text in high score file
        try {
			BufferedWriter bw = new BufferedWriter(new FileWriter("highScores.txt"));
			bw.flush();
			
			for (int i = 0; i < sortedScores.length; i++) {
				if (highScores.get(sortedScores[i]) == null) {
					bw.write("XXX " + sortedScores[i]);
				} else {
			        bw.write(highScores.get(sortedScores[i]) + " " + sortedScores[i]);
				}
			    bw.newLine();
			}
			
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}        
        
        // write out the text on a JDialog window
		LinkedList<String> textLines = new LinkedList<String>();
		try {
			Scanner r = new Scanner(new File("highScores.txt"));
			while (r.hasNextLine()) {
				textLines.add(r.nextLine());
			}
			r.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		
		String finalText = "<html> High Scores: <br> ";
		for (String line : textLines) {
        	finalText += "<br> " + line;
		}
		
		highScores_label.setText(finalText);		
		highScores_window.setVisible(true);		
	}
}
