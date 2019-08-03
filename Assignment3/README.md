**CS 1501 Programming Project 3**

Lempel-Ziv-Welch Compression

** **

**Online:**  Friday, June 14, 2019

**Due:**  All materials zipped into a single .zip file posted to the course submission site by 11:59PM on  **Saturday, June 29, 2019**

Late: All materials submitted by 11:59PM on Tuesday, July 2, 2019.

** **

**Purpose:**  The purpose of this assignment is for you to fully understand the LZW compression algorithm, its performance and its implementation.

** **

**Procedure:**

1)    Thoroughly read the description/explanation of the LZW compression algorithm as discussed in lecture and in the Sedgewick text.

2)    Read over and make sure you understand the code provided by Sedgewick in file LZW.java.

3)    There are three fundamental problems with this code as given:

a)     The code reads the entire file as a single string, then compresses the characters in this string.  This is problematic in that a very large file could not effectively be fit into a single Java string.  Further, since JDK 7, the substring() method in the Java String class actually generates a new String object, causing a lot of overhead when used in the manner shown in the author&#39;s LZW.java file.  In fact, with the textbook code using JDK 7+ and a large input file, the compression can take an excessive amount of time (ex: hours).   This is due primarily to the code on line 30 of the LZW.java file:

                        input = input.substring(t);

The purpose of this statement is to shift down in the string past the characters that have already matched in the LZW dictionary.  However, since in JDK 7+ the substring() method generates a new String, the effect of this statement is to create a copy of the entire file string, minus a few characters at the beginning that already matched in the dictionary.  It is easy to see how this process repeated many many times (thousands and even millions) can slow the program execution incredibly.

b)    The code uses a fixed length, relatively small codeword size (12 bits).  With this limit, the program will run out of codewords relatively quickly and will not handle large files (especially archives) well.

c)     When all code words are used, the program continues to use the existing dictionary for the remainder of the compression.  This may be ok if the rest file to be compressed is similar to what has already been compressed, but it may not be.

4)    In this assignment you will modify the author&#39;s code so as to correct (somewhat) these three problems.  Proceed in the following way:

a)     Download the implementation of the algorithm provided and get it to work (you will need to also download several other files to get it to compile – they are on the Handouts page).  Follow the instructions in the comments and run the program on a few test files to get familiar with using it.  Try running the program with a large input file to see the behavior discussed above.

b)    Examine the code very carefully, convincing yourself exactly what is accomplished by each function and by each statement within each function.

c)     Copy the code to a new file called LZWmod.java and modify the code so that during compress() the input file is read as a stream of characters / bytes rather than as a single string.  There are many ways to do this and most of the details are up to you.  However, here are a few requirements:

i)      The input file must be read in a single character / byte at a time.  Look over some classes in java.io that might be appropriate to use in this case.  You may also utilize any of the author&#39;s IO classes if you wish.  The idea is that rather than using the longestPrefixOf() method on a single String, you will find the longest prefix yourself by repeatedly reading and appending characters and looking the prefix up in the symbol table.

ii)    The &quot;strings&quot; that are looked up in the dictionary must actually be StringBuilder objects.  Using StringBuilder rather than String will allow the values to be updated more efficiently (ex: appending a character onto the end of the StringBuilder will not require a new StringBuilder object to be created, as it would with a String).

iii)  The dictionary in which the StringBuilders are looked up must be some type of symbol table where the keys are StringBuilder objects and the values are arbitrary Java types.  The dictionary must have (average) constant lookup time.  Note that the predefined Java Hashtable and HashMap classes are not appropriate in this case because StringBuilder does not override the hashCode() method to actually hash the string value.  Some options that could work include modifying one of the author&#39;s hash classes or the author&#39;s TrieST class so that they work for StringBuilder.  Another option is to modify your HybridTrieST class from Assignment 2 to use StringBuilder rather than String as the key.  Note that this should be a very simple change to your code and only requires changes to the HybridTrieST class and not the MTAlphaNode or DLBNode classes.

Note that you should not have to modify the expand() code for this feature at all.  Also note that this modification will take just a few lines of code but it may take a lot of trial and error before you get it working properly.  **I strongly recommend getting this to work before moving on to parts d) and e) below.  **However, if you cannot get this to work, you can still get credit for parts d) and e) below by performing it on the original LZW.java file.

d)    Also modify the code so that the LZW algorithm has a varying number of bits, as discussed in lecture.  Your codeword size should vary from 9 bits to 16 bits, and should increment the bitcount when all codes for the previous size have been used.  This also does not require a lot of modification to the program, but you must REALLY understand exactly what the program is doing at each step in order to do this successfully (so you can keep the compress and decompress processes in sync).  Once you get the program to work, thoroughly test it to make sure it is correct.  If the algorithm is correct, the byte count of the original file and the uncompressed copy should be identical.  Some hints about the variable-length code word implementation are given later on in this assignment.

e)     As a partial solution to the issue of the dictionary filling, you will give the user the option to reset the dictionary or not via a command line argument.  See more details on the command line arguments below, but the argument &quot;r&quot; will cause the dictionary to reset once all (16-bit) code words have been used.

i)      You will have to add code to reset the dictionary to the LZWmod.java file.  As discussed in lecture this option will erase and reset the entire dictionary and start rebuilding it from scratch.  As with the variable bits, be careful to sync both the compress and decompress to reset the dictionary at the same point.

ii)    Since now a file may be compressed with or without resetting the dictionary, in order to decompress correctly your program must be able to discern this fact.  This can be done quite simply by writing a flag / sentinel at the beginning of the output file (actually only 1 bit is needed for this). Then, before decompression, your program will read this flag and determine whether or not to reset the dictionary when running out of codewords.

5)    The author&#39;s interface already has a command line argument to choose compression or decompression.  File input and output can be supplied using the standard redirect operators for standard I/O: Use &quot;\&lt;&quot; to redirect the input to be a file and use &quot;\&gt;&quot; to redirect the output to be a file.  Modify the interface so that for compression a command line argument also allows the user to choose how to act when all codewords have been used. This extra argument should be an &quot;n&quot; for &quot;do nothing&quot;, or &quot;r&quot; for &quot;reset&quot;.  Note that these arguments are only used during compression – for decompression the algorithm should be able to automatically detect which technique was used and decompress accordingly.

For example, assuming your program is called LZWmod.java, if you wish to compress the file bogus.txt into the file bogus.lzw, resetting the dictionary when you run out of codewords, you would enter at the prompt:

$ java LZWmod - r \&lt; bogus.txt \&gt; bogus.lzw

To prevent headaches (especially during debugging), you should not replace the original file with the new one (i.e. leave the original file unchanged). Thus, make sure you use a name for the output file that is different from the input file.  If you then want to decompress the bogus.lzw file, you might enter at the prompt

$ java LZWmod + \&lt; bogus.lzw \&gt; bogus2.txt

The file bogus2.txt should now be identical to the file bogus.txt.  Note that there is no flag for what to do when the dictionary fills – this should be obtained from the front of the compressed file itself (which, again, requires only a single bit).

6)    Once you have your LZWmod.java program working, you should analyze its performance.  I will provide you with a number of files to use for testing – see the Assignments page for the link.  Specifically, you will compare the performance of 4 different implementations:

a.     The original LZW.java program using codewords of 12 bits (i.e. the way it is originally – you don&#39;t have to change anything)

b.     Your modified LZWmod.java program with the streaming input text and variable length BITS from 9 to 16 as explained above, WITHOUT dictionary reset

c.     Your modified LZWmod.java program with the streaming input text and variable length BITS from 9 to 16 as explained above, WITH dictionary reset

d.     The predefined Unix compress program (which also uses the lzw algorithm).  If you have a Mac or Linux machine you can run this version directly on your computer.   If you have a Windows machine, you can download this version of [compress.exe](http://people.cs.pitt.edu/~ramirez/cs1501/assigs/lzw/compress.exe) (obtained originally from [www.willus.com/archive/](http://www.willus.com/archive/) ).  To decompress with this program use the flag &quot;-d&quot;.

Run all programs on all of the files and for each file record the  **original**  **size** ,  **compressed size** , and  **compression ratio**  (original size / compressed size).

[Note: Because of the aforementioned run-time issues with the author&#39;s original code, it may take a prohibitive amount of time to get results for the larger files.   However, it should eventually complete – just leave yourself a lot of time for your runs.

7)    Write a short (~2 page) paper that discusses each of the following:

a)     How all four of the lzw variation programs compared to each other (via their compression ratios) for each of the different files.  Where there was a difference between them, be sure to explain (or speculate) why.  To support your assertions,  **include a table showing all of the results of your tests**  (original sizes, compressed sizes and compression ratios for each algorithm).

b)    For all algorithms, indicate which of the test files gave the best and worst compression ratios, and speculate as to why this was the case.  If any files did not compress at all or compressed very poorly (or even expanded), speculate as to why.

8)    Submit a single .zip file containing your **modified LZWmod.java source code and any other source files (ex: author supplied files) necessary to compile your code.  **Also include in your .zip file your paper along with your Assignment Information Sheet.  **Note 1: DO NOT submit any of the test files (input or output) – this will waste an incredible amount of space on the submission site! ****Note 2: Do not submit a NetBeans or Eclipse (or any other IDE) project file.  If you use one of these IDEs make sure you extract and test your Java files WITHOUT the IDE before submitting.**

9)     **W Section** : In addition to the paper from 7) above, write a second, ~2-3 page paper in which you discuss in detail your modifications to the program and how you got the streaming character input, the variable length codes and the dictionary reset to work.  Explain in detail everything you did to the program and why, especially issues that caused you any grief.   This paper will be evaluated for both form and content.   **Be sure to submit this paper as a .doc or .docx file so that we can easily insert comments / changes.**   You may also be required to write a revision of your paper later on.

10) Hints:

a)     In the author&#39;s code the bits per codeword (W) and number of code words (L) values are constants.  However, in your version you will need them to be variables.  Clearly, as the bits per codeword value increases, so does the number of code words value.

b)    The symbol table that you use for the compression dictionary (ex: Trie, DLB) can grow dynamically, so you do not have to alter this as the code word size increases.  However, for the expand() method an array of String is used for the dictionary.  Make sure this is large enough to accommodate the maximum possible number of code words.

c)     Carefully trace what your code is doing as you modify it.  You only have to write a few lines of code for this program, but it could still require a substantial amount of time to get to work properly.  Clearly the trickiest parts occur when the bits per code word values are increased and when the dictionary is reset.  It is vital that these changes be made in a consistent way during both compress and decompress.   I recommend tracing these portions of code, either on paper or with output statements to make sure your compress and expand sections are treating them correctly.  One idea is the have an extra output file for each of the compress() and expand() methods to output any trace code.  Printing out (codeword, string) pairs in the iterations just before and after a bit change or reset is done can help you a lot to synchronize your code properly.

11) If you want to try some extra credit on this project, you can implement the reset in seamless way, so that the user does not have to specify whether or not to reset the dictionary.  As discussed in lecture, this would involve some type of monitoring of the compression ratio once the code words are all used and a reset would occur only when the compression ratio degrades to some level (you may have to do some trial and error to find a good value for the reset trigger level).
