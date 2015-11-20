package required;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Serializing implements Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String word ;
	private String docid;
	
	
	public String getWord() {
		return word;
	}


	public void setWord(String word) {
		this.word = word;
	}


	public String getDocid() {
		return docid;
	}


	public void setDocid(String docid) {
		this.docid = docid;
	}
	
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		// TODO Auto-generated method stub
		Serializing s = new Serializing();
		s.setWord("hello");
		s.setDocid("2");
		FileOutputStream fo = new FileOutputStream("hahaObj.txt");
		ObjectOutputStream oos = new ObjectOutputStream(fo);
		oos.writeObject(s);
		oos.close();
		fo.close();
		// read the file now
		FileInputStream fi = new FileInputStream("hahaObj.txt");
		ObjectInputStream ooi = new ObjectInputStream(fi);
		s = (Serializing) ooi.readObject();
		ooi.close();
		System.out.println(s.getDocid());
		System.out.println(s.getWord());
	}

}
