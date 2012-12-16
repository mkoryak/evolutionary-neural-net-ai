package evolutionary_nn;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertyReader extends Properties {

	private static PropertyReader instance = null;
	private File props = new File("nn.properties");
	private PropertyReader() {
		try {
			readProps();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static PropertyReader getInstance(){
		if(instance == null)
			instance = new PropertyReader();
		return instance;
	}
	private void writeProps() throws IOException{
		if(!props.exists()) props.createNewFile();
		
		FileOutputStream st = new FileOutputStream(props);
		this.put("test1","test2");

		
		this.store(st, "");
	}
	public int getIntProperty(String p){
		return Integer.parseInt(this.getProperty(p));
	}
	private void readProps() throws IOException{
		FileInputStream st = new FileInputStream(props);
		this.load(st);
	}
	public static void main(String[] a){
		PropertyReader p = new PropertyReader();
		try {
			p.writeProps();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
