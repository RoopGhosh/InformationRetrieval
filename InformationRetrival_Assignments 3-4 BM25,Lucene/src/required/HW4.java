package required;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Fields;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.MultiFields;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.Version;

import required.HW3.ValueComparator;

/**
 * To create Apache Lucene index in a folder and add files into this index based
 * on the input of the user.
 */
public class HW4 {
   // private static Analyzer sanalyzer = new StandardAnalyzer(Version.LUCENE_47);
    private static Analyzer analyzer = new SimpleAnalyzer(Version.LUCENE_47);

    private IndexWriter writer;
    private ArrayList<File> queue = new ArrayList<File>();

    public static void main(String[] args) throws IOException, ClassNotFoundException {
    	
    	System.out.println("Enter the file which is the output of bm25 searching");
    	Scanner s22 = new Scanner(System.in);
    	String bm25Path = s22.nextLine();
    	s22.close();
    	System.out.println(bm25Path);
	System.out
		.println("Enter the FULL path where the index will be created: (e.g. /Usr/index or c:\\temp\\index)");

	String indexLocation = null;
	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	String s = br.readLine();

	HW4 indexer = null;
	try {
	    indexLocation = s;
	    indexer = new HW4(s);
	} catch (Exception ex) {
	    System.out.println("Cannot create index..." + ex.getMessage());
	    System.exit(-1);
	}

	// ===================================================
	// read input from user until he enters q for quit
	// ===================================================
	while (!s.equalsIgnoreCase("q")) {
	    try {
		System.out
			.println("Enter the FULL path to add into the index (q=quit): (e.g. /home/mydir/docs or c:\\Users\\mydir\\docs)");
		System.out
			.println("[Acceptable file types: .xml, .html, .html, .txt]");
		s = br.readLine();
		if (s.equalsIgnoreCase("q")) {
		    break;
		}

		// try to add file into the index
		indexer.indexFileOrDirectory(s);
	    } catch (Exception e) {
		System.out.println("Error indexing " + s + " : "
			+ e.getMessage());
	    }
	}

	
	
	// ===================================================
	// after adding, we always have to call the
	// closeIndex, otherwise the index is not created
	// ===================================================
	indexer.closeIndex();

	// =========================================================
	// Now search
	// =========================================================
	IndexReader reader = DirectoryReader.open(FSDirectory.open(new File(
		indexLocation)));
	IndexSearcher searcher = new IndexSearcher(reader);
	TopScoreDocCollector collector = TopScoreDocCollector.create(2000, true);

	File file2 = new File("writer.txt");
	FileWriter write = new FileWriter(file2);
	s = "";
	System.out.println("Enter the query file path. Make sure there is only one query in the file");
	Scanner s1 = new Scanner(System.in);
	s = s1.nextLine();
	s1.close();
	br.close();
	File file1 = new File(s);
	FileReader fr = new FileReader(file1);
	BufferedReader b = new BufferedReader(fr);
	while ((s=b.readLine())!=null) {
	    try {
		Query q = new QueryParser(Version.LUCENE_47, "contents",
			analyzer).parse(s);
		searcher.search(q, collector);
		ScoreDoc[] hits = collector.topDocs().scoreDocs;

		// 4. display results
		System.out.println("Found " + hits.length + " hits.");
		System.out.println("------------------------------------------------------------------------------------");
		for (int i = 0; i < hits.length; ++i) {
		    int docId = hits[i].doc;
		    Document d = searcher.doc(docId);
		    Path p = Paths.get(d.get("path"));
		    String finalStr = (i + 1) + ". " + p.getFileName().toString().replaceAll(".html", "")
				    + " score=" + hits[i].score;
		    System.out.println(finalStr);
		    write.write(finalStr+"\r\n");
		}
		// 5. term stats --> watch out for which "version" of the term
		// must be checked here instead!
		Term termInstance = new Term("contents", s);
		long termFreq = reader.totalTermFreq(termInstance);
		long docCount = reader.docFreq(termInstance);
		System.out.println(s + " Term Frequency " + termFreq
			+ " - Document Frequency " + docCount);

	    } catch (Exception e) {
		System.out.println("Error searching " + s + " : "
			+ e.getMessage());
		break;
	    }

	}
	write.close();
	b.close();
	Fields fields = MultiFields.getFields(reader);
	System.out.println(reader.getSumTotalTermFreq("portable"));
    Terms terms = fields.terms("contents");
    TermsEnum iterator = terms.iterator(null);
    BytesRef byteRef = null;
   TreeMap<String,Long> mainHash = new TreeMap<>();
    while((byteRef = iterator.next()) != null)
        {
        	String term = new String(byteRef.utf8ToString());
        	Term terminstance = new Term("contents",byteRef);
        	long freq = reader.totalTermFreq(terminstance);
        	mainHash.put(term, freq);
        }
    Set<Entry<String, Long>> set = mainHash.entrySet();
    List<Entry<String, Long>> list = new ArrayList<Entry<String, Long>>(set);
    Collections.sort( list, new Comparator<Map.Entry<String, Long>>()
    {
        public int compare( Map.Entry<String, Long> o1, Map.Entry<String, Long> o2 )
        {
            return (o2.getValue()).compareTo( o1.getValue() );
        }

    } );
    LinkedHashMap<String, Long> linkedHM = new LinkedHashMap<>();
    
    for(Map.Entry<String, Long> entry:list){
        linkedHM.put(entry.getKey(), entry.getValue());
    }
    System.out.println(linkedHM.size());
    
    try {
    		System.out.println("in try block");
    	      OutputStream file = new FileOutputStream("SortedFreq.r");
    	      OutputStream buffer = new BufferedOutputStream(file);
    	      ObjectOutput output = new ObjectOutputStream(buffer);
    	    
    	      output.writeObject(linkedHM);
    	      output.close();
    	      buffer.close();
    	      file.close();
    	    }  
    	    catch(IOException ex){
    	     System.out.println("AAAAAAAAAAAAAAAAAAA");
    	    }
    File fileo = new File("sortedList.txt");
    FileWriter re = new FileWriter(fileo);
    Iterator<String> it = linkedHM.keySet().iterator();
    while(it.hasNext())
    {
    	String temp = it.next();
    	re.write(temp+ "\t" + linkedHM.get(temp)+ "\r\n");
    }
    re.close();
    
    System.out.println("Calling graph plotter now : ");
    ChartPlotter.runMe();
    s1.close();
    }

    



	/**
     * Constructor
     * 
     * @param indexDir
     *            the name of the folder in which the index should be created
     * @throws java.io.IOException
     *             when exception creating index.
     */
    HW4(String indexDir) throws IOException {

	FSDirectory dir = FSDirectory.open(new File(indexDir));

	IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_47,
		analyzer);

	writer = new IndexWriter(dir, config);
    }

    /**
     * Indexes a file or directory
     * 
     * @param fileName
     *            the name of a text file or a folder we wish to add to the
     *            index
     * @throws java.io.IOException
     *             when exception
     */
    public void indexFileOrDirectory(String fileName) throws IOException {
	// ===================================================
	// gets the list of files in a folder (if user has submitted
	// the name of a folder) or gets a single file name (is user
	// has submitted only the file name)
	// ===================================================
	addFiles(new File(fileName));

	int originalNumDocs = writer.numDocs();
	for (File f : queue) {
	    FileReader fr = null;
	    try {
		Document doc = new Document();

		// ===================================================
		// add contents of file
		// ===================================================
		fr = new FileReader(f);
		doc.add(new TextField("contents", fr));
		doc.add(new StringField("path", f.getPath(), Field.Store.YES));
		doc.add(new StringField("filename", f.getName(),
			Field.Store.YES));

		writer.addDocument(doc);
		//System.out.println("Added: " + f);
	    } catch (Exception e) {
		System.out.println("Could not add: " + f);
	    } finally {
		fr.close();
	    }
	}

	int newNumDocs = writer.numDocs();
	System.out.println("");
	System.out.println("************************");
	System.out
		.println((newNumDocs - originalNumDocs) + " documents added.");
	System.out.println("************************");

	queue.clear();
    }

    private void addFiles(File file) {

	if (!file.exists()) {
	    System.out.println(file + " does not exist.");
	}
	if (file.isDirectory()) {
	    for (File f : file.listFiles()) {
		addFiles(f);
	    }
	} else {
	    String filename = file.getName().toLowerCase();
	    // ===================================================
	    // Only index text files
	    // ===================================================
	    if (filename.endsWith(".htm") || filename.endsWith(".html")
		    || filename.endsWith(".xml") || filename.endsWith(".txt")) {
		queue.add(file);
	    } else {
		System.out.println("Skipped " + filename);
	    }
	}
    }

    /**
     * Close the index.
     * 
     * @throws java.io.IOException
     *             when exception closing
     */

    public void closeIndex() throws IOException {
	writer.close();
    }
}