// Zhengming Wang zhw78 CS1501 Assignment 1
import java.io.*;
import java.util.*;

public class AnagramTest
{
	public static void main(String[] args) throws IOException
	{
		File inputFile;
		File outputFile;

		//Reading the dictionary into a Trie
		Scanner dictScan = new Scanner(new FileInputStream("dictionary.txt"));

		TrieSTNew<String> D = new TrieSTNew<String>(); //Making a new Trie
		String st; 
		while(dictScan.hasNext())
		{
			st = (dictScan.nextLine()).toLowerCase(); //Scan through the file and put it in the Trie.
			// System.out.println(st);
			D.put(st, st);
		}

		//Reading the input file into a queue.
		Scanner sc = new Scanner(System.in);
		System.out.println("Please give an input file: ");
		String inputName = sc.nextLine();
		Scanner inputScan;
		try{
			inputFile = new File(inputName);
			inputScan = new Scanner(new FileInputStream(inputFile));
		} catch(FileNotFoundException e){
			System.out.println("Error: Can't find input file!");
			return;
		}
		
		System.out.println("Please give an output file: ");
		String outputName = sc.nextLine();
		outputFile = new File(outputName);
		BufferedWriter writer;
		try{
			outputFile = new File(outputName);
			writer = new BufferedWriter(new FileWriter(outputFile,true));
		} catch(FileNotFoundException e){
			System.out.println("Error: Incompatible output file!");
			return;
		}
		
		
		ArrayList<String> q = new ArrayList<>();
		while(inputScan.hasNext())
		{
			st = inputScan.nextLine();
			q.add(st);
		}

		ArrayList<String> finalQ = new ArrayList<>();
		
		//Write the things to an output file.
		// 	int originalLength = inputWord.length();
		// 	int numOfWords = 1+(originalLength-inputWord.replaceAll(" ","").length());
		// 	int originalNumWords = 1+(originalLength-inputWord.replaceAll(" ","").length());
		// 	int phraseSubSize = 0;


		// 	String bigWord = tempQ.get(0);
		// 	int currentLength = bigWord.length();
		// 	if(bigWord.length()>originalLength)
		// 	{
		// 		numOfWords = 1+(currentLength-inputWord.replaceAll(" ","").length());
		// 		writer.write("The " + numOfWords+"-word solutions for '" + inputWord + "': \n");
		// 	}
		// 	else if(bigWord.length()<originalLength)
		// 	{
		// 		numOfWords = 1+(currentLength-bigWord.replaceAll(" ","").length());
		// 	}
		// 	for(String anagram: tempQ)
		// 	{	
		// 		// Every time the length of an anagram changes it means there is an extra space in the phrase
		// 		// This implies that there is an extra word in the phrase than before. 
		// 		// Because the arraylist is sorted by string length we know that every time the length of the anagram changes. It
		// 		// is because the size of the anagram increased.  
		// 		System.out.println(((Math.abs(anagram.length()-originalLength))+1));
		// 		if((Math.abs(anagram.length()-originalLength))+1 > numOfWords) 
		// 		{			
		// 			if(numOfWords>originalNumWords-1) // There are no 0 word solutions
		// 			{
		// 				writer.write("There were " +phraseSubSize+" of them!\n"); //Required for 5 points waow
		// 			}
		// 			phraseSubSize=0; // Reset the count of the phrases with a certain # of words
		// 			numOfWords = (anagram.length()-inputWord.replaceAll(" ","").length())+1; //  everytime the anagram increases in size, we know there is another word
		// 			// System.out.println(numOfWords);
		// 			writer.write("The " + numOfWords+"-word solutions for '" + inputWord + "': \n"); // For file output
		// 		}
		// 		phraseSubSize += 1;
		// 		writer.write(anagram);
		// 		writer.write("\n");
		// 	}
		// 	writer.write("There were " +phraseSubSize+" of them!\n");
		// 	writer.write("There were a total of " + tempQ.size() + " solution(s) for '" + inputWord +"'.\n\n"); //Prints size of the temporary Queue
		// }
		for(String inputWord: q)
		{
			ArrayList<String> anagramList = (ArrayList<String>) D.getAnagrams(inputWord);
			writer.write("Here are the results for '" + inputWord +"'.\n");
			String firstWord = anagramList.get(0);
			int numOfWords = 1+(firstWord.length()-firstWord.replaceAll(" ","").length());
			int currentLength = firstWord.length();
			writer.write("The " + numOfWords+"-word solutions for '" + inputWord+"':\n");

			int phraseSubSize = 0;
			for(String anagram: anagramList)
			{
				if(anagram.length()>currentLength)
				{
					currentLength= anagram.length();
					numOfWords = 1+(anagram.length()-anagram.replaceAll(" ","").length());
					writer.write("There were " +phraseSubSize+" of them!\n");
					phraseSubSize=0;
					writer.write("The " + numOfWords+"-word solutions for '" + inputWord+"':\n");
				}
				phraseSubSize+=1;
				writer.write(anagram+"\n");
			}
			writer.write("There were " +phraseSubSize+" of them!\n");
			writer.write("There were a total of " + anagramList.size() + " solution(s) for '" + inputWord +"'.\n\n"); //Prints size of the temporary Queue
		}
		writer.close();
	}
}