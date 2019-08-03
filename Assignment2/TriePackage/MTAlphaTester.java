package TriePackage;
import java.util.*;

public class MTAlphaTester 
{
  public static void main(String[] args) 
  {
  	char[] alphabet = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};
 //  	// Test 1: The Constructors... not 100% yet
  	System.out.println("Test 1:");
  	// MTAlphaNode<Character> mtNode = new MTAlphaNode('{');
	MTAlphaNode<Character> mtNode1 = new MTAlphaNode();
	System.out.println("Here");
	MTAlphaNode<Character> mtNode2 = new MTAlphaNode('c');
	// Test 2. set/getData() and alphaHash
	System.out.println("Test 2:");
	System.out.println(mtNode1.getData());
	mtNode1.setData('b');
	System.out.println(mtNode1.getData());
	System.out.println(mtNode2.getData()+"\n");

	// for(char c: alphabet)
	// {
	// 	//alphaHash works!
	// 	System.out.println(mtNode1.alphaHash(c));
	// }

	// Test 2.5 set/getNextNode getDegree()
	System.out.println("Test 2.5: ");
	mtNode1.setNextNode('c',mtNode2);
	mtNode1.getNextNode('c');
	mtNode1.setNextNode('b',mtNode1);
	System.out.println(mtNode1.getDegree());

	// Test 3 children()
	System.out.println("Test 3: ");
	MTAlphaNode<Character> testNode1 = new MTAlphaNode('a');
	MTAlphaNode<Character> testNode2 = new MTAlphaNode('b');
	MTAlphaNode<Character> testNode3 = new MTAlphaNode('c');
	MTAlphaNode<Character> testNode4 = new MTAlphaNode('d');
	MTAlphaNode<Character> testNode5 = new MTAlphaNode('e');
	MTAlphaNode<Character> testNode6 = new MTAlphaNode('f');

	testNode5.setNextNode('f',testNode6);
	testNode4.setNextNode('e',testNode5);
	testNode3.setNextNode('d',testNode4);
	testNode2.setNextNode('c',testNode3);
	testNode1.setNextNode('b',testNode2);

	Queue<TrieNodeInt<Character>> test3ChildQ = (Queue<TrieNodeInt<Character>>) testNode1.children();

	for(TrieNodeInt<Character> charNode : test3ChildQ)
	{	
		MTAlphaNode<Character> charN = (MTAlphaNode) charNode;
		System.out.println(charN.getData());
	}

  }
}