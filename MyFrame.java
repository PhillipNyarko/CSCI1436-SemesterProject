import java.awt.event.*;
import javax.swing.*;

public class MyFrame extends JFrame implements KeyListener{

	public static int gridRows = 6;
  public static int gridCols = 7;

  public static final String RED  = "\u001B[31m";
  public static final String BLUE = "\u001B[34m";
  public static final String ANSI_RESET = "\u001B[0m";
  public static final String BLINKON = "\033[5m"; // color forhighlighting of the column w/ slow blink (currently white)
		
  public static String redChip =  RED + "o" + ANSI_RESET; // red chip 
  public static String blueChip = BLUE + "o" + ANSI_RESET;

  public static String[][] gameBoard = new String[gridRows][gridCols]; //
  public static String[] selectionGrid = new String[gridCols];

  public static String currentPlayer = blueChip; // redChip starts as initial player
  public static int playerPosition = 0; // first players chip starts placed to the far left
	
	MyFrame(){		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(500,500);
		this.setLayout(null);
		this.addKeyListener(this);
		this.setVisible(true);
	}

	@Override
	public void keyTyped(KeyEvent e) {
		//keyTyped = Invoked when a key is typed. Uses KeyChar, char output
		switch(e.getKeyChar()) {
			case 'a': 
				moveLeft(); 
				update();
				break;
			case 'd': 
				moveRight();
				update();
				break;
			case ' ': 
				dropPiece();
				switchPlayer();
				update();
				break;
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		//keyPressed = Invoked when a physical key is pressed down. Uses KeyCode, int output
		switch(e.getKeyCode()) {
		case 37:
			moveLeft();
			update();
			break;
		case 39: 
			moveRight();
			update();
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	public static void update(){
		clearScreen();
		drawGrid(); // call the draw grid function to draw everything
	}
	
	public static void initGrid(){
		// fill the selection grid with empty space
		clearSelectionGrid();
		selectionGrid[0] = currentPlayer; // first space holds the piece for player one
	
		for(int i = 0; i < gameBoard[0].length; i++){
				for(int j = 0; j < gameBoard.length; j++){
					gameBoard[j][i] = "o"; // fill each index of the game board/2d array with an empty slot character
				}
		}
		drawGrid(); // call the draw grid function to draw everything
	}
	
	public static void drawGrid(){
		//  reset the highlight 
	
		// print two lines of space before rendering the game
		System.out.println("\n");    
	
		//print spacing for selection row
		System.out.print("                    ");
		//print the selection row 
		for(int i = 0; i < selectionGrid.length; i++){
			System.out.print(selectionGrid[i] + " "); // print the value of each element of the row
		} 
	
		// print new line after the selection row is printed
		System.out.println();                         
	
		//print the connect-four grid 
		for(int i = 0; i < gameBoard.length; i++){
				System.out.print("                    "); // print spacing before each row
				for(int j = 0; j < gameBoard[0].length; j++){
					if(j == playerPosition){
						System.out.print(BLINKON + gameBoard[i][j] + ANSI_RESET + " "); 
					}else{
						System.out.print(gameBoard[i][j] + " "); // print value of each index in corresponding row of grid
					}
				}
				System.out.println(); // print new line after each row is printed
		}
	
		// print the bottom barrier with title
		System.out.print("                   "); // print spacing
		for(int i = 0; i < 3; i++){
			System.out.print("≪");
		}
		System.out.print("CONNECT 4");
		for(int i = 0; i < 3; i++){
			System.out.print("≫");
		}
	}
	
	public static void moveRight(){
	 if(playerPosition != selectionGrid.length - 1){
		playerPosition += 1;
		clearSelectionGrid();
		selectionGrid[playerPosition] = currentPlayer;
	 }else{
		clearSelectionGrid();
		playerPosition = 0;
		selectionGrid[playerPosition] = currentPlayer;
	 }      
	}
	
	public static void moveLeft(){
	 if(playerPosition != 0){
		playerPosition -= 1;
		clearSelectionGrid();
		selectionGrid[playerPosition] = currentPlayer;
	 }else{
		playerPosition = selectionGrid.length - 1;
		clearSelectionGrid();
		selectionGrid[playerPosition] = currentPlayer;
	 }
	}
	
	public static void dropPiece(){
		clearSelectionGrid();
		update();
		for(int i = 0; i < gameBoard.length; i++){
			for(int j = 0; j < gameBoard[0].length; j++){
				if(j == playerPosition){
					if(gameBoard[i][j] == "o"){
						gameBoard[i][j] = currentPlayer;
						
					}
					if(i + 1 < gameBoard.length){
						if(gameBoard[i+1][j] == "o"){
							gameBoard[i][j] = "o";
							gameBoard[i+1][j] = currentPlayer;
						}
					}
					update();
					sleep(1);
				}
			}
		}
	}
	
	public static void checksRows(){
	}

	public static void switchPlayer(){
		if(currentPlayer == redChip){
		 currentPlayer = blueChip;
	 	}else{
		 currentPlayer = redChip;
	 	}
 	}
	
	public static void clearScreen(){
		System.out.print("\033[H\033[2J");  
		System.out.flush();
	}

	public static void sleep(int seconds){
    try {
      Thread.sleep(seconds * 100);
    } catch(InterruptedException e) {
      e.printStackTrace();
    }
  }
	
	public static void clearSelectionGrid(){
		for(int i = 0; i < selectionGrid.length; i++){
			selectionGrid[i] = " ";
		}
	}
}