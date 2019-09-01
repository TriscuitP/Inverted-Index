import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.TreeSet;

public class InvertedIndex 
{
	private TreeMap<String, TreeMap<String, TreeSet<Integer>>> invertedIndex;
	
	public InvertedIndex()
	{
		this.invertedIndex = new TreeMap<String, TreeMap<String, TreeSet<Integer>>>();
	}
	
	public TreeMap<String,TreeMap<String,TreeSet<Integer>>> getIndex()
	{
		return this.invertedIndex;
	}
	
	/**
	 * Adds the array of words at once, assuming the first word in the array is
	 * at the provided starting position
	 *
	 * @param words
	 *            array of words to add
	 * @param start
	 *            starting position
	 */
	public void addAll(String[] words, String html) 
	{
		int position = 1;
		for(String w : words)
		{
			add(w, html, position);
			position++;
		}
	}
	
	/**
	 * Adds the word, the html page, and the position it was found to the index.
	 *
	 * @param word
	 *            word to clean and add to index
	 * @param position
	 *            position word was found
	 */
	public void add(String word, String html, int position)
	{
		if(invertedIndex.get(word) == null)
			invertedIndex.put(word, new TreeMap<String, TreeSet<Integer>>());
		
		if(invertedIndex.get(word).get(html) == null)
			invertedIndex.get(word).put(html, new TreeSet<Integer>());
		
		invertedIndex.get(word).get(html).add(position);
	}
	
	/**
	 * searchHelper for the searching method
	 * @param word
	 * 			word to input
	 * @param searchResults
	 * 			SearchResults arrayList 
	 * @param searchMap
	 * 			SearchResults hashmap
	 */
	private void searchHelper(String word, ArrayList<SearchResult> searchResults, HashMap<String, SearchResult> searchMap)
	{
		TreeMap<String, TreeSet<Integer>> pathAndPositions = invertedIndex.get(word);
		for(String path : pathAndPositions.keySet())
		{
			int count = pathAndPositions.get(path).size();
			int position = pathAndPositions.get(path).first();
			
			if(searchMap.containsKey(path))
				searchMap.get(path).update(count, position);
			else
			{
				searchMap.put(path, new SearchResult(count, position, path));
				searchResults.add(searchMap.get(path));
			}
		}
	}
	
	/**
	 * Takes in a query and searches through the index for an exact match
	 * @param words
	 * 			each individual word
	 * @return
	 * 		returns a list of sorted exact search results
	 */
	public ArrayList<SearchResult> exactSearch(String[] words)
	{
		ArrayList<SearchResult> exactSearchResults = new ArrayList<SearchResult>();
		// Map used for multiple-word queries
		HashMap<String, SearchResult> searchMap = new HashMap<String, SearchResult>();
		for(String w : words)
		{
			if(invertedIndex.containsKey(w))
				searchHelper(w, exactSearchResults, searchMap);
		}
		
		Collections.sort(exactSearchResults);
		return exactSearchResults;
	}
	
	/**
	 * Takes in a query and searches through the index for a partial match
	 * @param words
	 * 			each individual word
	 * @return
	 * 		returns a list of sorted exact search results
	 */
	public ArrayList<SearchResult> partialSearch(String[] words)
	{
		ArrayList<SearchResult> partialSearchResults = new ArrayList<SearchResult>();
		
		// Map used for multiple-word queries
		HashMap<String, SearchResult> searchMap = new HashMap<String, SearchResult>();
		for(String partialWord : words)
		{
			for(String w : invertedIndex.tailMap(partialWord).keySet())
			{
				if(w.startsWith(partialWord))
					searchHelper(w, partialSearchResults, searchMap);
				else
					break;
			}	
		}
		
		Collections.sort(partialSearchResults);
		return partialSearchResults;
	}
}
