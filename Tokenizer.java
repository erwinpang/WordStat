import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.*;
import java.util.List;
import java.lang.StringBuilder;

public class Tokenizer {
	private String txtFile;
	private StringBuilder txtIn = new StringBuilder();
	private ArrayList<String> wordList = new ArrayList<String>();
	String regex = "\\w+";
	private ArrayList<String> wordPairs = new ArrayList<String>();
	
	//Constructor that reads text file line by line
	public Tokenizer(String fileName) throws IOException{
		try (BufferedReader br = new BufferedReader(new FileReader((fileName))))
		{
 
			String sCurrentLine;
 
			while ((sCurrentLine = br.readLine()) != null) {
				txtIn.append(sCurrentLine + " ");
			}
 
		} catch (IOException e) {
			e.printStackTrace();
		}
		txtFile =  txtIn.toString();
		txtFile = txtFile.toLowerCase();
	}
	
	//Constructor that reads string array 
	public Tokenizer(String[] text){
		//Convert to lower case and place in String
		for (int i = 0; i < text.length; i++){
			text[i] = text[i].toLowerCase();
			txtIn.append(text[i] + " ");
		}
		txtFile = txtIn.toString();
	}
	
	public ArrayList<String> wordList(){
		txtFile = txtFile.replaceAll("\\p{P}", "");
		wordList = regexChecker(regex, txtFile);
		return wordList;
	}
	
	public ArrayList<String> wordPairs(){
		for (int i = 0; i < wordList.size() - 1; i++)
		{
			wordPairs.add(wordList.get(i) + " " + wordList.get(i + 1));
		}
		return wordPairs;
	}
	
	
	public static void main(String[] args) throws IOException {
	}
	
	public static ArrayList<String> regexChecker(String theRegex, String strIn){
		Pattern checkRegex = Pattern.compile(theRegex);
		ArrayList<String> output = new ArrayList<String>();
		Matcher regexMatcher = checkRegex.matcher(strIn);
		while(regexMatcher.find()){
			if(regexMatcher.group().length() != 0){
				output.add(regexMatcher.group().trim().toLowerCase());
			}
		}
		return output;
	}

}
