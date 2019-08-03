package TriePackage;
import java.util.*;

public class HybridTester 
{
  public static void main(String[] args) 
  {	
  	// MultiWay Hybrid Trie
  	HybridTrieST<String> htMt = new HybridTrieST<>(1);
  	htMt.put("hello","hello");
  	htMt.put("hi","hi");
  	htMt.put("goodmorn","goodmorn");
  	htMt.put("whatsup", "whatsup");
  	htMt.put("q", "q");
  	htMt.put("good","good");

  	// get() Unit testing
  	assert htMt.get("no") == null;
  	assert htMt.get("good") != null;
  	assert htMt.get("hi") != null;
  	assert htMt.get("whatsup").equals("whatsup");
 
  	int[] degreeArr = htMt.degreeDistribution();
  	System.out.print("[");
  	for(int i =0; i<degreeArr.length;i++)
  	{
  		System.out.print(degreeArr[i]+",");
  	}
  	System.out.println("]");
 
  	// getSize()
  	System.out.println(htMt.getSize());
 
  	// countNodes()
  	assert htMt.countNodes(1) == 0;
  	System.out.println(htMt.countNodes(2));
 
  	// searchPrefix() Unit Testing
  	assert htMt.searchPrefix("good") == 3;
  	assert htMt.searchPrefix("whatsup") == 2;
  	assert htMt.searchPrefix("what") == 1;
  	assert htMt.searchPrefix("dumb") == 0;

	htMt.save("htMtTest.txt");
   	System.out.println("Looks good");  	
  }
}