package required;

import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;

public class Main {
	static ArrayList<String> repo = new ArrayList<>();
	static int depth=0;
    public static void parent(String url,String keyword,int counter) throws Exception {
        URL oracle = new URL(url);
        Thread.sleep(1000); // the program waits for while
        BufferedReader in = new BufferedReader(new InputStreamReader(oracle.openStream()));
        String inputLine;
        String delimiter = "href=\"/wiki";
        String link ="";
        Boolean keyFound = false;
        System.out.println("Start of while");
        while ((inputLine = in.readLine()) != null)
        {
        	int cutoff = repo.size();
        	if (inputLine.contains("href=\"/wiki") && !inputLine.contains(":") && !inputLine.contains("/Main_Page") )
        	{
        		int start = inputLine.indexOf(delimiter);
        		int end = inputLine.indexOf("\"", start + delimiter.length());
        		link = inputLine.substring(start + delimiter.length(),end);
        	}
        	if(inputLine.contains(keyword))
        		keyFound = true;
        	if(!repo.contains(link) && (link != ""))
        		repo.add(link);
        	if(!keyFound)
        		repo.subList(cutoff, repo.size()).clear();
        	keyFound = false;
        }
        for (String string : repo) {
			System.out.println(string);
		}
        in.close();
        write2File(repo);
        System.out.println("End of while");
        if (depth<5 && repo.size()<1000)
        {
        	String newurl = "https://en.wikipedia.org/wiki" + repo.get(counter);
        	depth++;
        	parent(newurl,keyword,repo.size());
        }
    }
    
    public static void write2File(ArrayList<String> repo)
    {
    	String temp="";
    	for(String str : repo)
    		temp = temp.concat("https://en.wikipedia.org/wiki" + str + "\n");
    	File file = new File("LoU.txt");
    	try {
			FileWriter fw = new FileWriter(file); 
			fw.write(temp);
			fw.flush();
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
    
    public static void main (String args[]) throws Exception
    {
    	String url ;
    	String keyword;
    	System.out.println("Enter the seed link");
    	Scanner scn = new Scanner(System.in);
    	url = scn.nextLine();
    	System.out.println("Enter the keyword");
    	keyword = scn.nextLine();
    	
    	// the main call to the function
    	parent(url,keyword,1);
    }
}