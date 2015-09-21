package required;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class TryClass {

	public static void main(String[] args) {
		ArrayList<String> haha = new ArrayList<>();
		haha.add("hello");
		haha.add("world");
		haha.add("new");
		haha.add("world");
		System.out.println(haha.size());
		haha.subList(2, haha.size()).clear();
		System.out.println(haha.size());
		Document html ;
		try {
			html = Jsoup.connect("https://en.wikipedia.org/wiki/Social_issues_in_India").get();
			Element para = html.body();
			String nm = para.text();
			System.out.println(nm);
			System.out.println("hello");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

}
