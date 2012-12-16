package evolutionary_nn;

import java.util.*;

import checkers.Checker;
import checkers.Move;
import checkers_framework.CheckerInterface;

public class MiniMaxAI extends CheckerInterface {
	private class Node {
		public Move m;

		public byte[][] board;

		private LinkedList children;

		public int alpha;

		public int beta;

		public Node(Move m, byte[][] board) {
			this.m = m;
			this.board = board;
			this.children = new LinkedList();
		}

		public boolean addChild(Node n) {
			return children.add(n);
		}

		public boolean removeChild(Node n) {
			return children.remove(n);
		}

		public Iterator iterator() {
			return children.iterator();
		}

		public boolean isCapture() {
			return (Math.abs(m.curcol - m.succol) == 2);
		}
	}

	private class MultiMove extends Move {
		public LinkedList moves;

		public boolean kinged;

		public MultiMove(int r, int c, int r1, int c1) {
			super(r, c, r1, c1);
			moves = new LinkedList();
			moves.add(new Move(r, c, r1, c1));
			kinged = false;
		}
	}

	private class Piece {
		int r;

		int c;

		public Piece(int r, int c) {
			this.r = r;
			this.c = c;
		}
	}

	private class Cache {
		private class CacheEntry implements Comparable {
			byte[][] board;

			long timestamp;

			int depth;

			int alpha;

			int beta;

			public CacheEntry(byte[][] b, int depth, int alpha, int beta) {
				board = b;
				this.depth = depth;
				this.alpha = alpha;
				this.beta = beta;
				timestamp = System.currentTimeMillis();
			}

			public boolean equals(Object o) {
				CacheEntry that = (CacheEntry) o;
				if (!(that instanceof CacheEntry))
					return false;
				if (this.depth > that.depth)
					return false;
				if (that.depth % 2 != this.depth % 2)
					return false;
				for (int x = 0; x < 8; x++) {
					for (int y = 0; y < 8; y++) {
						if (this.board[x][y] != that.board[x][y])
							return false;
					}
				}
				return true;
			}

			public int hashCode() {
				int ret = 0;
				for (int x = 0; x < 8; x++) {
					for (int y = 0; y < 8; y++) {
						ret += (x + 1) * (y + 1) * board[x][y];
					}
				}
				return ret;
			}

			public int compareTo(Object o) {
				CacheEntry that = (CacheEntry) o;
				if (this.timestamp < that.timestamp)
					return -1;
				if (this.timestamp > that.timestamp)
					return 1;
				return 0;
			}
		}

		private HashMap cache;

		private TreeSet cleanup;

		private int maxsize = 8000;

		private byte lastclear = 24;

		public Cache() {
			cache = new HashMap();
			cleanup = new TreeSet();
		}

		public boolean contains(byte[][] b, int depth) {
			CacheEntry entry = new CacheEntry(b, depth, 0, 0);
			return cache.keySet().contains(entry);
		}

		public void add(byte[][] b, int depth, int alpha, int beta) {
			if ((lastclear - numpieces) >= 4) {
				System.out.println("BOOM");
				cache.clear();
				lastclear = numpieces;
			} else if (cache.size() == maxsize) {
				Iterator it = cache.keySet().iterator();
				CacheEntry killthis = (CacheEntry) it.next();
				while (it.hasNext()) {
					CacheEntry e = (CacheEntry) it.next();
					if (e.timestamp < killthis.timestamp)
						killthis = e;
				}
				cache.remove(killthis);
			}
			// if we have a shallower(depth) alpha/beta eval than what we
			// currently see in there... remove the deeper one.
			if (contains(b, Integer.MAX_VALUE)) {
				CacheEntry testlower = (CacheEntry) cache.get(new CacheEntry(b,
						depth, 0, 0));
				cache.remove(testlower);
			}
			CacheEntry entry = new CacheEntry(b, depth, alpha, beta);
			cache.put(entry, entry);
		}

		public int[] getvals(byte[][] b, int depth) {
			CacheEntry entry = (CacheEntry) cache.get(new CacheEntry(b, depth,
					0, 0));
			entry.timestamp = System.currentTimeMillis();
			int[] ret = new int[3];
			ret[0] = entry.alpha;
			ret[1] = entry.beta;
			ret[2] = entry.depth;
			return ret;
		}
	}

	private final static int LEGALMOVE = 1;

	private final static int ILLEGALMOVE = 2;

	private final static int INCOMPLETEMOVE = 3;

	private static final int MAXDEPTH = 10;

	private byte numpieces = 0;

	private Cache cache;

	private int color = 0;

	private MultiMove lastmulti;

	private int DEPTH = 6;

	public MiniMaxAI(int depth) {
		super();
		lastmulti = null;
		cache = new Cache();
		DEPTH = depth;
	}

	public Move chooseMove(int board[][]) {
	//	System.out.println(Integer.MAX_VALUE);
		// cache.cache.clear();
		byte[][] b = fixBoard(board);
		color = this.getColor();
		Node now = new Node(null, b);
		now.alpha = -1;
		now.beta = -1;
		helper(maxDepth(numPieces(b)), 0, now);
		boolean frommulti = false;

		Move m = null;
		int best = -1;
		LinkedList bestll = new LinkedList();
		if (lastmulti == null) {
			Iterator it = now.iterator();
			while (it.hasNext()) {
				Node child = (Node) it.next();
				if (child.beta > best) {

					best = child.beta;
					bestll.clear();
					bestll.add(child.m);
				} else if (child.beta == best)
					bestll.add(child.m);
			}
			if (bestll.size() > 0)
				m = (Move) bestll.removeFirst();
			if (m instanceof MultiMove) {
				lastmulti = (MultiMove) m;
				m = (Move) lastmulti.moves.removeFirst();
				if (lastmulti.moves.size() == 0)
					lastmulti = null;
			}
		} else {
			m = (Move) lastmulti.moves.removeFirst();
			if (lastmulti.moves.size() == 0)
				lastmulti = null;
			frommulti = true;
		}
	//	System.out.println(best);
		
		if (m == null)
			return Move.pass;
		if(this.IsMoveLegal(b,m,getColor()) == ILLEGALMOVE && DEPTH <= MAXDEPTH){
			DEPTH++;
		//	System.out.println("illegal move detected :"+m);
			m = chooseMove(board);
			DEPTH--;
		}
		
		return m;
	}

	private void helper(int maxdepth, int depth, Node n) {
		boolean MAXNODE = (depth % 2 == 0);
		// if (MAXNODE && depth != 0 && cache.contains(n.board, depth)){
		// //System.out.println("hit"+depth);
		// int[] vals = cache.getvals(n.board, depth);
		// n.alpha = vals[0];
		// n.beta = vals[1];
		// return;
		// }
		int MAXCOLOR = color;
		int MINCOLOR = color * -1;
		if (depth == maxdepth) {
			if (MAXNODE) // MAX LEVEL
				n.alpha = customBoardEval(n.board, depth);
			else
				n.beta = customBoardEval(n.board, depth);
			return;
		}

		LinkedList moves = (depth % 2 == 0) ? getAllMoves(n.board, MAXCOLOR)
				: getAllMoves(n.board, MINCOLOR);

		if (moves.size() == 0) {
			if (MAXNODE) // MAX LEVEL
				n.alpha = customBoardEval(n.board, depth);
			else
				n.beta = customBoardEval(n.board, depth);
			return;
		}
		if (depth == 0 && moves.size() == 1) {
			n.addChild((Node) moves.removeFirst());
			return;
		}
		if (moves.size() == 1)
			maxdepth++;
		while (moves.size() > 0) {
			Node nextmove = (Node) moves.removeFirst();
			if (depth % 2 == 0) {
				nextmove.alpha = n.alpha;
				nextmove.beta = -1;
			} else {
				nextmove.alpha = -1;
				nextmove.beta = n.beta;
			}

			if (nextmove.board == null)
				nextmove.board = newBoard(n.board, nextmove.m);
			helper(maxdepth, depth + 1, nextmove);

			if (MAXNODE) {// MAX LEVEL
				if (n.alpha == -1 || nextmove.beta > n.alpha)
					n.alpha = nextmove.beta;
				if (nextmove.beta != -1 && depth == 0) {
					n.addChild(nextmove);
				}
			} else { // MIN LEVEL
				if (n.beta == -1 || nextmove.alpha < n.beta)
					n.beta = nextmove.alpha;
				if (n.alpha != -1 && n.alpha > nextmove.alpha) {
					n.beta = -1;
					return; // prepare for cutoff
				}
				if (depth == 0)
					n.addChild(nextmove);
			}
		}
		// if ( depth != 0 ){
		// if ( depth < 5 )
		// if(MAXNODE)
		// cache.add(n.board, depth, n.alpha, n.beta);
		// }
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
					// score -= (color < 0) ? POS*(7-i): POS*i;
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
					// score += (color < 0) ? POS*(7-i): POS*i;
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

	private byte[][] newBoard(byte[][] b, Move m) {
		byte[][] ret = new byte[8][8];
		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				ret[x][y] = b[x][y];
			}
		}
		if (Math.abs(m.sucrow - m.currow) == 1) {
			ret[m.sucrow][m.succol] = ret[m.currow][m.curcol];
			ret[m.currow][m.curcol] = Checker.EMPTY;
		} else { // capture
			ret[(m.currow + m.sucrow) / 2][(m.curcol + m.succol) / 2] = Checker.EMPTY;
			ret[m.sucrow][m.succol] = ret[m.currow][m.curcol];
			ret[m.currow][m.curcol] = Checker.EMPTY;
		}

		// check for new king
		if (ret[m.sucrow][m.succol] == Checker.WHITE && m.sucrow == 0) {
			ret[m.sucrow][m.succol] = Checker.WKing;
		} else if (ret[m.sucrow][m.succol] == Checker.BLACK && m.sucrow == 7) {
			ret[m.sucrow][m.succol] = Checker.BKing;
		}
		return ret;
	}

	private LinkedList movesForPiece(byte[][] b, int r, int c, int color) {
		LinkedList moves = new LinkedList();
		Move m;
		switch (b[r][c]) {
		case CheckerInterface.WKING: // white king
			if (color == CheckerInterface.BLACK)
				break;
			m = new Move(r, c, r + 2, c - 2);
			if (isValidMove(b, m, color))
				moves.add(new Node(m, newBoard(b, m)));
			m = new Move(r, c, r + 2, c + 2);
			if (isValidMove(b, m, color))
				moves.add(new Node(m, newBoard(b, m)));
			m = new Move(r, c, r + 1, c - 1);
			if (isValidMove(b, m, color))
				moves.add(new Node(m, newBoard(b, m)));
			m = new Move(r, c, r + 1, c + 1);
			if (isValidMove(b, m, color))
				moves.add(new Node(m, newBoard(b, m)));

		case CheckerInterface.WHITE: // normal white peice
			if (color == CheckerInterface.BLACK)
				break;
			m = new Move(r, c, r - 2, c - 2);
			if (isValidMove(b, m, color))
				moves.add(new Node(m, newBoard(b, m)));
			m = new Move(r, c, r - 2, c + 2);
			if (isValidMove(b, m, color))
				moves.add(new Node(m, newBoard(b, m)));
			m = new Move(r, c, r - 1, c - 1);
			if (isValidMove(b, m, color))
				moves.add(new Node(m, newBoard(b, m)));
			m = new Move(r, c, r - 1, c + 1);
			if (isValidMove(b, m, color))
				moves.add(new Node(m, newBoard(b, m)));
			break;

		case CheckerInterface.BKING: // black king
			if (color == CheckerInterface.WHITE)
				break;
			m = new Move(r, c, r - 2, c - 2);
			if (isValidMove(b, m, color))
				moves.add(new Node(m, newBoard(b, m)));
			m = new Move(r, c, r - 2, c + 2);
			if (isValidMove(b, m, color))
				moves.add(new Node(m, newBoard(b, m)));
			m = new Move(r, c, r - 1, c - 1);
			if (isValidMove(b, m, color))
				moves.add(new Node(m, newBoard(b, m)));
			m = new Move(r, c, r - 1, c + 1);
			if (isValidMove(b, m, color))
				moves.add(new Node(m, newBoard(b, m)));

		case CheckerInterface.BLACK: // normal black peice
			if (color == CheckerInterface.WHITE)
				break;
			m = new Move(r, c, r + 2, c + 2);
			if (isValidMove(b, m, color))
				moves.add(new Node(m, newBoard(b, m)));
			m = new Move(r, c, r + 2, c - 2);
			if (isValidMove(b, m, color))
				moves.add(new Node(m, newBoard(b, m)));
			m = new Move(r, c, r + 1, c + 1);
			if (isValidMove(b, m, color))
				moves.add(new Node(m, newBoard(b, m)));
			m = new Move(r, c, r + 1, c - 1);
			if (isValidMove(b, m, color))
				moves.add(new Node(m, newBoard(b, m)));
			break;
		}
		return moves;
	}

	private LinkedList getAllMoves(byte[][] b, int color) {
		LinkedList moves = new LinkedList();
		Object[] pieces = myPieces(b, color);
		if (pieces.length == 0)
			return new LinkedList();

		for (int i = 0; i < pieces.length; i++) {
			LinkedList mp = movesForPiece(b, ((Piece) pieces[i]).r,
					((Piece) pieces[i]).c, color);
			if (mp.size() != 0) {
				// this piece can move.
				boolean iscapture = ((Node) mp.get(0)).isCapture();
				// if iscapture, all moves here SHOULD be catures, as you must
				// capture.
				if (iscapture) {
					while (mp.size() != 0) {
						Node n = (Node) mp.getFirst();
						if (n.m instanceof MultiMove) {
							// this move is a multimove, m is a LinkedList of
							// moves.
							Move lastmove = (Move) ((MultiMove) n.m).moves
									.getLast(); // last move in the multi move.
							if (!((MultiMove) n.m).kinged
									&& this.canCapture(n.board,
											lastmove.sucrow, lastmove.succol)) {
								// I can STILL capture... cripes...
								LinkedList nextmoves = movesForPiece(n.board,
										lastmove.sucrow, lastmove.succol, color);
								if (nextmoves.size() == 1) {
									// only 1 move... tack it on the end
									Node next = (Node) nextmoves.getFirst();
									((MultiMove) n.m).moves.add(next.m);
									if (n.board[next.m.currow][next.m.curcol] != next.board[next.m.sucrow][next.m.succol])
										((MultiMove) n.m).kinged = true;
									n.board = next.board;
								} else {
									// there are many moves... so we have many
									// choices :-/ confusion ensues.
									while (nextmoves.size() > 0) {
										Node next = (Node) nextmoves
												.removeFirst();
										Node copyofn = new Node(n.m, next.board);
										copyofn.m = new MultiMove(n.m.currow,
												n.m.curcol, n.m.sucrow,
												n.m.succol);
										MultiMove cnmm = (MultiMove) copyofn.m;
										MultiMove nmm = (MultiMove) n.m;
										cnmm.moves = (LinkedList) nmm.moves
												.clone();
										copyofn.alpha = n.alpha;
										copyofn.beta = n.beta;
										((MultiMove) copyofn.m).moves
												.add(next.m);
										if (n.board[next.m.currow][next.m.curcol] != next.board[next.m.sucrow][next.m.succol])
											((MultiMove) copyofn.m).kinged = true;
										mp.add(copyofn);
									}
									mp.remove(n);
								}
							} else {
								// n is a multimove but it cannot capture again
								// so the move is terminated.
								moves.add(n);
								mp.remove(n);
							}
						} else {
							// this move is not a multi move, n is a Move (node
							// with a Move)
							if (this
									.canCapture(n.board, n.m.sucrow, n.m.succol)) {
								// this move can capture so we need to turn n
								// into a multimove
								Move oldsingle = n.m;
								n.m = new MultiMove(oldsingle.currow,
										oldsingle.curcol, oldsingle.sucrow,
										oldsingle.succol);
								LinkedList nextmoves = movesForPiece(n.board,
										n.m.sucrow, n.m.succol, color);
								if (nextmoves.size() == 1) {
									// only 1 move... tack it on the end
									Node next = (Node) nextmoves.getFirst();
									((MultiMove) n.m).moves.add(next.m);
									if (n.board[next.m.currow][next.m.curcol] != next.board[next.m.sucrow][next.m.succol])
										((MultiMove) n.m).kinged = true;
									n.board = next.board;
								} else {
									// there are many moves... so we have many
									// choices :-/ confusion ensues.
									while (nextmoves.size() > 0) {
										Node next = (Node) nextmoves
												.removeFirst();
										Node copyofn = new Node(n.m, next.board);
										copyofn.m = new MultiMove(n.m.currow,
												n.m.curcol, n.m.sucrow,
												n.m.succol);
										MultiMove cnmm = (MultiMove) copyofn.m;
										MultiMove nmm = (MultiMove) n.m;
										cnmm.moves = (LinkedList) nmm.moves
												.clone();
										copyofn.alpha = n.alpha;
										copyofn.beta = n.beta;
										((MultiMove) copyofn.m).moves
												.add(next.m);
										if (n.board[next.m.currow][next.m.curcol] != next.board[next.m.sucrow][next.m.succol])
											((MultiMove) copyofn.m).kinged = true;
										mp.add(copyofn);
									}
									mp.remove(n);
								}
							} else {
								// this move cannot further capture. We are done
								// here. Nothing to see. Move along.
								moves.add(n);
								mp.remove(n);
							}
						}
					}
				} else {
					// valid legal non capture move here means no pieces have
					// capture moves.
					moves.addAll(mp);
				}
			}
		}
		return moves;
	}

	private Object[] myPieces(byte[][] b, int color) {
		ArrayList al = new ArrayList();
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (b[i][j] == color || b[i][j] == color * 2)
					al.add(new Piece(i, j));
			}
		}
		return al.toArray();
	}

	public boolean canCapture(byte[][] b, int i, int j) {
		switch (b[i][j]) {
		case Checker.BLACK:
			// down right
			if (i + 2 < 8 && j + 2 < 8) {
				if (b[i + 1][j + 1] < 0 && b[i + 2][j + 2] == Checker.EMPTY)
					return true;
			}
			// down left
			if (i + 2 < 8 && j - 2 > -1) {
				if (b[i + 1][j - 1] < 0 && b[i + 2][j - 2] == Checker.EMPTY)
					return true;
			}
			break;
		case Checker.WHITE:
			// up right
			if (i - 2 > -1 && j + 2 < 8) {
				if (b[i - 1][j + 1] > 0 && b[i - 2][j + 2] == Checker.EMPTY)
					return true;
			}
			// up left
			if (i - 2 > -1 && j - 2 > -1) {
				if (b[i - 1][j - 1] > 0 && b[i - 2][j - 2] == Checker.EMPTY)
					return true;
			}
			break;
		case Checker.WKing:
			if (i + 2 < 8) {
				if (j + 2 < 8) {
					if (b[i + 1][j + 1] > 0 && b[i + 2][j + 2] == Checker.EMPTY)
						return true;
				}
				if (j - 2 > -1) {
					if (b[i + 1][j - 1] > 0 && b[i + 2][j - 2] == Checker.EMPTY)
						return true;
				}
			}
			if (i - 2 > -1) {
				if (j + 2 < 8) {
					if (b[i - 1][j + 1] > 0 && b[i - 2][j + 2] == Checker.EMPTY)
						return true;
				}
				if (j - 2 > -1) {
					if (b[i - 1][j - 1] > 0 && b[i - 2][j - 2] == Checker.EMPTY)
						return true;
				}
			}
			break;
		case Checker.BKing:
			if (i + 2 < 8) {
				if (j + 2 < 8) {
					if (b[i + 1][j + 1] < 0 && b[i + 2][j + 2] == Checker.EMPTY)
						return true;
				}
				if (j - 2 > -1) {
					if (b[i + 1][j - 1] < 0 && b[i + 2][j - 2] == Checker.EMPTY)
						return true;
				}
			}
			if (i - 2 > -1) {
				if (j + 2 < 8) {
					if (b[i - 1][j + 1] < 0 && b[i - 2][j + 2] == Checker.EMPTY)
						return true;
				}
				if (j - 2 > -1) {
					if (b[i - 1][j - 1] < 0 && b[i - 2][j - 2] == Checker.EMPTY)
						return true;
				}
			}
			break;
		}
		return false;
	}

	public boolean isValidMove(byte[][] b, Move m, int color) {
		switch (color) {
		case Checker.WHITE:
			if (!(b[m.currow][m.curcol] < 0)) // if not white
				return false;
			break;
		case Checker.BLACK:
			if (!(b[m.currow][m.curcol] > 0)) // if not black
				return false;
			break;
		}
		int dummy = IsMoveLegal(b, m, color);
		if (dummy == LEGALMOVE || dummy == INCOMPLETEMOVE)
			return true;
		return false;
	}

	private int IsMoveLegal(byte[][] b, Move m, int color) {
		if (!(MiniMaxAI.inRange(m.currow, m.curcol) && MiniMaxAI.inRange(
				m.sucrow, m.succol)))
			return ILLEGALMOVE;

		if (b[m.sucrow][m.succol] != Checker.EMPTY)
			return ILLEGALMOVE;
		/*
		 * This is commented out because we will handle this circumstance
		 * elsewhere if ( incomplete &&
		 * ((row_force!=m.currow)||col_force!=m.curcol)) return ILLEGALMOVE;
		 */
		int piece = b[m.currow][m.curcol];
		// non jump moves
		if (Math.abs(m.currow - m.sucrow) == 1) {
			// first see if any captures are possible (non jumps not allowed)
			switch (piece) {
			case Checker.WHITE:
			case Checker.WKing:
				for (int i = 0; i < 8; i++) {
					for (int j = 0; j < 8; j++) {
						if (b[i][j] < 0 && canCapture(b, i, j))
							return ILLEGALMOVE;
					}
				}
				break;

			case Checker.BLACK:
			case Checker.BKing:
				for (int i = 0; i < 8; i++) {
					for (int j = 0; j < 8; j++) {
						if (b[i][j] > 0 && canCapture(b, i, j))
							return ILLEGALMOVE;
					}
				}
				break;
			}
			switch (piece) {
			case Checker.WHITE:
				if ((m.sucrow - m.currow == -1)
						&& (m.succol - m.curcol == -1 || m.succol - m.curcol == 1))
					return LEGALMOVE;
				break;

			case Checker.BLACK:
				if ((m.sucrow - m.currow == 1)
						&& (m.succol - m.curcol == -1 || m.succol - m.curcol == 1))
					return LEGALMOVE;
				break;

			case Checker.WKing:
			case Checker.BKing:
				if (Math.abs(m.succol - m.curcol) == 1)
					return LEGALMOVE;
				break;
			}
			return ILLEGALMOVE;
		}
		// jump moves
		else if (Math.abs(m.currow - m.sucrow) == 2
				&& Math.abs(m.curcol - m.succol) == 2) {
			int capture_i = (m.currow + m.sucrow) / 2;
			int capture_j = (m.curcol + m.succol) / 2;
			int capture_piece = b[capture_i][capture_j];

			if (color < 0) {
				// white
				if (!(capture_piece > 0)) // we didnt capture black
					return ILLEGALMOVE;
			}

			if (color > 0) {
				// black
				if (!(capture_piece < 0)) // we didnt capture white
					return ILLEGALMOVE;

			}

			switch (piece) {
			case Checker.WHITE:
				if (m.sucrow - m.currow != -2)
					return ILLEGALMOVE;
				break;
			case Checker.BLACK:
				if (m.sucrow - m.currow != 2)
					return ILLEGALMOVE;
				break;
			case Checker.WKing:
			case Checker.BKing:
				if (Math.abs(m.sucrow - m.currow) != 2)
					return ILLEGALMOVE;
			}
			return INCOMPLETEMOVE;
		}
		// I dunno what kind of move makes it here...  but it cant be legal.
		return ILLEGALMOVE;
	}

	private static boolean inRange(int i, int j) {
		return (i > -1 && i < 8 && j > -1 && j < 8);
	}

	public void setColor(int c) {
		color = c;
	}

	public int getColor() {
		return color;
	}

	public String getName() {
		return "bob";
	}

	public int maxDepth(byte pieces) {
		return DEPTH;
	}

	public byte numPieces(byte[][] b) {
		byte num = 0;
		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				if (b[x][y] != 0)
					num++;
			}
		}
		return num;
	}

	public byte[][] fixBoard(int[][] b) {
		byte[][] board = new byte[8][8];
		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				board[x][y] = (byte) b[x][y];
			}
		}
		return board;
	}
}