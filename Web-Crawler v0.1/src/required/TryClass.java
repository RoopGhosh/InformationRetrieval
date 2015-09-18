package required;

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

	}

}
