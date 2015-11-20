package required;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Assignment3v03 {
	private String docid;
	private int record;
	public Assignment3v03(String docid)
	{
		this.docid = docid;
		this.record= 1;
	}
	
	public String getDocid() {
		return docid;
	}


	public void setDocid(String docid) {
		this.docid = docid;
	}


	public int getRecord() {
		return record;
	}


	public void setRecord(int record) {
		this.record = record;
	}


	public static void main (String args[]) throws IOException
	{
		HashMap<String, ArrayList<Assignment3v03>> indexer = new HashMap<>();
		HashMap<String, Integer> documentLength = new HashMap<>();
		String whileStr;
		String docid = "1";
		String accum ="";
		System.out.println("Enter the File to be read");
		Scanner s = new Scanner(System.in);
		String fileName = s.nextLine();
		s.close();
		FileReader fr = null;
		try {
			fr = new FileReader(fileName);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedReader br = new BufferedReader(fr);
		br.readLine();// to escape the first line;;
		while((whileStr = br.readLine())!=null)
		{
			
			// read line until EOF
			if(!whileStr.contains("#"))
			{
				// normal line
				accum = accum.concat(whileStr+" ");
			}
			else
			{
				System.out.println(docid);
				// # symbol found
				// find no of legal words
				ArrayList<String> legalWords = legalWords(accum);
				//adding to hashmap document length
				documentLength.put(docid, legalWords.size());
				// call the recorder here
				indexer.putAll(updateIndex(indexer,accum,legalWords,docid));
				docid = whileStr.split(" ")[1];
			}
		}
	}
	
	

	private static ArrayList<String> legalWords(String accum) {
		String[] words = accum.split(" ");
		ArrayList<String> legalWords = new ArrayList<>();
		for(String word:words)
			if(!isInteger(word))
				legalWords.add(word);
		return legalWords;
	}


	private static HashMap<String,ArrayList<Assignment3v03>> updateIndex(
			HashMap<String, ArrayList<Assignment3v03>> indexer, String accum, ArrayList<String> legalWords,
			String docid) 	
			{
		for(int i=0;i<legalWords.size();i++)
		{
			// start checking each word and insert/add in the required object.
			String word = legalWords.get(i);
			
			//check if the word has been seen before
			if(indexer.containsKey(word))
			{
				// check if the shud be created or appended
				ArrayList<Assignment3v03> looper = indexer.get(word);
				boolean check = false;
				for(int j=0;j<looper.size();j++)
				{
					// check if the docid matchees. if yes, update the same object.
					Assignment3v03 iterateObj = looper.get(j);
					if(iterateObj.getDocid().equals(docid))
						{
						iterateObj.setRecord(iterateObj.getRecord()+1);
						check = true;
						break;
						}
				}
				if(!check)
					{
					Assignment3v03 var = new Assignment3v03(docid);
					looper.add(var);
					}
				}
			else
			{
				Assignment3v03 var = new Assignment3v03(docid);
				ArrayList<Assignment3v03> init = new ArrayList<>();
				init.add(var);
				indexer.put(word, init);
			}
		}
		
		return indexer;
	}
	
	
	private static boolean isInteger(String str) {
		try {
			Integer.parseInt(str.trim());
		} catch (NumberFormatException e) {
			return false;
		}
			catch (NullPointerException e) {
				return false;
		}
		return true;
	}
	
}
