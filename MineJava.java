import java.util.Scanner;
public class MineJava{
	private static class Boxes{
		int row, column, value;
		boolean flagged, bomb, opened;
		Boxes(int r, int c){
			row = r;
			column = c;
			value = 0;
			flagged = false;
			bomb = false;
			opened = false;
		}
		void bombify(){
			bomb = true;
		}
		void open(){
			opened = true;
		}
		void toggleFlag(){
			flagged = !flagged;
		}
	}
	private static class Game{
		boolean gameOver;
		int boardLength, boardHeight, bombCount, flagCount;
		Boxes gameBoard[][];
		String bombs[];
		String numbers[];
		Game(int l, int h, int bc){
			boardLength = l;
			boardHeight = h;
			bombCount = bc;
			flagCount = 0;
			gameBoard = new Boxes[boardLength][boardHeight];
			bombs =  new String[bombCount];
			numbers = "❶ ❷ ❸ ❹ ❺ ❻ ❼ ❽ ❾".split(" ");
			// Set the boxes
			for (int i = 0; i < boardLength; i++) 
				for (int j = 0; j < boardHeight; j++) 
					gameBoard[i][j] = new Boxes(i, j);

			// Set bombs
			for (int i = 0; i < bombCount; i++) {
				do{
					boolean unique = true;
					int randomX, randomY;
					String random;
					randomX = (int)(Math.random() * boardLength);
					randomY = (int)(Math.random() * boardHeight);
					random = randomX + ":" + randomY; //As a double digit number
					for(int j = 0; j < i; j++){
						if (bombs[j].equals(random)) {
							unique = false;
							break;
						}
					}
					if(unique){
						bombs[i] = random;
						gameBoard[randomX][randomY].bombify();
						break;
					}
				}while(true);
			}
			// Assign values
			for (int i = 0; i < boardLength; i++) {
				for (int j = 0; j < boardHeight; j++) {
					//Now checking surrounding boxes
					for (int x = -1; x <= 1; x++) {
						for(int y = -1; y<= 1; y++){
							int X, Y;
							X = i + x; //X and Y store row and column
							Y = j + y; //of the surronding boxes
							if(X < 0 || Y < 0 || X == boardLength || Y == boardHeight || (x == 0 && y == 0))
								continue;
							if(gameBoard[X][Y].bomb)
								gameBoard[i][j].value++;
						}
					}
				}
			}
		}
		void crawlOut(int i, int j){
			gameBoard[i][j].open();
			if(gameBoard[i][j].value != 0)
				return;

			for (int x = -1; x <= 1; x++) {
				for(int y = -1; y<= 1; y++){
					int X, Y;
					X = i + x; //X and Y store row and column
					Y = j + y; //of the surronding boxes
					if(X < 0 || Y < 0 || X == boardLength || Y == boardHeight || (x == 0 && y == 0))
						continue;
					if(!gameBoard[X][Y].opened){
						crawlOut(X, Y);
					}
				}
			}
		}
		void printGameBoard(){
			System.out.print("\f");
			System.out.println("MineJava v1.0 - the epitome of shtheadness");
			System.out.println("Flags: " + flagCount + " / " + bombCount);
			System.out.print("   ");
			for (int i = 0; i < boardLength; i++) {
				System.out.print(i + ((i < 10) ? "  " : " "));
			}
			System.out.println();
			for (int i = 0; i < boardLength; i++) {
				System.out.print(i + ((i < 10) ? "  " : " "));
				for(int j = 0; j < boardHeight; j++) {
					String output;
					if(gameBoard[i][j].flagged)
						output = "⚐";
					else if(!gameBoard[i][j].opened)
						output = "☐";
					else if(gameBoard[i][j].value == 0)
						output = "░";
					else 
						output = numbers[gameBoard[i][j].value];
					System.out.print(output + "  ");
				}
				System.out.println();
			}
		}
	}
	public static void main(String args[]){
		Scanner sc = new Scanner(System.in);
		Game daGame = new Game(18, 18, 20);
		while(true){
			int x, y;
			String input;
			boolean toFlag;
			daGame.printGameBoard();
			System.out.println("Enter : separated values of row and column of box to open");
			System.out.println("Append F or f to flag/unflag the box instead");
			input = sc.next().toLowerCase();
			toFlag = input.contains("f");
			input = input.replace("f", "");
			x = Integer.parseInt(input.split(":")[1]);
			y = Integer.parseInt(input.split(":")[0]);
			if(toFlag){
				if(daGame.flagCount == daGame.bombCount){
					System.out.println("You have already used all " + daGame.bombCount + " flags!");
					System.out.println("Press enter to continue....");
					sc.next();
					continue;
				}
				daGame.flagCount += (daGame.gameBoard[x][y].flagged) ? -1 : 1;
				daGame.gameBoard[x][y].toggleFlag();
				continue;
			}
			if(daGame.gameBoard[x][y].bomb){
				daGame.gameOver = true;
				System.out.println("BOOM!");
				break;
			}
			daGame.crawlOut(x, y);
		}
	}
}