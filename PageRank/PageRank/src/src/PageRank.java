package src;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

public class PageRank {
	public static String filePath = "";
	public static int N = 0;
	public static int S = 0;
	public static final float d = 0.85f;
	public static boolean converge = false;
	public static ArrayList<Float> perplexList = new ArrayList<>();
	public static void main(String[] args) throws IOException, InterruptedException {
		System.out.println(" Enter your file location");
		Scanner s = new Scanner(System.in);
		filePath = s.nextLine();
		int L =0; // no of lines in the file
		FileReader fr = new FileReader(filePath);
		s.close();
		String str;
		@SuppressWarnings("resource")
		BufferedReader buff = new BufferedReader(fr);
		HashMap<String, ArrayList<String>> scounter = new HashMap<>();
		//ArrayList<String> z = new ArrayList<>();
		Set<String> set = new HashSet<String>();
		String lol = "";
		while((str = buff.readLine())!=null)
		{	String[] p = str.split(" ");
		//System.out.println(p.length);
			if(p.length == 1)
				L++;
			for(int c=0;c<p.length;c++)
				set.add(p[c]);
		}
		
		buff.close();
		fr.close();
		System.out.println(set.size());
		N = set.size();
		fr = new FileReader(filePath);
		buff = new BufferedReader(fr);
		while((str = buff.readLine()) !=null)
			{
				String val = String.valueOf(str);
				if(!val.trim().contains(" "))
					S++; /// dead code.. determine how many pages are unreachable.
				String[] backLinks = val.split(" ");
				for(int i=1;i<backLinks.length;i++) // starting from 1 to ignore destination page
				{
					ArrayList<String> temp = new ArrayList<>();
					if(scounter.get(backLinks[i])==null)// creating a new key
					{
						temp.add(backLinks[0]);
						scounter.put(backLinks[i], temp);
					}
					else
					{
						//System.out.println(backLinks[i]);
						temp = scounter.get(backLinks[i]);
						//System.out.println(temp.isEmpty());
						temp.add(backLinks[0]);
						scounter.put(backLinks[i], temp);
					}
					//temp.clear();
				}	
				//System.out.println(scounter);
			}
		
		buff.close();
		fr.close();
		
		
		// pre processing over......
		fr = new FileReader(filePath);
		buff = new BufferedReader(fr);
		HashMap<String, Float> PR = new HashMap<>();
		HashMap<String, Float> newPR = new HashMap<>();
		
		//step 1:
		while((str = buff.readLine()) != null)
				{
					String[] p = str.split(" ");
					//DecimalFormat df = new DecimalFormat("#0.0000");
					float temp = 1/N;
					/*float t = (int) temp*1000;
					temp = t/1000;*/
					//temp = Math.round(temp);
					PR.put(p[0], temp);
				}
		buff.close();
		fr.close();
		
		//step 2:
		int i=0;
		float perplexity =0;
		while(!converge) // change to appt condition
		{	System.out.println("On loop : " + i + " perplexity is : " + perplexity);
			i++;
			fr = new FileReader(filePath);
			buff = new BufferedReader(fr);
			float sinkPR = 0;
			
			//calculate total sink PR
/*			while((seq = buff.readLine())!=null)*/
			for(String seq: PR.keySet())
			{
				String[] p = seq.split(" ");
				if(!scounter.containsKey(p[0])) // if the node is a sink
					sinkPR += PR.get(p[0]);
			}
			buff.close();
			fr.close();
			/*fr = new FileReader(filePath);
			buff = new BufferedReader(fr);
			while((seq = buff.readLine())!=null)*/
			for(String seq: PR.keySet())
			{
				String[] p = seq.split(" ");
				float value = (1-d)/N;
				value  +=  d*sinkPR/N;
				/*float t = (int) value*1000;
				value = t/1000;*/
				newPR.put(p[0], value); 
			}
			buff.close();
			fr.close();
			fr = new FileReader(filePath);
			buff = new BufferedReader(fr);
			
			//for(String seq: PR.keySet())
			while((str = buff.readLine())!=null)
			{
				String[] p = str.split(" ");
				for(int j=1 ; j<p.length;j++)
					{
					//System.out.println(PR.get(p[j]));
					float prev =0;
					if(PR.get(p[j])==null)
							prev=0;
					else
						prev =PR.get(p[j]);
					float temp = d* prev/scounter.get(p[j]).size();
					temp += newPR.get(p[0]);
					/*float t = (int) temp*1000;
					temp = t/1000;*/
					newPR.put(p[0], temp);
					}
				PR.put(p[0], newPR.get(p[0]));
			}
			buff.close();
			fr.close();
		/*if( i==1 || i==10 || i==110)
			System.out.println(PR.keySet() + "\t" + PR.values());*/
			
			// calculate converge
			float H =0;
			for(String seq: PR.keySet())
			{
				H -= (PR.get(seq)*Math.log(PR.get(seq)));
			}
			perplexity = (float) Math.pow(2, H);
			if(perplexList.size()<4)
				{
				perplexList.add(perplexity);
				converge = false;
				System.out.println("hit");
				}
			else
			{	perplexList.set(0, perplexList.get(1));
				perplexList.set(1, perplexList.get(2));
				perplexList.set(2, perplexList.get(3));
				perplexList.set(3, perplexity);
				if((float) perplexList.get(1) == (float) perplexList.get(2) && 
						(float) perplexList.get(1) == (float) perplexList.get(3) && 
						(float) perplexList.get(1) == (float) perplexList.get(0)  )
					converge = true;
			}
		}
		
		fr = new FileReader(filePath);
		buff = new BufferedReader(fr);
		/*String path = "list.txt";
		FileWriter f = new FileWriter(path,true);
		while((seq = buff.readLine())!=null)*/
		ArrayList<String> top50 = new ArrayList<>();
		for(int j=0;j<50;j++)
		{
			String comp = "";
			for(String seq: PR.keySet())
			{
				String w = seq;
				if(comp.equals(""))
					comp = w;
				else
				{
					if(PR.get(comp) < PR.get(w) && !top50.contains(w))
						comp = w;
				}
			}
			top50.add(comp);
			System.out.println(comp + "\t" + PR.get(comp));
		}
		
		
		HashMap<String,Integer> top50InLinks = new HashMap<>();
		String temp ="";
		int count = 0;
		for(int l = 0;l<50;l++)
		{
			count =0;
			for(String s1 : scounter.keySet())
				{
				if(count<scounter.get(s1).size() && top50InLinks.get(s1) == null)
					{
						temp = s1;
						count = scounter.get(s1).size();
					}
				}
				top50InLinks.put(temp, scounter.get(temp).size());
				System.out.println(temp + " : " + scounter.get(temp).size());
		}
		
		// total no of nodes
		System.out.println("N: " + N);
		System.out.println("L: " + L);
		float f = (float) L/N;
		System.out.println("Proportion of source pages : " + f); // total no of source files
		f = N-scounter.size();
		f = f/N;
		System.out.println("Proportion of sink pages : " + f);
		float d = (float) 1/N;
		int uniform = 0;
		float sum = 0.00f;
		for(String seq: PR.keySet())
		{
			sum += PR.get(seq);
			if (PR.get(seq)< d)
				uniform++;
		}
		f = (float) uniform/N;
		System.out.println(" Proportion of values less than initial 1/N : " + f) ;
		System.out.println(sum);
		System.out.println(PR.get("WT24-B26-46"));
		//System.out.println(N-scounter.size());
		//D:\GitHubForWindows\InformationRetrieval\PageRank\123.txt
		System.out.println(L);
	}
}
