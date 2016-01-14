package required;

import java.net.*;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Main {
	
	// global constants
	static ArrayList<String> repo = new ArrayList<>();
	static int depth =0; // counts the depth of the pages crawled
	static int urlcount = 1; // tracks the count of the url traversed
	static int counter = 0;  // counts the no of valid urls to be written to the file
	static String wikiBase = "https://en.wikipedia.org/wiki";

	static int cutoff = 0;
	static int discovercount=0;
	// this is the parent class which is called from the main class where processing takes place
	
    public static void parent(String url,String keyword) throws Exception {
       System.out.println("hellllllo");
    	// reading data from the URL
    	URL urltype = new URL(url);
        //Thread.sleep(1000); // the program waits for while
        BufferedReader in = new BufferedReader(new InputStreamReader(urltype.openStream()));
        String inputLine;
        String delimiter = "href=\"/wiki";
        String link ="",newurl = "";
        int i=repo.size();
        //Boolean keyFound = false;
        System.out.println("Start of while");
        
        // reads line after line from input data in the loop 
    	
        while ((inputLine = in.readLine()) != null && discovercount <1001)
        {
        	if (inputLine.contains(delimiter) && !inputLine.contains(":") && !inputLine.contains("/Main_Page") )
        	{
        		int start = inputLine.indexOf(delimiter);
        		int end = inputLine.indexOf("\"", start + delimiter.length());
        		link = inputLine.substring(start + delimiter.length(),end);
        		if(!repo.contains(link) && (link != ""))
            		repo.add(link);
        		
        	}
        }
        in.close();
        
        // for debug purpose only
        /*for (String string : repo) 
			System.out.println(string);*/
        if(!checkForKeyword(url,keyword))
        {
        	repo.subList(i, repo.size()).clear();
        	System.out.println("depth :  " + depth + "reposize " + repo.size());
        }
        
        
        // call to function which writes the array to a text file named : LoU.txt present at the same level.
        //write2File(repo);
        
        System.out.println("End of while " + "depth " + depth + "counter " + counter + "cutoff " +cutoff + "reposize "+repo.size());
        
        // condition to keep the program sane if no matches to keywords are found.
        /*if (counter>=cutoff || repo.size()==0)
        {
        	newurl = url;
        	System.out.println("repo size: depth size : " + repo.size() +" " + depth); //debug only
        	System.out.println("ALERT!!! ALERT!!! ALERT!!! ALERT!!! ALERT!!! ALERT!!! ALERT!!! ");
        }*/
        // condition to increment the counter to read the next url from array and the function goes in a recursive call
        if (counter<cutoff) // counter increases
        {
        	counter++;
        	newurl = wikiBase+ repo.get(counter);
        	parent(newurl,keyword);
        }
        else if(counter>=cutoff && counter < repo.size())
        {
        	depth++;
        	System.out.println("increasing depth");
        	counter++;
        	cutoff = repo.size();
        	System.out.println("increasing curoff");
        	newurl = wikiBase + repo.get(counter);
        	parent(newurl,keyword);
        }
        // condition to check if the depth increases after the initial round of url traversals. and the function goes in a recursive call
    }
    
    // this function writes the array to the text file LoU.txt which is the list of valid URLs traversed till now.
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
    public static void write2File2(String url)
    {
    		String temp=url + "\n";
    		 //temp = temp.concat("https://en.wikipedia.org/wiki" + url + "\n");
    	File file = new File("LoU.txt");
    	try {
			FileWriter fw = new FileWriter(file,true); 
			fw.write(temp);
			fw.flush();
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
    }
    
    public static boolean checkForKeyword(String url, String keyword)
    {
    	Document html ;
		try {
			html = Jsoup.connect(url).get();
			String para = html.text().toLowerCase();
			if (para.contains(keyword.toLowerCase()))
			{
				System.out.println("in true mode");
				write2File2(url);
				discovercount++;
				return true;
			}
			else
			{
				System.out.println("in false mode");
				return false;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false; // default return settings.
    }
    
    //main function call where input provides the seed url.
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
    	parent(url,keyword);
    }
}