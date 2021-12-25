import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;


public class TetrisBoard extends Board {
	final int OFFSET = 5;
	final int MIN = 0;
	final int ROWS_MAX = 19;
	final int COLS_MAX = 9;
	int numEliminatedLines;
	
	public TetrisBoard(int rows, int cols) {
		
		super(rows, cols);
	}
	/**
	 * Removes all pegs at current shape's position
	 * 
	 */
	public void removeShape(Shape s) {
		//loops through coordinates and removes pegs accordingly
		for (Coordinate shapeCoord:s.getShapeCoords()) {
			removePeg(shapeCoord.getRow(), shapeCoord.getCol());

		}
	}
	/**
	 * Places pegs on current shape coordinates with the appropriate colour
	 * 
	 */
	public void drawShape(Shape s) {
		//Loops through coordinates and places pegs accordingly
		for (Coordinate shapeCoord:s.getShapeCoords()) {
			putPeg(s.getColour(), shapeCoord.getRow(), shapeCoord.getCol());
		}
	}

	/**
	 * Checks if any coordinates belonging to current shape are one row above the end of the board or another shape 
	 * Returns appropriate boolean value indicating if the shape has reached its lowest possible point
	 */
	public boolean isShapeDone(Shape s) {
		boolean isShapeDone = false;
		//Checks if any of the coordinates have reached the bottom of the board
		for (Coordinate shapeCoord:s.getShapeCoords()) {
			if (shapeCoord.getRow() == ROWS_MAX) {
				isShapeDone = true;
				break;
			} 
		}
		if (isShapeDone) {
			return isShapeDone;
		}
		//Checks if any of the coordinates are on top of an already present peg
		for (Coordinate shapeCoord:s.getShapeCoords()) {
			if (this.grid[shapeCoord.getCol()][shapeCoord.getRow() + 1] != null) {
				isShapeDone = true;
				break;
			}
		}
		return isShapeDone;
	}
	/**
	 * Creates a copy of the sent shape and moves the copied shape according the the key press sent
	 * Returns appropriate boolean value depending on if the temp shape's location overlaps and existing shape
	 * 
	 */
	public boolean isCanDraw(Shape s, char key) {
		boolean isCanDraw = true;
		boolean isSameCoord = false;
		Shape tempShape = Shape.copy(s);
		ArrayList<Coordinate> newCoords = new ArrayList<Coordinate>();
		//Moves the temp shape appropriately based on key pressed
		if (key == 'a' || key == 'd') {
			tempShape.moveShape(key);
		} else if (key == 'k') {
			tempShape.rotateShape();
		}else if (key==' ') {
			tempShape.moveToFinal(finalShapePosition(tempShape));
		}
		
		//Checks if shape is out of bounds (off the grid)
		for (Coordinate tempShapeCoord:tempShape.getShapeCoords()) {
			if (tempShapeCoord.getRow() > ROWS_MAX || tempShapeCoord.getRow() < MIN
					|| tempShapeCoord.getCol() > COLS_MAX
					|| tempShapeCoord.getCol() < MIN) {
				isCanDraw = false;
				return isCanDraw;
			}
		}

		// Eliminates coordinates tempShape and real shape have in common(already know those coordinates are valid)
		for (Coordinate tempShapeCoord:tempShape.getShapeCoords()) {
			isSameCoord = false;
			for (Coordinate shapeCoord:s.getShapeCoords()) {
				if (tempShapeCoord.getRow() == shapeCoord.getRow()
						&& tempShapeCoord.getCol() == shapeCoord.getCol()) {
					isSameCoord = true;
					break;
				}

			}
			//If coordinate is not common it is added to arraylist of new coordinates
			if (!isSameCoord) {
				newCoords.add(tempShapeCoord);
			}
		}
		//Checks to see if any of the new coordinates overlap with existing pegs, if so shape can not be drawn
		for (Coordinate newShapeCoord:newCoords) {
			if (this.grid[newShapeCoord.getCol()][newShapeCoord.getRow()] != null) {
				isCanDraw = false;
			}
		}
		return isCanDraw;
	}
	/**
	 * 
	 *Move copy of the current shape to the lowest available coordinates and returns coordinate value
	 *Used for hard/fast drop
	 * 
	 */
	public Coordinate[] finalShapePosition(Shape originalShape) {
		//makes a copy of the original shape
		Shape tempShape = Shape.copy(originalShape);
		
		//drops shape coordinates until it reaches the bottom or lands on other pegs
		do {
			for (int i = 0; i < tempShape.getShapeCoords().length; i++) {
				tempShape.getShapeCoords()[i] = new Coordinate(tempShape.getShapeCoords()[i].getRow() + 1,
						tempShape.getShapeCoords()[i].getCol());
			}

		} while (!isShapeDone(tempShape));
		//returns coordinates of new tempshape position
		return tempShape.getShapeCoords();
	}
	/**
	 * Loops through each row and checks if it is full
	 * Used for line elimination
	 * 
	 */
	private boolean isLineFull(int row){
		boolean isLineFull=true;
		//checks if each row is completely filled with pegs
		for(int j =0;j<=COLS_MAX;j++){
			if(this.grid[j][row]==null){
				isLineFull=false;
			}
				
		}
		return isLineFull;
	}
	/**
	 * Clears all pegs in any full rows and moves the rest of the pegs above the eliminated line one row lower
	 */
	public void eliminateFullLines(){
		numEliminatedLines = 0;
		//removes pegs from all full lines
		for(int row = 0;row<=ROWS_MAX;row++){
			if(isLineFull(row)){
				numEliminatedLines ++;
				for(int col = 0;col<=COLS_MAX;col++){
					removePeg(row,col);
				}
				//moves every line down a row
				for(int i = row;i>MIN;i--){
					for(int j =0;j<=COLS_MAX;j++){
						this.grid[j][i]=this.grid[j][i-1];
					}
				}
			}
		}
	}
	/**
	 *Returns the amount of lines that were eliminated. Used for the user statistics 
	 *
	 */
	public int getNumEliminatedLines() {
		return numEliminatedLines;
	}
}
