// CS 1501 Summer 2019
// HybridTrieST<V> class

package TriePackage;

import java.util.*;
import java.io.*;

public class HybridTrieST<V> {

    private TrieNodeInt<V> root;
    int treeType = 0;
    	// treeType = 0 --> multiway trie
    	// treeType = 1 --> DLB
    	// treeType = 2 --> hybrid

	// You must supply the methods for this class.  See test program
	// HybridTrieTest.java for details on the methods and their
	// functionality.  Also see handout TrieSTMT.java for a partial
	// implementation.
    public HybridTrieST(int TreeType)
    {
    	root = null; // root is null until we put something in the trie
    	treeType = TreeType;
    }

	public void put(String key, V val)
	{
		root = put(root, key, val, 0); // call the helper method.
	}

	private TrieNodeInt<V> put(TrieNodeInt<V> x, String key, V val, int d)
	{
		if(x==null) // x == null, means that no node exists for a certain char. So we must add one.
		{
			if(treeType==0) // type 0  means alphaNodes only
			{
				x = new MTAlphaNode<V>();
			}
			else // type 1 and 2 mean DLB or Hybrid. We always start with DLB for hybrid tries
			{	
				x = new DLBNode<V>();
			}
		}

		if(treeType==2 && x instanceof DLBNode<?>) // check if the current node needs to be converted from DLB to MTAlpha
		{
			if(x.getDegree()==9) // this is the only situation where it is necessary
			{
				x = new MTAlphaNode(x);
			}
		}

		if(d == key.length()) // means we reached the end of the key
		{
			x.setData(val); // this node gets to have a value because it's special (and because we are at the end of the key)
			return x;
		}

		char c = key.charAt(d); // traverse to the next char
		x.setNextNode(c, put(x.getNextNode(c),key, val, d+1)); // keep the recursion going!
		return x;
	}

	// check if a key is in table, if so retrieve it
	public V get(String key)
	{
		TrieNodeInt<V> returnNode = get(root,key,0); // call helper method
		if (returnNode==null) 
		{
			return null; // if helper method didn't find anything return null
		}
		return returnNode.getData(); // otherwise, return its data
	}

	private TrieNodeInt<V> get(TrieNodeInt<V> x, String key, int d) // get private method
	{
		if(x == null)
		{
			return null; // recursive base case
		}
		if(d == key.length()) 
		{
			return x; // reached end of key. we found it!
		}
		char c = key.charAt(d); // try next char
		return get(x.getNextNode(c),key,d+1); // just keep recursin'
	}

	// The degreeDistribution() method should traverse the trie and return an 
	// int [] of size K+1 (where K is the maximum possible degree of a node in
	// the trie), indexed from 0 to K.  The value of each location dist[i] will
	// be equal to the number of nodes with degree i in the trie.  Note that in
	// our trie, the value K should be 26 since we are limiting it to lower
	// case letters, but for an arbitrary trie K could be as large as 256.
	public int[] degreeDistribution()
	{
		Queue<TrieNodeInt<V>> totalQ = (Queue<TrieNodeInt<V>>) root.children(); // collect all the children
		ArrayList<Integer> arrList = new ArrayList<>(); // array list that holds all the degree values
		for(TrieNodeInt<V> child : totalQ) // add each child's degree value to arraylist
		{
			arrList.add(child.getDegree());
		}
		arrList.add(root.getDegree()); // add the root's degree as well!
		int maxDegree = Collections.max(arrList); // find the max so we can properly initialize an array's size
		int[] degreeArr = new int[maxDegree+1];  // int [] size of K + 1

		for(int i = 0; i<arrList.size();i++) // for every child degree in the arrayList
		{
			degreeArr[arrList.get(i)] += 1; // increment the num occurrences of a certain degree
		}
		// int[] degreeArr = (int[]) arrList.toArray();
		return degreeArr; // return the array
	}

	/*
	0 DNE and not a Prefix
	1 if prefix not a key
	2 if key is found in Trie but not a prefix
	3 if found and a prefix
	*/
	public int searchPrefix(String key)
	{
		int answer = 0; // the return value
		TrieNodeInt<V> curr = root;
		boolean done = false; // means we found the prefix
		int loc = 0; // length of current 

		while(curr != null && !done)
		{
			if(loc == key.length()) // we reached the key
			{
				if(curr.getData()!=null) // the curr node  does hold our key
				{
					answer += 2; // instant +2 bc key is found
				}
				if(curr.getDegree()>0) // if more than one child
				{ 
					answer += 1; // +1 bc issa prefix
				}
				done = true;
			}

			else
			{
				curr = curr.getNextNode(key.charAt(loc)); //keep traversing
				loc+=1;
			}
		}
		return answer; // return the return value 
	}

	public int getSize()
	{
		int accumulatedSize = 8; // size of 2 pointers
		Queue<TrieNodeInt<V>> childrenQ = (Queue<TrieNodeInt<V>>) root.children();

		for(TrieNodeInt<V> child : childrenQ)
		{
			accumulatedSize+=child.getSize(); // add each child's size to the return value
		}

		return accumulatedSize;
	}

	public int countNodes(int nodeType)
	{
		Queue<TrieNodeInt<V>> childrenQ = (Queue<TrieNodeInt<V>>) root.children(); // collect all children (is this traversing?)
		// childrenQ.add(root); // add the root as well!
		int count = 0; // for tracking purposes
		for(TrieNodeInt<V> child : childrenQ)
		{
			if(nodeType == 1 && child instanceof MTAlphaNode<?>) // we're only tracking MTAlphaNodes so that's all we care about
			{
				count += 1; 
			}

			if(nodeType == 2 && child instanceof DLBNode<?>) // only tracking DLBNodes
			{
				count+=1;
			}
		}

		return count; // return that shit
	}
	public void save(String saveFile) //this should work.
	{
		BufferedWriter writer;
		try{
			File outputFile = new File(saveFile);
			writer = new BufferedWriter(new FileWriter(outputFile,true));
		} catch(IOException e){
			System.out.println("Error: Incompatible output file!");
			return;
		}	
		

		ArrayList<V> valueList = new ArrayList<>();
		Queue<TrieNodeInt<V>> childrenQ = (Queue<TrieNodeInt<V>>) root.children();
		childrenQ.add(root);
		for(TrieNodeInt<V> child: childrenQ)
		{
			if(child.getData()!= null)
			{
				valueList.add(child.getData());
			}
		}

		for(V obj:valueList)
		{
			try{
				writer.write(obj.toString());
				writer.newLine();
			}catch(IOException e){
				System.out.println("IOException");
				return;
			}
		}
		try{writer.close();} catch(IOException e){return;}
	} 
}
