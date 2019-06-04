// Zhengming Wang zhw78 CS1501 Assignment 1
import java.io.*;
import java.util.*;

public class WeakTest
{
	public static void main(String[] args) throws IOException
	{
		Scanner dictScan = new Scanner(new FileInputStream("dictionary.txt"));

		TrieSTNew<String> D = new TrieSTNew<String>(); //Making a new Trie
		String st; 
		while(dictScan.hasNext())
		{
			st = (dictScan.nextLine()).toLowerCase(); //Scan through the file and put it in the Trie.
			D.put(st, st);
		}

		LinkedList<String> anaQ = (LinkedList<String>) D.getAnagrams("keba");


		// System.out.println(anaQ.size());
		System.out.println(D.searchPrefix("ri"));
	}
}