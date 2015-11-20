package required;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.SortedMap;
import java.util.TreeSet;

import org.json.JSONArray;
import org.json.JSONObject;

public class Assignment3 {
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		HashMap<String, HashMap<String, Integer>> mainHash = new HashMap<String,HashMap<String,Integer>>();
		HashMap<String, Integer> tokenCounter = new HashMap<>();
		@SuppressWarnings("rawtypes")
		ArrayList<HashMap> mainArr = new ArrayList<>();
		System.out.println("Enter the file name you want to read");
		Scanner s = new Scanner(System.in);
		String file = s.nextLine();
		s.close();
		FileReader fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);
		String str="";
		String concatStr ="";
		String docid = "1";
		br.readLine();
		while((str = br.readLine())!=null)
		{	
			if(str.contains("#"))
			{
				int n =0;
				String[] tokenArr = concatStr.split(" ");
				for(String string : tokenArr)
					if(isInteger(string))
						n++;
				tokenCounter.put(docid, n);
				// getting the count of legal words in  a document
				if (mainHash.size()> 50)
				{
					System.out.println("flushing hashmap");
					mainArr.add(mainHash);
					mainHash = new HashMap<>();
				}
				mainHash.putAll(createInvertedIndex(mainHash,concatStr,docid));
				//System.out.println("HELLO "+mainHash.keySet().toString());
				docid = str.split(" ")[1];
				concatStr = "";
				System.out.println(docid);
			}
			else
				concatStr = concatStr.concat(str + " ");
		}
		br.close();
		for(int i=0;i<mainArr.size();i++)
		if(mainArr.get(i).containsKey("decemb"))
			System.out.println(mainArr.get(i).get("decemb"));
		boolean lol = false;
		for(int i=0;i<mainArr.size();i++)
			if(mainArr.get(i).containsKey("cacm") && ((HashMap<String, Map<String, Integer>>) mainArr.get(i).get("cacm")).containsKey("3"))
				lol = true;
		System.out.println(lol);
		 JSONObject json = new JSONObject();
		 //System.out.println(json.toString());
		 FileWriter wr = new FileWriter("haha.txt");
		// wr.write(json.toString());
		 
		 System.out.println("ARRAY SIZE : "+mainArr.size());
		 JSONArray jasonarray = new JSONArray();
		 jasonarray.put(mainArr);
		 wr.write(jasonarray.toString());
		 wr.close();
		 json = new JSONObject(tokenCounter);
		 wr = new FileWriter("tokenCOunter.txt");
		 wr.write(json.toString());
		 wr.close();
	}
	private static HashMap<String, HashMap<String, Integer>> createInvertedIndex(HashMap<String, HashMap<String, Integer>> mainHash, String concatStr, String docid) {
		// TODO Auto-generated method stub
		
		HashMap<String,Integer> valHash = new HashMap<>();
		String[] listofObj = new String[0];
		if(concatStr.length() > 0)
			listofObj = concatStr.split(" ");
		for(String str : listofObj)
		{
			if(!isInteger(str))
			{
				//System.out.println(str);
				if(mainHash.get(str)==null || mainHash.get(str).get(docid)==null) // word seen first time
			{
					if(mainHash.get(str)==null)
						valHash.put(docid, 1);
					else// word seen first time in the document.
						{
							valHash = ((HashMap<String, Integer>) mainHash.get(str));
							//System.out.println(str + docid+": "+valHash.size());
							valHash.put(docid, 1);
							//System.out.println(str+" found same str in diff docs");
							//System.out.println(valHash.toString());
						}
			}
			else
				{
					 //valHash.putAll((HashMap<String, Integer>) mainHash.get(str));
					 int value = valHash.get(docid);
					 valHash  = new HashMap<>();
					 valHash.put(docid, value+1);
					 //System.out.println(str+ " found same str in same docs");
					 //System.out.println(valHash.toString());
				}
				mainHash.put(str, valHash);
			// System.out.println(mainHash.get("word1").get("docid"));
			 //System.out.println(json.toString());
			}
		}
		return mainHash;
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
