package evolutionary_framework;

import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

import evolutionary_nn.CheckerMatchMaker;
import evolutionary_nn.Main;
import evolutionary_nn.NeuralNetPair;

public class Evol_Algorithm 
{
	protected PrioQueue population;					//population of Evol_Objects
	protected Evol_Compare comparator;				//comparator for Evol_Objects
	protected int pop_size;							//size of population
	protected Evol_Object root;						//root element
	protected int max_generations;					//number of generations to stop at
	protected int stop_value;						//if fitness == to this value, stop. -1 disables this option.
	protected int num_elitist;						//number of elitist elements
	protected int generation;						//number of generations the search took
	protected boolean recompute_elitists;			//recompute the fitness of the elitist elements each generation? 
	protected int max_solutions;					//stop value; pop size * num generations
	protected boolean max_solut;					//enable another stopping condition: max number of solutions to view (pop size * generations)
	protected int selection_technique;				//1 == roulette selection, 2 == tournament selection
	protected boolean enable_print_population;		//print the current population's fitness every generation 
	protected int percent_crossovr;					//percent of children born through crossover (the rest are born through mutation)
	protected boolean crossover_two_child;			//if this is true, then every pair of parents produce two offspring; if false, new parents are found for each child produced
	protected boolean do_tournament;				//do a tournament to compute fitness?
	protected int truct_select_next;				//next to be chosen in uniform truncation selection
	protected int tournament_competition_number;	//number of times each element faces a random element to compute fitness
	
	public static boolean mimicFogel = true;
	/////////////////////////////////////////////////////////////////////////////////////////
//constructors
/////////////////////////////////////////////////////////////////////////////////////////	
	
	/*
	 * Default constructor.
	 */
	public Evol_Algorithm(Evol_Object rt)
	{
		this.comparator = new Evol_Compare();		
		this.population = new PrioQueue(comparator, pop_size);
		this.pop_size = 20;
		this.max_generations = 100;
		this.stop_value = -1;
		this.num_elitist = 1;
		this.root = rt;
		this.recompute_elitists = false;
		this.max_solut = false;
		this.enable_print_population = false;
		this.percent_crossovr = 0;
		this.crossover_two_child = false;
		this.do_tournament=false;
		this.truct_select_next=0;
		this.tournament_competition_number=5;
	}
	public Evol_Algorithm(PrioQueue p)
	{
		this.population = (PrioQueue)p.clone();
		this.pop_size = p.size();
		this.root = (Evol_Object)((Evol_Object)p.get(0)).clone();
	}
	
	/*
	 * Constructor.
	 */
	public Evol_Algorithm(Evol_Object rt, int population_size, int stop_val, int max_gen, int num_elite)
	{
		this.comparator = new Evol_Compare();		
		this.population = new PrioQueue(comparator, pop_size);
		this.pop_size = population_size;
		this.max_generations = max_gen;
		this.stop_value = stop_val;
		this.num_elitist = num_elite;
		this.root = rt;
		this.recompute_elitists = false;
		this.max_solut = false;
		this.selection_technique = 1;	//roulette
		this.enable_print_population = false;
		this.percent_crossovr = 0;
		this.crossover_two_child = false;
		this.do_tournament = false;
		this.truct_select_next=0;
		this.tournament_competition_number=5;
	}
	

/////////////////////////////////////////////////////////////////////////////////////////
//public methods
/////////////////////////////////////////////////////////////////////////////////////////
	
	public void enable_print_population()
	{
		enable_print_population = true;
	}
	
	public void print_population()
	{
		int sum=0;
		for(int i=0; i < this.pop_size; i++)
		{
			sum += ((Evol_Object)this.population.get(i)).get_fitness();
			System.out.print( ((Evol_Object)this.population.get(i)).get_fitness() + ", ");
		}
		System.out.println();
		System.out.println("total fitness = " + sum);System.out.println();System.out.println();
	}
	
	public int get_stop_value()
	{
		return(this.stop_value);
	}

	public int generations()
	{
		return(generation);
	}
	
	public int popsize()
	{
		return(this.pop_size);
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////
	//public methods
	/////////////////////////////////////////////////////////////////////////////////////////

	
	/*Continue a search where you left off*/
	public PrioQueue search(PrioQueue pop, int gens)
	{
		int prev_gen = pop.get_gen();
		
		//pick up where we left off
		this.population = pop;
		this.max_generations += gens;
	
		
		Evol_Object newObj;
		boolean done = false;
		int to_mutate=0;

		do
		{
			generation++;

			this.truct_select_next=0;
			
			//Set up the next generation
			PrioQueue newPopulation;// = new PrioQueue(this.comparator, this.pop_size);
			
			//save elitist elements
			newPopulation = save_elitists();

			//Generate the next generation
			for(int i=num_elitist; i < this.pop_size; i++)
			{
					to_mutate = this.select();
					if( is_crossover() )
					{
						int to_mutate2 = this.select();
						
						//This creates two children for each set of parents
						if( crossover_two_child )
						{
							i++;
							if(i >= this.pop_size) //uneven population, can not produce two children here
								break;
							newObj = ( ((Evol_Object)population.get(to_mutate)).crossover((Evol_Object)population.get(to_mutate2)) );
							newObj.compute_fitness();
							newPopulation.queue( newObj );
							newObj = ( ((Evol_Object)population.get(to_mutate)).crossover((Evol_Object)population.get(to_mutate2)) );
						}
						//This finds a new set of parents for each child
						else
							newObj = ( ((Evol_Object)population.get(to_mutate)).crossover((Evol_Object)population.get(to_mutate2)) );
					}
					else
						newObj = ((Evol_Object)population.get(to_mutate)).mutate();
					newObj.compute_fitness();
					newPopulation.queue( newObj );
			}

			//make the current population the new population
			this.population = newPopulation;

			if(this.do_tournament)
				this.tournament();
			
			//stopping condition 1 (number of generations)
			if(generation >= this.max_generations)
				done = true;
			
			//stopping condition 2 (stop_value)
			if(root.is_minimization() && ((this.stop_value != -1) && (((Evol_Object)population.get(0)).get_fitness() == stop_value)))
				done = true;
			else if(!root.is_minimization() && ((this.stop_value != -1) && (((Evol_Object)population.get(this.pop_size-1)).get_fitness() == stop_value)))
				done = true;
			
			//stopping condition 3 (num solutions viewed)
			if( this.max_solut && ((this.pop_size*this.generation) >= this.max_solutions) )
				done = true;

			if(this.enable_print_population)
			{
				System.out.println("generation " + prev_gen);
				print_population();
			}
			
		}	while( !done );
		
		//search complete, return current population
		
		population.set_gen(prev_gen);
		return(this.population);
	}

	public PrioQueue search()
	{		
		Evol_Object newObj;
		boolean done = false;
		int to_mutate=0;

		this.initialize();
		generation=0;
	
		do
		{
			generation++;

			this.truct_select_next=0;
			
			//Set up the next generation
			PrioQueue newPopulation;// = new PrioQueue(this.comparator, this.pop_size);
			
			if( this.do_tournament )
				tournament();
			
			if(this.enable_print_population)
			{
				System.out.println("generation " + generation);
				print_population();
			}
			
			//save elitist elements
			newPopulation = save_elitists();
			
			//Generate the next generation
			for(int i=num_elitist; i < this.pop_size; i++)
			{
					to_mutate = this.select();
					
					System.out.println("parent is " + to_mutate);
					
					if( is_crossover() )
					{
						int to_mutate2 = this.select();
						
						//This creates two children for each set of parents
						if( crossover_two_child )
						{
							i++;
							if(i >= this.pop_size) //uneven population, can not produce two children here
								break;
							newObj = ( ((Evol_Object)population.get(to_mutate)).crossover((Evol_Object)population.get(to_mutate2)) );
							newObj.compute_fitness();
							newPopulation.queue( newObj );
							newObj = ( ((Evol_Object)population.get(to_mutate)).crossover((Evol_Object)population.get(to_mutate2)) );
						}
						//This finds a new set of parents for each child
						else
							newObj = ( ((Evol_Object)population.get(to_mutate)).crossover((Evol_Object)population.get(to_mutate2)) );
					}
					else
						newObj = ((Evol_Object)population.get(to_mutate)).mutate();
					newObj.compute_fitness();
					
					if(this.do_tournament)
						newObj.set_fitness(0);
					
					newPopulation.queue( newObj );
			}

			//make the current population the new population
			this.population = newPopulation;

			//stopping condition 1 (number of generations)
			if(generation >= this.max_generations)
				done = true;
			
			//stopping condition 2 (stop_value)
			if(root.is_minimization() && ((this.stop_value != -1) && (((Evol_Object)population.get(0)).get_fitness() == stop_value)))
				done = true;
			else if(!root.is_minimization() && ((this.stop_value != -1) && (((Evol_Object)population.get(this.pop_size-1)).get_fitness() == stop_value)))
				done = true;
			
			//stopping condition 3 (num solutions viewed)
			if( this.max_solut && ((this.pop_size*this.generation) >= this.max_solutions) )
				done = true;
			
		}	while( !done );
		
		population.set_gen(this.max_generations);
		
	 System.out.println("hmm");
		
		//search complete, return current population
		return(this.population);
	}


	public void recompute_elitists()
	{
		this.recompute_elitists = true;
	}
	
	public void max_solutions(int max_num_solutions)
	{
		this.max_solut = true;
		this.max_solutions = max_num_solutions;
	}
	
	public PrioQueue get_population()
	{
		return(this.population);
	}
	
	public void set_selection_type(int selection_type)
	{
		this.selection_technique = selection_type;
	}
	
	public void enable_crossover(int percent_crossover)
	{
		this.percent_crossovr = percent_crossover;
	}
	
	public void crossover_two_children()
	{
		crossover_two_child = true;
	}
	
	public boolean is_crossover()
	{
		int rand = (int) (100 * Math.random());
		
		if(rand < this.percent_crossovr)
			return(true);
		return(false);
	}
	

/////////////////////////////////////////////////////////////////////////////////////////
//selection techniques
/////////////////////////////////////////////////////////////////////////////////////////
	
	private PrioQueue save_elitists()
	{
		Evol_Object newObj;
		PrioQueue newPopulation = new PrioQueue(this.comparator, this.pop_size);
		
		//Save the elitist elements
		for(int i=0; i < this.num_elitist; i++)
		{
			if(root.is_minimization())
			{
				newObj = ((Evol_Object)this.population.get(i));
				
				newObj.set_fitness(1);
				
				if(this.recompute_elitists)
					newObj.compute_fitness();
				newPopulation.queue(newObj);
			}
			else if(this.pop_size-1-i >= 0)
			{
				newObj = ((Evol_Object)this.population.get(this.pop_size-1-i));
				
				newObj.set_fitness(1);
				
				if(this.recompute_elitists)
					newObj.compute_fitness();
				
				newPopulation.queue(newObj);
			}
		}
		return(newPopulation);
	}


	/*
	 * Performs roulette selection on the population.
	 */
	private int roulette()
	{
		int sum = this.sum_fitness();
		int rand = (int)(sum * Math.random());
		int newsum=0;
		
		for(int i=0; i < this.pop_size; i++)
		{
			if(root.is_minimization())
				newsum += (((Evol_Object)this.population.get(this.pop_size-1)).get_fitness()) - (((Evol_Object)this.population.get(i)).get_fitness());
			else
				newsum += (((Evol_Object)this.population.get(i)).get_fitness()) - (((Evol_Object)this.population.get(0)).get_fitness());

			if(newsum >= rand)
				return( i );
		}
		
		return(pop_size-1);
	}
	
	
	public PrioQueue super_tourney(PrioQueue pop)
	{
		int gen = pop.get_gen();
		//PrioQueue new_pop = this.population;
		PrioQueue new_pop = new PrioQueue(comparator, pop_size);
		
		int opponent=-1; 
		int fitness[] = new int[this.pop_size];
		int winner;
		
		Arrays.fill(fitness,0);
		
		for(int i=0; i < this.pop_size; i++)
		{
			
			for(int j=0; j < this.pop_size; j++)
			{	
				
				if(j == i)
					break;
				
				opponent = j;
				
				winner = ((Evol_Object)pop.get(i)).head_to_head((Evol_Object)pop.get(opponent));
				
				switch(winner)
				{
					case(0):	//tie
						//fitness[i]--;
						//fitness[opponent]--;
						break;
						
					case(1):	//i wins, opponent loses
						fitness[i]++;
						fitness[opponent]--;
						break;
						
					case(2):	//i loses, opponent wins
						fitness[i]--;
						fitness[opponent]++;
						break;
				}
			}
		}
	
		for(int i=0; i < this.pop_size; i++)
		{
			Evol_Object new_obj = (Evol_Object)pop.get(i);
			new_obj.set_fitness(fitness[i]);
			new_pop.queue(new_obj);
		}
		this.population=new_pop;
		
		new_pop.set_gen(gen);
		
		return(new_pop);	
	}
	/*
	 * Performs tournament selection on the population and returns the winner.
	 * To calculate the fitness of each element, 5 opponents are chosen randomly (with replacement)
	 * and compete using Evol_Object's head_to_head class. A loss counts as -2, a tie counts as 0,
	 * and a win counts as +1.
	 */
	private void tournament()
	{
		//PrioQueue new_pop = this.population;
		PrioQueue new_pop = new PrioQueue(comparator, pop_size);
		
		int opponent=-1; 
		int fitness[] = new int[this.pop_size];
		int winner;
		
		Arrays.fill(fitness,0);
		
		for(int i=0; i < this.pop_size; i++)
		{
			
			for(int j=0; j < this.tournament_competition_number; j++)
			{
				//find an opponent
				if(mimicFogel){
					do
					{
						opponent = (int) (Math.random()*(this.pop_size));
					} while(opponent == i);
					
					winner = ((Evol_Object)this.population.get(i)).head_to_head((Evol_Object)this.population.get(opponent));
					
					switch(winner)
					{
						case(0):	//tie
							//fitness[i]--;
							//fitness[opponent]--;
							break;
							
						case(1):	//i wins, opponent loses
							fitness[i]++;
							//fitness[opponent]--;
							break;
							
						case(2):	//i loses, opponent wins
							fitness[i]-=2;
							//fitness[opponent]++;
							break;
					} 
				} else {
					int who;
					NeuralNetPair p = (NeuralNetPair)this.population.get(i);
					CheckerMatchMaker m = new CheckerMatchMaker(p);
					who = m.startMatch();
					if(who != 0) { 
						fitness[i] += (m.getWhiteScore() - m.getBlackScore());
					}else {
						int score = (m.getWhiteScore() - m.getBlackScore());
						score /= 2;
						fitness[i] += score;
					}
					if(fitness[i] > 0){
						PrioQueue best = new PrioQueue(comparator, 1);
						Evol_Object new_obj = (Evol_Object)this.population.get(i);
						new_obj.set_fitness(fitness[i]);
						best.queue(new_obj);
						Random rnd = new Random();
						try {
							Main.save(best, "nn_save_won_by"+fitness[i]+"_rand_"+rnd.nextInt(10)+"_.ser");
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
//					System.out.print(".");
//					m = new CheckerMatchMaker(p, 4);
//					who = m.startMatch();
//					if(who != 0) { 
//						fitness[i] += (m.getWhiteScore() - m.getBlackScore())*2;
//					}else {
//						int score = (m.getWhiteScore() - m.getBlackScore());
//						score /= 2;
//						fitness[i] += score*2;
//					}
//					System.out.print(".");
//					m = new CheckerMatchMaker(p, 6);
//					who = m.startMatch();
//					if(who != 0) { 
//						fitness[i] += (m.getWhiteScore() - m.getBlackScore())*3;
//					}else {
//						int score = (m.getWhiteScore() - m.getBlackScore());
//						score /= 2;
//						fitness[i] += score;
//					}
//					if(fitness[i] > 0){
//						m = new CheckerMatchMaker(p, 8);
//						who = m.startMatch();
//						if(who != 0) { 
//							fitness[i] += (m.getWhiteScore() - m.getBlackScore())*4;
//						}else {
//							int score = (m.getWhiteScore() - m.getBlackScore());
//							score /= 2;
//							fitness[i] += score;
//						}
//					}
//					System.out.print(".");
				}
			}
		}
		
		for(int i=0; i < this.pop_size; i++)
		{
			Evol_Object new_obj = (Evol_Object)this.population.get(i);
			new_obj.set_fitness(fitness[i]);
			new_pop.queue(new_obj);
		}
		this.population = new_pop;
	}
	
	private int most_fit()
	{
		if( ((Evol_Object)(this.population.get(0))).is_minimization() )
			return( 0 );
		else
			return( this.pop_size-1 );
	}

	private int uniform_truncation()
	{
		//this selection technique is invalid for odd population size -- fixing it
		if(this.pop_size %1 != 0)
			this.pop_size++;
		
		this.truct_select_next++;
		
		if(this.root.is_minimization())
			return(this.truct_select_next-1);
		else
			return(this.pop_size-1-(this.truct_select_next-1));
	}
	
	private int select()
	{
		if(selection_technique == 1)	//roulette
			return( roulette() );
		else if(selection_technique == 2)	//only fittest element
			return( most_fit() );
		else if(selection_technique == 3)	//50% truncation uniform selection
			return( uniform_truncation() );
		return( roulette() );
	}
	
	public void do_tournament(int tournament_redundency)
	{
		this.tournament_competition_number = tournament_redundency;
		do_tournament = true;
	}
	
	/*
	 * Returns the sum of fitness of each element in population. Used with roulette selection.
	 */
	private int sum_fitness()
	{
		int sum=0;
		
		for(int i=0; i < this.pop_size; i++)
		{
			if(root.is_minimization())
				sum += (((Evol_Object)this.population.get(this.pop_size-1)).get_fitness()) - (((Evol_Object)this.population.get(i)).get_fitness());
			else
				sum += (((Evol_Object)this.population.get(i)).get_fitness()) - (((Evol_Object)this.population.get(0)).get_fitness());
		}
		
		return(sum);
	}


/////////////////////////////////////////////////////////////////////////////////////////
//other private methods
/////////////////////////////////////////////////////////////////////////////////////////
	
	/*
	 * Creates Population0 (inital population)
	 */
	private void initialize()
	{
		Evol_Object newObj;

		for(int i=0; i < this.pop_size; i++)
		{
			newObj = root.randomize();
			newObj.compute_fitness();
			this.population.queue(newObj);
		}
	}
}