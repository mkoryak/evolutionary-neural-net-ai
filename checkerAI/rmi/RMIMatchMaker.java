package rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

import evolutionary_nn.NeuralNetPair;

public interface RMIMatchMaker extends Remote {
	
	public RMIResponse doMatch(NeuralNetPair p1,NeuralNetPair p2) throws RemoteException;
	
}
