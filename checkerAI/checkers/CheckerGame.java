package checkers;

import checkers_framework.CheckerInterface;
import checkers_framework.Move;


// OthelloGame.java
//package tournament_checkers;

public class CheckerGame extends Thread {
	private CheckerBoard currentBoard;
	private boolean gameIsOver;
	private boolean blackIsAI;
	private boolean whiteIsAI;	private boolean blackMove;	private CheckerFrame UI;	private CheckerInterface blackAI;	private CheckerInterface whiteAI;	private GameClock blackGameClock;	private GameClock whiteGameClock;	private String winner ="not selected yet";
	private CheckerInterface won;
	private CheckerInterface lost;
	private String whiteName;
	private String blackName;
	private int cheatMode = 0;
	
	private int timeToSleep = 1000;
	
	public int[] lastMove = new int[4];
	public CheckerGame(int secondsPerPlayer, CheckerInterface blackAgent, 					   CheckerInterface whiteAgent, CheckerFrame frame, String whiteName, String blackName,
					   int movePause, boolean whiteOnTop) {
		
		gameIsOver = false;
		blackIsAI = (blackAgent != null);
		whiteIsAI = (whiteAgent != null);
		blackAI = blackAgent;
		whiteAI = whiteAgent;
		blackMove = true;
		UI = frame;
		timeToSleep = movePause;
		
		this.whiteName = whiteName;
		this.blackName = blackName;
		cheatMode = checkCheatMode();//egg!
		currentBoard = new CheckerBoard(whiteOnTop);
		
		blackGameClock = new GameClock(900000000);
		whiteGameClock = new GameClock(900000000);
		lastMove[0] = -1;
		lastMove[1] = -1;

	}
	private int checkCheatMode() {
	    int mode = 0;
	    if(!whiteIsAI && !blackIsAI) {
	        if(whiteName.toLowerCase().equals("god")) {
	            mode = CheckerInterface.WH;
	        } else if (blackName.toLowerCase().equals("god")) {
	            mode = CheckerInterface.BL;
	        }
	    }
	    return mode;
	}
	private void setLastMove(Move m) {
	    lastMove[0] = m.sucrow;
	    lastMove[1] = m.succol;
	    lastMove[2] = m.currow;
	    lastMove[3] = m.curcol;
	}
	private void logMove(Move m, int color, long time) {
		if ( color == CheckerInterface.WH){
		    if (whiteGameClock.millisRemaining() < 0 ){
			//	logger.reportMove(m, color, -1);
				endGame();
				won = blackAI;
				lost =whiteAI;
		
				winner = "Winner: Black! White ran out of time.";
				return;
			}
	//		else
	//			logger.reportMove(m, color, time);
		}
		else if ( color == CheckerInterface.BL){
			if (blackGameClock.millisRemaining() < 0 ){
		//		logger.reportMove(m, color, -1);
				winner = "Winner: White! Black ran out of time";
				endGame();
				won = whiteAI;
				lost =blackAI;
				return;
			}
//			else
//				logger.reportMove(m, color, time);
		}
	}
	
	public void run() {
	  	while (!isGameOver()) {
	    	int i = 0;	
	    	if (blackMove) {
	    		blackGameClock.start();	
	    		if (blackIsAI) {
	    			do {
	    	        	try {
	    	        		Move m = blackAI.chooseMove(currentBoard.getCopyOfBoardArray());
	    	        		long elapsed = blackGameClock.pause();
		    				logMove(m,CheckerInterface.BL, elapsed);
		    				
		  
		    				if (m == Move.pass) {
		    					endGame();
		    					won = whiteAI;
		    					lost =blackAI;
		    					if(getBlackScore() == 0) {
		    							winner = "Winner: Black! White has no peices left.";
		    					} else {
		    					    winner = "Winner: White! Black AI passed when it did have a legal move.";
		    					}
		    				}
		    				else {
		    					i = currentBoard.move(m, Checker.BLACK);
		    					if (i == currentBoard.ILLEGALMOVE) {
		    						endGame();
		    						won = whiteAI;
		    						lost = blackAI;
		    						winner = "Winner: White! Black AI selected an invalid move: " + m;
		    					} 
		    					else {
		    					    setLastMove(m);
		    					}
		    				}
	    	        	}catch (Exception e){
	    	        		endGame();
	    	        		won = whiteAI;
    						lost = blackAI;
    						winner = "Winner: White! Black AI threw and Exception: " + e.toString();
	    	        	}
	    			} while (i == currentBoard.INCOMPLETEMOVE);
	    			
	    		}
	    		else { // wait for user to move	    			try {	    				synchronized (UI) {	    					UI.wait();	    				}	    			}catch (InterruptedException e) {}
	    		}
	    	
	    	}	
	    	else { // ( white move )
	    		whiteGameClock.start();	
	    		if (whiteIsAI) {
	    			do {
	    				try{
		    				Move m = null;		    				m = whiteAI.chooseMove( currentBoard.getCopyOfBoardArray());
		    				long elapsed = whiteGameClock.pause();		    				logMove(m,CheckerInterface.WH, elapsed);		    				
		    				if (m == Move.pass) {		    					endGame();
		    					won = blackAI;
		    					lost =whiteAI;		    					if(getWhiteScore() == 0) {
		    							winner = "Winner: Black! White has no peices left.";		    					} else {
		    							winner = "Winner: Black! White AI passed when it did have a legal move.";
		    					}		    				}
		    				else {
		    					i = currentBoard.move(m, Checker.WHITE);
		    					if (i == currentBoard.ILLEGALMOVE) {		    						endGame();
			    					won = blackAI;
			    					lost = whiteAI;
		    						winner = "Winner: Black! White AI selected an invalid move: " + m;
		    						//throw new RuntimeException("White AI selected an invalid move: " + m);
		    					} else {
		    					    setLastMove(m);
		    					}
		    				}
	    	        	}catch (Exception e){
	    	        		endGame();
	    	        		won = blackAI;
    						lost = whiteAI;
    						winner = "Winner: Black! White AI threw and Exception: " + e.toString();
	    	        	}
	    			} while (i == currentBoard.INCOMPLETEMOVE);
	    		}
	    		else { // wait for user to move	    			try {	    				synchronized (UI) {	    					UI.wait();	    				}	    			} catch (InterruptedException e) {}
	    		}
	    
	    	}	
	    	// See who moves next	    	if (blackMove)
	    		blackMove = false; // now WHITE's move	    	else 	    		blackMove = true; // now BLACK's move	
		    // Redraw and pause momentarily.
		    UI.refreshUI();		
		    if (blackIsAI && whiteIsAI) {
		      	try {
		      		Thread.sleep(timeToSleep);
		        } catch (InterruptedException e) {}
		    }
		    else {		    	try {		    		Thread.sleep(500);		    	} catch (InterruptedException e) {}
		    }
	    }	    // Game Over!
	    UI.refreshUI();
	    return;
	}	
	public void endGame() {
  		gameIsOver = true;
	}
	public boolean isGameOver() {
		return gameIsOver;	}
	public boolean isBlacksMove() {		return blackMove;
	}
	public long getRemainingTime(int color) {
	    if (color == Checker.BLACK)
	    	return blackGameClock.millisRemaining();
	    else
	    	return whiteGameClock.millisRemaining();
	}	
	// Called by CyberthelloFrame when the human has moved.
	public int makeHumanMove(int currow, int curcol, int sucrow, int succol, int color) {		// Return true if valid move, false otherwise.		Move m = new Move(currow, curcol, sucrow, succol);
		int i = currentBoard.move(m, color);
		long blackElapsed=0;
		long whiteElapsed=0;
		if(color == CheckerInterface.BL) {
		    blackElapsed = blackGameClock.pause();
		} else {
		    whiteElapsed = whiteGameClock.pause();
		}
		if(i != currentBoard.ILLEGALMOVE) {
			if (i != currentBoard.INCOMPLETEMOVE)
				setLastMove(m);
		    logMove(m,color, (color==CheckerInterface.BL)?blackElapsed:whiteElapsed);
		}
		return i;
	}
	public CheckerBoard getCurrentBoard() {
		return currentBoard;
	}
	public boolean blackIsHuman() {
		return!blackIsAI;
	}
	public boolean whiteIsHuman() {
		return!whiteIsAI;
	}
	public int getBlackScore() {
		return currentBoard.getBlackScore();
	}
	public int getWhiteScore() {
		return currentBoard.getWhiteScore();	}		public int getSquareContents(int row, int col) {
		return currentBoard.getSquareContents(row, col);
	}
	public String getWinner(){
		return winner;	}	/**
	 * @return Returns the blackAI.
	 */
	public CheckerInterface getBlackAI() {
		return blackAI;
	}
	/**
	 * @return Returns the whiteAI.
	 */
	public CheckerInterface getWhiteAI() {
		return whiteAI;
	}
    /**
     * @return Returns the logger.
     */

	/**
	 * @return Returns the won.
	 */
	public CheckerInterface getWon() {
		return won;
	}
	
	public CheckerInterface getLost(){
		return lost;
	}
	
}

