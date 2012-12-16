package crapplet;

// CyberthelloFrame.java
//package tournament_checkers;

import java.awt.*;
import java.awt.event.*;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

import javax.swing.*;

import checkers.Checker;

import checkers_framework.CheckerInterface;
import checkers_framework.Move;
import checkers_framework.Strong_CheckerAI;

import evolutionary_nn.NeuralNetPair;
import evolutionary_nn.NeuralNetworkAI;

public class CheckerFrameApplet extends JApplet implements ActionListener {
	private static final Color bgColor = Color.black;

	private static final Color textFgColor = Color.white;

	private JPanel mainPanel;

	private GridBagLayout mainLayout;

	private JLabel gameInProgressLabel;

	private JLabel blackLabel;

	private JLabel blackScoreLabel;

	private JLabel whiteLabel;

	private JLabel whiteScoreLabel;

	private JMenu gameMenu;

	private CheckerPaneApplet cyberthelloPane;

	private JLabel infoLabel;

	private JMenuItem mnuPlayAsWhite, mnuPlayAsBlack;

	private CheckerGameApplet currentGame;

	private String whitename;

	private String blackname;

	private JMenu mnuAIs;

	private NeuralNetAIContainer nnContainer;

	private ButtonGroup group;


	public CheckerFrameApplet() {
		currentGame = null;
		
		setName("Java Checkers");

		Dimension dim = getToolkit().getScreenSize();
		this.setLocation(dim.width / 2 - this.getWidth() / 2, dim.height / 2
				- this.getHeight() / 2);

	}

	public void repaint(Graphics g) {
		update(g);
		refreshUI();
	}

	public void init() {

		nnContainer = new NeuralNetAIContainer();
		createGUI();


	}

	private void createGUI() {
		buildMenu();
		buildUI();
		setSize(488, 600);
		refreshUI();
		this.setVisible(true);
	}

	private void buildMenu() {
		JMenuBar menuBar = new JMenuBar();

		gameMenu = new JMenu("Game");
		
		mnuAIs = new JMenu("Set Opponent");
		String names[] = nnContainer.getAINames();
		group = new ButtonGroup();
		for (int i = 0; i < names.length; i++) {
			JRadioButtonMenuItem temp = new JRadioButtonMenuItem(names[i]);
			temp.setActionCommand(names[i]);
			temp.addActionListener(this);
			group.add(temp);
			mnuAIs.add(temp);
			if(i==0) {
				temp.setSelected(true);
				nnContainer.loadAIForName(names[i]);
			}
		}

		mnuPlayAsWhite = new JMenuItem("Play as white");
		mnuPlayAsWhite.setActionCommand("playwhite");
		mnuPlayAsWhite.addActionListener(this);
		gameMenu.add(mnuPlayAsWhite);

		mnuPlayAsBlack = new JMenuItem("Play as blue");
		mnuPlayAsBlack.setActionCommand("playblack");
		mnuPlayAsBlack.addActionListener(this);
		
		gameMenu.add(mnuPlayAsBlack);

		gameMenu.setEnabled(false);

		menuBar.add(gameMenu);
		menuBar.add(mnuAIs);

		setJMenuBar(menuBar);
	}

	private void buildUI() {
		mainPanel = new JPanel();
		mainPanel.setBackground(bgColor);
		setContentPane(mainPanel);

		mainLayout = new GridBagLayout();
		mainPanel.setLayout(mainLayout);

		gameInProgressLabel = new JLabel("Blah blah");
		gameInProgressLabel.setForeground(textFgColor);
		mainPanel.add(gameInProgressLabel);
		mainLayout.setConstraints(gameInProgressLabel, new GridBagConstraints(
				0, 0, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));

		blackLabel = new JLabel("Black: ");
		blackLabel.setForeground(textFgColor);
		mainPanel.add(blackLabel);
		mainLayout.setConstraints(blackLabel, new GridBagConstraints(0, 1, 1,
				1, 1.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(10, 5, 2, 5), 0, 0));

		blackScoreLabel = new JLabel("2");
		blackScoreLabel.setForeground(textFgColor);
		mainPanel.add(blackScoreLabel);
		mainLayout.setConstraints(blackScoreLabel, new GridBagConstraints(1, 1,
				1, 1, 1.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(10, 5, 2, 5), 0, 0));

		whiteLabel = new JLabel("White: ");
		whiteLabel.setForeground(textFgColor);
		mainPanel.add(whiteLabel);
		mainLayout.setConstraints(whiteLabel, new GridBagConstraints(0, 2, 1,
				1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(2, 5, 5, 5), 0, 0));

		whiteScoreLabel = new JLabel("2");
		whiteScoreLabel.setForeground(textFgColor);
		mainPanel.add(whiteScoreLabel);
		mainLayout.setConstraints(whiteScoreLabel, new GridBagConstraints(1, 2,
				1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(2, 5, 5, 5), 0, 0));

		cyberthelloPane = new CheckerPaneApplet(this);
		mainPanel.add(cyberthelloPane);
		mainLayout.setConstraints(cyberthelloPane, new GridBagConstraints(0, 3,
				3, 1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(10, 10, 10, 10), 0, 0));

		infoLabel = new JLabel("Created by Mikhail Koryak and Ari Packer");
		infoLabel.setForeground(textFgColor);
		mainPanel.add(infoLabel);
		mainLayout.setConstraints(infoLabel, new GridBagConstraints(0, 4, 3, 1,
				1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
				new Insets(5, 5, 5, 5), 0, 0));

		cyberthelloPane.setVisible(true);
		mainPanel.setVisible(true);

	}

	public void refreshUI() {
		cyberthelloPane.repaint();

		if (currentGame == null) {
			gameInProgressLabel.setText("Please wait while we load the AI |");
			setName("Java Checkers");
			blackLabel.setText("");
			blackScoreLabel.setText("");
			whiteLabel.setText("");
			whiteScoreLabel.setText("");
			System.out.println("blee");
		} else {
			if (currentGame.isGameOver()) {
				gameInProgressLabel.setText("No game in progress");
				infoLabel.setText("Game over: " + currentGame.getWinner());
			} else {
				if (currentGame.isBlacksMove()) {
					gameInProgressLabel.setText("Game in progress: "
							+ blackname + "'s move (Black)");

					if (currentGame.blackIsHuman())
						infoLabel.setText("Waiting for Black (" + blackname
								+ ")  to move...");
					else
						infoLabel.setText("Black (" + blackname
								+ ") is deciding on a move...");
				} else {
					gameInProgressLabel.setText("Game in progress: "
							+ whitename + "'s move (White)");

					if (currentGame.whiteIsHuman())
						infoLabel.setText("Waiting for White (" + whitename
								+ ") to move...");
					else
						infoLabel.setText("White (" + whitename
								+ ") is deciding on a move...");
				}
			}

			blackLabel.setText("(" + blackname + ") Black : ");
			blackScoreLabel.setText("" + currentGame.getBlackScore());
			whiteLabel.setText("(" + whitename + ") White : ");
			whiteScoreLabel.setText("" + currentGame.getWhiteScore());
		}
	}

	public void playgame(CheckerGameApplet game, int i, String black,
			String white) {
		this.blackname = black;
		this.whitename = white;
		currentGame = game;
		cyberthelloPane.setGame(currentGame);
		refreshUI();
		currentGame.start();

		setName("Java Checkers Tournament : Playing Game " + i);
	}

	private synchronized void fileNewGame(boolean playAsWhite) {

		whitename = "White";
		blackname = "Blue";

		if (currentGame != null) {
			currentGame.endGame();
			notify(); // wake up any waiting threads
		}
		if (playAsWhite) {
			currentGame = new CheckerGameApplet(30000, getBlackAgent(),
					getWhiteAgent(), this, whitename, blackname, 1000, false);
		} else {
			CheckerInterface mook = getBlackAgent();
			mook.setColor(Checker.WHITE);
			currentGame = new CheckerGameApplet(30000, getWhiteAgent(), mook,
					this, whitename, blackname, 1000, false);
		}

		cyberthelloPane.setGame(currentGame);
		refreshUI();
		currentGame.start();

	}

	private CheckerInterface getWhiteAgent() {
		//white is human
		return null;
	}

	private CheckerInterface getBlackAgent() {
		NeuralNetPair p = nnContainer.getLoadedAI();
		if (p != null) {
			NeuralNetworkAI misha = new NeuralNetworkAI(p);
			misha.setColor(Checker.BLACK);
			return misha;
		} else {
			Strong_CheckerAI bob = new Strong_CheckerAI();
			bob.setColor(Checker.BLACK);
			return bob;
		}
	}

	// This method is called when the human has indicated a move
	public synchronized void makeMove(int currrow, int currcol, int sucrow,
			int succol) {
		int legalMove = 0;
		if (currentGame == null) {
			return;
		}

		if (currentGame.isBlacksMove()) {
			if (!currentGame.blackIsHuman()) // ignore spurious clicks
				return;
			legalMove = currentGame.makeHumanMove(currrow, currcol, sucrow,
					succol, Checker.BLACK);

			if (!(legalMove == 1 || legalMove == 3))
				return;
		} else {
			if (!currentGame.whiteIsHuman()) // ignore spurious clicks
				return;
			legalMove = currentGame.makeHumanMove(currrow, currcol, sucrow,
					succol, Checker.WHITE);

			if (!(legalMove == 1 || legalMove == 3))
				return;
		}

		// Valid human move: wake up all threads waiting on this object
		// (there is just one, of course).
		Move m = new Move(currrow, currcol, sucrow, succol);

		if (legalMove == 1)
			notify();
		refreshUI();
	}

	public void actionPerformed(ActionEvent e) {

		if(e.getActionCommand().endsWith("playwhite")){
			fileNewGame(true);
		} else if(e.getActionCommand().equals("playblack")){
			fileNewGame(false);
		} else { // otherwise this is a command to set an AI
			nnContainer.loadAIForName(e.getActionCommand());
			JRadioButtonMenuItem ob = (JRadioButtonMenuItem)e.getSource();
			ob.setSelected(true);
		}
			

	}

	

	public class NeuralNetAIContainer extends Thread implements ActionListener {
		private String[] aiNames;

		private int numAIs;

		private HashMap nnpLocator;

		private Timer loading;

		private int loadCounter = 0;

		private volatile boolean showLoading = true;

		private String[] loadingString = { "|", "/", "-", "\\" };
		
		private NeuralNetURLReader reader;
		
		private String currentAI;

		public NeuralNetAIContainer() {
			String num = getParameter("number_of_AIs");
			System.out.println(num);
			numAIs = Integer.parseInt(num);
			aiNames = new String[numAIs];
			nnpLocator = new HashMap(numAIs);

			populateFields();
			loading = new Timer(500, this);
		}

		private void populateFields() {
			for (int i = 0; i < numAIs; i++) {
				aiNames[i] = getParameter("AI_name_" + i);
				nnpLocator.put(aiNames[i], getParameter("AI_URL_" + i));
			}
		}

		public String[] getAINames() {
			return aiNames;
		}

		public String getURLForName(String name) {
			return (String) nnpLocator.get(name);
		}

		public void loadAIForName(String name) {
			gameMenu.setEnabled(false);
			currentAI = name;
			reader = new NeuralNetURLReader(getURLForName(name));
			reader.start();	
		}
		public NeuralNetPair getLoadedAI(){
			return reader.getNeuralNetPair();
		}

		public void actionPerformed(ActionEvent e) {
			if (showLoading) {

				loadCounter = (loadCounter + 1) % 4;
				gameInProgressLabel
						.setText("Please wait while we load the AI ["+currentAI+"] "
								+ loadingString[loadCounter]);
			}
		}
		
		public class NeuralNetURLReader extends Thread {
			private NeuralNetPair nnp = null;

			private String url_;

			private volatile boolean done;
			
			

			public NeuralNetURLReader(String u) {
				url_ = u;
				done = false;
				showLoading = true;
				
			}

			public void run() {
				URL url;
				loading.start();
				try {
					url = new URL(url_);
					URLConnection con = url.openConnection();
					InputStream is = con.getInputStream();

					ObjectInputStream os = new ObjectInputStream(is);
					nnp = (NeuralNetPair) os.readObject();
					showLoading = false;
					gameMenu.setEnabled(true);
					gameInProgressLabel.setText("AI loaded, now you can play!");
					done = true;
					loading.stop();
					is.close();
				} catch (MalformedURLException e) {
					gameInProgressLabel.setText("AI does not exist at URL");
				} catch (IOException e) {
					gameInProgressLabel.setText("IO error while loading AI");
				} catch (ClassNotFoundException e) {
					gameInProgressLabel.setText("AI class not found, whooops!");
				}
			}

			public NeuralNetPair getNeuralNetPair() {
				return nnp;
			}

			public boolean isDownloadComplete() {
				return done;
			}
		}
	}

}
