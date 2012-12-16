package evolutionary_nn;


import checkers_framework.Checker;
import checkers_framework.CheckerBoard;
import checkers_framework.Move;
import checkers_framework.Strong_CheckerAI;

import checkers_framework.CheckerInterface;


public class CheckerMatchMaker {

	public CheckerBoard currentBoard;

	private boolean gameIsOver;

	private boolean blackMove;

	private CheckerInterface blackAI;

	private CheckerInterface whiteAI;

	public String winner = "not selected yet";

	public int[] lastMove = new int[4];

	private int turns;

	private String whitename = "white fogel ai";

	private String blackname = "black fogel ai";


	public static boolean PRINT_MATCH_INFO = false; //to print out info about each match

	private int num_moves = 100;
	
	public CheckerMatchMaker(NeuralNetPair white, NeuralNetPair black) {
		currentBoard = new CheckerBoard();
		//white = (NeuralNetPair) white.randomize();
		//black = (NeuralNetPair) black.randomize();
		whiteAI = new NeuralNetworkAI(white);
		whiteAI.setColor(Checker.WHITE);
		blackAI = new NeuralNetworkAI(black);
		blackAI.setColor(Checker.BLACK);
		setMatch();
	}
	public CheckerMatchMaker(NeuralNetPair white, int opponentDepth) {
		currentBoard = new CheckerBoard();
		whiteAI = new NeuralNetworkAI(white);
		whiteAI.setColor(Checker.WHITE);
		blackAI = new MiniMaxAI(opponentDepth);
		blackAI.setColor(Checker.BLACK);
		setMatch();
	}
	public CheckerMatchMaker(NeuralNetPair white) {
		currentBoard = new CheckerBoard();
		whiteAI = new NeuralNetworkAI(white);
		whiteAI.setColor(Checker.WHITE);
		blackAI = new Strong_CheckerAI();
		blackAI.setColor(Checker.BLACK);
		setMatch();
	}

	private void setMatch() {
		gameIsOver = false;


		blackMove = true;

		lastMove[0] = -1;
		lastMove[1] = -1;

	}

	public static void main(String args[]) {
		//for(int i = 0; i < 100; i++){
		JavaMLP a1 = new JavaMLP();
		JavaMLP a2 = new JavaMLP();

		JavaMLP b1 = new JavaMLP();
		JavaMLP b2 = new JavaMLP();
		for (int i = 0; i < 500; i++) {
			NeuralNetPair a = new NeuralNetPair(a1, a2);
			NeuralNetPair b = new NeuralNetPair(b1, b2);
			CheckerMatchMaker e = new CheckerMatchMaker(a, b);
			int winner = e.startMatch();
			
			System.out.println("winner:" + winner + " won becuase:" + e.winner);
			System.out.print("last board:");
		
		}
	}

	///////////////////
	

	private void setLastMove(Move m) {
		lastMove[0] = m.sucrow;
		lastMove[1] = m.succol;
		lastMove[2] = m.currow;
		lastMove[3] = m.curcol;
	}

	public int startMatch() {
		long start = System.currentTimeMillis();
		int who;
		who = doMatch();
		long end = System.currentTimeMillis();
		long time = (end - start);
		
		if(PRINT_MATCH_INFO){
		System.out.println("match ran in " + time + " ms, which is "
				+ (time / 1000) + " seconds. the match took " + turns
				+ " moves. win reason: "+winner);
		}
		return who;
	}

	private int doMatch() {
		int whoWon = -10;
		while (!isGameOver()) {
			int i = 0;
			if (blackMove) {
				do {
					try {
						Move m = blackAI.chooseMove(currentBoard
								.getCopyOfBoardArray());

						if (m == Move.pass) {
							endGame();
							if (getBlackScore() == 0) {
								winner = "Winner: White! Black has no peices left.";
								whoWon = Checker.WHITE;
							} else {
								winner = "Winner: White! Black AI lost when it did not have a legal move.";
								whoWon = Checker.WHITE;
							}
						} else {
							i = currentBoard.move(m, Checker.BLACK);
							if (i == currentBoard.ILLEGALMOVE) {
								endGame();
								winner = "Winner: White! Black AI selected an invalid move: "
										+ m;
								whoWon = Checker.WHITE;
							} else {
								setLastMove(m);
							}
						}
					} catch (Exception e) {
						endGame();
						winner = "Winner: White! Black AI threw and Exception: "
								+ e.toString();
						e.printStackTrace();
						whoWon = Checker.WHITE;
					}
				} while (i == currentBoard.INCOMPLETEMOVE);
			} else { // ( white move )
				do {
					try {
						Move m = null;
						m = whiteAI.chooseMove(currentBoard.getCopyOfBoardArray());
						if (m == Move.pass) {
							endGame();
							if (getWhiteScore() == 0) {
								winner = "Winner: Black! White has no peices left.";
								whoWon = Checker.BLACK;
							} else {
								winner = "Winner: Black! White AI lost when it did not have a legal move.";
								whoWon = Checker.BLACK;
							}
						} else {
							i = currentBoard.move(m, Checker.WHITE);
							if (i == currentBoard.ILLEGALMOVE) {
								endGame();
								winner = "Winner: Black! White AI selected an invalid move: "+ m;
								whoWon = Checker.BLACK;
							} else {
								setLastMove(m);
							}
						}
					} catch (Exception e) {
						endGame();
						winner = "Winner: Black! White AI threw and Exception: "+ e.toString();
						whoWon = Checker.BLACK;
						e.printStackTrace();
					}
				}
				while (i == currentBoard.INCOMPLETEMOVE);
			}

			// See who moves next
			if (blackMove)
				blackMove = false; // now WHITE's move
			else
				blackMove = true; // now BLACK's move
			
		
			if (++turns >= num_moves) {
				whoWon = 0;
				winner = "Game tied, " + num_moves + " moves per person were made!";
				endGame();
			}
		}

		return whoWon;
	}

	public void endGame() {
		gameIsOver = true;
	}

	public boolean isGameOver() {
		return gameIsOver;
	}

	// Called by CyberthelloFrame when the human has moved.

	public CheckerBoard getCurrentBoard() {
		return currentBoard;
	}

	public int getBlackScore() {
		return currentBoard.getBlackScore();
	}

	public int getWhiteScore() {
		return currentBoard.getWhiteScore();
	}

	public String getWinnerString() {
		return winner;
	}
}
