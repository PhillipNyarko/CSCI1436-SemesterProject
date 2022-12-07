import java.awt.event.*;
import javax.swing.*;

public class MyFrame extends JFrame implements KeyListener{

	public static int gridRows = 6;
  public static int gridCols = 7;

  public static final String RED  = "\u001B[31m";
  public static final String BLUE = "\u001B[34m";
	public static final String YELLOW = "\033[38;5;220m";
	public static final String LIME_GREEN = "\033[38;5;40m";
  public static final String ANSI_RESET = "\u001B[0m";
  public static final String BLINKON = "\033[5m"; // color forhighlighting of the column w/ slow blink (currently white)
		
  public static String redChip =  RED + "o" + ANSI_RESET; // red chip 
  public static String blueChip = BLUE + "o" + ANSI_RESET;

  public static String[][] gameBoard = new String[gridRows][gridCols]; //
  public static String[] selectionGrid = new String[gridCols];

  public static String currentPlayer = blueChip; // blueChip starts as initial player
  public static int playerPosition = 0; // first players chip starts placed to the far left
	
	MyFrame(){		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(500,500);
		this.setLayout(null);
		this.addKeyListener(this);
		this.setVisible(true);
		// add an image that will display a welcom to connect four, click the window and then play in the terminal/ console message
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
				checkRows();
				switchPlayer();
				selectionGrid[playerPosition] = currentPlayer;
				update(); 
				break;
			// add a case that terminates the program if the user presses q
			// tell the user thanks for playing first
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

		displayCurrentPlayer();
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
		// if you try to place a piece in a column thata is filled already it doesnt give you a second chance it just switches the use 
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
	
	public static void checkRows(){
		// check for winner and add to player score
		for(int i = 0; i < gameBoard.length; i++){
			for(int j = 0; j < gameBoard[0].length; j++){
				
				int consecutivePieces = 0;
				if(gameBoard[i][j] == currentPlayer){
					gameBoard[i][j] = YELLOW + "o"+ ANSI_RESET;
					update();
					sleep(5);

					if(j-1 >= 0 && gameBoard[i][j-1] == currentPlayer){ // check if left is in bounds and equal to player
						gameBoard[i][j-1] = YELLOW + "o"+ ANSI_RESET;
						update();
						sleep(5);
						gameBoard[i][j-1] = currentPlayer;
						update();
						sleep(5);
						consecutivePieces += 2; // add one to the 4 pieces needed to win 
						for(int k = 2; k < 4; k++){
							if(j-k >= 0 && gameBoard[i][j-k] == currentPlayer){
								gameBoard[i][j-k] = YELLOW + "o"+ ANSI_RESET;
								update();
								sleep(5);
								gameBoard[i][j-k] = currentPlayer;
								update();
								sleep(5);
								consecutivePieces += 1; 
							}
						}
						if(consecutivePieces != 4){
							consecutivePieces = 0;
							for(int k = 1; k < 4; k++){
								if(j-k >= 0 && gameBoard[i][j-k] == YELLOW){
									gameBoard[i][j-k] = RED;
									update();
									sleep(5);
								}
							}
					  }
					}else{
						gameBoard[i][j] = currentPlayer;
						update();
						sleep(5);
					}
					
					if(j-1 >= 0 && i-1 >= 0){ // check if left up is in bounds
					}
					if(i-1 >= 0){ // check if up is in bounds
					}
					if(j+1 < gameBoard[0].length && i-1 >= 0){ // check if right up is in bounds
					}
					if(j+1 < gameBoard[0].length){ // check if right is in bounds
					}
					if(j+1 < gameBoard[0].length && i+1 < gameBoard.length){ // check if right down is in bounds
					}
					if(i+1 < gameBoard.length){ // check if down is in bounds 
					}
					if(i+1 < gameBoard.length && j-1 >= 0){ // check if down left is in bounds
					}
				}
			}
		}
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
      Thread.sleep(seconds * 50);
    } catch(InterruptedException e) {
      e.printStackTrace();
    }
  }
	
	public static void clearSelectionGrid(){
		for(int i = 0; i < selectionGrid.length; i++){
			selectionGrid[i] = " ";
		}
	}

	public static void displayCurrentPlayer(){
		// Displaying the current player name/color
		System.out.println();
		if(currentPlayer == redChip){
			System.out.println("");
			System.out.println("            " + RED + "Red" + ANSI_RESET + " Player: Select your spot ");
			}else{
			currentPlayer = blueChip;
				System.out.println("");
				System.out.println("             " + BLUE + "Blue" + ANSI_RESET + " Player: Select your spot");
			}
	}
	
	public static void addToScore(){
		// Display the current scores of the players
    
	}
	
}