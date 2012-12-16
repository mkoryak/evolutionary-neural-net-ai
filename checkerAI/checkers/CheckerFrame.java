package checkers;

// CyberthelloFrame.java
//package tournament_checkers;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.swing.*;

import checkers_framework.CheckerInterface;
import checkers_framework.Move;




public class CheckerFrame extends JFrame implements ActionListener, Runnable {
	private static final Color bgColor = Color.black;
	private static final Color textFgColor = Color.white;
	private JPanel mainPanel;
	private GridBagLayout mainLayout;
	private JLabel gameInProgressLabel;
	private JLabel blackLabel;
	private JLabel blackScoreLabel;
	private JLabel whiteLabel;
	private JLabel whiteScoreLabel;
	private JLabel blackTimeRemainingLabel, whiteTimeRemainingLabel;
	private CheckerPane cyberthelloPane;
	private JLabel infoLabel;
	private JMenuItem fileNewGame_MenuItem;
	private JMenuItem fileTournament_MenuItem;
	private JMenuItem fileExit_MenuItem;
	private JMenuItem fileNewRemoteGame_MenuItem;
	private JMenuItem gameLogGame_MenuItem;
	private JMenuItem gameReplayLog_MenuItem;
	private JMenuItem helpHelp;
	private JMenuItem helpAbout;
	private CheckerGame currentGame;
	private Timer timer;
	private String whitename;
	private String blackname;

	public CheckerFrame() {		currentGame = null;		setSize(488, 600);
		setTitle("Java Checkers");		buildMenu();		buildUI();		timer = new Timer(1000, // one second
						        new ActionListener() {
									public void actionPerformed(ActionEvent e) {										updateClock();									}
								}						 );
		addWindowListener(
							new WindowAdapter() {								public void windowClosing(WindowEvent e) {									fileExit();								}							}						 );
		Dimension dim = getToolkit().getScreenSize();
		this.setLocation(dim.width/2 - this.getWidth()/2,
		        dim.height/2 - this.getHeight()/2);
		refreshUI();
	}

  private void buildMenu() {
    JMenuBar menuBar = new JMenuBar();
    JMenu fileMenu = new JMenu("File");
    
    JMenu gameMenu = new JMenu("Game");
    JMenu helpMenu = new JMenu("Help");
    helpHelp = new JMenuItem("Help");
    helpAbout = new JMenuItem("About");
    helpHelp.addActionListener(this);
    helpAbout.addActionListener(this);
    helpMenu.add(helpHelp);
    helpMenu.add(helpAbout);
    
    gameLogGame_MenuItem = new JMenuItem("Save Game Log");
    gameLogGame_MenuItem.addActionListener(this);
    gameMenu.add(gameLogGame_MenuItem);
    gameReplayLog_MenuItem = new JMenuItem("Replay Game");
    gameReplayLog_MenuItem.addActionListener(this);
    gameMenu.add(gameReplayLog_MenuItem);
    
    fileNewGame_MenuItem = new JMenuItem("New Game...");
    fileNewGame_MenuItem.addActionListener(this);
    fileMenu.add(fileNewGame_MenuItem);
    
    fileNewRemoteGame_MenuItem = new JMenuItem("Join Remote Game...");
    fileNewRemoteGame_MenuItem.addActionListener(this);
    fileMenu.add(fileNewRemoteGame_MenuItem);
    
    fileTournament_MenuItem = new JMenuItem("Start a Tournament");
    fileTournament_MenuItem.addActionListener(this);
    fileMenu.add(fileTournament_MenuItem);
    
    fileExit_MenuItem = new JMenuItem("Exit");
    fileExit_MenuItem.addActionListener(this);
    fileMenu.add(fileExit_MenuItem);

    menuBar.add(fileMenu);
    menuBar.add(gameMenu);
    menuBar.add(helpMenu);

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
    mainLayout.setConstraints(
        gameInProgressLabel,
        new GridBagConstraints(
        0, 0, 3, 1, 0.0, 0.0,
        GridBagConstraints.CENTER, GridBagConstraints.NONE,
        new Insets(5, 5, 5, 5), 0, 0));

    blackLabel = new JLabel("Black: ");
    blackLabel.setForeground(textFgColor);
    mainPanel.add(blackLabel);
    mainLayout.setConstraints(
        blackLabel,
        new GridBagConstraints(
        0, 1, 1, 1, 1.0, 0.0,
        GridBagConstraints.EAST, GridBagConstraints.NONE,
        new Insets(10, 5, 2, 5), 0, 0));

    blackScoreLabel = new JLabel("2");
    blackScoreLabel.setForeground(textFgColor);
    mainPanel.add(blackScoreLabel);
    mainLayout.setConstraints(
        blackScoreLabel,
        new GridBagConstraints(
        1, 1, 1, 1, 1.0, 0.0,
        GridBagConstraints.WEST, GridBagConstraints.NONE,
        new Insets(10, 5, 2, 5), 0, 0));

    blackTimeRemainingLabel = new JLabel(" ");
    blackTimeRemainingLabel.setForeground(textFgColor);
    mainPanel.add(blackTimeRemainingLabel);
    mainLayout.setConstraints(
        blackTimeRemainingLabel,
        new GridBagConstraints(
        2, 1, 1, 1, 1.0, 0.0,
        GridBagConstraints.WEST, GridBagConstraints.NONE,
        new Insets(10, 5, 2, 5), 0, 0));

    whiteLabel = new JLabel("White: ");
    whiteLabel.setForeground(textFgColor);
    mainPanel.add(whiteLabel);
    mainLayout.setConstraints(
        whiteLabel,
        new GridBagConstraints(
        0, 2, 1, 1, 0.0, 0.0,
        GridBagConstraints.EAST, GridBagConstraints.NONE,
        new Insets(2, 5, 5, 5), 0, 0));

    whiteScoreLabel = new JLabel("2");
    whiteScoreLabel.setForeground(textFgColor);
    mainPanel.add(whiteScoreLabel);
    mainLayout.setConstraints(
        whiteScoreLabel,
        new GridBagConstraints(
        1, 2, 1, 1, 0.0, 0.0,
        GridBagConstraints.WEST, GridBagConstraints.NONE,
        new Insets(2, 5, 5, 5), 0, 0));

    whiteTimeRemainingLabel = new JLabel(" ");
    whiteTimeRemainingLabel.setForeground(textFgColor);
    mainPanel.add(whiteTimeRemainingLabel);
    mainLayout.setConstraints(
        whiteTimeRemainingLabel,
        new GridBagConstraints(
        2, 2, 1, 1, 1.0, 0.0,
        GridBagConstraints.WEST, GridBagConstraints.NONE,
        new Insets(10, 5, 2, 5), 0, 0));

    cyberthelloPane = new CheckerPane(this);
    mainPanel.add(cyberthelloPane);
    mainLayout.setConstraints(
        cyberthelloPane,
        new GridBagConstraints(
        0, 3, 3, 1, 1.0, 1.0,
        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
        new Insets(10, 10, 10, 10), 0, 0));

    infoLabel = new JLabel("!");
    infoLabel.setForeground(textFgColor);
    mainPanel.add(infoLabel);
    mainLayout.setConstraints(
        infoLabel,
        new GridBagConstraints(
        0, 4, 3, 1, 1.0, 0.0,
        GridBagConstraints.CENTER, GridBagConstraints.NONE,
        new Insets(5, 5, 5, 5), 0, 0));
    gameLogGame_MenuItem.setEnabled(false);
    ImageIcon image = new ImageIcon("icon.gif");
    this.setIconImage(image.getImage());
   
  }

public void refreshUI() {
	cyberthelloPane.repaint();

	if (currentGame == null) {
		gameInProgressLabel.setText("No game in progress");
		setTitle("Java Checkers");
		blackLabel.setText("");
		blackScoreLabel.setText("");
		whiteLabel.setText("");
		whiteScoreLabel.setText("");
	}
	else {
		if (currentGame.isGameOver()) {
			gameInProgressLabel.setText("No game in progress");
			infoLabel.setText("Game over: " + currentGame.getWinner());
		}
		else{
			if (currentGame.isBlacksMove()) {
				gameInProgressLabel.setText("Game in progress: "+blackname+"'s move (Black)");
				
				if (currentGame.blackIsHuman()) 
					infoLabel.setText("Waiting for Black ("+blackname+")  to move...");
				else 
					infoLabel.setText("Black ("+blackname+") is deciding on a move...");
			}
			else {
				gameInProgressLabel.setText("Game in progress: "+whitename+"'s move (White)");
	
				if (currentGame.whiteIsHuman())
					infoLabel.setText("Waiting for White ("+whitename+") to move...");
				else
					infoLabel.setText("White ("+whitename+") is deciding on a move...");
			}
		}

		blackLabel.setText("("+blackname+") Black : ");
		blackScoreLabel.setText("" + currentGame.getBlackScore());
		whiteLabel.setText("("+whitename+") White : ");
		whiteScoreLabel.setText("" + currentGame.getWhiteScore());
	}
}

  private void updateClock() {
    if (currentGame == null) {
      return;
    }

    int blackSeconds = (int) currentGame.getRemainingTime(Checker.BLACK) / 1000;
    int whiteSeconds = (int) currentGame.getRemainingTime(Checker.WHITE) / 1000;

    int blackMinutes = blackSeconds / 60;
    blackSeconds -= blackMinutes * 60;
    blackTimeRemainingLabel.setText("" + blackMinutes + ":" +
                                    (blackSeconds < 10 ? "0" : "") +
                                    blackSeconds +
                                    " remaining");

    int whiteMinutes = whiteSeconds / 60;
    whiteSeconds -= whiteMinutes * 60;
    whiteTimeRemainingLabel.setText("" + whiteMinutes + ":" +
                                    (whiteSeconds < 10 ? "0" : "") +
                                    whiteSeconds +
                                    " remaining");

    if (currentGame.isGameOver()) {
      timer.stop();
    }
  }
	private synchronized void fileNewRemoteGame() {

	}
	private synchronized void gameSaveLog() {
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new File("."));
		File f = null;
		while(f == null) {
			if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION)
				return;
			f = chooser.getSelectedFile();
			if(f.exists()) {
				Object[] options = { "OK", "CANCEL" };
				int choice = JOptionPane.showOptionDialog(null, "Overwrite existing file?", "Warning: file exists.", 
											 			  JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, 
														  null, options, options[0]);
				if ( choice == 0 )
					f.delete();
				else
					f = null;
			}
		}
	    
		if(f != null) {
			boolean success = true;
			String name = f.getName();
	  		int loc = name.lastIndexOf(".");
	  		if(loc == -1) {
	  		    name += ".log";
	  		    f = new File(f.getParent(),name);
	  		}
	 // 		success = currentGame.getLogger().saveLog(f);	    
	  		if(!success) {
	  			Object[] options = { "OK" };
				int choice = JOptionPane.showOptionDialog(null, "Unable to save file.", "Error", 
		 			  JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, 
					  null, options, options[0]);
				
	      }
	    }
	}
	private synchronized void helpHelp() {
	    CheckerHelpHelpDialog d = new CheckerHelpHelpDialog(this);
	    d.setLocationRelativeTo(this);
	    d.setVisible(true);
	}
	private synchronized void replayGame() {
      return;
	}
	private synchronized void helpAbout() {
	    CheckerHelpAboutDialog d = new CheckerHelpAboutDialog(this);
	    d.setLocationRelativeTo(this);
	    d.setVisible(true);
	}
	private synchronized void startTournament() {

	}
	public void playgame(CheckerGame game, int i, String black, String white){
		this.blackname = black;
		this.whitename = white;
		currentGame = game;
		cyberthelloPane.setGame(currentGame);
    	refreshUI();
    	currentGame.start();
    	timer.start();
    	setTitle("Java Checkers Tournament : Playing Game " + i);
	}
	private synchronized void fileNewGame() 
	{
	    //TODO: look here for an example on how to create a graphical game.
		
	    CheckerNewGameDialog d = new CheckerNewGameDialog(this);
	    d.setLocationRelativeTo(this);
	    d.setVisible(true);
	    whitename = d.getWhiteName();
	    blackname = d.getBlackName();
	    if (d.okPressed()) {
        if (currentGame != null) {
  	    		currentGame.endGame();
  	    		notify(); // wake up any waiting threads
  	    	}
	    }
	    	currentGame = new CheckerGame( d.secondsPerPlayer(), d.blackAgent(), d.whiteAgent(), this
	    	        ,whitename, blackname,1000,d.isWhiteOnTop());
	    	if (d.blackAgent()!= null) d.blackAgent().setColor(CheckerInterface.BL);
	    	if (d.whiteAgent()!= null) d.whiteAgent().setColor(CheckerInterface.WH);
	    	cyberthelloPane.setGame(currentGame);
	    	refreshUI();
	    	currentGame.start();
	    	timer.start();
	    	gameLogGame_MenuItem.setEnabled(true);
	    	setTitle("Java Checkers: Playing Game");
	    
	}

  private void fileExit() {
    System.exit(0);
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
    		legalMove = currentGame.makeHumanMove(currrow, currcol, sucrow, succol, Checker.BLACK);
    		
    		if (! (legalMove == 1 || legalMove == 3))
    			return;
    	}
    	else {
    		if (!currentGame.whiteIsHuman()) // ignore spurious clicks
    			return;
    		legalMove = currentGame.makeHumanMove(currrow, currcol, sucrow, succol, Checker.WHITE);
    		
    		if (! (legalMove == 1 || legalMove == 3))
    			return;
    	}

    	// Valid human move: wake up all threads waiting on this object
    	// (there is just one, of course).
    	Move m = new Move(currrow, currcol, sucrow, succol);
	     
    	if (legalMove==1) 
    		notify();
    	refreshUI();
	}

  public void actionPerformed(ActionEvent e) {
    Object obj = e.getSource();

    if (obj == fileNewGame_MenuItem) {
      fileNewGame();
    }
    else if (obj == fileExit_MenuItem) {
      fileExit();
    } else if(obj == fileNewRemoteGame_MenuItem) {
        fileNewRemoteGame();
    } else if(obj == gameLogGame_MenuItem) {
        gameSaveLog();
    } else if(obj == helpHelp) {
        helpHelp();
    } else if(obj == helpAbout) {
        helpAbout();
    } else if(obj == gameReplayLog_MenuItem) {
        replayGame();
    } else if(obj == fileTournament_MenuItem) {
        startTournament();
    }
  }
  public void enterTournyMode(){
    	fileNewGame_MenuItem.setEnabled(false);
    	fileTournament_MenuItem.setEnabled(false);
      fileNewRemoteGame_MenuItem.setEnabled(false);
    	gameLogGame_MenuItem.setEnabled(false);
    	gameReplayLog_MenuItem.setEnabled(false);
  }
  public void exitTournyMode(){
    	fileNewGame_MenuItem.setEnabled(true);
    	fileTournament_MenuItem.setEnabled(true);
      fileNewRemoteGame_MenuItem.setEnabled(true);
    	gameLogGame_MenuItem.setEnabled(true);
    	gameReplayLog_MenuItem.setEnabled(true);
  }
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {}
}

