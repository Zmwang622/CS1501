import java.util.*;
import java.io.*;
import java.net.*;
import java.math.*;

public class Substitute implements SymCipher
{
	private byte[] key;

	public Substitute()
	{
		key = new byte[256];
		List<Byte> shuffled = new ArrayList<>(256);
		for(short i = 0; i< 255;i++)
		{
			shuffled.add((byte)i);
		}
		Collections.shuffle(shuffled);

		for(int i = 0;i<shuffled.size();i++)
		{
			key[i] = shuffled.get(i);
		}
	}

	public Substitute(byte[] param)
	{
		key = param;
	}

	public byte[] getKey()
	{
		return key;
	}

	public byte[] encode(String s)
	{
		char[] originalString = s.toCharArray();
		byte[] encodeString = new byte[s.length()];
		for(int i = 0; i < originalString.length; i++)
		{
			encodeString[i] = key[originalString[i]];
		}
		return encodeString;
	}

	public String decode(byte[] bytes)
	{
		byte[] decodeString = new byte[bytes.length];
		Hashtable<Byte,Byte> inverse = getInverse();
		for(int i = 0; i < bytes.length; i++)
		{
			decodeString[i] = inverse.get(bytes[i]); 
		}

		String originalString = new String(decodeString);
		return originalString;
	}

	private Hashtable<Byte,Byte> getInverse()
	{
		Hashtable<Byte,Byte> ht = new Hashtable<>(256);
		for(int i = 0; i < key.length; i++)
		{
			ht.put(key[i],(byte)(char) i);
		}
		return ht;
	}

	public static void main(String[] args)
	{
		Substitute test = new Substitute();
		byte[] jumble = test.encode("Now that i found you, i wanna stay around you.");
		String plsWork = test.decode(jumble);
		System.out.println(plsWork);
	}
}