import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeMap;

/** 
 * Parses queries for partial and exact search
 * TreeMap
 * 		- Key: Query
 * 		- Values: Results
 */
public class QuerySearch 
{
	private InvertedIndex indexMap;
	private TreeMap<String, ArrayList<SearchResult>> map;
	
	public QuerySearch(InvertedIndex inputMap)
	{
		this.indexMap = inputMap;
		map = new TreeMap<String, ArrayList<SearchResult>>();
	}
	
	/**
	 * Method that parses queries by reading the file line by line and calling the appropriate exact/partial search method
	 * @param path
	 * 			path to input
	 * @param exact
	 * 			boolean exact which is used to later call the exact/partial search methods accordingly
	 * @throws IOException
	 */
	public void parseQuery(Path path, boolean exact) throws IOException
	{
		try(BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8))
		{
			// Parse each line
			String line = reader.readLine();
			while(line != null)
			{
				// Cleaned query string
				String[] words = WordParser.parseWords(line);
				
				if(words.length > 0)
				{
					// Sort array of words
					Arrays.sort(words);
					line = String.join(" ", words);
					
					// Exact/partial search result
					ArrayList<SearchResult> result;
					
					if(exact)
						result = indexMap.exactSearch(words);
					else
						result = indexMap.partialSearch(words);
					
					// Add to mapping
					map.put(line, result);
				}
				
				line = reader.readLine();
			}
		}
	}
	
	/**
	 * Writes the data structure to JSON format
	 * @param path
	 * 			path to input
	 * @throws IOException
	 */
	public void toJSON(Path path) throws IOException 
	{
		JSONWriter.toSearchFormat(map, path);
	}
}
