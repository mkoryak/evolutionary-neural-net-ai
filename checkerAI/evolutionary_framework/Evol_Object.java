/*
 * Ari Packer
 */

/*
 * ALL methods MUST be overwritten.
 */

package evolutionary_framework;

public interface Evol_Object 
{

	/*
	 * Calculate and store the fitness value for this element.
	 */
	public abstract void compute_fitness();
	
	
	
	/*
	 * 1. Create a clone of this.
	 * 2. Perform a mutation on the child (clone).
	 * 3. Return the newly mutated child.
	 */
	public abstract Evol_Object mutate();	
	
	
	
	/*
	 * 1. Create a clone of this.
	 * 2. Randomize the clone.
	 * 3. Return the clone.
	 */
	public abstract Evol_Object randomize();
	
	
	/*
	 *  Rewrite this function to create a new cloned evol_object.
	 *  Use this function in mutate().
	 */
	public abstract Object clone();
	
	

	/*
	 * Returns the value of the fittest element in the population.
	 */
	public abstract int get_fitness();
	
	
	
	/*
 	 * Set the fitness for this object. Used with tournament selection.
	 */
	public abstract void set_fitness(int fitness);
	
	
	/*
	 * True if this is a minimization problem, false otherwise.
	 */
	public abstract boolean is_minimization();	
	
	
	/*
	 * This method initializes a problem representation and can be used on your initially
	 * or not used at all. An example would be initializing the distance
	 * matrix for a TSP or the starting puzzle for an N-puzzle.
	 */
	public abstract void initialize_representation();
	
	
	
	/*
	 * This method may be implemented with an empty body ( {} ) if crossover is not required.
	 * 1. Create a new (child) Evol_Object that is a clone of this.
	 * 2. Make the child the result of crossover between parent2 and this.
	 * 3. Return the child
	 */
	public abstract Evol_Object crossover(Evol_Object parent2);
	
	
	/*
	 * Used with tournament selection. Contender goes head to head with this.
	 * Return the winner. (contender or this)
	 * If tournament selection is not required, may be implemented as a semi-empty
	 * method such as { return(this); }
	 * 
	 * 1 = this won
	 * 2 = contender won
	 * 0 = tie
	 */
	public abstract int head_to_head(Evol_Object contender);
}