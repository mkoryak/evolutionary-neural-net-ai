package evolutionary_nn;

import evolutionary_framework.Evol_Object;
import evolutionary_framework.PrioQueue;
import checkers_framework.Checker;

import java.io.Serializable;
import java.util.Random;

public  class NeuralNetPair implements Evol_Object, Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8540111986768308443L;

	private static int END_GAME_PEICE_COUNT = 0;
	
	private int fitness=0;

	private JavaMLP nn1;
	private JavaMLP nn2;


	private double[] nn1_input_weights = new double[854];
	private double[] nn2_input_weights = new double[854];
	
	private double[][] nn1_layer1 = new double[91][40];
	private double[][] nn2_layer1 = new double[91][40];
	
	private double[][] nn1_layer2 = new double[40][10];
	private double[][] nn2_layer2 = new double[40][10];
	
	private double[][] nn1_layer3 = new double[10][1];
	private double[][] nn2_layer3 = new double[10][1];
	
	private double nn1_king_value = 2;
	private double nn2_king_value = 2;
	
	//reusable input arrays
	private double[] nn1_input = new double[91];
	private double[] nn2_input = new double[91];

	private double[] nn1_biases_layer1 = new double[91];
	private double[] nn1_biases_layer2 = new double[40];
	private double[] nn1_biases_layer3 = new double[10];
	private double[] nn1_biases_layer4 = new double[1];
	
	private double[] nn2_biases_layer1 = new double[91];
	private double[] nn2_biases_layer2 = new double[40];
	private double[] nn2_biases_layer3 = new double[10];
	private double[] nn2_biases_layer4 = new double[1];

	private SAP self_adaptive_params;
	
	public NeuralNetPair(JavaMLP nn1, JavaMLP nn2) 
	{
		super();
		this.nn1 = nn1;
		this.nn2 = nn2;
		this.set_layers();
		self_adaptive_params = new SAP();
	}

	public void compute_fitness() {}
	public void initialize_representation() {}
	public int get_fitness() {return(fitness);}
	public boolean is_minimization() {return false;}
	public void set_fitness(int new_fitness) {this.fitness = new_fitness;}
	
	/*
	 * This method is used with mutate() to determine the amount to adjust the weight or bias.
	 */
	private double rand_mutate()
	{
		//double mutation;
		//double self_adaptive_parameter_replacement = .05;
		//mutation = self_adaptive_parameter_replacement * gaus;
		
		double gaus = new Random().nextGaussian();
		return(gaus);
	}
	public static NeuralNetPair getBestMember(PrioQueue p) { 
		return(NeuralNetPair)( p.get(p.size()-1) ); 
	}
	
	
	/*
	 * Creates a new NeuralNetPair object which is a clone of this.
	 * All weights and biases are adjusted, and the child (mutated) copy is returned.
	 */
	public Evol_Object mutate() 
	{
		NeuralNetPair newNNP = (NeuralNetPair)this.clone();
		
		newNNP.self_adaptive_params.mutate();
		
		
		double[] nn1_input_weights = newNNP.getNn1_input_weights();
		for(int i=0; i < 854; i++)
			nn1_input_weights[i] += rand_mutate()*newNNP.self_adaptive_params.nn1_input_weights(i);		
		newNNP.setNn1_input_weights(nn1_input_weights);
		

		double[][] nn1_layer1 = newNNP.getNn1_layer1();
		for(int i=0; i < 91; i++)
			for(int j=0; j < 40; j++)
				nn1_layer1[i][j] += rand_mutate()*newNNP.self_adaptive_params.nn1_layer1(i,j);
		newNNP.setNn1_layer1(nn1_layer1);
		

		double[][] nn1_layer2 = newNNP.getNn1_layer2();
		for(int i=0; i < 40; i++)
			for(int j=0; j < 10; j++)
				nn1_layer2[i][j] += rand_mutate()*newNNP.self_adaptive_params.nn1_layer2(i,j);
		newNNP.setNn1_layer2(nn1_layer2);


		double[][] nn1_layer3 = newNNP.getNn1_layer3();
		for(int i=0; i < 10; i++)
			nn1_layer3[i][0] += rand_mutate()*newNNP.self_adaptive_params.nn1_layer3(i);
		newNNP.setNn1_layer3(nn1_layer3);


		double[] nn1_biases_layer1 = newNNP.get_nn1_biases_layer1();
		for(int i=0; i < 91; i++)
			nn1_biases_layer1[i] += rand_mutate()*newNNP.self_adaptive_params.nn1_biases_layer1(i);
		newNNP.set_nn1_biases_layer1(nn1_biases_layer1);


		double[] nn1_biases_layer2 = newNNP.get_nn1_biases_layer2();		
		for(int i=0; i < 40; i++)
			nn1_biases_layer2[i] += rand_mutate()*newNNP.self_adaptive_params.nn1_biases_layer2(i);	
		newNNP.set_nn1_biases_layer2(nn1_biases_layer2);


		double[] nn1_biases_layer3 = newNNP.get_nn1_biases_layer3();
		for(int i=0; i < 10; i++)
			nn1_biases_layer3[i] += rand_mutate()*newNNP.self_adaptive_params.nn1_biases_layer3(i);
		newNNP.set_nn1_biases_layer3(nn1_biases_layer3);


		double[] nn1_biases_layer4 = newNNP.get_nn1_biases_layer4();	
		nn1_biases_layer4[0] += rand_mutate()*newNNP.self_adaptive_params.nn1_biases_layer4(0);
		newNNP.set_nn1_biases_layer4(nn1_biases_layer4);


		double[] nn2_input_weights = newNNP.getNn2_input_weights();
		for(int i=0; i < 854; i++)
			nn2_input_weights[i] += rand_mutate()*newNNP.self_adaptive_params.nn2_input_weight(i);
		newNNP.setNn2_input_weights(nn2_input_weights);


		double[][] nn2_layer1 = newNNP.getNn2_layer1();		
		for(int i=0; i < 91; i++)
			for(int j=0; j < 40; j++)
				nn2_layer1[i][j] += rand_mutate()*newNNP.self_adaptive_params.nn2_layer1(i,j);
		newNNP.setNn2_layer1(nn2_layer1);


		double[][] nn2_layer2 = newNNP.getNn2_layer2();
		for(int i=0; i < 40; i++)
			for(int j=0; j < 10; j++)
				nn2_layer2[i][j] += rand_mutate()*newNNP.self_adaptive_params.nn2_layer2(i,j);
		newNNP.setNn2_layer2(nn2_layer2);


		double[][] nn2_layer3 = newNNP.getNn2_layer3();
		for(int i=0; i < 10; i++)
			nn2_layer3[i][0] += rand_mutate()*newNNP.self_adaptive_params.nn2_layer3(i);
		newNNP.setNn2_layer3(nn2_layer3);


		double[] nn2_biases_layer1 = newNNP.get_nn2_biases_layer1();
		for(int i=0; i < 91; i++)
			nn2_biases_layer1[i] += rand_mutate()*newNNP.self_adaptive_params.nn2_biases_layer1(i);
		newNNP.set_nn2_biases_layer1(nn2_biases_layer1);


		double[] nn2_biases_layer2 = newNNP.get_nn2_biases_layer2();
		for(int i=0; i < 40; i++)
			nn2_biases_layer2[i] += rand_mutate()*newNNP.self_adaptive_params.nn2_biases_layer2(i);	
		newNNP.set_nn2_biases_layer2(nn2_biases_layer2);


		double[] nn2_biases_layer3 = newNNP.get_nn2_biases_layer3();
		for(int i=0; i < 10; i++)
			nn2_biases_layer3[i] += rand_mutate()*newNNP.self_adaptive_params.nn2_biases_layer3(i);
		newNNP.set_nn2_biases_layer3(nn2_biases_layer3);


		double[] nn2_biases_layer4 = newNNP.get_nn2_biases_layer4();	
		nn2_biases_layer4[0] += rand_mutate()*newNNP.self_adaptive_params.nn2_biases_layer4(0);
		newNNP.set_nn2_biases_layer4(nn2_biases_layer4);

	
		double king = this.nn1_king_value + Math.random()/5 - .1;
		if(king < 1) king = 1;
		else if(king > 3) king = 3;
		newNNP.set_nn1_king_value(king);

		king = this.nn2_king_value + Math.random()/5 - .1;
		if(king < 1) king = 1;
		else if(king > 3) king = 3;
		newNNP.set_nn2_king_value(king);
				
		newNNP.set_layers();
		
		return(newNNP);
	}
	
	public void randomize_NN2()
	{
		double[] nn2_input_weights_2 = new double[854];
		for(int i=0; i < 854; i++) nn2_input_weights_2[i] = (Math.random()*(.4)) - .2;
		
		double[][] nn2_layer1_2 = new double[91][40];
		for(int i=0; i < 91; i++)
			for(int j=0; j < 40; j++)
				nn2_layer1_2[i][j] = (Math.random()*(.4)) - .2;
		
		double[][] nn2_layer2_2 = new double[40][10];
		for(int i=0; i < 40; i++)
			for(int j=0; j < 10; j++)
				nn2_layer2_2[i][j] = (Math.random()*(.4)) - .2;
		
		double[][] nn2_layer3_2 = new double[10][1];
		for(int i=0; i < 10; i++)
			nn2_layer3_2[i][0] = (Math.random()*(.4)) - .2;

		double[] nn2_biases_layer1A = new double[91];
		for(int i=0; i < 91; i++)
			nn2_biases_layer1A[i] = (Math.random()*(.4)) - .2;

		double[] nn2_biases_layer2A = new double[40];
		for(int i=0; i < 40; i++)
			nn2_biases_layer2A[i] = (Math.random()*(.4)) - .2;

		double[] nn2_biases_layer3A = new double[10];
		for(int i=0; i < 10; i++)
			nn2_biases_layer3A[i] = (Math.random()*(.4)) - .2;

		double[] nn2_biases_layer4A = new double[1];
		nn2_biases_layer4A[0] = (Math.random()*(.4)) - .2;
	
		double nn2_kv = 2;
		
		this.setNn2_input_weights(nn2_input_weights_2);
		this.setNn2_layer1(nn2_layer1_2);
		this.setNn2_layer2(nn2_layer2_2);
		this.setNn2_layer3(nn2_layer3_2);
		this.set_nn2_biases_layer1(nn2_biases_layer1A);
		this.set_nn2_biases_layer2(nn2_biases_layer2A);
		this.set_nn2_biases_layer3(nn2_biases_layer3A);
		this.set_nn2_biases_layer4(nn2_biases_layer4A);
		this.set_nn2_king_value(nn2_kv);
		this.set_layers();		
	}
	
	public Evol_Object randomize() 
	{
		JavaMLP net1 = new JavaMLP();
		JavaMLP net2 = new JavaMLP();
		NeuralNetPair newNNP = new NeuralNetPair(net1, net2);
		
		newNNP.self_adaptive_params.initialize();
		
		double[] nn1_input_weights_2 = new double[854];
		for(int i=0; i < 854; i++) nn1_input_weights_2[i] = (Math.random()*(.4)) - .2;
		
		double[] nn2_input_weights_2 = new double[854];
		for(int i=0; i < 854; i++) nn2_input_weights_2[i] = (Math.random()*(.4)) - .2;
		
		double[][] nn1_layer1_2 = new double[91][40];
		for(int i=0; i < 91; i++)
			for(int j=0; j < 40; j++)
				nn1_layer1_2[i][j] = (Math.random()*(.4)) - .2;
		
		double[][] nn2_layer1_2 = new double[91][40];
		for(int i=0; i < 91; i++)
			for(int j=0; j < 40; j++)
				nn2_layer1_2[i][j] = (Math.random()*(.4)) - .2;
		
		double[][] nn1_layer2_2 = new double[40][10];
		for(int i=0; i < 40; i++)
			for(int j=0; j < 10; j++)
				nn1_layer2_2[i][j] = (Math.random()*(.4)) - .2;
		
		double[][] nn2_layer2_2 = new double[40][10];
		for(int i=0; i < 40; i++)
			for(int j=0; j < 10; j++)
				nn2_layer2_2[i][j] = (Math.random()*(.4)) - .2;
		
		double[][] nn1_layer3_2 = new double[10][1];
		for(int i=0; i < 10; i++)
			nn1_layer3_2[i][0] = (Math.random()*(.4)) - .2;
			
		double[][] nn2_layer3_2 = new double[10][1];
		for(int i=0; i < 10; i++)
			nn2_layer3_2[i][0] = (Math.random()*(.4)) - .2;

		double[] nn1_biases_layer1A = new double[91];
		for(int i=0; i < 91; i++)
			nn1_biases_layer1A[i] = (Math.random()*(.4)) - .2;

		double[] nn1_biases_layer2A = new double[40];
		for(int i=0; i < 40; i++)
			nn1_biases_layer2A[i] = (Math.random()*(.4)) - .2;

		double[] nn1_biases_layer3A = new double[10];
		for(int i=0; i < 10; i++)
			nn1_biases_layer3A[i] = (Math.random()*(.4)) - .2;

		double[] nn1_biases_layer4A = new double[1];
		nn1_biases_layer4A[0] = (Math.random()*(.4)) - .2;

		double[] nn2_biases_layer1A = new double[91];
		for(int i=0; i < 91; i++)
			nn2_biases_layer1A[i] = (Math.random()*(.4)) - .2;

		double[] nn2_biases_layer2A = new double[40];
		for(int i=0; i < 40; i++)
			nn2_biases_layer2A[i] = (Math.random()*(.4)) - .2;

		double[] nn2_biases_layer3A = new double[10];
		for(int i=0; i < 10; i++)
			nn2_biases_layer3A[i] = (Math.random()*(.4)) - .2;

		double[] nn2_biases_layer4A = new double[1];
		nn2_biases_layer4A[0] = (Math.random()*(.4)) - .2;
	
		double nn1_kv = 2;
		double nn2_kv = 2;
		
		newNNP.setNn1_input_weights(nn1_input_weights_2);
		newNNP.setNn2_input_weights(nn2_input_weights_2);
		newNNP.setNn1_layer1(nn1_layer1_2);
		newNNP.setNn2_layer1(nn2_layer1_2);
		newNNP.setNn1_layer2(nn1_layer2_2);
		newNNP.setNn2_layer2(nn2_layer2_2);
		newNNP.setNn1_layer3(nn1_layer3_2);
		newNNP.setNn2_layer3(nn2_layer3_2);
		newNNP.set_nn1_biases_layer1(nn1_biases_layer1A);
		newNNP.set_nn1_biases_layer2(nn1_biases_layer2A);
		newNNP.set_nn1_biases_layer3(nn1_biases_layer3A);
		newNNP.set_nn1_biases_layer4(nn1_biases_layer4A);
		newNNP.set_nn2_biases_layer1(nn2_biases_layer1A);
		newNNP.set_nn2_biases_layer2(nn2_biases_layer2A);
		newNNP.set_nn2_biases_layer3(nn2_biases_layer3A);
		newNNP.set_nn2_biases_layer4(nn2_biases_layer4A);
		newNNP.set_nn1_king_value(nn1_kv);
		newNNP.set_nn2_king_value(nn2_kv);
		newNNP.set_layers();
		
		return(newNNP);
	}

	
	public Object clone() 
	{
		JavaMLP new_nn1 = new JavaMLP();
		JavaMLP new_nn2 = new JavaMLP();
		NeuralNetPair newNNP = new NeuralNetPair(new_nn1, new_nn2);
		
		newNNP.setNn1_input_weights( (double[])this.nn1_input_weights.clone() );
		newNNP.setNn2_input_weights( (double[])this.nn2_input_weights.clone() );
	
		newNNP.setNn1_layer1( cloneDoubleArray(this.nn1_layer1) );
		newNNP.setNn2_layer1( cloneDoubleArray(this.nn2_layer1) );
		
		newNNP.setNn1_layer2( cloneDoubleArray(this.nn1_layer2) );
		newNNP.setNn2_layer2( cloneDoubleArray(this.nn2_layer2) );
		
		newNNP.setNn1_layer3( cloneDoubleArray(this.nn1_layer3) );
		newNNP.setNn2_layer3( cloneDoubleArray(this.nn2_layer3) );
	
		newNNP.set_nn1_biases_layer1( (double[])this.nn1_biases_layer1.clone() );
		newNNP.set_nn1_biases_layer2( (double[])this.nn1_biases_layer2.clone() );
		newNNP.set_nn1_biases_layer3( (double[])this.nn1_biases_layer3.clone() );
		newNNP.set_nn1_biases_layer4( (double[])this.nn1_biases_layer4.clone() );
		
		newNNP.set_nn2_biases_layer1( (double[])this.nn2_biases_layer1.clone() );
		newNNP.set_nn2_biases_layer2( (double[])this.nn2_biases_layer2.clone() );
		newNNP.set_nn2_biases_layer3( (double[])this.nn2_biases_layer3.clone() );
		newNNP.set_nn2_biases_layer4( (double[])this.nn2_biases_layer4.clone() );
		
		newNNP.set_nn1_king_value( this.nn1_king_value );
		newNNP.set_nn2_king_value( this.nn2_king_value );
		
		newNNP.set_SAP( (SAP) this.self_adaptive_params.clone() );
		
		newNNP.set_layers();
		
		return(newNNP);
	}


	//.2 percent change child gets all NN1 weights/biases/kingvalue from this and all NN2 stuff from parent2
	//otherwise, child gets all NN1&NN2 weights/biases from this, except for 1 set (of this 16) from parent2
	public Evol_Object crossover(Evol_Object parent2) 
	{
		NeuralNetPair child = (NeuralNetPair)this.clone();
			
		//Take weights of net1 from one parent and weights of net2 from other parent to form child
		if(Math.random() < 0.2)	//child gets this's nn1 and parent2's nn2
		{
			child.setNn1_input_weights( (double[])this.getNn1_input_weights().clone() );
			child.setNn1_layer1(cloneDoubleArray(this.getNn1_layer1()));
			child.setNn1_layer2(cloneDoubleArray(this.getNn1_layer2()));
			child.setNn1_layer3(cloneDoubleArray(this.getNn1_layer3()));
			
			child.set_nn1_biases_layer1( (double[])this.get_nn1_biases_layer1().clone() );
			child.set_nn1_biases_layer2( (double[])this.get_nn1_biases_layer2().clone() );
			child.set_nn1_biases_layer3( (double[])this.get_nn1_biases_layer3().clone() );
			child.set_nn1_biases_layer4( (double[])this.get_nn1_biases_layer4().clone() );
			
			child.set_nn1_king_value( this.nn1_king_value );
			
			child.setNn2_input_weights( (double[])((NeuralNetPair)parent2).getNn2_input_weights().clone() );
			child.setNn2_layer1( cloneDoubleArray(((NeuralNetPair)parent2).getNn2_layer1()));
			child.setNn2_layer2(cloneDoubleArray(((NeuralNetPair)parent2).getNn2_layer2()));
			child.setNn2_layer3( cloneDoubleArray(((NeuralNetPair)parent2).getNn2_layer3()) );
			
			child.set_nn2_biases_layer1( (double[])((NeuralNetPair)parent2).get_nn2_biases_layer1().clone() );
			child.set_nn2_biases_layer2( (double[])((NeuralNetPair)parent2).get_nn2_biases_layer2().clone() );
			child.set_nn2_biases_layer3( (double[])((NeuralNetPair)parent2).get_nn2_biases_layer3().clone() );
			child.set_nn2_biases_layer4( (double[])((NeuralNetPair)parent2).get_nn2_biases_layer4().clone() );
	
			child.set_nn2_king_value( ((NeuralNetPair)parent2).nn2_king_value );
		}
		else
		{
			child.setNn1_input_weights( (double[])this.getNn1_input_weights().clone() );
			child.setNn1_layer1(cloneDoubleArray(this.getNn1_layer1()));
			child.setNn1_layer2(cloneDoubleArray(this.getNn1_layer2()));
			child.setNn1_layer3(cloneDoubleArray(this.getNn1_layer3()));
			
			child.set_nn1_biases_layer1( (double[])this.get_nn1_biases_layer1().clone() );
			child.set_nn1_biases_layer2( (double[])this.get_nn1_biases_layer2().clone() );
			child.set_nn1_biases_layer3( (double[])this.get_nn1_biases_layer3().clone() );
			child.set_nn1_biases_layer4( (double[])this.get_nn1_biases_layer4().clone() );
			
			child.setNn2_input_weights( (double[])this.getNn2_input_weights().clone() );
			child.setNn2_layer1( cloneDoubleArray(this.getNn2_layer1()) );
			child.setNn2_layer2(cloneDoubleArray(this.getNn2_layer2()));
			child.setNn2_layer3( cloneDoubleArray(this.getNn2_layer3()) );
			
			child.set_nn2_biases_layer1( (double[])this.get_nn2_biases_layer1().clone() );
			child.set_nn2_biases_layer2( (double[])this.get_nn2_biases_layer2().clone() );
			child.set_nn2_biases_layer3( (double[])this.get_nn2_biases_layer3().clone() );
			child.set_nn2_biases_layer4( (double[])this.get_nn2_biases_layer4().clone() );
			
			double rand = Math.random();
			
			if(rand < .0625)
				child.setNn1_input_weights( (double[])((NeuralNetPair)parent2).getNn1_input_weights().clone() );
			else if(rand < .125)
				child.setNn1_layer1(cloneDoubleArray(((NeuralNetPair)parent2).getNn1_layer1()));
			else if(rand < .1875)
				child.setNn1_layer2(cloneDoubleArray(((NeuralNetPair)parent2).getNn1_layer2()));
			else if(rand < .25)
				child.setNn1_layer3(cloneDoubleArray(((NeuralNetPair)parent2).getNn1_layer3()));
			else if(rand < .3125)
				child.set_nn1_biases_layer1( (double[])((NeuralNetPair)parent2).get_nn1_biases_layer1().clone() );
			else if(rand < .375)
				child.set_nn1_biases_layer2( (double[])((NeuralNetPair)parent2).get_nn1_biases_layer2().clone() );
			else if(rand < .4375)
				child.set_nn1_biases_layer3( (double[])((NeuralNetPair)parent2).get_nn1_biases_layer3().clone() );
			else if(rand < .50)
				child.set_nn1_biases_layer4( (double[])((NeuralNetPair)parent2).get_nn1_biases_layer4().clone() );
			else if(rand < .5625)
				child.setNn2_input_weights( (double[])((NeuralNetPair)parent2).getNn2_input_weights().clone() );
			else if(rand < .625)
				child.setNn2_layer1( cloneDoubleArray(((NeuralNetPair)parent2).getNn2_layer1()) );
			else if(rand < .6875)
				child.setNn2_layer2(cloneDoubleArray(((NeuralNetPair)parent2).getNn2_layer2()));
			else if(rand < .75)
				child.setNn2_layer3( cloneDoubleArray(((NeuralNetPair)parent2).getNn2_layer3()) );
			else if(rand < .8125)
				child.set_nn2_biases_layer1( (double[])((NeuralNetPair)parent2).get_nn2_biases_layer1().clone() );
			else if(rand < .875)
				child.set_nn2_biases_layer2( (double[])((NeuralNetPair)parent2).get_nn2_biases_layer2().clone() );
			else if(rand < .9375)
				child.set_nn2_biases_layer3( (double[])((NeuralNetPair)parent2).get_nn2_biases_layer3().clone() );
			else
				child.set_nn2_biases_layer4( (double[])((NeuralNetPair)parent2).get_nn2_biases_layer4().clone() );
		}
		
		child.set_layers();
		
		return(child);
	}

	public void print_nn1inputweights()
	{
		for(int i=0; i < 91; i++)
			System.out.println(this.nn1_input_weights[i]);
	}

	public int head_to_head(Evol_Object contender) 
	{
		CheckerMatchMaker compete;
		System.out.print(".");
		
		if(Math.random() > .5)	//WHITE
		{
			compete = new CheckerMatchMaker(this, (NeuralNetPair)contender);
			int winner = compete.startMatch();
			
			if(winner == Checker.WHITE)
				return(1);
			else if(winner == Checker.BLACK)
				return(2);
			else
				return(0);	
		}
		else	//BLACK
		{
			compete = new CheckerMatchMaker((NeuralNetPair)contender, this);
			int winner = compete.startMatch();
			
			if(winner == Checker.BLACK)
				return(1);
			else if(winner == Checker.WHITE)
				return(2);
			else
				return(0);
		}
	}

	public double eval(byte[][] board, int color)
	{
		/* Opponent piece count; used to determine when NN2 should play. */
		int opponent_pieces = Util.numPieces(board, -1* color);
		byte[][] board2 = new byte[8][8];

		/*
		 * All positions that are wins for the player (no remaining opposing pieces) are assigned a value of exactly
		 * 1, and likewise all losing pieces are assigned a value of exactly 0.
		 */
		if(opponent_pieces == 0)
			return(1);
		else if(Util.numPieces(board,color) == 0)
			return(0);
		
		/* If color is -1, make flip the board over and change its color for evaluation. */
		if(color < 0)
		{			
			for(int i=0; i < 8; i++)
			{
				for(int j=0; j < 8; j++)
					board2[7-i][7-j] = (byte)((int)(-1)*board[i][j]);
			}
			
			board = board2;
		}

		if(opponent_pieces >= END_GAME_PEICE_COUNT){
			nn1_input =  Util.produce_input(board, nn1_input_weights, this.nn1_king_value, color);
			return nn1.calcNet(nn1_input);
		} else {
			nn2_input =  Util.produce_input(board, nn2_input_weights, this.nn2_king_value, color);
			return nn2.calcNet(nn2_input);
		}
	}

	
	public double test()
	{
		return( nn2.calcNet(nn2_input) );
	}

	public double get_nn1_king_value()
	{
		return nn1_king_value;
	}
	
	public double get_nn2_king_value()
	{
		return nn2_king_value;
	}
	
	public void set_nn1_king_value(double kv)
	{
		nn1_king_value = kv;
	}
	
	public void set_nn2_king_value(double kv)
	{
		nn2_king_value = kv;
	}

	public double[] getNn1_input_weights() {
		return nn1_input_weights;
	}

	public void setNn1_input_weights(double[] nn1_input_weights) {
		this.nn1_input_weights = nn1_input_weights;
	}

	public double[][] getNn1_layer1() {
		return nn1_layer1;
	}

	public void setNn1_layer1(double[][] nn1_layer1) {
		this.nn1_layer1 = nn1_layer1;
	}

	public double[][] getNn1_layer2() {
		return nn1_layer2;
	}

	public void setNn1_layer2(double[][] nn1_layer2) {
		this.nn1_layer2 = nn1_layer2;
	}

	public double[][] getNn1_layer3() {
		return nn1_layer3;
	}

	public void setNn1_layer3(double[][] nn1_layer3) {
		this.nn1_layer3 = nn1_layer3;
	}

	public double[] getNn2_input_weights() {
		return nn2_input_weights;
	}

	public void setNn2_input_weights(double[] nn2_input_weights) {
		this.nn2_input_weights = nn2_input_weights;
	}

	public double[][] getNn2_layer1() {
		return nn2_layer1;
	}

	public void setNn2_layer1(double[][] nn2_layer1) {
		this.nn2_layer1 = nn2_layer1;
	}

	public double[][] getNn2_layer2() {
		return nn2_layer2;
	}

	public void setNn2_layer2(double[][] nn2_layer2) {
		this.nn2_layer2 = nn2_layer2;
	}

	public double[][] getNn2_layer3() {
		return nn2_layer3;
	}

	public void setNn2_layer3(double[][] nn2_layer3) {
		this.nn2_layer3 = nn2_layer3;
	}
	
	public void set_layers()
	{
		nn1.setWeights(0, nn1_layer1);
		nn1.setWeights(1, nn1_layer2);
		nn1.setWeights(2, nn1_layer3);

		nn2.setWeights(0, nn2_layer1);
		nn2.setWeights(1, nn2_layer2);
		nn2.setWeights(2, nn2_layer3);
		
		nn1.setBiases(0, nn1_biases_layer1);
		nn1.setBiases(1, nn1_biases_layer2);
		nn1.setBiases(2, nn1_biases_layer3);	
		nn1.setBiases(3, nn1_biases_layer4);
	
		
		nn2.setBiases(0, nn2_biases_layer1);
		nn2.setBiases(1, nn2_biases_layer2);
		nn2.setBiases(2, nn2_biases_layer3);
		nn2.setBiases(3, nn1_biases_layer4);
	}
	

	
	public void set_input_nn1(double[] input)
	{
		nn1_input = input;
	}
	
	public void set_input_nn2(double[] input)
	{
		nn2_input = input;
	}
	
	public double[] get_input_nn1()
	{
		return(nn1_input);
	}
	
	public double[] get_input_nn2()
	{
		return(nn2_input);
	}
	
	public void set_nn1_biases_layer1(double[] nn1_biases)
	{
		this.nn1_biases_layer1 = nn1_biases;
	}

	public void set_nn2_biases_layer1(double[] nn2_biases)
	{
		this.nn2_biases_layer1 = nn2_biases;
	}

	public double[] get_nn1_biases_layer1()
	{
		return(nn1_biases_layer1);
	}

	public double[] get_nn2_biases_layer1()
	{
		return(nn2_biases_layer1);
	}
	
	public void set_nn1_biases_layer2(double[] nn1_biases)
	{
		this.nn1_biases_layer2 = nn1_biases;
	}

	public void set_nn2_biases_layer2(double[] nn2_biases)
	{
		this.nn2_biases_layer2 = nn2_biases;
	}

	public double[] get_nn1_biases_layer2()
	{
		return(nn1_biases_layer2);
	}

	public double[] get_nn2_biases_layer2()
	{
		return(nn2_biases_layer2);
	}
	
	public void set_nn1_biases_layer3(double[] nn1_biases)
	{
		this.nn1_biases_layer3 = nn1_biases;
	}

	public void set_nn2_biases_layer3(double[] nn2_biases)
	{
		this.nn2_biases_layer3 = nn2_biases;
	}

	public double[] get_nn1_biases_layer3()
	{
		return(nn1_biases_layer3);
	}

	public double[] get_nn2_biases_layer3()
	{
		return(nn2_biases_layer3);
	}
	
	public void set_nn1_biases_layer4(double[] nn1_biases)
	{
		this.nn1_biases_layer4 = nn1_biases;
	}

	public void set_nn2_biases_layer4(double[] nn2_biases)
	{
		this.nn2_biases_layer4 = nn2_biases;
	}

	public double[] get_nn1_biases_layer4()
	{
		return(nn1_biases_layer4);
	}

	public double[] get_nn2_biases_layer4()
	{
		return(nn2_biases_layer4);
	}
	
	public boolean equals(NeuralNetPair newNNP)
	{
		double[] nn1_input_weightsA = newNNP.getNn1_input_weights();
		double[][] nn1_layer1A = newNNP.getNn1_layer1();
		double[][] nn1_layer2A = newNNP.getNn1_layer2();
		double[][] nn1_layer3A = newNNP.getNn1_layer3();
		double[] nn1_biases_layer1A = newNNP.get_nn1_biases_layer1();
		double[] nn1_biases_layer2A = newNNP.get_nn1_biases_layer2();
		double[] nn1_biases_layer3A = newNNP.get_nn1_biases_layer3();
		double[] nn1_biases_layer4A = newNNP.get_nn1_biases_layer4();	
		double[] nn1_inputA = newNNP.get_input_nn1();

		double[] nn2_input_weightsA = newNNP.getNn2_input_weights();
		double[][] nn2_layer1A = newNNP.getNn2_layer1();
		double[][] nn2_layer2A = newNNP.getNn2_layer2();
		double[][] nn2_layer3A = newNNP.getNn2_layer3();
		double[] nn2_biases_layer1A = newNNP.get_nn2_biases_layer1();
		double[] nn2_biases_layer2A = newNNP.get_nn2_biases_layer2();
		double[] nn2_biases_layer3A = newNNP.get_nn2_biases_layer3();
		double[] nn2_biases_layer4A = newNNP.get_nn2_biases_layer4();	
		double[] nn2_inputA = newNNP.get_input_nn2();
		
		double nn1_king = newNNP.get_nn1_king_value();
		double nn2_king = newNNP.get_nn2_king_value();
		
		for(int i=0; i < nn1_input_weightsA.length; i++)
			if(nn1_input_weightsA[i] != nn1_input_weights[i])
			{
				System.out.println("err1");
				return(false);
			}
		
		for(int i=0; i < nn1_input_weightsA.length; i++)
			if(nn2_input_weightsA[i] != nn2_input_weights[i])
			{
				System.out.println("err2: its nn2 input weight:"+nn2_input_weightsA[i]+" mine:"+nn2_input_weights[i]);
				return(false);
			}

		for(int i=0; i < 91; i++)
			for(int j=0; j < 40; j++)
				if(nn1_layer1A[i][j] != nn1_layer1[i][j])
				{
					System.out.println("err3");
					return(false);
				}
		
		for(int i=0; i < 91; i++)
			for(int j=0; j < 40; j++)
				if(nn2_layer1A[i][j] != nn2_layer1[i][j])
				{
					System.out.println("err4");
					return(false);
				}
		
		for(int i=0; i < 40; i++)
			for(int j=0; j < 10; j++)
				if(nn1_layer2A[i][j] != nn1_layer2[i][j])
				{
					System.out.println("err5");
					return(false);
				}

		
		for(int i=0; i < 40; i++)
			for(int j=0; j < 10; j++)
				if(nn2_layer2A[i][j] != nn2_layer2[i][j])
				{
					System.out.println("err6");
					return(false);
				}
		
		for(int i=0; i < 10; i++)
			if(nn1_layer3A[i][0] != nn1_layer3[i][0])
			{
				System.out.println("err7");
				return(false);
			}
		
		for(int i=0; i < 10; i++)
			if(nn2_layer3A[i][0] != nn2_layer3[i][0])
			{
				System.out.println("err8");
				return(false);
			}
		
		for(int i=0; i < 91; i++)
			if(nn1_biases_layer1A[i] != nn1_biases_layer1[i])
			{
				System.out.println("err9");
				return(false);
			}
		
		for(int i=0; i < 91; i++)
			if(nn2_biases_layer1A[i] != nn2_biases_layer1[i])
			{
				System.out.println("err10");
				return(false);
			}
		
		for(int i=0; i < 40; i++)
			if(nn1_biases_layer2A[i] != nn1_biases_layer2[i])
			{
				System.out.println("err11");
				return(false);
			}
		
		for(int i=0; i < 40; i++)
			if(nn2_biases_layer2A[i] != nn2_biases_layer2[i])
			{
				System.out.println("err12");
				return(false);
			}
		
		for(int i=0; i < 10; i++)
			if(nn1_biases_layer3A[i] != nn1_biases_layer3[i])
			{
				System.out.println("err13");
				return(false);
			}
		
		for(int i=0; i < 10; i++)
			if(nn2_biases_layer3A[i] != nn2_biases_layer3[i])
			{
				System.out.println("err14");
				return(false);
			}		
	
		if(nn1_biases_layer4A[0] != nn1_biases_layer4[0])
		{
			System.out.println("err15");
			return(false);
		}		
	
		
		if(nn2_biases_layer4A[0] != nn2_biases_layer4[0])
		{
			System.out.println("err16");
			return(false);
		}		
	
		if(nn1_king != this.nn1_king_value || nn2_king != this.nn2_king_value)
		{
			System.out.println("err17");
			return(false);
		}
		
		for(int i=0; i < 91; i++)
			if(nn1_input[i] != nn1_inputA[i])
			{
				System.out.println("err18");
				return(false);
			}		
	
		for(int i=0; i < 91; i++)
			if(nn2_input[i] != nn2_inputA[i])
			{
				System.out.println("err19");
				return(false);
			}
		
		return(true);
	}
	public static double[][] cloneDoubleArray(double[][] input){
		double[][] output = new double[input.length][input[0].length];
		for (int i = 0; i < input.length; i++) {
			for (int j = 0; j < input[i].length; j++) {
				output[i][j] = input[i][j];
			}
		}
		return output;
	}
	
	private void set_SAP(SAP self_adapt)
	{
		this.self_adaptive_params = self_adapt;
	}
	
	private SAP get_SAP()
	{
		return(this.self_adaptive_params);
	}
	
}
