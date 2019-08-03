package TriePackage;
import java.util.*;

public class DLBTester 
{
  public static void main(String[] args) 
  {	
  	DLBNode<String> dlbnBegin = new DLBNode();
  	DLBNode<String> dlbnA = new DLBNode();
  	DLBNode<String> dlbnB = new DLBNode();
  	DLBNode<String> dlbnC = new DLBNode();
    DLBNode<String> dlbnH = new DLBNode();
    DLBNode<String> dlbnI = new DLBNode();
    
  	// DLBNode<String> dlbnD = new DLBNode("abcd");
  	DLBNode<String> dlbnE = new DLBNode("abce");

    dlbnBegin.setNextNode('h',dlbnH);
    dlbnH.setNextNode('e',dlbnE);
    dlbnH.setNextNode('i',dlbnI);

    assert dlbnH.getNextNode('i') != null;
    assert dlbnH.getNextNode('e') != null;
  	// // set/getData() Unit Testing
  	// assert dlbnA.getData() == null;
  	// assert dlbnD.getData().equals("abcd");
  	// dlbnA.setData("a");
  	// assert dlbnA.getData().equals("a");

  	// // Prepping nodes // setNextNode() Unit Testing
  	// dlbnC.setNextNode('e',dlbnE);
  	// dlbnC.setNextNode('d',dlbnD);
  	// dlbnB.setNextNode('c',dlbnC);
  	// dlbnA.setNextNode('b',dlbnB);
  	// dlbnBegin.setNextNode('a',dlbnA);

  	// // getDegree() Unit Testing
  	// int beginDegree = dlbnBegin.getDegree();
  	// int bDegree = dlbnB.getDegree();
  	// int cDegree = dlbnC.getDegree();
  	// assert beginDegree == 1;
  	// assert bDegree == 1;
  	// assert cDegree == 2;

  	// // getChar() Unit Testing
  	// // assert dlbnC.getChar() == 'd';

  	// // getNextNode() Unit Testing
  	// assert dlbnC.getNextNode('d') == dlbnD;
  	// assert dlbnC.getNextNode('e') == dlbnE;
  	// assert dlbnB.getNextNode('q') == null;

  	// // children() Unit Testing
  	// Queue<TrieNodeInt<String>> childrenQ = (Queue<TrieNodeInt<String>>) dlbnB.children();
  	// System.out.println(childrenQ.size());
  	// System.out.println(dlbnB.getDegree());
  	// // assert childrenQ.size() == dlbnB.getDegree();
  	
  	// for(TrieNodeInt<String> child:childrenQ)
  	// {
  	// 	DLBNode<String> dlbChild = (DLBNode<String>) child;
  	// 	System.out.println(dlbChild.getData());
  	// }


    // getSize() Unit Testing
    // System.out.println(dlbnC.getSize());
    // System.out.println(dlbnB.getSize());
  	System.out.print("looks good");
  }
}