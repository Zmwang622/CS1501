// CS 1501 Summer 2019
// MultiWay Trie Node implemented as an external class which
// implements the TrieNodeInt InterfaceAddress.  For this
// class it is assumed that all characters in any key will
// be letters between 'a' and 'z'.

package TriePackage;

import java.util.*;
public class MTAlphaNode<V> implements TrieNodeInt<V> 
{
	private static final int R = 26;	// 26 letters in
										// alphabet

    protected V val; // Word or null
    protected TrieNodeInt<V> [] next; // Next layer of nodes
	protected int degree;

	public MTAlphaNode()
	{
		val = null;
		next= (TrieNodeInt<V>[]) new TrieNodeInt<?>[R];
		degree = 0;
	}

	public MTAlphaNode(V data)
	{
		val = data;
		next= (TrieNodeInt<V>[]) new TrieNodeInt<?>[R];
		degree = 0;
	}
	
	public MTAlphaNode(DLBNode<V> dlbNode)
	{
 		val = dlbNode.getData();
 		degree = dlbNode.getDegree();
 		next = (TrieNodeInt<V>[]) new TrieNodeInt<?>[R];
 		//correct now
 		DLBNode.Nodelet currentNodelet = dlbNode.front; // traverse through the linked list and add each nodelet's node and char to the array.
 		while(currentNodelet!=null)
 		{
 			int index = alphaHash(currentNodelet.cval);
 			next[index] = currentNodelet.child;
 			currentNodelet = currentNodelet.rightSib;
 		}
	}

	// You must supply the methods for this class.  See TrieNodeInt.java
	// for the specifications.  See also handout MTNode.java for a
	// partial implementation.  Don't forget to include the conversion
	// constructor (passing a DLBNode<V> as an argument).
	
	// Custom hashing method for alphabet
	private int alphaHash(char c) throws IllegalArgumentException
	{
		int charVal = (int) c;
		if(charVal > 122 || charVal < 96)
			{throw new IllegalArgumentException("Alphabet nodes only.");}
		return (charVal- 97)%26; //Index between 0 and 25
	}

	public TrieNodeInt<V> getNextNode(char c)
	{
		int hashIndex = alphaHash(c); // Find index
		return next[hashIndex]; // Null or TrieNode
	}

	public void setNextNode(char c, TrieNodeInt<V> node)
	{
		int hashIndex = alphaHash(c); // get index for char c
		if(next[hashIndex] == null)
		{
			degree++; // Increase the degree if there wasn't a prior node
		}
		next[hashIndex] = node; // new node pointer!
	}

	public V getData()
	{
		return val;
	}

	public void setData(V data)
	{
		val = data;
	}

	public int getDegree()
	{
		return degree;
	}

	public int getSize()
	{
		return ((4*26)+4+4); //constant size				
	}

	public Iterable<TrieNodeInt<V>> children()
	{
		Queue<TrieNodeInt<V>> childrenQ = new LinkedList<>(); // store children in queue
		collect(this,childrenQ); // send queue into private method
		if(childrenQ.size()>1)
		{
			childrenQ.remove();
		}
		return childrenQ;
	}
	private void collect(TrieNodeInt<V> x, Queue<TrieNodeInt<V>> q)
	{
		if(x == null) 
		{
			return; // x == null means we're at an index that has no node. Leave.
		}
		
		q.add(x); // if not null then it we need it. Add this to the queue.
		for(int c = 0; c < R; c++)
		{
			char currentChar = (char) (c+97); // try every single char.
			collect(x.getNextNode(currentChar),q); // recursivly check child.
		}
	}
}
