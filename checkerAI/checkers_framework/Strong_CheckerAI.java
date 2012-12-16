package checkers_framework;


import java.util.Date;
import java.util.Random;
import java.util.Vector;


public class Strong_CheckerAI extends CheckerInterface
{

    private int color;
    private Random rnd;
    private Move expected_move;

    public Strong_CheckerAI()
    {
    	super();
        Date date = new Date();
        rnd = new Random(date.getTime());
    }

    public void setColor(int i)
    {
        color = i;
    }

    private void setExpectedMove(Move move)
    {
        expected_move = new Move(move.currow, move.curcol, move.sucrow, move.succol);
    }

    public Move chooseMove( int ai[][])
    {
        
        float f = (float)(24D / (double)(getWhiteScore(ai) + getBlackScore(ai)));
        f = (float)Math.sqrt(Math.sqrt(f));
        int j = (int)(6F * f) <= 12 ? (int)(6F * f) : 12;
        j = 2;
      int i = AlphaBeta(ai, 0, j, color);
        if(expected_move.sucrow > 0 || expected_move.succol > 0)
        {
            int k = expected_move.currow;
            int l = expected_move.curcol;
            int i1 = expected_move.sucrow;
            int j1 = expected_move.succol;
            expected_move = new Move(k, l, i1 % 10, j1 % 10);
            k = i1 % 10;
            l = j1 % 10;
            i1 /= 10;
            j1 /= 10;
            return expected_move;
        }
        if(expected_move.currow == 0 && expected_move.curcol == 0 && expected_move.sucrow == 0 && expected_move.succol == 0)
        {
            return Move.pass;
        } else
        {
            return expected_move;
        }
    }

    public int customBoardEval(int ai[][])
    {
        int i = 0;
        int j = 0;
        for(int k = 0; k < 8; k++)
        {
            for(int l = 0; l < 8; l++)
            {
                if(ai[k][l] != 0)
                {
                    j++;
                }
            }

        }

        for(int i1 = 0; i1 < 8; i1++)
        {
            for(int j1 = 0; j1 < 8; j1++)
            {
                if(ai[i1][j1] == -1)
                {
                    i -= 100;
                    if(j >= 6)
                    {
                        i -= 2 * i1 * i1;
                    }
                } else
                if(ai[i1][j1] == -2)
                {
                    i -= 200;
                    if((i1 == 0 || i1 == 7) && j >= 6)
                    {
                        i += 10;
                    }
                    if((j1 == 0 || j1 == 7) && j >= 6)
                    {
                        i += 10;
                    }
                } else
                if(ai[i1][j1] == 2)
                {
                    i += 200;
                    if((i1 == 0 || i1 == 7) && j >= 6)
                    {
                        i -= 10;
                    }
                    if((j1 == 0 || j1 == 7) && j >= 6)
                    {
                        i -= 10;
                    }
                } else
                if(ai[i1][j1] == 1)
                {
                    i += 100;
                    if(j >= 6)
                    {
                        i += 2 * (7 - i1) * (7 - i1);
                    }
                }
            }

        }

        i += (int)(rnd.nextFloat() * 10F);
        return i;
    }

    public int AlphaBeta(int ai[][], int i, int j, int k)
    {
        int l = AlphaBeta(ai, i, j, k, 0x7fffffff, 0x80000001);
        return l;
    }

    int AlphaBeta(int ai[][], int i, int j, int k, int l, int i1)
    {
        Move move = new Move(0, 0, 0, 0);
        Thread.yield();
        int k1;
        if(i == j)
        {
            k1 = customBoardEval(ai);
        } else
        {
            Vector vector = generate_moves(ai, k);
            k1 = which_turn(k);
            int l1;
            switch(vector.size())
            {
            case 0: // '\0'
                setExpectedMove(Move.pass);
                return k1;

            case 1: // '\001'
                if(i == 0)
                {
                    move = (Move)vector.elementAt(0);
                    setExpectedMove(move);
                    return 0;
                }
                j++;
                // fall through

            default:
                l1 = 0;
                break;
            }
            for(; l1 < vector.size(); l1++)
            {
                int ai1[][] = copy_board(ai);
                move_board(ai1, (Move)vector.elementAt(l1));
                int j1 = AlphaBeta(ai1, i + 1, j, opponent(k), l, i1);
                if(k == 1 && j1 > k1)
                {
                    move = (Move)vector.elementAt(l1);
                    k1 = j1;
                    if(k1 <= i1)
                    {
                        continue;
                    }
                    if(k1 >= l)
                    {
                        break;
                    }
                    i1 = k1;
                    continue;
                }
                if(k != -1 || j1 >= k1)
                {
                    continue;
                }
                move = (Move)vector.elementAt(l1);
                k1 = j1;
                if(k1 >= l)
                {
                    continue;
                }
                if(k1 <= i1)
                {
                    break;
                }
                l = k1;
            }

        }
        setExpectedMove(move);
        return k1;
    }

    public int opponent(int i)
    {
        return i != 1 ? 1 : -1;
    }

    public int which_turn(int i)
    {
        return whichcolor(i) != 1 ? 0x7fffffff : 0x80000001;
    }

    private int whichcolor(int i)
    {
        switch(i)
        {
        case 1: // '\001'
        case 2: // '\002'
            return 1;

        case -2: 
        case -1: 
            return -1;

        case 0: // '\0'
        default:
            return 0;
        }
    }

    private int getBlackScore(int ai[][])
    {
        int i = 0;
        for(int j = 0; j < 8; j++)
        {
            for(int k = 0; k < 8; k++)
            {
                if(ai[j][k] == 1 || ai[j][k] == 2)
                {
                    i++;
                }
            }

        }

        return i;
    }

    private int getWhiteScore(int ai[][])
    {
        int i = 0;
        for(int j = 0; j < 8; j++)
        {
            for(int k = 0; k < 8; k++)
            {
                if(ai[j][k] == -1 || ai[j][k] == -2)
                {
                    i++;
                }
            }

        }

        return i;
    }

   
	private Vector generate_moves(int ai[][], int i)
    {
        Vector vector = new Vector();
        int i1 = 0;
        for(int j1 = 7; j1 >= 0; j1--)
        {
            for(int k1 = 0; k1 < 8; k1++)
            {
                if(i == whichcolor(ai[j1][k1]))
                {
                    if(Capturable(ai, i))
                    {
                        for(int l1 = -2; l1 <= 2; l1 += 4)
                        {
                            for(int j2 = -2; j2 <= 2; j2 += 4)
                            {
                                int j = IsMoveLegal(ai, j1, k1, j1 + l1, k1 + j2, i);
                                if(j == 3)
                                {
                                    int ai1[] = new int[4];
                                    ai1[0] = j1;
                                    ai1[1] = k1;
                                    ai1[2] = j1 + l1;
                                    ai1[3] = k1 + j2;
                                    int ai3[][] = copy_board(ai);
                                    int k = ApplyMove(ai3, j1, k1, j1 + l1, k1 + j2);
                                    if(k == 3)
                                    {
                                        hasToCapture(ai3, i, ai1, vector, 10);
                                    } else
                                    {
                                        Move move1 = new Move(ai1[0], ai1[1], ai1[2], ai1[3]);
                                        vector.addElement(move1);
                                    }
                                }
                            }

                        }

                    } else
                    {
                        for(int i2 = -1; i2 <= 2; i2 += 2)
                        {
                            for(int k2 = -1; k2 <= 2; k2 += 2)
                            {
                                if(isInRange(j1 + i2, k1 + k2))
                                {
                                    int l = IsWalkLegal(ai, j1, k1, j1 + i2, k1 + k2, i);
                                    if(l == 1)
                                    {
                                        int ai2[] = new int[4];
                                        ai2[0] = j1;
                                        ai2[1] = k1;
                                        ai2[2] = j1 + i2;
                                        ai2[3] = k1 + k2;
                                        Move move = new Move(ai2[0], ai2[1], ai2[2], ai2[3]);
                                        vector.addElement(move);
                                        i1++;
                                    }
                                }
                            }

                        }

                    }
                }
            }

        }

        return vector;
    }

    private int IsWalkLegal(int ai[][], int i, int j, int k, int l, int i1)
    {
        if(!isInRange(i, j) || !isInRange(k, l))
        {
            return 2;
        }
        if(ai[k][l] != 0)
        {
            return 2;
        }
        int j1 = ai[i][j];
        if(Math.abs(j - l) == 1)
        {
            switch(j1)
            {
            case 0: // '\0'
            default:
                break;

            case 1: // '\001'
                if(k - i == 1)
                {
                    return 1;
                }
                break;

            case -1: 
                if(k - i == -1)
                {
                    return 1;
                }
                break;

            case -2: 
            case 2: // '\002'
                if(Math.abs(k - i) == 1)
                {
                    return 1;
                }
                break;
            }
            return 2;
        } else
        {
            return 2;
        }
    }

    private boolean isInRange(int i, int j)
    {
        return i > -1 && i < 8 && j > -1 && j < 8;
    }

	private void hasToCapture(int ai[][], int i, int ai1[], Vector vector, int j)
    {
        int k = ai1[2];
        int l;
        for(l = ai1[3]; k > 7 || l > 7; l /= 10)
        {
            k /= 10;
        }

        for(int i1 = -2; i1 <= 2; i1 += 4)
        {
            for(int j1 = -2; j1 <= 2; j1 += 4)
            {
                if(isInRange(k + i1, l + j1))
                {
                    int ai2[][] = copy_board(ai);
                    int k1 = ApplyMove(ai2, k, l, k + i1, l + j1);
                    if(k1 == 1)
                    {
                        int ai3[] = new int[4];
                        ai3[0] = ai1[0];
                        ai3[1] = ai1[1];
                        ai3[2] = ai1[2] + (k + i1) * j;
                        ai3[3] = ai1[3] + (l + j1) * j;
                        Move move = new Move(ai3[0], ai3[1], ai3[2], ai3[3]);
                        vector.addElement(move);
                    } else
                    if(k1 == 3)
                    {
                        int ai4[] = new int[4];
                        ai4[0] = ai1[0];
                        ai4[1] = ai1[1];
                        ai4[2] = ai1[2] + (k + i1) * j;
                        ai4[3] = ai1[3] + (l + j1) * j;
                        hasToCapture(ai2, i, ai4, vector, j * 10);
                    }
                }
            }

        }

    }

    private int ApplyMove(int ai[][], int i, int j, int k, int l)
    {
        int i1 = IsMoveLegal(ai, i, j, k, l, whichcolor(ai[i][j]));
        if(i1 != 2)
        {
            if(Math.abs(l - j) == 1)
            {
                ai[k][l] = ai[i][j];
                ai[i][j] = 0;
            } else
            {
                ai[(i + k) / 2][(j + l) / 2] = 0;
                ai[k][l] = ai[i][j];
                ai[i][j] = 0;
            }
            if(i1 == 3 && !Capturable(ai, k, l))
            {
                i1 = 1;
            }
            if(ai[k][l] == 1 && k == 7)
            {
                ai[k][l] = 2;
            } else
            if(ai[k][l] == -1 && k == 0)
            {
                ai[k][l] = -2;
            }
        }
        return i1;
    }

    private int IsMoveLegal(int ai[][], int i, int j, int k, int l, int i1)
    {
        if(!isInRange(i, j) || !isInRange(k, l))
        {
            return 2;
        }
        if(ai[k][l] != 0)
        {
            return 2;
        }
        int j1 = ai[i][j];
        if(Math.abs(j - l) == 1)
        {
            switch(j1)
            {
            case 1: // '\001'
            case 2: // '\002'
                for(int k1 = 0; k1 < 8; k1++)
                {
                    for(int i2 = 0; i2 < 8; i2++)
                    {
                        if((ai[k1][i2] == 1 || ai[k1][i2] == 2) && Capturable(ai, k1, i2))
                        {
                            return 2;
                        }
                    }

                }

                break;

            case -2: 
            case -1: 
                for(int k2 = 0; k2 < 8; k2++)
                {
                    for(int i3 = 0; i3 < 8; i3++)
                    {
                        if((ai[k2][i3] == -1 || ai[k2][i3] == -2) && Capturable(ai, k2, i3))
                        {
                            return 2;
                        }
                    }

                }

                break;
            }
            switch(j1)
            {
            case 0: // '\0'
            default:
                break;

            case 1: // '\001'
                if(k - i == 1)
                {
                    return 1;
                }
                break;

            case -1: 
                if(k - i == -1)
                {
                    return 1;
                }
                break;

            case -2: 
            case 2: // '\002'
                if(Math.abs(k - i) == 1)
                {
                    return 1;
                }
                break;
            }
            return 2;
        }
        if(Math.abs(j - l) == 2)
        {
            int l1 = (i + k) / 2;
            int j2 = (j + l) / 2;
            int l2 = ai[l1][j2];
            if(i1 == 1)
            {
                if(l2 != -1 && l2 != -2)
                {
                    return 2;
                }
            } else
            if(l2 != 1 && l2 != 2)
            {
                return 2;
            }
            switch(j1)
            {
            case 0: // '\0'
            default:
                break;

            case 1: // '\001'
                if(k - i != 2)
                {
                    return 2;
                }
                break;

            case -1: 
                if(k - i != -2)
                {
                    return 2;
                }
                break;

            case -2: 
            case 2: // '\002'
                if(Math.abs(k - i) != 2)
                {
                    return 2;
                }
                break;
            }
            return 3;
        } else
        {
            return 2;
        }
    }

    private boolean Capturable(int ai[][], int i)
    {
        for(int j = 0; j < 8; j++)
        {
            for(int k = 0; k < 8; k++)
            {
                if(whichcolor(ai[j][k]) == i && Capturable(ai, j, k))
                {
                    return true;
                }
            }

        }

        return false;
    }

    private boolean Capturable(int ai[][], int i, int j)
    {
        switch(ai[i][j])
        {
        case 0: // '\0'
        default:
            break;

        case 1: // '\001'
            if(i + 2 < 8 && j + 2 < 8 && (ai[i + 1][j + 1] == -1 || ai[i + 1][j + 1] == -2) && ai[i + 2][j + 2] == 0)
            {
                return true;
            }
            if(j - 2 > -1 && i + 2 < 8 && (ai[i + 1][j - 1] == -1 || ai[i + 1][j - 1] == -2) && ai[i + 2][j - 2] == 0)
            {
                return true;
            }
            break;

        case -1: 
            if(j + 2 < 8 && i - 2 > -1 && (ai[i - 1][j + 1] == 1 || ai[i - 1][j + 1] == 2) && ai[i - 2][j + 2] == 0)
            {
                return true;
            }
            if(j - 2 > -1 && i - 2 > -1 && (ai[i - 1][j - 1] == 1 || ai[i - 1][j - 1] == 2) && ai[i - 2][j - 2] == 0)
            {
                return true;
            }
            break;

        case 2: // '\002'
            if(j + 2 < 8)
            {
                if(i + 2 < 8 && (ai[i + 1][j + 1] == -1 || ai[i + 1][j + 1] == -2) && ai[i + 2][j + 2] == 0)
                {
                    return true;
                }
                if(i - 2 > -1 && (ai[i - 1][j + 1] == -1 || ai[i - 1][j + 1] == -2) && ai[i - 2][j + 2] == 0)
                {
                    return true;
                }
            }
            if(j - 2 <= -1)
            {
                break;
            }
            if(i + 2 < 8 && (ai[i + 1][j - 1] == -1 || ai[i + 1][j - 1] == -2) && ai[i + 2][j - 2] == 0)
            {
                return true;
            }
            if(i - 2 > -1 && (ai[i - 1][j - 1] == -1 || ai[i - 1][j - 1] == -2) && ai[i - 2][j - 2] == 0)
            {
                return true;
            }
            break;

        case -2: 
            if(j + 2 < 8)
            {
                if(i + 2 < 8 && (ai[i + 1][j + 1] == 1 || ai[i + 1][j + 1] == 2) && ai[i + 2][j + 2] == 0)
                {
                    return true;
                }
                if(i - 2 > -1 && (ai[i - 1][j + 1] == 1 || ai[i - 1][j + 1] == 2) && ai[i - 2][j + 2] == 0)
                {
                    return true;
                }
            }
            if(j - 2 <= -1)
            {
                break;
            }
            if(i + 2 < 8 && (ai[i + 1][j - 1] == 1 || ai[i + 1][j - 1] == 2) && ai[i + 2][j - 2] == 0)
            {
                return true;
            }
            if(i - 2 > -1 && (ai[i - 1][j - 1] == 1 || ai[i - 1][j - 1] == 2) && ai[i - 2][j - 2] == 0)
            {
                return true;
            }
            break;
        }
        return false;
    }

    private void move_board(int ai[][], Move move)
    {
        int i = move.currow;
        int j = move.curcol;
        int k = move.sucrow;
        for(int l = move.succol; k > 0 || l > 0; l /= 10)
        {
            ApplyMove(ai, i, j, k % 10, l % 10);
            i = k % 10;
            j = l % 10;
            k /= 10;
        }

    }

    private int[][] copy_board(int ai[][])
    {
        int ai1[][] = new int[8][8];
        for(int i = 0; i < 8; i++)
        {
            for(int j = 0; j < 8; j++)
            {
                ai1[i][j] = ai[i][j];
            }

        }

        return ai1;
    }



	public String getName() {
		// TODO Auto-generated method stub
		return "name";
	}


}
