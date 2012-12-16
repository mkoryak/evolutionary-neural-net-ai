/*
 * Ari Packer
 */

/*
 * A flexible implementation of a priority queue which allows for all the actions of ArrayList.
 * Both dequeue and enqueue occur in logarithmic time, which was the main reason for implementing this.
 */

package evolutionary_framework;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;

public class PrioQueue extends ArrayList implements Serializable {

	private Comparator comparator;
	private int generation;
	
	public PrioQueue(Comparator comp) 
	{
		super();
		comparator = comp;
	}
	
	public int get_gen()
	{
		return(this.generation);
	}
	
	public void set_gen(int gen)
	{
		this.generation = gen;
	}
	
	public PrioQueue(Comparator comp, int initialCapacity) 		
	{
		super(initialCapacity);
		comparator = comp;
	}
	
	/*
	 * Queue method with approximately logarithmic complexity.
	 */		
	public void queue(Object newObject)
	{
		int size = this.size();
		int cwss = size/2;	//current working set size
		int current_index = size/2;	//current index that we're working with
		int comparison = 0;
		
		//I combine two if statements into one because their conditions are rare. This saves one comparison almost 100% of the time.
		//This case is extremely rare (queue size <= 1)
		if(size <= 1)
		{
			if(size == 0)
				this.add(newObject);
			else //size == 1
			{
				if(comparator.compare(newObject, this.get(0)) < 0)
					this.add(0, newObject);
				else
					this.add(newObject);
			}
			return;
		}
		
		int remainderTracker = size%2;
		
		while( cwss > 0 )
		{
			if((comparison = comparator.compare(newObject, this.get(current_index))) == 0)
			{
				this.add(current_index, newObject);
				return;
			}

			remainderTracker += cwss % 2;
			cwss /= 2;
			
			if(remainderTracker == 2)
			{
				cwss++;
				remainderTracker=0;
			}
			
			if(comparison < 0)	//newPuzzle < item at current_index
				current_index -= cwss;
			else	//newPuzzle > item at current index
				current_index += cwss;
		}
		
		if(cwss == 0)
		{
			comparison = comparator.compare(newObject, this.get(current_index));
			
			if(comparison == 0)
			{
			 	this.add(current_index, newObject);
			 	return;
			}
			
			int comparisonPrev, comparisonNext;
			
			if(current_index == size-1) //current index == last element
			{
				if(comparison < 0) //newPuzzle < item at current_index
				{
					comparisonPrev = comparator.compare(newObject, this.get(current_index-1));	
					
					if(comparisonPrev < 0)
						this.add(current_index-1, newObject);
					else
						this.add(current_index, newObject);
				}
				else
					this.add(newObject);
			}
			else if(current_index == 0)	//current index == first element
			{
				if(comparison > 0) //new puzzle > item at current_index
				{
					comparisonNext = comparator.compare(newObject, this.get(current_index+1));
					
					if(comparisonNext > 0)
						this.add(current_index+2, newObject);
					else
						this.add(current_index+1, newObject);
				}
				else
					this.add(0, newObject);
			}
			else	//current index != first or last element
			{	
				if(comparison < 0)	//new item < item at current index
				{		
					comparisonPrev = comparator.compare(newObject, this.get(current_index-1));
					
					if(comparisonPrev < 0)
						this.add(current_index-1, newObject);
					else
						this.add(current_index, newObject);					
				}
				else //new item > item @ current index
				{
					comparisonNext = comparator.compare(newObject, this.get(current_index+1));
		
					if(comparisonNext > 0) //new item > item @ curr index +1
					{
						this.add(current_index+2, newObject);
					}
					else
						this.add(current_index+1, newObject);
				}
			}
		}
		else
			add(current_index, newObject);
	}
	
	
	
	/*
	 * Primitive enqueue function with o( n ) complexity. Unrealistic for large queues. 
	 */
	public void enqueue(Object newObject)
	{
		for(int i=0; i < this.size(); i++)
		{
			if(comparator.compare(newObject, this.get(i)) < 0)
			{
				this.add(i, newObject);
				return;
			}
		}
		this.add(newObject);		
	}
	
	public boolean correct()
	{
		for(int i=0; i < this.size()-1; i++)
		{
			if(comparator.compare(this.get(i), this.get(i+1)) > 0)
					return(false);					
		}
		return(true);
	}

	public Object dequeue()
	{
		if(!this.isEmpty())
		{
			Object dequeued = this.get(0);
			this.remove(0);
			return(dequeued);
		}
		else
			return(null);
	}	
	
	public Object clone()
	{
		PrioQueue clone = new PrioQueue(comparator, this.size());
		
		for(int i=0; i < this.size(); i++)
			clone.add(this.get(i));
		
		return(clone);
	}

}