package checkers;
	final static int LEGALMOVE = 1;
	}
	private void initializeBoard(boolean whiteOnTop) {

	public int getSquareContents(int row, int col){
	public int getBlackScore(){
		int score = 0;
	public int getWhiteScore(){
	public int move(Move m, int moveColor) {
		if (moveColor==-1) 
		System.out.println(turn+": moved " + m + " | "+legal);
	// This method has all the logic for determining whether the
	// move passed in the parameters is legal.  If the last
	// parameter is true, it actually updates the board.
		return ApplyMove(board, currow, curcol, sucrow, succol);
		switch (position[i][j]) {
						}
							return true;
				}
	// canWalk() returns true if the piece on (i,j) can make a
			case Checker.WHITE:
	private static boolean isEmpty(int[][] position, int i, int j) {
	private static boolean noMovesLeft(int[][] position, int toMove) {
					}
						else if (canCapture(position, i, j))
		return true;
	}
	// IsMoveLegal checks if the move entered is legal.
			// first see if any captures are possible
				case Checker.BLACK:
			}
					if ((end_i - start_i == -1)&&(end_j - start_j == -1||end_j - start_j == 1))
		}
					return ILLEGALMOVE;
	}
	// checkers that i and j are between 0 and 7 inclusive
		
				else {
			// check for new king

}
