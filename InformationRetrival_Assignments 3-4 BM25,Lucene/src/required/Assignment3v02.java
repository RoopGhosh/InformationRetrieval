package required;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;

public class Assignment3v02 {

	public static void main(String[] args) throws IOException, JSONException {
		// TODO Auto-generated method stub
		HashMap<String, int[]> mainHash = new HashMap<>();
		@SuppressWarnings("rawtypes")
		ArrayList<HashMap> mainArr = new ArrayList<>();
		long heapsize=Runtime.getRuntime().totalMemory();
	    System.out.println("heapsize is::"+heapsize);
		System.out.println("Enter the file name you want to read");
		Scanner s = new Scanner(System.in);
		String file = s.nextLine();
		s.close();
		FileReader fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);
		String str="";
		String concatStr ="";
		int docid = 0;
		while((str = br.readLine())!=null)
		{	
			if(str.contains("#"))
			{
				
				mainHash = createInvertedIndex(mainHash,concatStr,docid);
				if (mainHash.size()> 100)
				{
					mainArr.add(mainHash);
					mainHash = new HashMap<>();
				}
				//System.out.println("HELLO "+mainHash.keySet().toString());
				docid = Integer.parseInt(str.split(" ")[1]);
				concatStr = "";
				//System.out.println(docid);
				
			}
			else
				concatStr = concatStr.concat(str + " ");
		}
		br.close();
		//System.out.println(n);
		JSONObject json = new JSONObject();
		 //System.out.println(json.toString());
		 FileWriter wr = new FileWriter("hahav02.txt");
		// wr.write(json.toString());
		 
		 System.out.println("ARRAY SIZE : "+mainArr.size());
		 JSONArray jasonarray = new JSONArray();
		 jasonarray.put(mainArr);
		 wr.write(jasonarray.toString());
		 wr.close();
		//Gson gson = new Gson();
		//gson.toJson(mainHash);
	}
	
	private static HashMap<String, int[]> createInvertedIndex(HashMap<String, int[]> mainHash,String concatStr, int docid) {
		// TODO Auto-generated method stub
		HashMap<String,Integer> valHash = new HashMap<>();
		String[] listofObj;
		if(concatStr.length() > 0)
			listofObj = concatStr.split(" ");
		else
			listofObj = new String[0];
		for(String str : listofObj)
		{
			if(!isInteger(str))
			{
				//System.out.println(str);
				if(mainHash.get(str)==null) // word seen first time
				{
					int[] arr = new int[3204];
					arr[docid]=1;
					mainHash.put(str,arr);
				}
				else
				{
					int[] arr = mainHash.get(str);
					arr[docid] += 1;
					mainHash.put(str, arr);
				}
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
