package rmi;


import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.security.Permission;

import evolutionary_nn.NeuralNetPair;

public class RMIMatchMaker_Impl extends UnicastRemoteObject implements RMIMatchMaker {

	RMIMatchMaker server;
	
	public RMIMatchMaker_Impl() throws RemoteException {
		super();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
        if (System.getSecurityManager() == null) {
        	System.setSecurityManager(new SecurityManager(){
        		 public void checkPermission(Permission perm) { }
        		 public void checkPermission(Permission perm, Object context) { }
        	});//new RMISecurityManager());
        }
        String host = System.getProperty("host");
        String name = "checkers";
        String sname = "//"+host+":1099/"+name;
        try {
        	RMIMatchMaker server = new RMIMatchMaker_Impl();
        	int serverPort = 1099;
        	Registry registry = LocateRegistry.createRegistry(serverPort);
        	registry.rebind(sname, server);
            System.out.println("RMIServerInterface bound:"+registry.toString());
        } catch (Exception e) {
            System.err.println("RMIServerInterface exception: " + 
       e.getMessage());
            e.printStackTrace();
        }

	}

	public RMIResponse doMatch(NeuralNetPair p1, NeuralNetPair p2) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

}
