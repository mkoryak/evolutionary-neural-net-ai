package evolutionary_nn;

import java.util.ArrayList;
import java.util.Arrays;

public class Util 
{
	private Util() 
	{
	}
	
	/**
	 * 
	 * @param in_board - 8x8 int array of board
	 * @param weights - size 854 double array
	 * @param king_value - double value of king
	 * @return
	 */
	public static double[] produce_input(byte in_board[][], double weights[], double king_value, int myColor)
	{
		double board[][] = new double[8][8];
		int weight=0;
		double piece_diff=0;
		
		/* Get piece differential and produce new board with correct king values. */
		for(int i=0; i < 8; i++)
		{
			for(int j=0; j < 8; j++)
			{	
				if(Math.abs(in_board[i][j]) == 2)
				{
					if(in_board[i][j] < 0)
						board[i][j] = (-1)*king_value;
					else
						board[i][j] = king_value;					
				}
				else
					board[i][j] = in_board[i][j];
				
				piece_diff += board[i][j];
			}
		}
		
		double[] output = new double[92]; //extra one for peice difference
		double[] temp;
		ArrayList decomposed = decompose(board);
		Arrays.fill(output, 0);
		
		//print_subboards(decomposed);
		
		/* Perform layer 1 NN stuff. */
		for(int i=0; i < 91; i++)
		{
			temp = (double[])decomposed.get(i);
			
			for(int j=0; j < temp.length; j++)
			{
				output[i] += (weights[weight] * temp[j]);
				weight++;
			}
			
			output[i] = JavaMLP.tanh(output[i]);
		}
		output[91] = piece_diff; //(mine - his);
		
		/* Output is hidden layer 1.*/
		return(output);
	}
	
	/*
	 * For all [1] 8x8 matrices, size = 8.
	 * ...
	 * For all [36] 3x3 matrices, size = 3.
	 */
	private static ArrayList decompose(double board[][])
	{
		ArrayList vector = new ArrayList();
		
		int index;
		int start_row = 0;
		int start_col = 0;
		int vector_size;
		
		for(int size=3; size <= 8; size++)
		{
			for(int n = 0; n < (8-(size-1))*(8-(size-1)); n++)
			{
				if( (start_row%2 != start_col%2)&&(size%2 == 1))
					vector_size = (size*size)/2+1;
				else
					vector_size = (size*size)/2;
			
				double subboard[] = new double[vector_size];
				index = 0;
	
				for(int i = start_row; i < size+start_row; i++)
				{
					for(int j = start_col; j < size+start_col; j++)
					{
						if( (i%2 != 0 && j%2 == 0) || (i%2 == 0 && j%2 != 0) )
						{	subboard[index] = board[i][j];
							index++;
						}
					}
				}
		
				start_col ++;
			
				if(start_col == 8-size+1)
				{
					start_row ++;
					start_col = 0;
				}
				
				vector.add(subboard);
			}
			start_row = 0;
			start_col = 0;
		}
		
		//print_subboards(vector);
		
		return(vector);
	}

	  public static int numPieces(byte[][] board, int color) {
		int num = 0;
		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				if ((board[x][y] == color) || board[x][y] == color * 2) {
					num++;
				}
			}
		}
		return num;
	}
	  
	  private static void print_subboards(ArrayList subboards)
	  {
		  for(int i=0; i < subboards.size(); i++)
		  {
			  double subboard[] = (double[])subboards.get(i);
			  
			  for(int j=0; j < subboard.length; j++)
				  System.out.print(subboard[j] + " ");
				
			  System.out.println();
		  }

		  System.out.println();
		  System.out.println();
		  System.out.println();
	  }
}
