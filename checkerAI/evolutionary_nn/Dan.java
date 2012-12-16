package evolutionary_nn;

public class Dan {

	public Dan() {
		super();
		// TODO Auto-generated constructor stub
	}

	int color = 1;
	/**
	 * @param args
	 */
	public static void main(String[] args) {

	}

	private int customBoardEval(byte[][] b, int depth) {
		int CHECKER = 1000; // one checker worth 100
		int POS = 2; // one position along the -i worth 1
		int KING = 2000; // A king worth 200
		int EDGE = 500; // effect of king being on the edge
		int score = 30000000;

		int num = 0;
		int mykings = 0;
		int yourkings = 0;
		int mypieces = 0;
		int yourpieces = 0;

		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (b[i][j] == color * -1) {
					yourpieces++;
					//		         score -= (color < 0) ? POS*(7-i): POS*i;
				} else if (b[i][j] == color * -2) {
					yourpieces++;
					yourkings++;
					if (i == 0 || i == 7)
						score += EDGE;
					if (j == 0 || j == 7)
						score += EDGE;
				} else if (b[i][j] == color * 2) {
					mykings++;
					mypieces++;
					if (i == 0 || i == 7)
						score -= EDGE;
					if (j == 0 || j == 7)
						score -= EDGE;
				} else if (b[i][j] == color) {
					mypieces++;
					//		   score += (color < 0) ? POS*(7-i): POS*i;
				}
			}
		}
		if (yourpieces == 0)
			return Integer.MAX_VALUE - depth;
		if (mypieces == 0)
			return 0;

		score += (mypieces - yourpieces) * CHECKER;
		score += (mykings - yourkings) * KING;
		score += (24 - (mypieces + yourpieces));
		return score;
	}
}
