import java.awt.event.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
/*
 * Shawn Plotko
 * June 14, 2019
 * Tetris
 * 
 * Sources:
 * Moodle for JOptionPane information
 * https://stackoverflow.com/questions/5131547/java-keylistener-in-separate-class used for IObserver information 
 * https://docs.oracle.com/javase/7/docs/api/javax/swing/JFrame.html for JFrame information
 * https://www.javatpoint.com/java-jpanel for JPanel information
 */
public class Tetris implements IObserver {
	static TetrisBoard board =null;
	Shape shape;
	int score = 0, level = 1, numTotalEliminatedLines = 0;
	public static void main(String[] args) throws InterruptedException, IOException  {
		//Creates new start page
		StartPage start = new StartPage();
		//Loops infinitely until a button is pressed. Attempt to make StartPage main was made but this breaks a lot of other code 
		while(start.isPlayBtnPressed()==false) {
			System.out.println("");
		}
		//Removes start page
		start.setVisible(false);
		//Creates the board
		board =  new TetrisBoard(20,10) ; 
		
		//Starts a new tetris game, once game is over asks user if they want to play again, if yes board is cleared and the game starts again
		do {
			resetBoard();
			new Tetris();
		}while(endOfGameMessage()==0);
		
		System.exit(0);
		
		
		
		
	}
	/**
	 * All pegs are removed from the board
	 */
	private static void resetBoard () {
		//removes all pegs on board
		for(int i=board.MIN;i<=board.COLS_MAX;i++) {
			for(int j=board.MIN;j<=board.ROWS_MAX;j++) {
				board.grid[i][j]=null;
			}
		}
	}
	/**
	 * Creates JOptionPane asking user if they would like to play again. Gives option to play again or exit. Returns which button was pressed.
	 * 
	 */
	private static int endOfGameMessage() {
		Object[] options = {"Play again", "Quit"};
		int btnPressed = JOptionPane.showOptionDialog(null, "Would you like to play again?","Game Over", JOptionPane.OK_CANCEL_OPTION,
		                     JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
		return btnPressed;
	}
	/**
	 * Uses JOptionPane and JTextField to get and return the user's name
	 * 
	 */
	private String getUsersName() {
		final int CANCEL_OPTION = 1;
		Object[] options = {"Save", "Don't Save"};
		//Creates JPanel with textfield where user can enter their name
		JPanel panel = new JPanel();
		panel.add(new JLabel("Game Over! Enter your name to save:"));
		JTextField textField = new JTextField(10);
		panel.add(textField);
		//Creates JOptionPane with Save and Don't Save buttons
		int result = JOptionPane.showOptionDialog(null, panel, "Game Over",
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE,
				null, options, null);
		//If the user doesn't want to save null is returned
		if (result == CANCEL_OPTION){
			return null;
		}
		//Otherwise user's name is returned
		String usersName = textField.getText();
		return usersName;
	}
	/**
	 * Writes all the user's statistics to a text file. 
	 * 
	 */
	private  void saveStats(String usersName) throws IOException {
		String path = "stats.txt";
		Writer writer =null;
		writer = new FileWriter(path,true);
		File file = new File(path);
		PrintWriter output = null;
		output = new PrintWriter(writer);
		//Writes user info to file and formats it
		if(usersName != null){
			output.printf("%-10s %-7s %-9s %-5s\n",usersName ,score,level,numTotalEliminatedLines);
		}
		output.close();
	}
	/**
	 *Acts as the main game loop. Generates new shape, drops it, updates and displays current user statistics 
	 *
	 */
	private void gameLoop() throws InterruptedException, IOException
	{
		final int originalDelay = 1000;
		do {
			//Displays score, number of eliminated lines and level
			String message =String.format("%-15s %5s %15s", "Score: " + score,"Lines: " + numTotalEliminatedLines, "Level: " + level); 
			board.displayMessage(message);
			
			//Creates a new shape
			createRandomShape();
			//Breaks out of loop and ends game if shape would overlap existing shape
			if(isGameOver()) {
				saveStats(getUsersName());
				break;
			}
			
			do{
				
				board.drawShape(shape);
				//Creates delay which slows down piece falling and allows for brief movement once shape reaches ground
				Thread.sleep(originalDelay-level*100);
				board.removeShape(shape);
				//Draws shape one last time and breaks out of loop if shape has reached its end point
				if(board.isShapeDone(shape)) {
					board.drawShape(shape);
					break;
				}else {
					//If end point is not reached shape is dropped again
					shape.dropShape();
				}

			}while(true);
		
			//Checks for and eliminates any full lines 
			board.eliminateFullLines();
			
			//Calculates total eliminated lines
			numTotalEliminatedLines+=board.getNumEliminatedLines();
			
			//Calculates level
			level = numTotalEliminatedLines/10 +1;
			
			//Calculates score based on num of eliminated lines
			switch(board.getNumEliminatedLines()) {
			case 1:
				score+=40*(level+1);
				break;
			case 2:
				score+=100*(level+1);
				break;
			case 3:
				score +=300*(level+1);
				break;
			case 4:
				score += 1200*(level+1);
				break;
			}
		}while(true);
		
	}
	
	/**
	 * Observes the Board class and receives KeyEvents
	 * Allows user to move shape left and right, rotate and quick drop based on key pressed
	 */
	@Override
	public void update(KeyEvent keyEvent) {
		
		//Moves shape left or right
		if((board.getKey()=='a' || board.getKey()=='d')&& board.isCanDraw(shape, board.getKey())) {
			board.removeShape(shape);
			shape.moveShape(board.getKey());
			board.drawShape(shape);
		//Rotates shape
		}else if(board.getKey()=='k'&&board.isCanDraw(shape, board.getKey())) {
			board.removeShape(shape);
			shape.rotateShape();
			board.drawShape(shape);
		//Quick drops shape
		}else if(board.getKey()==' '&&board.isCanDraw(shape,board.getKey())){
			board.removeShape(shape);
			shape.moveToFinal(board.finalShapePosition(shape));
			board.drawShape(shape);
		}
	}

	/**
	 * Sets shape as a new random shape
	 */
	private void createRandomShape(){
		shape = new Shape();
	}
	/**
	 *Tetris constructor. Adds Tetris as an observer of the Board class and starts the game loop. 
	 *
	 */
	public Tetris() throws InterruptedException, IOException{
		//Allows tetris class to observe events of the board class
		board.addObserver(this);
		//Starts game
		gameLoop();
	}
	
	/**
	 * 
	 * Returns true if spawned shape coordinates overlap an existing shape
	 * Used to check if the game is over
	 */
	private boolean isGameOver() {
		//Checks if spawned shape coords overlap existing shape
		for(Coordinate element:shape.getShapeCoords()) {
			if(element!=null&&board.grid[element.getCol()][element.getRow()]!=null) {
				return true;
			}
		}
		return false;
	}
	
	
	
}

