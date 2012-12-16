package dialogs;


import java.awt.*;
import javax.swing.table.*;
import javax.swing.*;
import java.awt.event.*;

public class CheckerHelpAboutDialogApplet extends javax.swing.JDialog implements ActionListener{
	private JTable jTable1;
	private JTree jTree1;
	private JButton btnExit;

	/**
	* Auto-generated main method to display this JDialog
	*/

	
	public CheckerHelpAboutDialogApplet(JFrame frame) {
		super(frame,"About Checkers", true);
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
                TableModel jTable1Model = new DefaultTableModel(new String[][] {
                        { "One", "Two" }, { "Three", "Four" } }, new String[] {
                        "Column 1", "Column 2" });
                jTable1 = new JTable();
                this.getContentPane().add(
                    jTable1,
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
                jTable1.setModel(jTable1Model);
            }
            {
                jTree1 = new JTree();
                this.getContentPane().add(
                    jTree1,
                    new GridBagConstraints(
                        0,
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
	}

    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent arg0) {
        Object obj = arg0.getSource();
        if(obj == btnExit) {
            this.dispose();
        }
    }

}
