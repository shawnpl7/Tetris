import java.util.Random;

public class Shape {
	
	private Coordinate[][] coords;
	private Coordinate[] shapeCoords;
	public static enum Tetronimo{LINE,SQUARE,L_PIECE,J_PIECE,T_PIECE,Z_PIECE,S_PIECE}
	public static enum Colour{CYAN,YELLOW,ORANGE,BLUE,PINK,RED,GREEN};
	private Tetronimo currentShape;
	private Colour currentColour;
	final int OFFSET = 4;
	
	public Shape() {
		//initializes coordinates of all shapes
		shapesCords();
		//sets a random shape
		setRandomShape();
	}
	
	/**
	 * 
	 * Makes a new shape with the same coordinate values
	 */
	public static Shape copy( Shape othershape)
	{
		Shape newShape = new Shape();
		if (othershape.shapeCoords != null){
			//Makes elements in copied shape's array equal to those in the original 
			for (int i = 0; i < othershape.shapeCoords.length; i++){
				newShape.shapeCoords[i] = othershape.shapeCoords[i];
			}
		}
		
		
		
		return newShape;
	}
	/**
	 * Generates a random shape, sets the appropriate coordinate values, color and name
	 */
	private void setRandomShape(){
		Random r = new Random();
		shapeCoords = new Coordinate[4];
		//generates a random number between 0 and 7
		int shapeValue = r.nextInt(7); 
		//Sets array of appropriate coordinates for shape generated
		for(int i =0;i<shapeCoords.length;i++) {
			shapeCoords[i] = coords[shapeValue][i];
			//Adds offset to each coordinate so that shape spawns in the centre of the screen
			shapeCoords[i].setCol(shapeCoords[i].getCol()+OFFSET);
		}
		//Sets colour and name of shape
		currentShape = Tetronimo.values()[shapeValue];
		currentColour = Colour.values()[shapeValue];
	}
	/**
	 * 
	 * Returns colour of the shape
	 */
	public String getColour() {
		return currentColour.toString();
	}
	
	/**
	 * 
	 * Returns the current shape's coordinates
	 */
	public Coordinate[] getShapeCoords(){
		return shapeCoords;
	}
	
	/**
	 * Sets the origin coordinates of all possible shapes
	 */
	private void shapesCords() {
		//Creates array of the original coordinates of all the shapes. First coordinate of each shape is used as the rotation point
		coords = new Coordinate[7][4];
		//line
		coords[0][0] = new Coordinate(1,0);
		coords[0][1] = new Coordinate(0,0);
		coords[0][2] = new Coordinate(2,0);
		coords[0][3] = new Coordinate(3,0);
		//square
		coords[1][0] = new Coordinate(0,0);
		coords[1][1] = new Coordinate(0,1);
		coords[1][2] = new Coordinate(1,0);
		coords[1][3] = new Coordinate(1,1);
		//L-Piece
		coords[2][0] = new Coordinate(1,0);
		coords[2][1] = new Coordinate(0,0);	
		coords[2][2] = new Coordinate(2,0);
		coords[2][3] = new Coordinate(2,1);
		//J-Piece
		coords[3][0] = new Coordinate(1,1);
		coords[3][1] = new Coordinate(0,1);
		coords[3][2] = new Coordinate(2,0);
		coords[3][3] = new Coordinate(2,1);
		//T-Piece
		coords[4][0] = new Coordinate(1,0);
		coords[4][1] = new Coordinate(0,0);
		coords[4][2] = new Coordinate(2,0);
		coords[4][3] = new Coordinate(1,1);
		//Z-Piece
		coords[5][0] = new Coordinate(1,1);
		coords[5][1] = new Coordinate(0,0);
		coords[5][2] = new Coordinate(0,1);
		coords[5][3] = new Coordinate(1,2);
		//S-Piece
		coords[6][0] = new Coordinate(1,1);
		coords[6][1] = new Coordinate(0,1);
		coords[6][2] = new Coordinate(0,2);
		coords[6][3] = new Coordinate(1,0);
		
	}
	/**
	 * Lowers the shape coordinates by a single row
	 */
	public void dropShape() {
		//lowers y coordinate (row) of shape by 1
		for(int i =0;i<shapeCoords.length;i++) {
			shapeCoords[i]= new Coordinate(shapeCoords[i].getRow()+1,shapeCoords[i].getCol());
		}
	}
	
	/**
	 * 
	 * Takes key pressed as parameter and changes shape coordinates left or right one column accordingly
	 */
	public void moveShape(char key) {
		//moves shape either one to the right or one to the left depending on key press
		for(int i =0;i< shapeCoords.length;i++) {
			if(key=='a') {
				shapeCoords[i]= new Coordinate(shapeCoords[i].getRow(),shapeCoords[i].getCol()-1);	
			}else if (key=='d') {
				shapeCoords[i]= new Coordinate(shapeCoords[i].getRow(),shapeCoords[i].getCol()+1);
			}
			
		}
	}
	
	/**
	 * Adds sets row and column to all row and column values of current shape coordinates
	 * Used to move shape coordinate to 0,0 so that it can be rotated mathematically. Also changes shape coordinates back to original position once rotated.
	 */
	private void translate(int row,int col) {
		for(int i =0;i<shapeCoords.length;i++) {
			shapeCoords[i]=new Coordinate(shapeCoords[i].getRow()+row,shapeCoords[i].getCol()+col);
		}
	}
	
	/**
	 * Rotates shape coordinates by changing shape coordinates so that rotation point is 0,0, rotates shape and then moves it back
	 */
	public void rotateShape(){
		int originalRow,originalCol;
		//Saves original row and column value of one of the shape coordinates
		originalRow = shapeCoords[0].getRow();
		originalCol = shapeCoords[0].getCol();
		//moves shape to (0,0)
		translate(originalRow*-1,originalCol*-1);
		//rotates the shape by making the row equal the column and the column equal the negative value of the row
		for(int i = 0;i<shapeCoords.length;i++) {
			shapeCoords[i] = new Coordinate(shapeCoords[i].getCol(),shapeCoords[i].getRow()*-1);
			
		}
		//Moves the shape back to its original position
		translate(originalRow,originalCol);
	}
	
	/**
	 * 
	 * Gets shape's final possible position and changes coordinates to it
	 */
	public void moveToFinal(Coordinate[] finalPosition){
		//Changes shape coords to the final possible position 
		for(int i = 0;i<shapeCoords.length;i++){
			shapeCoords[i]=finalPosition[i];
		}
	}
	
}

