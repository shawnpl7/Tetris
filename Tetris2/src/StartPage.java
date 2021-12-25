import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class StartPage extends JFrame  {
	boolean isPlayBtnPressed = false;
	
	public StartPage() throws IOException {
		//Sets background image as a JLable
		
		String path = "StartPageBackground.jpg";
	    File file = new File(path);
	    Font btnFont = new Font("Dialog",1,20);
	    BufferedImage image = ImageIO.read(getClass().getResourceAsStream(path));
	    JLabel label = new JLabel(new ImageIcon(image));
	    setTitle("Tetris");
	    //Creates play button and sets appropriate size and coordinates
	    JButton playBtn = new JButton("New Game");
	    playBtn.setFont(btnFont);
	    playBtn.setBounds(20, 430, 150, 80);
	    
	    //Creates statistics button and sets appropriate size and coordinates
	    JButton statsBtn = new JButton("Statistics");
	    statsBtn.setFont(btnFont);
	    statsBtn.setBounds(220, 430, 150, 80);
		
		//Creates help button and sets appropriate size and coordinates
		JButton helpBtn = new JButton("Help");
		helpBtn.setFont(btnFont);
		helpBtn.setBounds(420, 430, 150, 80);
		
		//Creates a JPanel
		JPanel panel = new JPanel();
		
		//Adds all the buttons and background image and puts them in the appropriate container so that they are displayed
		panel.add(playBtn);
		panel.add(statsBtn);
		panel.add(helpBtn);
		getContentPane().add(helpBtn);
		getContentPane().add(statsBtn);
		getContentPane().add(playBtn);
		getContentPane().add(label);
		//Exits JPanel if top right x is clicked
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		//Makes JPanel appear in the middle of the screen
		setLocationRelativeTo(null);
		//Makes JPanel visible
		setVisible(true);
		
		
		//Handles play button clicks
		playBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				isPlayBtnPressed=true;
				
			}
			
		});
		//Handles statistics button clicks
		statsBtn.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
					try {
						showStatsBoard();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				
			}
			
		});
		//Handles help button clicks
		helpBtn.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
					showHelpScreen();
				
			}
			
		});
	}
	
	/**
	 * Returns button that was pressed
	 * 
	 */
	public boolean isPlayBtnPressed() {
		return isPlayBtnPressed;
	}
	
	/**
	 * Reads text file and saves contents of text file as a string
	 * This string is returned
	 */
	private String getStats() throws FileNotFoundException{
		Scanner input = null;
		File file = new File("stats.txt");
		//Creates the heading for statistics page
		String heading = String.format("%-10s %-7s %-9s %-5s\n", "Name","Score","Level","Lines");
		String stats = heading;
		//Reads each line of the text file and adds it to stats String
		input = new Scanner(file);
		while(input.hasNextLine()){
			stats = stats + "\n"+ input.nextLine();
		}
		
		return stats;
	}
	
	/**
	 * Creates and displays JFrame with statistics. Gets the statistics and displays them inside of a JTextArea which is added to the frame.
	 * 
	 */
	private void showStatsBoard() throws IOException{
		JFrame frame = new JFrame("Stats");
		//Creates non-editable JTextArea with stats displayed inside
		JTextArea stats = new JTextArea(getStats());
		stats.setEditable(false);
		stats.setFont(new Font("Courier",1,16));
		//Sets frame size
		frame.setPreferredSize(new Dimension(350,500));
		//Adds text are to frame
		frame.add(stats);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.pack();
		//Makes visible in centre of screen
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	/**
	 * Opens pdf file with instructions
	 */
	private void showHelpScreen() {
		File file = new File("Instructions.pdf");
		try {
			Desktop.getDesktop().open(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	
}
