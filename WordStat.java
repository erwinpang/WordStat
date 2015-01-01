import java.io.IOException;
import java.util.ArrayList;


public class WordStat {
	private Tokenizer tkz;
	private HashTable ht;
	private HashTable pt;
	private ArrayList<String> wordList;
	private ArrayList<String> pairList;
	private int[] rankedTable;
	private String[] rankedWords;
	private int[] rankedPairTable;
	private String[] rankedPairs;
	private int[] rankedTableWithoutDupes;
	private int[] rankedPairTableWithoutDupes;

	public WordStat(String fileName) throws IOException{
		tkz = new Tokenizer(fileName);
		ArrayList<String> wordArr = tkz.wordList();
		ht = new HashTable();
		int size = wordArr.size();
		wordList = new ArrayList<String>();
		
		//Build HashTable and generate ArrayList of words
		for (int i = 0; i < size; i++){
			if(ht.search(wordArr.get(i)) == -1){
				ht.put(wordArr.get(i), 1);
				wordList.add(wordArr.get(i));
			}
			else{
				ht.update(wordArr.get(i), ht.get(wordArr.get(i)) + 1);
			}
		}
		
		/*
		 * Order words by rank
		 * Eventually map words to rank
		 */
		rankedWords = new String[wordList.size()];
		rankedTable = new int[wordList.size()];
		for (int i = 0; i < wordList.size(); i++){
			rankedWords[i] = wordList.get(i);
			rankedTable[i] = ht.get(wordList.get(i));
		}
		selectionSort(rankedTable, rankedWords, rankedTable.length);
		rankedTableWithoutDupes = new int[rankedTable.length];
		rankedTableWithoutDupes[rankedTable.length - 1] = 1;
		int j = 1;
		for (int i = rankedTable.length - 2; i > 0; i--){
			if(rankedTable[i] == rankedTable[i + 1]){
				rankedTableWithoutDupes[i] = rankedTableWithoutDupes[i + 1];
				j++;
			}
			else{
				j++;
				rankedTableWithoutDupes[i] = j;
			}
		}
		
		/*
		 * Same as above, but for word pairs
		 */
		ArrayList<String> pairArr = tkz.wordPairs();
		pt = new HashTable();
		pairList = new ArrayList<String>();
		
		for(int i = 0; i < pairArr.size(); i++){
			if(pt.search(pairArr.get(i)) == -1){
				pt.put(pairArr.get(i), 1);
				pairList.add(pairArr.get(i));
			}
			else{
				pt.update(pairArr.get(i), pt.get(pairArr.get(i)) + 1);
			}
		}
		
		rankedPairs = new String[pairList.size()];
		rankedPairTable = new int[pairList.size()];
		for (int i = 0; i < pairList.size(); i++){
			rankedPairs[i] = pairList.get(i);
			rankedPairTable[i] = pt.get(pairList.get(i));
		}
		selectionSort(rankedPairTable, rankedPairs, rankedPairTable.length); 
	}
	
	public WordStat(String[] words){
		tkz = new Tokenizer(words);
		ArrayList<String> wordArr = tkz.wordList();
		ht = new HashTable();
		int size = wordArr.size();
		wordList = new ArrayList<String>();
		
		//Build HashTable and generate ArrayList of words
		for (int i = 0; i < size; i++){
			if(ht.search(wordArr.get(i)) == -1){
				ht.put(wordArr.get(i), 1);
				wordList.add(wordArr.get(i));
			}
			else{
				ht.update(wordArr.get(i), ht.get(wordArr.get(i)) + 1);
			}
		}
		
		/*
		 * Order words by rank
		 * Eventually map words to rank
		 */
		rankedWords = new String[wordList.size()];
		rankedTable = new int[wordList.size()];
		for (int i = 0; i < wordList.size(); i++){
			rankedWords[i] = wordList.get(i);
			rankedTable[i] = ht.get(wordList.get(i));
		}
		selectionSort(rankedTable, rankedWords, rankedTable.length);
		
		
		/*
		 * Same as above, but for word pairs
		 */
		ArrayList<String> pairArr = tkz.wordPairs();
		pt = new HashTable();
		pairList = new ArrayList<String>();
		
		for(int i = 0; i < pairArr.size(); i++){
			if(pt.search(pairArr.get(i)) == -1){
				pt.put(pairArr.get(i), 1);
				pairList.add(pairArr.get(i));
			}
			else{
				pt.update(pairArr.get(i), pt.get(pairArr.get(i)) + 1);
			}
		}
		
		rankedPairs = new String[pairList.size()];
		rankedPairTable = new int[pairList.size()];
		for (int i = 0; i < pairList.size(); i++){
			rankedPairs[i] = pairList.get(i);
			rankedPairTable[i] = pt.get(pairList.get(i));
		}
		selectionSort(rankedPairTable, rankedPairs, rankedPairTable.length);
	}
	
	public int wordCount(String word){
		if(ht.get(word) == -1) return 0;
		return ht.get(word);
	}
	
	public int wordPairCount(String w1, String w2){
		if(pt.get(w1 + " " + w2) == -1) return 0;
		return pt.get(w1 + " " + w2);
	}
	
	/*
	 * Does not account for tied ranks yet
	 */
	public int wordRank(String word){
		int rankIndex = 0;
		for (int i = 0; i < rankedTable.length; i++){
			//System.out.println(rankedTable[i]);
			if(rankedWords[i] == word) rankIndex = i;
		}
		return rankIndex + 1;
		//return rankedTableWithoutDupes[rankIndex];
	}
	
	public int wordPairRank(String w1, String w2){
		int rankIndex = 0;
		for (int i = 0; i < rankedPairTable.length; i++){
			if(rankedPairs[i] == (w1 + " " + w2)) rankIndex = i;
		}
		return rankIndex + 1;
	}
	
	public String[] mostCommonWords(int k){
		String[] mostCommonWords = new String[k];
		int j = 0;
		for (int i = rankedWords.length - 1; i > rankedWords.length - k - 1; i--){
			mostCommonWords[j] = rankedWords[i];
			System.out.println(mostCommonWords[j]);
			j++;
		}
		return mostCommonWords;
	}
	
	public String[] leastCommonWords(int k){
		String[] leastCommonWords = new String[k];
		for (int i = 0; i < k; i++){
			leastCommonWords[i] = rankedWords[i];
		}
		return leastCommonWords;
	}
	
	public String[] mostCommonWordPairs(int k){
		String[] mcWordPairs = new String[k];
		int j = 0;
		for (int i = rankedPairs.length - 1; i > rankedPairs.length - k - 1; i--){
			mcWordPairs[j] = rankedPairs[i];
			j++;
		}
		return mcWordPairs;
	}
	
	/*
	 * Check this
	 */
	
	public String[] mostCommonCollocs(int k, String baseWord, int i){
		String[] mcCollocs = new String[k];
		int[] mcCollocsCount = new int[k];
		int j = 0;
		for (int l = rankedPairs.length - 1; l > -1; l--){
			if(i == 1){
				if(rankedPairs[l].substring(0, rankedPairs[l].length() - 1).contains(baseWord)){
					mcCollocs[j] = rankedPairs[l];
					j++;
					if(j == k) break;
				}
			}
			else if(i == -1){
				if(rankedPairs[l].substring(1, rankedPairs[l].length()).contains(baseWord)){
					mcCollocs[j] = rankedPairs[l];
					j++;
					if(j == k) break;
				}
			}
		}
		return mcCollocs;
	}
	
	public String[] mostCommonCollocsExc(int k, String baseWord, int i, String[] exclusions){
		String[] mcCollocs = new String[k];
		int[] mcCollocsCount = new int[k];
		int j = 0;
		for (int l = rankedPairs.length - 1; l > -1; l--){
			if(i == 1){
				if(rankedPairs[l].substring(0, rankedPairs[l].length() - 1).contains(baseWord) && containsColloc(rankedPairs[l], exclusions)){
					mcCollocs[j] = rankedPairs[l];
					j++;
					if(j == k) break;
				}
			}
			else if(i == -1){
				if(rankedPairs[l].substring(1, rankedPairs[l].length()).contains(baseWord) && containsColloc(rankedPairs[l], exclusions)){
					mcCollocs[j] = rankedPairs[l];
					j++;
					if(j == k) break;
				}
			}
		}
		return mcCollocs;
	}
	
	public boolean containsColloc(String str, String[] exclusions){
		for (String s:exclusions){
			if(s.compareTo(str) == 0) return true;
		} 
		return false;
	}
	
	public String generateWordString(int k, String startWord){
		return null;
	}
	
	public static void selectionSort(int[] arr, String[] strArr, int length) {
		for (int i = 0; i < length - 1; i++) {
			int j = indexSmallest(arr, i, length - 1);
			swap(arr, i, j);
			swap(strArr, i, j);
		}
	}
	
	public static int indexSmallest(int[] arr, int lower, int upper) {
		int indexMin = lower;
		for (int i = lower + 1; i <= upper; i++)
			if (arr[i] < arr[indexMin])
				indexMin = i;
		return indexMin;
	}
	
	public static void swap(int[] arr, int i, int j){
		int temp = arr[i];
		arr[i] = arr[j];
		arr[j] = temp;
	}
	
	public static void swap(String[] arr, int i, int j){
		String temp = arr[i];
		arr[i] = arr[j];
		arr[j] = temp;
	}
	
	public static void main(String[] args) throws IOException{
		WordStat ws = new WordStat("844.txt");
		int k = 3;
		String[] ex = ws.mostCommonWords(3);
		for (String i:ex ) System.out.println(i);
	}
}
