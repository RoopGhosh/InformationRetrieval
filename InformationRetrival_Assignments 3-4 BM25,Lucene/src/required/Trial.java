package required;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;

public class Trial {

	public static void main(String[] args) {
		ArrayList<String> hkma = new ArrayList<>();
		int i=0;
		/*while (i<460000)
		{	hkma.add("hellow");
			System.out.println(i);
			i++;
		}*/
		Document doc;
		try {
			doc = Jsoup.connect("https://en.wikipedia.org/wiki/Hugh_of_Saint-Cher").get();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Document d = Jsoup.parseBodyFragment("https://en.wikipedia.org/wiki/Hugh_of_Saint-Cher");
		String e = d.text();
		System.out.println(e);
	}

}
