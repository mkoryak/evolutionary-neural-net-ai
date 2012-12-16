package dialogs;



import javax.swing.JFrame;
import java.awt.*;
import javax.swing.*;

import crapplet.CheckerFrameApplet;

import java.awt.event.*;

public class CheckerHelpHelpDialogApplet extends javax.swing.JDialog implements ActionListener{
	private JTextPane jTextPane1;
	private JButton btnExit;
	private JTextField txtBID;
	private JTextField txtSeconds;
	private JLabel lblspp;
	private JTextField txtWID;
	private JButton btnCancel;
	private JButton btnOk;
	private JLabel lblAiId2;
	private JButton btnPickBAi;
	private JRadioButton radWHuman;
	private JRadioButton radBHuman;
	private JRadioButton radWRemote;
	private JRadioButton radBRemote;
	private JButton btnPickWAi;
	private JLabel lblAiId;
	private JRadioButton radBAi;
	private JRadioButton radWAi;
	private JLabel lblBlack;
	private JLabel lblWhite;
	private JDialog jDialog1;

	/**
	* Auto-generated main method to display this JDialog
	*/
	
	public CheckerHelpHelpDialogApplet(CheckerFrameApplet applet) {
	  super(null, "Help", true);
		initGUI();
	}
	
	private void initGUI() {
		try {
			GridBagLayout thisLayout = new GridBagLayout();
			thisLayout.columnWeights = new double[] {0.1};
			thisLayout.columnWidths = new int[] {7};
			thisLayout.rowWeights = new double[] {0.1,0.1,0.1};
			thisLayout.rowHeights = new int[] {7,7,7};
			this.getContentPane().setLayout(thisLayout);
			setSize(400, 300);
            {
            	{
	            	jDialog1 = new JDialog(this);
	            	GridBagLayout jDialog1Layout = new GridBagLayout();
	            	jDialog1Layout.columnWidths = new int[] {7,7,7,7,7,7,7,7,7,7,7,7};
	            	jDialog1Layout.rowHeights = new int[] {7,7,7,7,7,7,7,7,7,7,7,7};
	            	jDialog1Layout.columnWeights = new double[] {0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1};
	            	jDialog1Layout.rowWeights = new double[] {0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1};
	            	jDialog1.getContentPane().setLayout(thisLayout);
	            	jDialog1.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	            	jDialog1.setSize(8, 27);
            	}
            	{
	            	lblWhite = new JLabel();
	            	lblWhite.setText("White");
            	}
            	{
	            	lblBlack = new JLabel();
	            	lblBlack.setText("Black");
            	}
            	{
	            	radWHuman = new JRadioButton();
	            	radWHuman.setText("Human");
	            	radWHuman.setSelected(true);
                    radWHuman.addActionListener(this);
            	}
            	{
	            	radWAi = new JRadioButton();
	            	radWAi.setText("AI");
                    radWAi.addActionListener(this);
            	}
            	{
	            	radBHuman = new JRadioButton();
	            	radBHuman.setText("Human");
	            	radBHuman.setSelected(true);
                    radBHuman.addActionListener(this);
            	}
            	{
	            	radBAi = new JRadioButton();
	            	radBAi.setText("AI");
                    radBAi.addActionListener(this);
            	}
            	{
	            	lblAiId = new JLabel();
	            	lblAiId.setText("Identification:");
            	}
            	{
	            	btnPickWAi = new JButton();
	            	btnPickWAi.setEnabled(false);
	            	btnPickWAi.setText("Pick AI");
                    btnPickWAi.addActionListener(this);
            	}
            	{
	            	radWRemote = new JRadioButton();
	            	radWRemote.setText("Remote");
	            	radWRemote.setToolTipText("When selected, the remote player will be this color");
                    radWRemote.addActionListener(this);
            	}
            	{
	            	radBRemote = new JRadioButton();
	            	radBRemote.setText("Remote");
	            	radBRemote.setToolTipText("When selected, the remote player will be this color");
                    radBRemote.addActionListener(this);
            	}
            	{
	            	btnPickBAi = new JButton();
	            	btnPickBAi.setEnabled(false);
	            	btnPickBAi.setText("Pick AI");
                    btnPickBAi.addActionListener(this);
            	}
            	{
	            	lblAiId2 = new JLabel();
	            	lblAiId2.setText("Identification:");
            	}
            	{
	            	btnOk = new JButton();
	            	btnOk.setText("Start Game");
                    btnOk.addActionListener(this);
            	}
            	{
	            	btnCancel = new JButton();
	            	btnCancel.setText("Cancel");
                    btnCancel.addActionListener(this);
            	}
            	{
	            	txtWID = new JTextField();
	            	txtWID.setText("Harry Richards");
            	}
            	{
	            	txtBID = new JTextField();
	            	txtBID.setText("Captain Planet");
            	}
            	{
	            	lblspp = new JLabel();
	            	lblspp.setText("Seconds Per Player:");
            	}
            	{
	            	txtSeconds = new JTextField();
	            	txtSeconds.setText("300");
            	}
                jTextPane1 = new JTextPane();
                this.getContentPane().add(
                    jTextPane1,
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
            }
            {
                btnExit = new JButton();
                this.getContentPane().add(
                    btnExit,
                    new GridBagConstraints(
                        0,
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
                btnExit.setText("Exit");
                btnExit.addActionListener(this);
            }
		} catch (Exception e) {
			e.printStackTrace();
		}
		jTextPane1.setText("How to determine your IP address:\n"+
		        "Windows XP: Start / Run / type ‘cmd’ / type ‘ipconfig’\n"+ 
		        "Windows 98: Start / Run / type ‘winipcfg’\n"+
		        "FreeBSD: type ‘ifconfig’\n\n"+
		        "If all else fails go to www.whatismyip.com"); 
		jTextPane1.setEditable(false);
		jTextPane1.setEnabled(false);
	}

    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent arg0) {
        Object o = arg0.getSource();
        if(o == btnExit) {
            this.dispose();
        }
    }

}
