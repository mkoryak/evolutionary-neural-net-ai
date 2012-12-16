package checkers_framework;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import javax.swing.JButton;

import javax.swing.JFileChooser;
import javax.swing.WindowConstants;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;


/**
* This code was generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* *************************************
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED
* for this machine, so Jigloo or this code cannot be used legally
* for any corporate or commercial purpose.
* *************************************
*/
/**
 * I am sick of writing the same code over and over again, this is the last time im ever writing a file loading component
 */
public class GuiBrowsePanel extends javax.swing.JPanel implements ActionListener {
	private JTextField txtPath;
	private JLabel lblLabel;
	private JButton btnBrowse;
	
	private ArrayList listeners = new ArrayList();

	private String loadPath = "";
	
	private String[] acceptedFileTypes = new String[]{".*"};//accept all by defualt
	private boolean acceptedDirsOnly = false;
	/**
	* Auto-generated main method to display this 
	* JPanel inside a new JFrame.
	*/
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.getContentPane().add(new GuiBrowsePanel());
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
	
	public GuiBrowsePanel() {
		super();
		initLoadPath();
		initGUI();
	}
	
	private void initGUI() {
		try {
			GridBagLayout thisLayout = new GridBagLayout();
			thisLayout.columnWeights = new double[] {0.1,2.1,0.1};
			thisLayout.columnWidths = new int[] {7,7,7};
			thisLayout.rowWeights = new double[] {0.1};
			thisLayout.rowHeights = new int[] {7};
			this.setLayout(thisLayout);
			this.setPreferredSize(new java.awt.Dimension(378, 28));
			{
				txtPath = new JTextField();
				this.add(txtPath, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				txtPath.setText(loadPath);
				txtPath.addActionListener(this);
			}
			{
				btnBrowse = new JButton();
				this.add(btnBrowse, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				btnBrowse.setText("..");
				btnBrowse.addActionListener(this);
			}
			{
				lblLabel = new JLabel();
				this.add(lblLabel, new GridBagConstraints(
					0,
					0,
					1,
					1,
					0.0,
					0.0,
					GridBagConstraints.CENTER,
					GridBagConstraints.HORIZONTAL,
					new Insets(0, 0, 0, 0),
					0,
					0));
				lblLabel.setText("Select file:");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void initLoadPath(){
		String currentPath = System.getProperty("user.dir");
		File longPath = new File(currentPath);
		loadPath = longPath.getAbsolutePath();
	}


    public String startFileChooser(Container parent) {
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File(loadPath));
        
        FileFilter filter;
        filter = new FileTypeFilter(acceptedFileTypes);
        chooser.setFileFilter(filter);
        if(acceptedDirsOnly) {
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        } else {
            chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        }
        
        if (chooser.showOpenDialog(parent) !=
            JFileChooser.APPROVE_OPTION)
            return null;
        return chooser.getSelectedFile().getAbsolutePath();
    }
    public static class FileTypeFilter extends FileFilter
    {
      
      private String[] acceptedTypes;
      public FileTypeFilter(String[] accepted){
    	  acceptedTypes = accepted;
      }
      public String getDescription() {
          return Arrays.asList(acceptedTypes)+" file types";
      }
      public boolean accept(File f) {   
        if(f == null)
            return false;
        if(f.isDirectory())
        	return true;
        for (int i = 0; i < acceptedTypes.length; i++) {
			if(f.getName().toLowerCase().endsWith(acceptedTypes[i].toLowerCase())|| acceptedTypes[i].equals(".*"))
				return true;
		}
        return false;
      }
    }
	public void actionPerformed(ActionEvent arg0) {
		Object src = arg0.getSource();
		if(src.equals(btnBrowse)){
			String temp = startFileChooser(this);
			if(temp != null) {
				loadPath = temp;
				txtPath.setText(loadPath);
				notifyListners();
			}
		} else {
			loadPath = txtPath.getText();
			notifyListners();
		}
		
	}
	public void addActionListner(ActionListener obj){
		listeners.add(obj);
	}
	private void notifyListners(){
		ActionEvent event = new ActionEvent(this, 0, loadPath);
		for (Iterator iter = listeners.iterator(); iter.hasNext();) {
			ActionListener element = (ActionListener) iter.next();
			element.actionPerformed(event);
		}
	}
	/**
	 * get the String pathname of the current contents of the textbox
	 * @return
	 */
	public String getSelectedPath(){
		return loadPath;
	}
	/**
	 * sets the label to text you want
	 * @param text
	 */
	public void setLabel(String text){
		lblLabel.setText(text);
	}
    /**
     * Sets up JFileChooser to show only specified filetypes when browsing. 
     * valid filetypes must start with DOT end in extension. Ex: ".xml"
     * @param types
     */
    public void setAcceptedFileTypes(String[] types){
    	acceptedFileTypes = types;
    }
    public void setDirectoriesOnlyAccepted(boolean dirs){
    	acceptedDirsOnly = dirs;
    }
    /**
     * set the currenly selected path, if path doesnt exist, and mustExist is true path initilized to user directory
     * @param path
     */
    public void setSelectedPath(String path, boolean mustExist){
    	if(mustExist){
	    	if((new File(path)).exists()){
	    		loadPath = path;
	    		txtPath.setText(loadPath);
	    	} else {
	    		initLoadPath();
	    	}
    	} else { 
    		loadPath = path;
    		txtPath.setText(loadPath);
    	}
    }
	

}
