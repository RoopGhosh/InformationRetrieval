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
	static String url ="";
	static String keyword ="";
	static int discovercount=0;
	// this is the parent class which is called from the main class where processing takes place
	
    public static void parent() throws Exception {
    	//while(discovercount<1001 && depth <= 5)
    	//{
    		//System.out.println(url);
    	// reading data from the URL
    	URL urltype = new URL(url);
        Thread.sleep(1000); // the program waits for while
        BufferedReader in = new BufferedReader(new InputStreamReader(urltype.openStream()));
        String inputLine;
        String delimiter = "href=\"/wiki";
        String link ="";
        int i=repo.size();
        
        while ((inputLine = in.readLine()) != null)
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
        
        if(!checkForKeyword(url,keyword))
        {
        	repo.subList(i, repo.size()).clear();
        	System.out.println("depth: " + depth + " reposize: " + repo.size()+ " URLCount: " + discovercount);
        }
   
        // condition to increment the counter to read the next url from array and the function goes in a recursive call
        if (counter<cutoff) // counter increases
        {
        	counter++;
        	url = wikiBase+ repo.get(counter);
        }
        else if(counter>=cutoff && counter < repo.size())
        {
        	depth++;
        	System.out.println("increasing depth & cutoff");
        	counter++;
        	cutoff = repo.size();
        	url = wikiBase + repo.get(counter);
        }
        if(discovercount<1001 && depth<=5)
        	parent();
    	}
       // }
    
   
    @SuppressWarnings("resource")
	public static boolean write2File2(String url)
    {
    		String temp=url + "\n";
    	File file = new File("LoU.txt");
    	String aLine ="";
    	try {
    		FileReader fr = new FileReader(file);
    		BufferedReader br = new BufferedReader(fr);
    		while((aLine = br.readLine()) != null)
    			if (aLine.equals(url))
    			{
    				br.close();
    				fr.close();
    				return false;
    			}
			FileWriter fw = new FileWriter(file,true); 
			
			fw.write(temp);
			fw.flush();
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
    	return true;
    }
    
    public static boolean checkForKeyword(String url, String keyword)
    {
    	Document html ;
		try {
			html = Jsoup.connect(url).get();
			//System.out.println(html.toString());
			String para = html.text().toLowerCase();
			String[] x = para.split("\"https://en.wikipedia.org");
			String[] y = x[x.length-1].split("\"");
			para = x[0] + y[1];
			if (para.contains(keyword.toLowerCase()))
			{
				System.out.println("Truthfully :" + discovercount);
				discovercount++;
				return write2File2(url);
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
    	
    	System.out.println("Enter the seed link");
    	@SuppressWarnings("resource")
		Scanner scn = new Scanner(System.in);
    	url = scn.nextLine();
    	System.out.println("Enter the keyword");
    	keyword = scn.nextLine();
    	// the main call to the function
    	System.out.println(url);
    	System.out.println(keyword);
    	
    	parent();
    	System.out.println("Crawling over");
    	}
}