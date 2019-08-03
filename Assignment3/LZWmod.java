/*************************************************************************
 *  Compilation:  javac LZW.java
 *  Execution:    java LZW - < input.txt   (compress)
 *  Execution:    java LZW + < input.txt   (expand)
 *  Dependencies: BinaryIn.java BinaryOut.java
 *
 *  Compress or expand binary input from standard input using LZW.
 *
 *
 *************************************************************************/
import java.lang.StringBuilder;
public class LZWmod {
    private static final int R = 256;        // number of input chars
    private static int W = 9;         // codeword width
    private static int L = (int) Math.pow(2,W);       // number of codewords = 2^W
    private static int byteCount = 0;
    private static int compressCount = 0;   

    // Helper method that updates the variable length codeword by incrementing the width (W) by 1.
    // Also update the number of codewords (L).     
    private static void updateW()
    {
        W+=1; // update values
        L = (int) Math.pow(2,W);
    }

    // Helper method that updates the variable length codeword for expansion.
    // Increments width (W) by 1
    // Updates number of code words (L).
    // Fills the old dictionary in a new one with the new L size.
    private static String[] updateW(String[] st)
    {
        W+=1; // update values
        L = (int) Math.pow(2,W);

        String[] newSt = new String[L];
        for(int i = 0; i < st.length; i++)
        {
            newSt[i] = st[i]; // Transfer things over
        }

        return newSt;
    }

    // Private method that clears the symbol table. Used for resetting expansion.
    private static String[] clearST(String[] st)
    {
        String[] newST = new String[L];
        int i;
        for (i = 0; i < R; i++)
        {    
            newST[i] = "" + (char) i;
        }
        newST[i++] = ""; // For EOF
        W= 9; // Back to small codewords.
        return newST;
    }

    // Private method that clears the symbol table. Used for compression
    private static TrieSTMT<Integer> clearST()
    {
        TrieSTMT<Integer> st = new TrieSTMT<Integer>();
        for (int i = 0; i < R; i++)
            st.put(new StringBuilder("" + (char) i), i);
        return st;
    }

    // Compression where nothing is done.
    public static void nCompress() 
    { 
        TrieSTMT<Integer> st = new TrieSTMT<Integer>();
        for (int i = 0; i < R; i++) // Fill the Trie up
            st.put(new StringBuilder("" + (char) i), i);
        int code = R+1;  // R is codeword for EOF

        StringBuilder input = new StringBuilder(); // String replacement.
        char lastChar =' '; // last character processed 
        while(!BinaryStdIn.isEmpty())
        {
            lastChar = BinaryStdIn.readChar(); // Read in the next Char
            byteCount+=1; // Increment byte counter by one.
            input.append(lastChar); // add the last char into the String Builder.
            if(!st.contains(input)) // Means we've found the longest prefix
            {
                StringBuilder original = new StringBuilder(input.toString()); // Remeber the new input
                BinaryStdOut.write(st.get(input.deleteCharAt(input.length()-1)),W); // Write the code of the longest prefix (string builder without last char)
                compressCount+=W; //Increment the compressed bytes counter.

                if(code<L-2) // Add the new code to the dictionary.
                {
                    st.put(original,code++);
                }
                
                if(code==L-1 && W<17) // Update W if needed
                {
                    updateW();
                }
                input = new StringBuilder(""+lastChar);
            }
        }
        BinaryStdOut.write(st.get(input),W);
        BinaryStdOut.write(R, W);
        BinaryStdOut.close();
    } 

    public static void nExpand() 
    {
        String[] st = new String[L];
        int i; // next available codeword value

        // initialize symbol table with all 1-character strings
        for (i = 0; i < R; i++)
        {   
            st[i] = "" + (char) i;
        }
        st[i++] = "";                        // (unused) lookahead for EOF

        int codeword = BinaryStdIn.readInt(W);
        String val = st[codeword];

        while (true) {
            BinaryStdOut.write(val);
            codeword = BinaryStdIn.readInt(W);
            if (codeword == R) 
            {
                break;
            }

            String s = st[codeword];
            if (i == codeword)
            { 
                s = val + val.charAt(0);   // special case hack
            }
            if (i < L-2) // Mimic the behavior of nCompress. 
            {
                st[i++] = val + s.charAt(0);
            }

            if(i == L-1 && W<17) // We need to sync the W updates between compress and expansions. Which mean that the two methods need
                                // to have identical conditions
            {
                st = updateW(st);
            }

            val = s;
        }

        BinaryStdOut.close();
    }


    //Compress where we reset the dictionary when it fills up
    // Nearly identical to nCompress()
    public static void eCompress() 
    { 
        TrieSTMT<Integer> st = new TrieSTMT<Integer>();
        for (int i = 0; i < R; i++)
            st.put(new StringBuilder("" + (char) i), i);
        int code = R+1;  // R is codeword for EOF
        StringBuilder input = new StringBuilder();
        boolean done = false;
        char lastChar =' ';
        while(!BinaryStdIn.isEmpty())
        {
            lastChar =  BinaryStdIn.readChar();
            byteCount+=1;
            input.append(lastChar);
            // System.out.println(input.toString());
            if(!st.contains(input))
            {
                StringBuilder original = new StringBuilder(input.toString());
                BinaryStdOut.write(st.get(input.deleteCharAt(input.length()-1)),W);
                compressCount += W;
                if(code<L-2)
                {
                    st.put(original,code++);
                }

                if(code==L-1 && W<17)
                {
                    updateW();
                }

                if(W==16 && code == L-1) // The only unique part of eCompress(). We reset at 16-bit code words.
                {
                    st = clearST(); // Reset the trie
                    code = 257; // Code needs to be reset as well.
                }

                input = new StringBuilder(""+lastChar);
            }
        }
        BinaryStdOut.write(st.get(input),W);
        BinaryStdOut.write(R, W);
        BinaryStdOut.close();
    } 

    // expansion method that accounts for dictionary resets. Nearly identical to nExpand()
    public static void eExpand() 
    {
        String[] st = new String[L];
        int i; // next available codeword value

        // initialize symbol table with all 1-character strings
        for (i = 0; i < R; i++)
            st[i] = "" + (char) i;
        st[i++] = "";                        // (unused) lookahead for EOF

        int codeword = BinaryStdIn.readInt(W);
        String val = st[codeword];

        while (true) {
            BinaryStdOut.write(val);
            codeword = BinaryStdIn.readInt(W);
            if (codeword == R) 
            {
                break;
            }
            String s = st[codeword];
            if (i == codeword)
            {
                s = val + val.charAt(0);   // special case hack
            }
 

            if (i < L-2)
            {
                st[i++] = val + s.charAt(0);
            }

            if(i == L-1 && W<17)
            {
                st = updateW(st);
            }
            
            if(W==16 && i == L-1) // The unique part of eExpand(). We sync the resets here.
            {
                st = clearST(st);
                i = 257;
            }
            val = s;
        }
        BinaryStdOut.close();
    }

    //Method that determines how we compress
    public static void compress(String flag)
    {
        char val;
        if(flag.equals("n"))
        {
            val = 'n';
            BinaryStdOut.write(val,16); // Write a val so that during expansion we know what mode we are using.
            nCompress();
        }

        else if(flag.equals("r"))
        {
            val = 'r';
            BinaryStdOut.write(val,16);
            eCompress();
        }
        else throw new RuntimeException("Illegal command line argument");  
    }

    // Method that determines how we expand.
    public static void expand()
    {
        char version = BinaryStdIn.readChar(); // Read the char to get the correct version.
        version = BinaryStdIn.readChar();
        // System.out.println(version);
        if(version=='n')
        {
            nExpand();
        }
        else if(version == 'r')
        {
            eExpand();
        }

        else throw new RuntimeException("Illegal Compressed Input File"); 
    }

    public static void main(String[] args) 
    {
        if(args[0].equals("+"))
        {
            expand();
        }

        else if(args[0].equals("-"))
        {
            if(args.length < 2) throw new RuntimeException("Insufficient command line arguments"); // Argument handlings.
            compress(args[1]);
        }      
        else throw new RuntimeException("Illegal command line argument");            
    }

}
