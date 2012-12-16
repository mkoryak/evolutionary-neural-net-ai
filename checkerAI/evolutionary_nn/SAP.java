package evolutionary_nn;

import java.io.Serializable;
import java.util.Random;

public class SAP implements Serializable
{
	private static final long serialVersionUID = 4144870539627521107L;
	
	private double[] nn1_input_weights = new double[854];
	private double[][] nn1_layer1 = new double[91][40];
	private double[][] nn1_layer2 = new double[40][10];
	private double[][] nn1_layer3 = new double[10][1];
	private double[] nn1_biases_layer1 = new double[91];
	private double[] nn1_biases_layer2 = new double[40];
	private double[] nn1_biases_layer3 = new double[10];
	private double[] nn1_biases_layer4 = new double[1];
	
	private double[] nn2_input_weights = new double[854];
	private double[][] nn2_layer3 = new double[10][1];
	private double[][] nn2_layer2 = new double[40][10];
	private double[][] nn2_layer1 = new double[91][40];
	private double[] nn2_biases_layer1 = new double[91];
	private double[] nn2_biases_layer2 = new double[40];
	private double[] nn2_biases_layer3 = new double[10];
	private double[] nn2_biases_layer4 = new double[1];

	private static double THETA = .0839;
	
	public SAP() 
	{
	}

	public void initialize()
	{
		for(int i=0; i < 854; i++)
		{
			nn1_input_weights[i] = .05;
			nn2_input_weights[i] = .05;
		}
		
		for(int i=0; i < 91; i++)
		{
			for(int j=0; j < 40; j++)
			{
				nn1_layer1[i][j] = .05;
				nn2_layer1[i][j] = .05;
			}
		}
		
		for(int i=0; i < 40; i++)
		{
			for(int j=0; j < 10; j++)
			{
				nn1_layer2[i][j] = .05;
				nn2_layer2[i][j] = .05;
			}
		}
		
		for(int i=0; i < 10; i++)
		{
			nn1_layer3[i][0] = .05;
			nn2_layer3[i][0] = .05;
		}
	
		for(int i=0; i < 91; i++)
		{
			nn1_biases_layer1[i] = .05;
			nn2_biases_layer1[i] = .05;
		}
		
		for(int i=0; i < 40; i++)
		{
			nn1_biases_layer2[i] = .05;
			nn2_biases_layer2[i] = .05;
		}
		
		for(int i=0; i < 10; i++)
		{
			nn1_biases_layer3[i] = .05;
			nn2_biases_layer3[i] = .05;
		}
		
		nn1_biases_layer4[0] = .05;
		nn2_biases_layer4[0] = .05;				

	}
	
	public void mutate()
	{
		for(int i=0; i < 854; i++)
		{
			nn1_input_weights[i] *= rand_mutate();
			nn2_input_weights[i] *= rand_mutate();
		}
		
		for(int i=0; i < 91; i++)
		{
			for(int j=0; j < 40; j++)
			{
				nn1_layer1[i][j] *= rand_mutate();
				nn2_layer1[i][j] *= rand_mutate();
			}
		}
		
		for(int i=0; i < 40; i++)
		{
			for(int j=0; j < 10; j++)
			{
				nn1_layer2[i][j] *= rand_mutate();
				nn2_layer2[i][j] *= rand_mutate();
			}
		}
		
		for(int i=0; i < 10; i++)
		{
			nn1_layer3[i][0] *= rand_mutate();
			nn2_layer3[i][0] *= rand_mutate();
		}
	
		for(int i=0; i < 91; i++)
		{
			nn1_biases_layer1[i] *= rand_mutate();
			nn2_biases_layer1[i] *= rand_mutate();
		}
		
		for(int i=0; i < 40; i++)
		{
			nn1_biases_layer2[i] *= rand_mutate();
			nn2_biases_layer2[i] *= rand_mutate();
		}
		
		for(int i=0; i < 10; i++)
		{
			nn1_biases_layer3[i] *= rand_mutate();
			nn2_biases_layer3[i] *= rand_mutate();
		}
		
		nn1_biases_layer4[0] *= rand_mutate();
		nn2_biases_layer4[0] *= rand_mutate();					
	}	
	
	
	
	
	public Object clone() 
	{
		SAP newSAP = new SAP();
		
		newSAP.setNn1_input_weights( (double[])this.nn1_input_weights.clone() );
		newSAP.setNn2_input_weights( (double[])this.nn2_input_weights.clone() );
	
		newSAP.setNn1_layer1( cloneDoubleArray(this.nn1_layer1) );
		newSAP.setNn2_layer1( cloneDoubleArray(this.nn2_layer1) );
		
		newSAP.setNn1_layer2( cloneDoubleArray(this.nn1_layer2) );
		newSAP.setNn2_layer2( cloneDoubleArray(this.nn2_layer2) );
		
		newSAP.setNn1_layer3( cloneDoubleArray(this.nn1_layer3) );
		newSAP.setNn2_layer3( cloneDoubleArray(this.nn2_layer3) );
	
		newSAP.setNn1_biases_layer1( (double[])this.nn1_biases_layer1.clone() );
		newSAP.setNn1_biases_layer2( (double[])this.nn1_biases_layer2.clone() );
		newSAP.setNn1_biases_layer3( (double[])this.nn1_biases_layer3.clone() );
		newSAP.setNn1_biases_layer4( (double[])this.nn1_biases_layer4.clone() );
		
		newSAP.setNn2_biases_layer1( (double[])this.nn2_biases_layer1.clone() );
		newSAP.setNn2_biases_layer2( (double[])this.nn2_biases_layer2.clone() );
		newSAP.setNn2_biases_layer3( (double[])this.nn2_biases_layer3.clone() );
		newSAP.setNn2_biases_layer4( (double[])this.nn2_biases_layer4.clone() );

		return(newSAP);
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
	
	

	double nn1_input_weights(int i) {return(nn1_input_weights[i]);}
	double nn1_layer1(int i, int j) {return(nn1_layer1[i][j]);}
	double nn1_layer2(int i, int j) {return(nn1_layer2[i][j]);}
	double nn1_layer3(int i) {return(nn1_layer3[i][0]);}

	double nn1_biases_layer1(int i) { return( nn1_biases_layer1[i]);}
	double nn1_biases_layer2(int i) { return( nn1_biases_layer2[i]);}
	double nn1_biases_layer3(int i) { return( nn1_biases_layer3[i]);}
	double nn1_biases_layer4(int i) { return( nn1_biases_layer4[i]);}
	
	double nn2_input_weight(int i) {return(nn2_input_weights[i]);}
	double nn2_layer1(int i, int j) {return(nn2_layer1[i][j]);}
	double nn2_layer2(int i, int j) {return(nn2_layer2[i][j]);}
	double nn2_layer3(int i) {return(nn2_layer3[i][0]);}

	double nn2_biases_layer1(int i) { return( nn2_biases_layer1[i]);}
	double nn2_biases_layer2(int i) { return( nn2_biases_layer2[i]);}
	double nn2_biases_layer3(int i) { return( nn2_biases_layer3[i]);}
	double nn2_biases_layer4(int i) { return( nn2_biases_layer4[i]);}

	
	private double rand_mutate()
	{
		return(Math.exp( THETA*(new Random().nextGaussian())));
	}


	public double[] getNn1_biases_layer1() {
		return nn1_biases_layer1;
	}


	public void setNn1_biases_layer1(double[] nn1_biases_layer1) {
		this.nn1_biases_layer1 = nn1_biases_layer1;
	}


	public double[] getNn1_biases_layer2() {
		return nn1_biases_layer2;
	}


	public void setNn1_biases_layer2(double[] nn1_biases_layer2) {
		this.nn1_biases_layer2 = nn1_biases_layer2;
	}


	public double[] getNn1_biases_layer3() {
		return nn1_biases_layer3;
	}


	public void setNn1_biases_layer3(double[] nn1_biases_layer3) {
		this.nn1_biases_layer3 = nn1_biases_layer3;
	}


	public double[] getNn1_biases_layer4() {
		return nn1_biases_layer4;
	}


	public void setNn1_biases_layer4(double[] nn1_biases_layer4) {
		this.nn1_biases_layer4 = nn1_biases_layer4;
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


	public double[] getNn2_biases_layer1() {
		return nn2_biases_layer1;
	}


	public void setNn2_biases_layer1(double[] nn2_biases_layer1) {
		this.nn2_biases_layer1 = nn2_biases_layer1;
	}


	public double[] getNn2_biases_layer2() {
		return nn2_biases_layer2;
	}


	public void setNn2_biases_layer2(double[] nn2_biases_layer2) {
		this.nn2_biases_layer2 = nn2_biases_layer2;
	}


	public double[] getNn2_biases_layer3() {
		return nn2_biases_layer3;
	}


	public void setNn2_biases_layer3(double[] nn2_biases_layer3) {
		this.nn2_biases_layer3 = nn2_biases_layer3;
	}


	public double[] getNn2_biases_layer4() {
		return nn2_biases_layer4;
	}


	public void setNn2_biases_layer4(double[] nn2_biases_layer4) {
		this.nn2_biases_layer4 = nn2_biases_layer4;
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
}