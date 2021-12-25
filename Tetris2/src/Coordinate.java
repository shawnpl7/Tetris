public class Coordinate
{
  private int row;
  private int col;
  
  public Coordinate(int theRow, int theCol)
  {
    row = theRow;
    col = theCol;
  }
  
  public int getRow()
  {
    return row;
  }
    
  public int getCol()
  {
    return col;
  }
  public void setRow(int r) {
	  row = r;
  }
  public void setCol(int c) {
	  col = c;
  }
} 
