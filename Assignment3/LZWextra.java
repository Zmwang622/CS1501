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
public class LZWextra {
    private static final int R = 256;        // number of input chars
    private static int W = 9;         // codeword width
    private static int L = (int) Math.pow(2,W);       // number of codewords = 2^W
    private static int byteCount = 0;
    private static int compressCount = 0;   
    private static final double THRESHOLD = 0.9;
    
    private static void updateW()
    {
        W+=1;
        L = (int) Math.pow(2,W);
    }

    private static String[] updateW(String[] st)
    {
        W+=1;
        L = (int) Math.pow(2,W);

        String[] newSt = new String[L];
        for(int i = 0; i < st.length; i++)
        {
            newSt[i] = st[i];
        }

        return newSt;
    }

    private static String[] clearST(String[] st)
    {
        String[] newST = new String[L];
        int i;
        for (i = 0; i < R; i++)
        {    
            newST[i] = "" + (char) i;
        }
        newST[i++] = "";
        return newST;
    }

    private static TrieSTMT<Integer> clearST()
    {
        TrieSTMT<Integer> st = new TrieSTMT<Integer>();
        for (int i = 0; i < R; i++)
            st.put(new StringBuilder("" + (char) i), i);
        return st;
    }

    public static void compress() 
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
            if(!st.contains(input))
            {
                StringBuilder original = new StringBuilder(input.toString());
                BinaryStdOut.write(st.get(input.deleteCharAt(input.length()-1)),W);
                compressCount += W;
                if(code<L-2)
                {
                    st.put(original,code++);
                }

                if(code == L-1 && ((double) byteCount/ (double) compressCount) < THRESHOLD) // Check to reset before we check to update the dictionary.
                {
                    st = clearST();
                    code = 257;
                }

                // L will be different if we reset before.
                else if(code==L-1 && W<12)
                {
                    updateW();
                }

                //Still reset when full.
                if(W==16 && code == L-1)
                {
                    st = clearST();
                    code=257;
                }


                input = new StringBuilder(""+lastChar);
            }
        }
        BinaryStdOut.write(st.get(input),W);
        BinaryStdOut.write(R, W);
        BinaryStdOut.close();
    } 

    public static void expand() 
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
            byteCount+=(val.length() * 8);
            compressCount +=W;
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

            if(i == L-1 && ((double) (byteCount/compressCount)) < THRESHOLD) // syncing dictinoary reset.
            {
                st = clearST(st);
                i = 257;
            }

            if(i == L-1 && W<17)
            {
                st = updateW(st);
            }
            
            if(W==16 && i == L-1)
            {
                st = clearST(st);
                i = 257;
            }
            val = s;
        }
        BinaryStdOut.close();
    }

    public static void main(String[] args) 
    {
        if      (args[0].equals("-")) compress();
        else if (args[0].equals("+")) expand();
        else throw new RuntimeException("Illegal command line argument");     
    }

}
