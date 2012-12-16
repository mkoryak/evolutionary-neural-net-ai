package evolutionary_nn;



import java.io.Serializable;
import java.lang.Math;

public class JavaMLP implements Serializable {


	private static int numInputs = 91; //number of inputs - this includes the input bias also [92]nd slot is the peice difference

	private static int numHidden1 = 40; //number of hidden units
	private static int numHidden2 = 10; //number of hidden units
	
	//the outputs of the hidden neurons
	private  double[] hiddenVal1 = new double[numHidden1];
	private  double[] hiddenVal2 = new double[numHidden2];

	//the weights
	private  double[][] weightsIH1 = new double[numInputs][numHidden1];
	private  double[][] weightsH1H2 = new double[numHidden1][numHidden2];
	private  double[][] weightsH2O = new double[numHidden2][1];
	
	private double[] biases0 = new double[numInputs];
	private double[] biases1 = new double[numHidden1];
	private double[] biases2 = new double[numHidden2];
	private double[] biases3 = new double[1]; //output bias
	
	private static int idcount = 0;
	private int id;

	//==============================================================
	//********** THIS IS THE MAIN PROGRAM **************************
	//==============================================================

	public JavaMLP(){
		id = idcount++;
	}

	//============================================================
	//********** END OF THE MAIN PROGRAM **************************
	//=============================================================

	//************************************
	public double calcNet(double[] pattern) {
		//calculate the outputs of the hidden neurons
		//the hidden neurons are tanh
		double output = 0;
		int count = 0;

		
		for (int i = 0; i < numHidden1; i++) 
		{
			hiddenVal1[i] = 0.0;

			for (int j = 0; j < numInputs; j++)
			{
				pattern[j] += biases0[j];
				hiddenVal1[i] += (pattern[j] * weightsIH1[j][i]);
			}
			hiddenVal1[i] += biases1[count++];
			hiddenVal1[i] = tanh(hiddenVal1[i]);
		}
		
		count = 0;
		
		for (int i = 0; i < numHidden2; i++) {
			hiddenVal2[i] = 0.0;

			for (int j = 0; j < numHidden1; j++)
				hiddenVal2[i] += (hiddenVal1[j] * weightsH1H2[j][i]);
			hiddenVal2[i] += biases2[count++];
			hiddenVal2[i] = tanh(hiddenVal2[i]);
		}
		
		count = 0;
		
		for(int i = 0;i<numHidden2;i++)
		    output += hiddenVal2[i] * weightsH2O[i][0];
		
		output += biases3[0];
		output += pattern[91];	//peice difference
		
		double d= tanh(output)/2+.5;
		
		return d;
	}
	
	
	public void setBiases(int layer, double[] d){
		if(layer == 0){
			biases0 = (double[])d.clone();
		} else if(layer == 1){
			biases1 = (double[])d.clone();
		} else if(layer == 2) {
			biases2 = (double[])d.clone();
		} else
			biases3 = (double[])d.clone();
	}
	public double[] getBiases(int layer){
		if(layer == 0){
			return biases0;
		} else if(layer == 1){
			return biases1;
		} else if(layer == 2){
			return biases2;
		} else
			return biases3;
	}
	public void setWeights(int layer, double[][] d){
		if(layer == 0){
			weightsIH1 = NeuralNetPair.cloneDoubleArray(d);
		} else if(layer == 1){
			weightsH1H2 = NeuralNetPair.cloneDoubleArray(d);
		} else 
			weightsH2O = NeuralNetPair.cloneDoubleArray(d);
	}
	public double[][] getWeights(int layer){
		if(layer == 0){
			return weightsIH1;
		} else if(layer == 1){
			return weightsH1H2;
		} else 
			return weightsH2O;
	}

	//************************************
	public static double tanh(double x) {
		//System.out.println("tanh: check what value x is between, change this function accordingly!!!! x:"+x);
		if (x > 20)
			return 1;
		else if (x < -20)
			return -1;
		else {
			double a = Math.exp(x);
			double b = Math.exp(-x);
			return (a - b) / (a + b);
		}
	}



}
