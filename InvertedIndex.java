import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class InvertedIndex 
	{
	TreeMap<String, TreeMap<String, TreeSet<Integer>>> invertedIndex;
	
	public InvertedIndex()
		{
		this.invertedIndex = new TreeMap<String, TreeMap<String, TreeSet<Integer>>>();
		}
	
	public TreeMap<String,TreeMap<String,TreeSet<Integer>>> getIndex()
		{
		return invertedIndex;
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
	}
