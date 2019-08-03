import java.util.*;
import java.io.*;
import java.net.*;
import java.math.*;

public class Add128 implements SymCipher
{
	private byte[] key;


	public Add128()
	{
		key = new byte[128];
		Random random = new Random();
		random.nextBytes(key);
	}

	public Add128(byte[] param)
	{
		key = param;
	}

	public byte[] getKey()
	{
		return key;
	}

	public byte[] encode(String s)
	{
		byte[] originalString = s.getBytes();
		int i = 0;
		int j = 0;
		byte[] encodeString = new byte[s.length()];
		while(i<originalString.length)
		{	
			encodeString[i] = (byte) (originalString[i] + key[j]);
			i+=1;
			j = j + 1 % key.length;
		}

		return encodeString;
	} 

	public String decode(byte[] bytes)
	{
		int i = 0;
		int j = 0;
		byte[] decodeString = new byte[bytes.length];
		while(i<bytes.length)
		{
			decodeString[i] =(byte) (bytes[i] - key[j]);
			i+=1;
			j = j + 1 % key.length;
		}

		String origString = new String(decodeString);
		return origString;
	}
	

	// public static void main(String[] args)
	// {
	// 	Add128 test = new Add128();
	// 	byte[] jumble = test.encode("Why are we being graded for GUI in an Algorithms Class ???");
	// 	String plsWork = test.decode(jumble);
	// 	System.out.println(plsWork);
	// 	System.out.println(test.getKey());
	// }
}