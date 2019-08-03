**CS 1501**

**Algorithm Implementation**

**Summer 2019**

**Programming Project 2**

**Online:**  Friday, May 31, 2019

**Due: ** All assignment materials: 1) All files necessary for your trie implementation, correctly stored within the TriePackage directory.  These files include: TrieNodeInt.java (provided), MTAlphaNode.java, DLBNode.java and HybridTrieST.java, 2) The HybridTrieTest.java main program file (provided) 3) Any other files that you have written 4) **[W section only]** OOP paper ** ** and 5) Assignment Information Sheet.  All materials must be zipped into a single .zip file and submitted  **via the course submission site**  by ** 11:59 PM on Friday, June 14, 2019**.

**Late Due Date: ** 11:59PM on Monday, June 17, 2019

**Note 1: Do not submit ** a NetBeans or Eclipse (or any other IDE) project file.  If you use one of these IDEs make sure you extract and test your Java files WITHOUT the IDE before submitting.

**Note 2: Do not submit**  your revdict.txt or output dictionary files – they are too large.  The TA will have a copy of the revdict.txt file to use for testing.

**Background:**

Now that we have discussed implementing tries using arrays for the nodes and also linked-lists for the nodes (DLBs) we would like to implement a hybrid trie which attempts to benefit from both implementations.  As discussed in lecture, the DLB implementation is clearly more memory efficient in nodes with few children, with a relatively small time cost for traversing the list of nodelets in order to find the appropriate child at each node.  However, as the number of children grows, the DLB implementation begins to suffer in two ways: 1) The memory can actually surpass that required of the array-based nodes, since each DLB nodelet requires more memory than a single pointer in an array and 2) The time to find a child becomes significant as the list of children in a DLB node grows longer.

Thus, it makes sense to implement a trie with the following policy:

       Initially make a new node in the trie a DLB node

       Monitor the degree (number of children) of each node in the trie.  If the degree gets past a certain threshold, convert the node to an array-based node and use that node for future access.

Even though a DLB node and an array-based trie node are very different in structure, since they will have the same functionality we can represent both as implementations of the TrieNodeInt\&lt;V\&gt; interface.  We discussed the TrieNodeInt\&lt;V\&gt; interface in lecture and saw a simple implementation (MTNode.java).  In this assignment we will be using an enhanced TrieNodeInt\&lt;V\&gt; interface to allow additional functionality in each node and thus in our trie overall.

**Details:**

Consider the interface TrieNodeInt\&lt;V\&gt; in file [TrieNodeInt.java](http://people.cs.pitt.edu/~ramirez/cs1501/assigs/hybridtrie/TrieNodeInt.java). **[Important Note: Be sure to get the **[**TrieNodeInt.java**](http://people.cs.pitt.edu/~ramirez/cs1501/assigs/hybridtrie/TrieNodeInt.java)** file from the Assignments page and NOT the one from the Handouts page.  The version on the Assignments page is newer and has more methods]**.

This interface defines the functionality of a single trie node.  As discussed in lecture, we can use this interface as the basis for our overall trie structure.   **Read this interface very carefully, and note the comments describing each of the methods.**   Many of the methods should be familiar to you, but there are several new methods that you should also note.

You must create two implementations of the TrieNodeInt\&lt;V\&gt; interface:

**MTAlphaNode\&lt;V\&gt;**  (in file MTAlphaNode.java):

This implementation is similar to the one provided for you in the handout from lecture (MTNode.java).  However, for this implementation we will limit our nodes to lower case letters, so there will be only 26 children per node.  Also, since all character values are no longer valid indices in the array, you will need to map the character values to the correct positions in the array, throwing an exception if the character is invalid.  In addition, there are new methods to consider in the interface – think carefully about how you will implement these.

Finally, in order to allow your DLBNode\&lt;V\&gt; nodes to be converted to MTAlphaNode\&lt;V\&gt; nodes as described above, you will need a constructor for your MTAlphaNode\&lt;V\&gt; class that takes an argument of a DLBNode\&lt;V\&gt;:

**public MTAlphaNode(DLBNode\&lt;V\&gt; oldNode) {}**

This constructor will need to iterate through the nodelets in the DLBNode\&lt;V\&gt; and assign the appropriate child pointers to the MTAlphaNode\&lt;V\&gt;.  To do this, your MTAlphaNode\&lt;V\&gt; class will need access to the instance variables within the DLBNode\&lt;V\&gt; class.  Thus, you should make your DLBNode\&lt;V\&gt; instance variables  **protected**  rather than  **private**  (since being in the same package allows access to protected variables).

**DLBNode\&lt;V\&gt;****  (in file DLBNode.java):**

This implementation will be of a de la Briandais tree node as discussed in lecture.  The children of each DLBNode\&lt;V\&gt; will be represented in a linked-list of nodelets.  Thus, you will need an inner class in your DLBNode\&lt;V\&gt; class to represent a nodelet(child reference, sibling reference, character).  See course notes for some details on the DLBNode\&lt;V\&gt; structure.  To allow for access of your inner class and instance variables by the MTAlphaNode\&lt;V\&gt; constructor, make all of these declarations protected rather than private.

Clearly the TrieNodeInt\&lt;V\&gt; interface methods will be implemented very differently in this class, as discussed in lecture.  For example, the method getNextNode(char c) in the MTAlphaNode\&lt;V\&gt; class simply returns the reference at the index corresponding to c.  However, in the DLBNode\&lt;V\&gt; class this same method will require a traversal of the linked-list of nodelets until either the character is found or the end of the list is reached.  Think about the DLBNode\&lt;V\&gt; structure when considering the implementations of the other interface methods.

Note that in the setNextNode(char c, TrieNodeInt\&lt;V\&gt; node) method a new nodelet may be created to store the reference to the argument node.  For the trie functionality this nodelet could be anywhere within the linked list corresponding to the current DLBNode\&lt;V\&gt;.  However, since we want to keep the strings in alphabetical order, your setNextNode() method should make sure that the new nodelet is placed into the proper position within the linked list based on the character values.

To utilize your TrieNodeInt\&lt;V\&gt; implementations, you will also write a new trie class:

**HybridTrieST\&lt;V\&gt;**  (in file HybridTrieST.java):

The data for your HybridTrieST\&lt;V\&gt; class will be a single reference to the root of your tree:

             **private TrieNodeInt\&lt;V\&gt; root;**

Note that this reference is of type TrieNodeInt\&lt;V\&gt; and thus could refer to either an MTAlphaNode\&lt;V\&gt; or to a DLBNode\&lt;V\&gt;.

The constructor for the HybridTrieST\&lt;V\&gt; class will have a parameter with 3 possible values:

            0: use MTAlphaNode\&lt;V\&gt; nodes for all nodes in the trie

            1: use DLBNode\&lt;V\&gt; nodes for all nodes in the trie

            2: use nodes of both types for the tree, as explained above

In the case of 2, the threshold for converting a node from a DLBNode\&lt;V\&gt; to an MTAlphaNode\&lt;V\&gt; is when its degree gets to the value 12.  The justification for this is based on the memory required for each node.  If we assume that each Java reference requires 4 bytes of memory and that a char requires 1 byte of memory, and considering only the memory required for branching, we get the following:

            A DLBNode\&lt;V\&gt; requires 9 bytes for each nodelet (child reference, sibling reference, char value), representing each child of the node

            An MTAlphaNode\&lt;V\&gt; requires 26 x 4 = 104 bytes for all of its child pointers in its array, whether they are used or not

Thus, for degree 11, a DLBNode\&lt;V\&gt; requires 11 x 9 = 99 bytes for its child references, but for degree 12 it requires 12 x 9 = 108 bytes, which is more than the 104 bytes required for the MTAlphaNode\&lt;V\&gt;.  Thus, from degree 12 and up, the MTAlphaNode\&lt;V\&gt; is more memory efficient than the DLBNode\&lt;V\&gt;.

In order to convert properly from a DLBNode\&lt;V\&gt; to an MTAlphaNode\&lt;V\&gt;, you will need to test the actual type of the node as well as its degree.  The degree is a method in the TrieNodeInt\&lt;V\&gt; interface, and the type can be tested using the  **instanceof** operator in Java.  Due to Java type restrictions, to test the types of these nodes, you should use the following syntax:

**if (currentNode instanceof DLBNode\&lt;?\&gt;) …**

Note that the parameter for the DLBNode is ? rather than a specific type value such as V. You will also need to test the type of a node for the countNodes() method.

Overall, you can see the requirements for the HybridTrieST\&lt;V\&gt; class in the test file [HybridTrieTest.java](http://people.cs.pitt.edu/~ramirez/cs1501/assigs/hybridtrie/HybridTrieTest.java).  Read this file very carefully and note how the methods are called and what they are required to do (via the comments).  Also see the expected execution in the test output files: [Simple-out.txt](http://people.cs.pitt.edu/~ramirez/cs1501/assigs/hybridtrie/Simple-out.txt), [MT-out.txt](http://people.cs.pitt.edu/~ramirez/cs1501/assigs/hybridtrie/MT-out.txt), [DLB-out.txt](http://people.cs.pitt.edu/~ramirez/cs1501/assigs/hybridtrie/DLB-out.txt) and [Hybrid-out.txt](http://people.cs.pitt.edu/~ramirez/cs1501/assigs/hybridtrie/Hybrid-out.txt).  Your output should match these exactly.

To get you started with this project I have provided skeletons for the three classes that you must write.  These contain the class headers and data declarations.  You must provide all of the methods so that these classes work with the HybridTrieTest.java program.  See:

[DLBNode.java](http://people.cs.pitt.edu/~ramirez/cs1501/assigs/hybridtrie/DLBNode.java)             [MTAlphaNode.java](http://people.cs.pitt.edu/~ramirez/cs1501/assigs/hybridtrie/MTAlphaNode.java)       [HybridTrieST.java](http://people.cs.pitt.edu/~ramirez/cs1501/assigs/hybridtrie/HybridTrieST.java)

**Important Notes:**

–      In your MTAlphaNode\&lt;V\&gt; class you will be making an array of a generic type.  See handout MTNode.java from the CS 1501 handouts page for how to do this without generating a compilation error (however, it will generate a warning – that is ok).

–      You must utilize the Java Iterable interface in this project.  This is a very useful interface and you should look it up in the documentation for more information.

–       **W students: ** In addition to your program you must** write a short (4-6 pages, double-spaced) technical paper on Object-Oriented Programming.  **Include a brief history, the primary features, some example languages and key differences between them.**  You must use at least two verifiable (i.e. published) references that are NOT Wikipedia.  For ease of grading your paper should be in the .doc or .docx format.**

–       **Extra Credit: ** If you add some non-trivial extra functionality to your HybridTrieST\&lt;V\&gt; you can get some extra credit.  Think about what might be useful to do and make sure it is implemented using the TrieNodeInt\&lt;V\&gt; interface exclusively (i.e. independent of a particular node type).
