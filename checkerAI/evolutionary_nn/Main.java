package evolutionary_nn;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import evolutionary_framework.Evol_Algorithm;
import evolutionary_framework.Evol_Compare;
import evolutionary_framework.Evol_Object;
import evolutionary_framework.PrioQueue;

public class Main {
	private static String saveFileName = System.getProperty("user.dir")
			+ File.separator + "nn_save.ser";

	private boolean saveProgress = true;
	
	private static int count = 0;

	public Main() {

		// ///////////////////////////////////////////////////////////////////////////////

		boolean SUPER_TOURNEY = false; // set false for normal evol
		boolean START_FROM_SCRATCH = true; // true = start from scratch; false
											// = loud prev population and
											// continue


		CheckerMatchMaker.PRINT_MATCH_INFO = false; // print information about
													// each match
		Evol_Algorithm.mimicFogel = true; 	// set to false if you want to evolve
											// against classical minimax ai

		boolean doingSomethingElse = true;
		// ///////////////////////////////////////////////////////////////////////////////

		// what i hcagned: increased tie to 500 moves per person. changed tro 10
		// gens to switch

		if(doingSomethingElse){
		
			String load = System.getProperty("user.dir")
			+ File.separator + "fogelgen180.ser";
			
			String save = System.getProperty("user.dir")
			+ File.separator + "fogelgen180.nnp";
			try {
				PrioQueue p = load(load);
				saveNNP(NeuralNetPair.getBestMember(p),save);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.exit(0);
		}

		/* Some Options - leave these alone if not starting from scratch */
		int pop_size = 40;
		int num_elite = 10;

		JavaMLP nn1 = new JavaMLP();
		JavaMLP nn2 = new JavaMLP();
		NeuralNetPair test = new NeuralNetPair(nn1, nn2);

		Evol_Algorithm EA = new Evol_Algorithm(test, pop_size, -1, 1, num_elite);

		PrioQueue results = null;		
		
		
		if (SUPER_TOURNEY) {
			try {
				results = load();
				System.out.println("RESUMING GENERATION " + results.get_gen());
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			results = EA.super_tourney(results);
			EA.print_population();

			try {
				save(results);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		else {

			// EA.enable_crossover(4);
			if (Evol_Algorithm.mimicFogel) {
				EA.set_selection_type(3);
				EA.enable_print_population();
				EA.do_tournament(4);
			} else {
				EA.enable_print_population();
				EA.enable_crossover(7);
				EA.do_tournament(1);
			}

			if (START_FROM_SCRATCH) {
				results = EA.search();
				results.set_gen(0);
			} else {
				try {
					results = load();
					System.out.println("RESUMING GENERATION "
							+ results.get_gen());
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}

			for (int i = 1; i < 1000000000; i++) {
				results.set_gen(results.get_gen() + 1);
				results = EA.search(results, 1);

				try {
					save(results);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void main(String[] args) {
		new Main();
	}

	private void save(PrioQueue p) throws IOException {
		if (saveProgress) {
			File save = new File(saveFileName+(count++%6));
			if (!save.exists()) {
				save.createNewFile();
			} else {
				save.delete();
				save.createNewFile();
			}
			System.out.println("generation " + p.get_gen() + " saved at:"
					+ save.getAbsolutePath());
			ObjectOutputStream oout = new ObjectOutputStream(
					new FileOutputStream(save));
			oout.writeObject(p);
			oout.flush();
			oout.close();
		}
	}
	public static void save(PrioQueue p, String name) throws IOException {
		
			File save = new File(name);
			if (!save.exists()) {
				save.createNewFile();
			} else {
				save.delete();
				save.createNewFile();
			}
			System.out.println("best member saved " + p.get_gen() + " saved at:"
					+ save.getAbsolutePath());
			ObjectOutputStream oout = new ObjectOutputStream(
					new FileOutputStream(save));
			oout.writeObject(p);
			oout.flush();
			oout.close();
		
	}
	public static void saveNNP(NeuralNetPair p, String name) throws IOException {
		
		File save = new File(name);
		if (!save.exists()) {
			save.createNewFile();
		} else {
			save.delete();
			save.createNewFile();
		}
		System.out.println("best NN saved  saved at:"
				+ save.getAbsolutePath());
		ObjectOutputStream oout = new ObjectOutputStream(
				new FileOutputStream(save));
		oout.writeObject(p);
		oout.flush();
		oout.close();
	
	}
	public static PrioQueue load() throws FileNotFoundException, IOException,
			ClassNotFoundException {
		File save = new File(saveFileName);
		ObjectInputStream oin = new ObjectInputStream(new FileInputStream(save));
		PrioQueue p = (PrioQueue) oin.readObject();
		return p;
	}
	public static PrioQueue load(String name) throws FileNotFoundException, IOException,
		ClassNotFoundException {
	File save = new File(name);
	ObjectInputStream oin = new ObjectInputStream(new FileInputStream(save));
	PrioQueue p = (PrioQueue) oin.readObject();
	return p;
	}
}