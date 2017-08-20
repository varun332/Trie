package structures;

import java.util.ArrayList;

/**
 * This class implements a compressed trie. Each node of the tree is a CompressedTrieNode, with fields for
 * indexes, first child and sibling.
 * 
 * @author Sesh Venugopal
 *
 */
public class Trie {
	
	/**
	 * Words indexed by this trie.
	 */
	ArrayList<String> words;
	
	/**
	 * Root node of this trie.
	 */
	TrieNode root;
	
	/**
	 * Initializes a compressed trie with words to be indexed, and root node set to
	 * null fields.
	 * 
	 * @param words
	 */
	public Trie() {
		root = new TrieNode(null, null, null);
		words = new ArrayList<String>();
	}
	
	/**
	 * Inserts a word into this trie. Converts to lower case before adding.
	 * The word is first added to the words array list, then inserted into the trie.
	 * 
	 * @param word Word to be inserted.
	 */
	public void insertWord (String word) {
		/** COMPLETE THIS METHOD **/
		word = word.toLowerCase();  
		addToList(word, root);
	}
	
	private TrieNode addToList(String wordToAdd, TrieNode currentNode){
		
		TrieNode familyCousin = currentNode.sibling;
		TrieNode familyChild = currentNode.firstChild;
		String ptrString = findString(currentNode);
		String previousString = " ";

		if (wordToAdd.equals(ptrString)) {
			return currentNode;			
		}
	
		if (familyChild == null && familyCousin == null) {
			
			if (ptrString == " ") {
				words.add(wordToAdd);
				previousString = ptrString;
				
				if (previousString.length() == 0){
					previousString = " ";
				}
				
					Indexes totalIndex = new Indexes(words.size() - 1, (short) (previousString.length() - 1), (short) (wordToAdd.length() - 1));
					TrieNode toBeAdded = new TrieNode(totalIndex, null, null);	
					currentNode.firstChild = toBeAdded;
					previousString = wordToAdd;
					
					return currentNode;
			}
		
			if (ptrString != " " && wordToAdd.contains(ptrString)) {
				words.add(wordToAdd);
				previousString = ptrString;
				
				if (previousString.length() == 0){
					previousString = " ";
				}
				
					Indexes totalIndex = new Indexes(words.size()-1, (short)(previousString.length()-1), (short)(wordToAdd.length()-1));
					TrieNode toBeAdded = new TrieNode(totalIndex, null, null);	
					currentNode.firstChild = toBeAdded;
					previousString = wordToAdd;
					
					return currentNode;
			}
			
			if (ptrString.contains(wordToAdd)) {
				
				Indexes brandNewIndexes = currentNode.substr;
				short node = brandNewIndexes.startIndex;
				brandNewIndexes.startIndex = (short) (wordToAdd.length() - 1);
				brandNewIndexes.endIndex = (short) (ptrString.length() - 1);
				words.add(wordToAdd);
				Indexes familyParent = new Indexes(words.size() - 1, (short) node, (short) (wordToAdd.length() - 1));
				TrieNode temporaryNode = new TrieNode(brandNewIndexes, null, null);
				currentNode.substr = familyParent;
				currentNode.firstChild = temporaryNode;
			
				return currentNode;
			}

			String isEqual = findPreviousLetters(wordToAdd, ptrString);
			
			String freeSpace = "";
			
			if (isEqual == "-1"){
				words.add(wordToAdd);
				previousString = ptrString;
				
				if (previousString.length() == 0){
					previousString = " ";
				}
				
				Indexes totalIndex = new Indexes(words.size() - 1, (short) (freeSpace.length() - 1), (short) (wordToAdd.length() - 1));
				TrieNode toBeAdded = new TrieNode(totalIndex, null, null);	
				currentNode.sibling = toBeAdded;
				previousString = wordToAdd;
				System.out.println(words);
				
				return currentNode;
			}
			
			else {
				
				words.add(wordToAdd);
				
				Indexes familyParent = currentNode.substr;
				familyParent.endIndex = (short) (isEqual.length() - 1);
				Indexes familyChildPlace = new Indexes(familyParent.wordIndex, (short) (isEqual.length()), (short) (ptrString.length() - 1));
				Indexes familyCousinPlace = new Indexes(familyParent.wordIndex + 1, (short) (isEqual.length()), (short) (wordToAdd.length() - 1));
				TrieNode newFamilyCousin = new TrieNode(familyCousinPlace, null, null);
				TrieNode newFamilyChild = new TrieNode(familyChildPlace, null, newFamilyCousin);
				currentNode.firstChild = newFamilyChild;
				
				return currentNode;
			}
		}

		if (familyCousin != null) {
			return addToList(wordToAdd, familyCousin);
		}
		
		if (familyChild != null && familyCousin == null) {
			return addToList(wordToAdd, familyChild);
		}
		
		return currentNode;
	}
	
private String findPreviousLetters (String first, String second){
		
		String space = "";
		
		int lengthOfFirst = first.length();
		int lengthOfSecond = second.length();
		
		int valueOfInt = 0;
		
		if (lengthOfFirst > lengthOfSecond){
			valueOfInt = lengthOfSecond;
		}
		
		else {
			valueOfInt = lengthOfFirst;
		}
		
		if (second.charAt(0) != first.charAt(0)){
			return "-1";
		}
		
		for (int i = 0; i < valueOfInt; i++){
			if (first.charAt(i) == second.charAt(i)){
				space = space + second.charAt(i);
			}
		}
		
		if (space == ""){
			return "-1";
		}
		
		else{
			return space;
		}
	}
	
	private String findString (TrieNode current) {

		if (words.size() == 0) {
			return " ";
		}
		
		Indexes temporaryIndex = current.substr;
		
		if (temporaryIndex == null) {
			return " ";
		}
		
		int finalIndex = temporaryIndex.wordIndex;
		
		return words.get(finalIndex);
	}
	
	/**
	 * Given a string prefix, returns its "completion list", i.e. all the words in the trie
	 * that start with this prefix. For instance, if the tree had the words bear, bull, stock, and bell,
	 * the completion list for prefix "b" would be bear, bull, and bell; for prefix "be" would be
	 * bear and bell; and for prefix "bell" would be bell. (The last example shows that a prefix can be
	 * an entire word.) The order of returned words DOES NOT MATTER. So, if the list contains bear and
	 * bell, the returned list can be either [bear,bell] or [bell,bear]
	 * 
	 * @param prefix Prefix to be completed with words in trie
	 * @return List of all words in tree that start with the prefix, order of words in list does not matter.
	 *         If there is no word in the tree that has this prefix, null is returned.
	 */
	public ArrayList<String> completionList(String prefix) {
		/** COMPLETE THIS METHOD **/
		
		/** FOLLOWING LINE IS A PLACEHOLDER FOR COMPILATION **/
		/** REPLACE WITH YOUR IMPLEMENTATION **/
		ArrayList<String> completeList = new ArrayList<String>();
		
		for (int i = 0; i < words.size(); i++){
			int begin = 0;
			
			int end = 0;
			
			while (end != prefix.length()){
				
				if (words.get(i).charAt(end) == prefix.charAt(end)) {
					begin++;
					end++;		
				}
				
				else {
					break;
				}
			}
				
			if (begin == prefix.length()){
				completeList.add(words.get(i));		
			}		
		}
		
		return completeList;
	}
	
	public void print() {
		print(root, 1, words);
	}
	
	private static void print(TrieNode root, int indent, ArrayList<String> words) {
		if (root == null) {
			return;
		}
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		
		if (root.substr != null) {
			System.out.println("      " + words.get(root.substr.wordIndex));
		}
		
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		System.out.print(" ---");
		System.out.println("(" + root.substr + ")");
		
		for (TrieNode ptr=root.firstChild; ptr != null; ptr=ptr.sibling) {
			for (int i=0; i < indent-1; i++) {
				System.out.print("    ");
			}
			System.out.println("     |");
			print(ptr, indent+1, words);
		}
	}
 }
