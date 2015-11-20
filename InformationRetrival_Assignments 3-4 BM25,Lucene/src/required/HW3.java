package required;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;


public class HW3 {
	static double k1 =1.2;;
	static double k2 = 100;;
	static double b= 0.75;
	static float avdl = 0.0f;
	static HashMap<String,HashMap<String,Integer>> mainHash = new HashMap<String,HashMap<String,Integer>>();
	static BufferedReader reader;
	static String word;
	static int Documents;
	static HashMap<String,Double> DocIDF = new HashMap<String,Double>();
	static String[] DocumentIds;
	static Integer Results = 100;
	
	public static void main (String args[]) throws JSONException, IOException
	{
		HW3 hw3 = new HW3();
		System.out.println("ENter file name");
		Scanner s = new Scanner(System.in);
		String fileName = s.nextLine();
		String indexerFile = "";
		String queryFile = "";
		 System.out.println("Enter the index.out file");
		 Scanner s1 = new Scanner(System.in);
			indexerFile = s.nextLine(); //input file for processing READ FILE
			System.out.println("Enter the query file");
			s1 = new Scanner(System.in);
			queryFile = s1.nextLine();
			s.close();
			File file = new File(indexerFile);
			FileReader fr = new FileReader(file);
			String json ="";
			BufferedReader br = new BufferedReader(fr);
			String str;
			while((str=br.readLine())!= null)
			json = json.concat(str);
			JSONObject jsonobj = new JSONObject(json);
			hw3.averageComp(jsonobj);
			hw3.readHashMapFromJson(jsonobj);
			hw3.ComputeScoreForDocuments(queryFile);
	}
	public  String indexer(String fileName) throws IOException {
		// TODO Auto-generated method stub
		HashMap<String, HashMap<String,Integer>> mainHash = new HashMap<String,HashMap<String,Integer>>();
		HashMap<String,Integer> tokenCounter = new HashMap<>();
		
		FileReader fr = new FileReader(fileName);
		BufferedReader br = new BufferedReader(fr);
		String readLine ;
		String docid="1";
		String accum="";
		br.readLine(); // reading off the first line.
		while((readLine = br.readLine())!=null)
		{
			if(readLine.contains("#"))
			{
				System.out.println(docid);
				// there is a close of the previous file
				ArrayList<String> temp = legalWords(accum);
				String[] legalWords = arrayListToArray(temp);
				tokenCounter.put(docid, legalWords.length);
				updateHashMap(mainHash,docid,legalWords);
				docid = readLine.split(" ")[1];
				accum="";
			}
			else
				accum=accum.concat(readLine +" ");
		}
		//after creating the bigAss hashMap, lets check if the values persist
		System.out.println(mainHash.get("cacm").size());
		JSONObject json = new JSONObject(mainHash);
		FileWriter wr = new FileWriter("mainHash.txt");
		wr.write(json.toString());
		wr.close();
		json = new JSONObject(tokenCounter);
		wr = new FileWriter("tokenCounter.txt");
		wr.write(json.toString());
		wr.close();
		/*FileOutputStream f = new FileOutputStream("mainHash.txt");
        ObjectOutputStream s1 = new ObjectOutputStream(f);
        s1.writeObject(mainHash);
        s1.close();
        f.close();*/
		return "mainHash.txt";
	}

	public  String[] arrayListToArray(ArrayList<String> temp) {
		int size = temp.size();
		String[] legalWords = new String[size];
		for(int i=0;i<size;i++)
			legalWords[i]= temp.get(i);
		return legalWords;
	}
	
	private  ArrayList<String> legalWords(String accum) {
		String[] words = accum.split(" ");
		ArrayList<String> legalWords = new ArrayList<>();
		for(String word:words)
			if(!isInteger(word))
				legalWords.add(word);
		return legalWords;
	}

	private  boolean isInteger(String str) {
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
	
	
	private  HashMap<String,HashMap<String,Integer>> updateHashMap(HashMap<String, HashMap<String, Integer>> mainHash, String docid, String[] legalWords) {
	
		for(String word: legalWords)
		{
			if(mainHash.get(word)==null)
			{
				// create new chapter.
				HashMap<String, Integer> valHash = new HashMap<>();
				valHash.put(docid, 1);
				mainHash.put(word,valHash );
			}
			else
				if(mainHash.get(word).get(docid)==null) // the word exists but first time in the document.
				{
					HashMap<String,Integer> valHash2 = mainHash.get(word);
					valHash2.put(docid, 1);
					//valHash2.put(docid, valHash2.get(docid)+1);
					mainHash.put(word, valHash2);
				}
				else
				{
					// the word was seen just a few mins back...
					HashMap<String,Integer> valHash3 = mainHash.get(word);
					valHash3.put(docid, valHash3.get(docid)+1);
					mainHash.put(word, valHash3);
				}
		}
		return mainHash;
	}
	private  void ComputeScoreForDocuments(String queryFile) throws IOException {
		reader = new BufferedReader(new FileReader(queryFile));
		String line = null;
		while((line = reader.readLine()) != null)
			ranking(line);
	
}

private  void ranking(String line) {
{
	String[] strings = line.split(" ");
	for(String string : strings)
	{
		HashMap<String,Integer> valHash = mainHash.get(string);
		Object[] objectArray = valHash.keySet().toArray();
		String[] documents = Arrays.copyOf(objectArray, objectArray.length, String[].class);
		Integer n1 = documents.length;
		System.out.println(n1);
		Integer N = Documents;
		System.out.println("DOCUID : "+DocumentIds.length);
		for(String doc : DocumentIds)
			if(Arrays.asList(documents).contains(doc))
				{
					System.out.print("oooooo Inside calculating scores");
					double upper = (0.5 / 0.5);
					double lower = (n1 + 0.5) / (N - n1 + 0.5) ;
					double cal = upper / lower;
					
					Integer f1 = valHash.get(doc);
					Double k = k1 * ((1-b) + b * (double)avdl);	
					Double score = DocIDF.get(doc);
					if(score == null)
						score = 0.0;
					// now the final score calculations...
					score = score + (Math.log(cal) * (k1 + 1) * f1) / (k + f1) * (k2 + 1) / (k2 + 1);
					DocIDF.put(doc, score);
				}
	}
	
	
	display();
}
}

private void display() {
int id = 1;
ValueComparator valMap = new ValueComparator(DocIDF);
TreeMap<String,Double> ascResults = new TreeMap<String,Double>(valMap);
ascResults.putAll(DocIDF);
Integer Rank = 1;
String Q0 = "Q0";
String hostname = "WIN-I3DPM9IK28T";
System.out.println(DocIDF.size());
for(Map.Entry<String,Double> entry : ascResults.entrySet())
{
		String documentId = entry.getKey();
		Double score = entry.getValue();
		System.out.println(id +"\t"+ Q0 +"\t"+ documentId + "\t" + Rank +"\t" + score + "\t" + hostname);
		Rank++;
		if(Rank > Results)
		break;
}
id++;
System.out.println("---------------------------------------------------------------");
DocIDF.clear(); 
}

//refer:http://stackoverflow.com/questions/109383/how-to-sort-a-mapkey-value-on-the-values-in-java
class ValueComparator implements Comparator<String> {
HashMap<String, Double> base;

public ValueComparator(HashMap<String,Double> base) {
    this.base = base;
}

// Note: this comparator imposes orderings that are inconsistent with
// equals.
public int compare(String a, String b) {
    if (base.get(a) >= base.get(b)) {
        return -1;
    } else {
        return 1;
    } 
}
}

@SuppressWarnings("unused")
private  void makeHashMap(String queryWord, JSONObject tokenObj, JSONObject jsonobj, HashMap<String, Float> bmHash, float avdl) throws JSONException {
// TODO Auto-generated method stub
float k  = 0.0f;
float b = 0.75f;
float k1 = 1.2f;
float k2 = 100;
@SuppressWarnings("unchecked")
HashMap<String,Integer> valHash = (HashMap<String, Integer>) jsonobj.get(queryWord);
HashMap<String,Double> scores = new HashMap<String,Double>();
Set<String> he = valHash.keySet();
String[] h1 = he.toArray(new String[valHash.size()]);
for(String str : h1)
{
int f1 = valHash.get(str);
k=k1*(1-b)+b*(float)(tokenObj.getInt(str))/avdl;
int N = 3204;
int n1 = valHash.size();
double upper = (0.5 / 0.5);
double lower = (n1 + 0.5) / (N - n1 + 0.5) ;
double firstPart = upper / lower;
double firstPartLog = Math.log(firstPart);

Double SecondPart = (double) (((k1 + 1) * f1) / (k + f1));

Double ThirdPart = (double) ((k2 + 1) / (k2 + 1));
Double score = scores.get(str);
if(score == null)
	score = 0.0;

score = score + (firstPartLog * SecondPart * ThirdPart);
scores.put(str, score);
}
System.out.println(scores.values());
}

private  void readHashMapFromJson(JSONObject jsonobj) throws JSONException {
// TODO Auto-generated method stub
String jsonString = jsonobj.toString();
mainHash = new Gson().fromJson(jsonString, new TypeToken<HashMap<String, HashMap<String,Integer>>>(){}.getType());
DocumentIds = mainHash.keySet().toArray(new String[mainHash.size()]);
}

private  void averageComp(JSONObject jsonobj) throws JSONException, IOException {
// TODO Auto-generated method stub
JSONObject tokenObj = calculateavdl("tokenCounter.txt");
DocumentIds = JSONObject.getNames(tokenObj);
int count=0;
for(String string :DocumentIds)
	count +=  Integer.parseInt(String.valueOf(tokenObj.get(string)));
System.out.println(count);
avdl = (float) count/3204;
}


private  JSONObject calculateavdl(String filepath) throws JSONException, IOException {
File file = new File(filepath);
FileReader fr = new FileReader(file);
String json ="";
BufferedReader br = new BufferedReader(fr);
String str;
while((str=br.readLine())!= null)
json = json.concat(str);
br.close();
JSONObject jsonobj = new JSONObject(json);
return jsonobj;
}


}


/*	double k1;
double k2;
double b;
//HashMap<String,Float> avDL = new HashMap<String,Float>();
HashMap<String,HashMap<String,Integer>> mainHash = new HashMap<String,HashMap<String,Integer>>();
BufferedReader reader;
static String word;
Integer Documents;
HashMap<String,Double> DocIDF = new HashMap<String,Double>();
String[] DocumentIds;
static Integer Results;
static Integer queryId = 1;



public static void main(String[] args) throws ClassNotFoundException, IOException, JSONException {
// TODO Auto-generated method stub
// read DS first
HashMap<String,HashMap<String,Integer>> mainHash = new HashMap<>();
FileInputStream fin = new FileInputStream("mainHash.txt");
@SuppressWarnings("resource")
ObjectInputStream ois = new ObjectInputStream(fin);

File file = new File("mainHash.txt");
FileReader fr = new FileReader(file);
String json ="";
BufferedReader br = new BufferedReader(fr);
String str;
while((str=br.readLine())!= null)
json = json.concat(str);
JSONObject jsonobj = new JSONObject(json);
JSONObject tokenObj = calculateavdl("tokenCounter.txt");
String[] temp = jsonobj.getNames(tokenObj);
int count=0;
for(String string :temp)
count +=  Integer.parseInt(String.valueOf(tokenObj.get(string)));
System.out.println(count);

float avdl = (float) count/3204;
/////// lets do it!!!!


String queryWord = "portabl";
HashMap<String,Float> bmHash = new HashMap<>();
makeHashMap(queryWord,tokenObj,jsonobj,bmHash,avdl);

}

private static void makeHashMap(String queryWord, JSONObject tokenObj, JSONObject jsonobj, HashMap<String, Float> bmHash, float avdl) throws JSONException {
// TODO Auto-generated method stub
float k  = 0.0f;
float b = 0.75f;
float k1 = 1.2f;
float k2 = 100;
@SuppressWarnings("unchecked")
HashMap<String,Integer> valHash = (HashMap<String, Integer>) jsonobj.get(queryWord);
HashMap<String,Double> scores = new HashMap<String,Double>();
Set<String> he = valHash.keySet();
String[] h1 = he.toArray(new String[valHash.size()]);
for(String str : h1)
{
int f1 = valHash.get(str);
k=k1*(1-b)+b*(float)(tokenObj.getInt(str))/avdl;
int N = 3204;
int n1 = valHash.size();
double upper = (0.5 / 0.5);
double lower = (n1 + 0.5) / (N - n1 + 0.5) ;
double firstPart = upper / lower;
double firstPartLog = Math.log(firstPart);

Double SecondPart = (double) (((k1 + 1) * f1) / (k + f1));

Double ThirdPart = (double) ((k2 + 1) / (k2 + 1));
Double score = scores.get(str);
if(score == null)
	score = 0.0;

score = score + (firstPartLog * SecondPart * ThirdPart);
scores.put(str, score);
}
System.out.println(scores.values());
}

private static JSONObject calculateavdl(String filepath) throws JSONException, IOException {
File file = new File(filepath);
FileReader fr = new FileReader(file);
String json ="";
BufferedReader br = new BufferedReader(fr);
String str;
while((str=br.readLine())!= null)
json = json.concat(str);
JSONObject jsonobj = new JSONObject(json);
return jsonobj;
}
}*/

