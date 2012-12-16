package dialogs;




import javax.swing.WindowConstants;


/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/

import javax.swing.*;
import java.awt.*;
import javax.swing.filechooser.FileFilter;


import java.awt.event.*;
import java.io.*;
import java.net.*;

import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.ListModel;
import javax.swing.JLabel;
import javax.swing.DefaultComboBoxModel;

import checkers_framework.CheckerInterface;
import checkers_framework.Strong_CheckerAI;

import evolutionary_framework.PrioQueue;
import evolutionary_nn.NeuralNetPair;
import evolutionary_nn.NeuralNetworkAI;

import java.awt.GridBagLayout;

public class CheckerNewGameDialogApplet extends JDialog implements ActionListener {
	private ButtonGroup btngrpWhite;
	private JLabel lblWhite;
	private JLabel lblBlack;
	private JRadioButton radBHuman;
	private JRadioButton radWRemote;
	private JTextField txtBID;
	private JList lstFoundIPs;
	private ListModel lstIPsModel;
	private JLabel lblDetectedIps;
	private JTextField txtSeconds;
	private JLabel lblspp;
	private JTextField txtWID;
	private JButton btnCancel;
	private JButton btnOk;
	private JLabel lblAiId2;
	private JButton btnPickBAi;
	private JRadioButton radBRemote;
	private JButton btnPickWAi;
	private JLabel lblAiId;
	private JRadioButton radWHuman;
	private JRadioButton radBAi;
	private JRadioButton radWAi;
	private ButtonGroup btngrpBlack;
	private int secondsperplayer;
	private CheckerInterface whiteAgent = null;
	private CheckerInterface blackAgent = null;
	
	private final String whitenamedefault = "White's Name";
	private final String blacknamedefault = "Black's Name";
	
	private boolean okPressed;
	
	private boolean AIChosen = false;
	private String lastName;
	private boolean whiteOnTop;
	

	/**
	* Auto-generated main method to display this JFrame
	*/
	
	public CheckerNewGameDialogApplet(JFrame parent){
    super(parent, "Start New Game",	true); // modal
		initGUI();
	}
	
	private void initGUI() {
		try {
			{
				btngrpBlack = new ButtonGroup();
			}
			{
				btngrpWhite = new ButtonGroup();
			}
			this.setSize(466, 288);
			GridBagLayout thisLayout = new GridBagLayout();
			thisLayout.columnWeights = new double[] {0.1,0.1,0.1,0.1};
			thisLayout.columnWidths = new int[] {7,7,7,7};
			thisLayout.rowWeights = new double[] {0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1};
			thisLayout.rowHeights = new int[] {7,7,7,7,7,7,7,7};
			this.getContentPane().setLayout(thisLayout);
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            {
            	
            	{
            	}
            	{
	            	//lstIPsModel = new ListModel();
            	}
                lblWhite = new JLabel();
                this.getContentPane().add(
                    lblWhite,
                    new GridBagConstraints(
                        0,
                        0,
                        1,
                        1,
                        0.0,
                        0.0,
                        GridBagConstraints.CENTER,
                        GridBagConstraints.NONE,
                        new Insets(0, 0, 0, 0),
                        0,
                        0));
                lblWhite.setText("White");
            }
            {
                lblBlack = new JLabel();
                this.getContentPane().add(
                    lblBlack,
                    new GridBagConstraints(
                        0,
                        3,
                        1,
                        1,
                        0.0,
                        0.0,
                        GridBagConstraints.CENTER,
                        GridBagConstraints.NONE,
                        new Insets(0, 0, 0, 0),
                        0,
                        0));
                lblBlack.setText("Black");
            }
            {
                radWHuman = new JRadioButton();
                getContentPane().add(radWHuman, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
                radWHuman.setText("Human");
                radWHuman.addActionListener(this);
            }
            {
                radWAi = new JRadioButton();
                this.getContentPane().add(
                    radWAi,
                    new GridBagConstraints(
                        2,
                        1,
                        1,
                        1,
                        0.0,
                        0.0,
                        GridBagConstraints.CENTER,
                        GridBagConstraints.NONE,
                        new Insets(0, 0, 0, 0),
                        0,
                        0));
                radWAi.setText("AI");
                radWAi.addActionListener(this);
            }
            {
                radBHuman = new JRadioButton();
                getContentPane().add(radBHuman, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
                radBHuman.setText("Human");
                radBHuman.addActionListener(this);
            }
            {
                radBAi = new JRadioButton();
                this.getContentPane().add(
                    radBAi,
                    new GridBagConstraints(
                        2,
                        4,
                        1,
                        1,
                        0.0,
                        0.0,
                        GridBagConstraints.CENTER,
                        GridBagConstraints.NONE,
                        new Insets(0, 0, 0, 0),
                        0,
                        0));
                radBAi.setText("AI");
                radBAi.addActionListener(this);
            }
            {
                lblAiId = new JLabel();
                this.getContentPane().add(
                    lblAiId,
                    new GridBagConstraints(
                        2,
                        2,
                        1,
                        1,
                        0.0,
                        0.0,
                        GridBagConstraints.CENTER,
                        GridBagConstraints.NONE,
                        new Insets(0, 0, 0, 0),
                        0,
                        0));
                lblAiId.setText("Identification:");
            }
            {
                btnPickWAi = new JButton();
                this.getContentPane().add(
                    btnPickWAi,
                    new GridBagConstraints(
                        3,
                        1,
                        1,
                        1,
                        0.0,
                        0.0,
                        GridBagConstraints.CENTER,
                        GridBagConstraints.NONE,
                        new Insets(0, 0, 0, 0),
                        0,
                        0));
                btnPickWAi.setText("Load .SER");
                btnPickWAi.addActionListener(this);
            }
            {
                radWRemote = new JRadioButton();
                getContentPane().add(radWRemote, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
                radWRemote.setText("Play on top");
                radWRemote.setToolTipText("When selected, the remote player will be this color");
                radWRemote.addActionListener(this);
            }
            {
                radBRemote = new JRadioButton();
                getContentPane().add(radBRemote, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
                radBRemote.setText("Play on top");
                radBRemote.setToolTipText("When selected, the remote player will be this color");
                radBRemote.addActionListener(this);
            }
            {
                btnPickBAi = new JButton();
                this.getContentPane().add(
                    btnPickBAi,
                    new GridBagConstraints(
                        3,
                        4,
                        1,
                        1,
                        0.0,
                        0.0,
                        GridBagConstraints.CENTER,
                        GridBagConstraints.NONE,
                        new Insets(0, 0, 0, 0),
                        0,
                        0));
                btnPickBAi.setText("Load .SER");
                btnPickBAi.addActionListener(this);
            }
            {
                lblAiId2 = new JLabel();
                this.getContentPane().add(
                    lblAiId2,
                    new GridBagConstraints(
                        2,
                        5,
                        1,
                        1,
                        0.0,
                        0.0,
                        GridBagConstraints.CENTER,
                        GridBagConstraints.NONE,
                        new Insets(0, 0, 0, 0),
                        0,
                        0));
                lblAiId2.setText("Identification:");
            }
            {
                btnOk = new JButton();
                this.getContentPane().add(
                    btnOk,
                    new GridBagConstraints(
                        2,
                        7,
                        1,
                        1,
                        0.0,
                        0.0,
                        GridBagConstraints.CENTER,
                        GridBagConstraints.NONE,
                        new Insets(0, 0, 0, 0),
                        0,
                        0));
                btnOk.setText("Start Game");
                btnOk.addActionListener(this);
            }
            {
                btnCancel = new JButton();
                this.getContentPane().add(
                    btnCancel,
                    new GridBagConstraints(
                        3,
                        7,
                        1,
                        1,
                        0.0,
                        0.0,
                        GridBagConstraints.CENTER,
                        GridBagConstraints.NONE,
                        new Insets(0, 0, 0, 0),
                        0,
                        0));
                btnCancel.setText("Cancel");
                btnCancel.addActionListener(this);
            }
            {
                txtWID = new JTextField(8);
                this.getContentPane().add(
                    txtWID,
                    new GridBagConstraints(
                        3,
                        2,
                        1,
                        1,
                        0.0,
                        0.0,
                        GridBagConstraints.CENTER,
                        GridBagConstraints.NONE,
                        new Insets(0, 0, 0, 0),
                        0,
                        0));
                txtWID.setDocument(new JTextFieldLimit(16));
                txtWID.setText(whitenamedefault);   
            }
            {
                txtBID = new JTextField(8);
                this.getContentPane().add(
                    txtBID,
                    new GridBagConstraints(
                        3,
                        5,
                        1,
                        1,
                        0.0,
                        0.0,
                        GridBagConstraints.CENTER,
                        GridBagConstraints.NONE,
                        new Insets(0, 0, 0, 0),
                        0,
                        0));
                txtBID.setDocument(new JTextFieldLimit(16));
                txtBID.setText(blacknamedefault);
            }
            {
                lblspp = new JLabel();
                this.getContentPane().add(
                    lblspp,
                    new GridBagConstraints(
                        0,
                        6,
                        1,
                        1,
                        0.0,
                        0.0,
                        GridBagConstraints.CENTER,
                        GridBagConstraints.NONE,
                        new Insets(0, 0, 0, 0),
                        0,
                        0));
                lblspp.setText("Seconds Per Player:");
            }
            {
                txtSeconds = new JTextField(2);
                this.getContentPane().add(
                    txtSeconds,
                    new GridBagConstraints(
                        1,
                        6,
                        1,
                        1,
                        0.0,
                        0.0,
                        GridBagConstraints.CENTER,
                        GridBagConstraints.NONE,
                        new Insets(0, 0, 0, 0),
                        0,
                        0));
                txtSeconds.setDocument(new JTextFieldLimit(4));
                txtSeconds.setText("300");
            }
            {
                lblDetectedIps = new JLabel();
                this.getContentPane().add(
                    lblDetectedIps,
                    new GridBagConstraints(
                        0,
                        7,
                        1,
                        1,
                        0.0,
                        0.0,
                        GridBagConstraints.CENTER,
                        GridBagConstraints.NONE,
                        new Insets(0, 0, 0, 0),
                        0,
                        0));
                lblDetectedIps.setText("Your Detected IPs:");
            }
            {
                ListModel lstFoundIPsModel = new DefaultComboBoxModel(
                    new String[] { "255.255.255.255" });
                lstFoundIPs = new JList();
                this.getContentPane().add(
                    lstFoundIPs,
                    new GridBagConstraints(
                        1,
                        7,
                        1,
                        1,
                        0.0,
                        0.0,
                        GridBagConstraints.CENTER,
                        GridBagConstraints.NONE,
                        new Insets(0, 0, 0, 0),
                        0,
                        0));
                lstFoundIPs.setModel(lstFoundIPsModel);
            }
		} catch (Exception e) {
			e.printStackTrace();
		}
		btngrpBlack.add(radBHuman);
		btngrpBlack.add(radBAi);
		btngrpWhite.add(radWHuman);
		btngrpWhite.add(radWAi);
		radBHuman.setSelected(false);
		radWHuman.setSelected(false);
		this.radBAi.setSelected(true);
		this.radWAi.setSelected(true);
		btnPickWAi.setEnabled(true);
		btnPickBAi.setEnabled(true);
		selectWAI(false);
		ButtonGroup grp = new ButtonGroup();
		grp.add(radWRemote);
		grp.add(radBRemote);
		radWRemote.setSelected(false);
		radBRemote.setSelected(true);
		 whiteOnTop = false;
		selectBAI(false);
		populateFoundIPs();
	}
	 public boolean okPressed(){
           return okPressed;
   }

   public boolean blackIsHuman() 
   {
           return radBHuman.isSelected();
   }

   public CheckerInterface whiteAgent() {
       return whiteAgent;
   }

   public CheckerInterface blackAgent() {
       return blackAgent;
   }
   public void setWhiteAgent(CheckerInterface w) {
       whiteAgent = w;
   }
   public void setBlackAgent(CheckerInterface b) {
       blackAgent = b;
   }
   public boolean whiteIsHuman()
   {
           return radWHuman.isSelected();
   }

   public int secondsPerPlayer()
   {
       String s = txtSeconds.getText();
       int seconds = 120;
       try { seconds = Integer.parseInt(s); }
       catch (NumberFormatException e) { }
       return seconds;  // will be 120 if text is invalid
   }
    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void setSecondsPerPlayer(int s) {
        txtSeconds.setText(String.valueOf(s));
    }
    public void setWhiteName(String n) {
        txtWID.setText(n);
    }
    public void setBlackName(String n) {
        txtBID.setText(n);
    }
   	public void selectWRemote() {
   	  
      whiteOnTop = true;
       txtWID.setText(""); 
   	}
   	public void selectWHuman() {
   	   txtWID.setEnabled(true);
       radBRemote.setEnabled(true);
       btnPickWAi.setEnabled(false);
    
       txtWID.setText("");
   	}
	//////////////////////////////////////////////
   	public void selectWAI(boolean load) {
   	   txtWID.setEnabled(false);
       radBRemote.setEnabled(true);
       btnPickWAi.setEnabled(true);
       if(load) {
	       NeuralNetPair n =  loadNNP();
	       if(n != null){
	    	   whiteAgent = new NeuralNetworkAI(n);
	    	   txtWID.setText(whiteAgent.getName());
	    	   whiteAgent.setColor(CheckerInterface.WH);
	       }
       } else {
    	   whiteAgent = new NeuralNetworkAI();
    	   txtWID.setText("white devil");
    	   whiteAgent.setColor(CheckerInterface.WH);
       }
       
   	}
   
  	public void selectBAI(boolean load) {
        txtBID.setEnabled(false);
        radWRemote.setEnabled(true);
        btnPickBAi.setEnabled(true);
        if(load) {
	        NeuralNetPair n =  loadNNP();
	        if(n != null){
	        	blackAgent = new NeuralNetworkAI(n);
	        	txtBID.setText(lastName);
	        	blackAgent.setColor(CheckerInterface.BL);
	        	
	        }
        } else {
        	blackAgent = new Strong_CheckerAI();
        	txtBID.setText("black hawk");
        	blackAgent.setColor(CheckerInterface.BL);
        }
   	}
  	 ////////////////////////////////////////////////////
   	private void loadWAI(File fileWAI) {
        if(fileWAI != null) {
            try {
                whiteAgent = getCheckerInterfaceFromFile(fileWAI);
                txtWID.setText(lastName);
                whiteAgent.setColor(CheckerInterface.WH);
            } catch (Exception e1) {
                txtWID.setForeground(Color.RED);
                txtWID.setText("File NOT an AI");
                e1.printStackTrace();
            }       
        }
   	}

   	public void selectBRemote() {
   	   whiteOnTop = true;
        blackAgent = null;
        txtBID.setText(whitenamedefault);
   	}
   	public void selectBHuman() {
        txtBID.setEnabled(true);
        radWRemote.setEnabled(true);
        btnPickBAi.setEnabled(false);
        blackAgent = null;
        txtBID.setText(whitenamedefault);
   	}
   	public NeuralNetPair loadNNP(){
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new File("."));
	    // Note: source for ExampleFileFilter can be found in FileChooserDemo,
	    // under the demo/jfc directory in the Java 2 SDK, Standard Edition.
		NeuralNetPair pair = null;
	    int returnVal = chooser.showOpenDialog(this);
	    if(returnVal == JFileChooser.APPROVE_OPTION) {
	       File save = chooser.getSelectedFile();
	       lastName = save.getName();
			ObjectInputStream oin;
			try {
				oin = new ObjectInputStream(new FileInputStream(save));
				PrioQueue p = (PrioQueue) oin.readObject();
				pair = NeuralNetPair.getBestMember(p);
				System.out.println("loaded brain from: "+chooser.getSelectedFile().getName());
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
	    }
	    return pair;
   	}
 
   	private void loadBAI(File fileBAI) {
   	   if(fileBAI != null) {
           try {
               blackAgent = getCheckerInterfaceFromFile(fileBAI);
               txtBID.setText(blackAgent.getName());
               blackAgent.setColor(CheckerInterface.BL);
           } catch (Exception e1) {
               txtBID.setForeground(Color.RED);
               txtBID.setText("File NOT an AI");
               e1.printStackTrace();
           }
       }
   	}
    public void actionPerformed(ActionEvent e) {
        //do whites actions first:
        Object obj = e.getSource();
        if(obj == radWRemote) { //remote 
            selectWRemote();
        }
        if(obj == radWHuman) { // human
            selectWHuman();
        }
        if(obj == radWAi) { //AI
             selectWAI(false);
        }
        if(obj == btnPickWAi) {
        	 selectWAI(true);
        }
        //do blacks actions:
        if(obj == radBRemote) { //remote 
            selectBRemote();
        }
        if(obj == radBHuman) { // human
            selectBHuman();
        }
        if(obj == radBAi) { //AI
            selectBAI(false);
        }
        if(obj == btnPickBAi) {
        	selectBAI(true);
        }
        if (obj == btnOk) {
	        okPressed = true;
	        
	        processWindowEvent(
            new WindowEvent(
	            this,
	            WindowEvent.WINDOW_CLOSING));
        }
        else if (obj == btnCancel) {
	        okPressed = false;
	        this.dispose();
        }
    }

    private File loadAIFromClass() {
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File("."));
        chooser.setFileFilter(new ExtensionFilter("class", "Java Class File"));
        if (chooser.showOpenDialog(this) !=
            JFileChooser.APPROVE_OPTION)
            return null;
        File f = chooser.getSelectedFile();
        if(f.getName().toLowerCase().endsWith(".class")) {
            return f;
        }
        return null;
    }
	public String getWhiteName(){
		if ( whiteAgent== null)
			return txtWID.getText();
		else
			return whiteAgent.getName();
	}
	public String getBlackName(){
		if (blackAgent== null)
			return txtBID.getText();
		else
			return blackAgent.getName();
	}
	private void populateFoundIPs()  {
	    InetAddress   in;
	    InetAddress[] all;
	    String[] foundIPs;
	    lstFoundIPs.setListData(new String[] {"None Found"});
        try {
            in = InetAddress.getLocalHost();
            all = InetAddress.getAllByName(in.getHostName());
            foundIPs = new String[all.length];
      	    for (int i=0; i<all.length; i++) {
      	        String temp = all[i].toString();
      	        int loc = temp.lastIndexOf("/");
      	        if(loc >= 0 || temp.length() >0)
      	            foundIPs[i] = temp.substring(temp.lastIndexOf("/")+1, temp.length());
      	    }
      	    if(foundIPs.length > 0)
      	        lstFoundIPs.setListData(foundIPs);
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
	}
    private CheckerInterface getCheckerInterfaceFromFile(File f) throws Exception {
    	URL urls[] = null;
	    URL blee;
	    CheckerInterface ci = null;
	    blee = f.getParentFile().toURL();
	    urls = new URL[] {blee};
	    ClassLoader loader = new URLClassLoader(urls);       
	    Class cls = loader.loadClass(f.getName().substring(0,f.getName().length()-6));
	    ci =(CheckerInterface) cls.newInstance();
	    return ci;
    }

	public boolean isWhiteOnTop() {
		return whiteOnTop;
	}
}
