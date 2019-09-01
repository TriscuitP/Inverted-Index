import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeMap;
import java.util.TreeSet;

public class JSONWriter 
{

	/**
	 * Returns a String with the specified number of tab characters.
	 *
	 * @param times
	 *            number of tab characters to include
	 * @return tab characters repeated the specified number of times
	 */
	public static String indent(int times) 
	{
		char[] tabs = new char[times];
		Arrays.fill(tabs, '\t');
		return String.valueOf(tabs);
	}

	/**
	 * Returns a quoted version of the provided text.
	 *
	 * @param text
	 *            text to surround in quotes
	 * @return text surrounded by quotes
	 */
	public static String quote(String text) 
	{
		return String.format("\"%s\"", text);
	}

	/**
	 * Writes the set of elements as a JSON array at the specified indent level.
	 *
	 * @param writer
	 *            writer to use for output
	 * @param elements
	 *            elements to write as JSON array
	 * @param level
	 *            number of times to indent the array itself
	 * @throws IOException
	 */
	private static void asArray(TreeSet<Integer> elements, Writer writer, int level) throws IOException 
	{
		writer.write("[\n");
		
		int i = 0;
		for(Integer num : elements)
		{
			writer.write(indent(level + 1) + num);
			
			if(i < elements.size() - 1)
				writer.write(",");
			
			writer.write("\n");
			i++;
		}

		writer.write(indent(level) + "]");
		writer.flush();
	}
	
	/**
	 * Writes the set of elements as a JSON object with a nested array to the
	 * path using UTF8.
	 *
	 * @param elements
	 *            elements to write as a JSON object with a nested array
	 * @param path
	 *            path to write file
	 * @throws IOException
	 */
	public static void asNestedObject(TreeMap<String, TreeSet<Integer>> elements, BufferedWriter writer) throws IOException 
	{
		int level = 2;
		int i = 0;
		for(String html : elements.keySet())
		{
			writer.write(indent(level) + quote(html) + ": ");
			asArray(elements.get(html), writer, level);
			
			if(i < elements.size() - 1)
				writer.write(",");
			
			i++;
			writer.write("\n");
		}
		
		writer.flush();
	}
	
	/**
	 * Writes the set of elements as a JSON object with a double nested array to the
	 * path using UTF8.
	 *
	 * @param elements
	 *            elements to write as a JSON object with a double nested array
	 * @param path
	 *            path to write file
	 * @throws IOException
	 */
	public static void asInvertedIndexObject(TreeMap<String, TreeMap<String, TreeSet<Integer>>> elements, Path path) throws IOException 
	{
		try(BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8))
		{
			writer.write("{\n");
			int i = 0;
			for(String word : elements.keySet())
			{
				writer.write(indent(1) + quote(word) + ": {\n");
				for(String html : elements.get(word).keySet())
				{
					asNestedObject(elements.get(word), writer);
					writer.write(indent(1) + "}");
					
					if(i < elements.size()-1)
						writer.write(",");
					
					i++;
					writer.write("\n");
					break; // This is so asNestedObject doesn't repeat
				}
			}
			writer.write("}");
			writer.flush();
		}
	}
	
	private static void resultArray(BufferedWriter writer, ArrayList<SearchResult> elements, int level) throws IOException
	{
		writer.write(indent(level + 1) + quote("results") + ": [\n" );
		int i = 0;
		for(SearchResult sr : elements)
		{
			writer.write(indent(level + 2) + "{\n");
			
			writer.write(indent(level + 3) + quote("where") + ": " + quote(sr.getLoc()) + ",\n");
			writer.write(indent(level + 3) + quote("count") + ": " + sr.getFreq() + ",\n");
			writer.write(indent(level + 3) + quote("index") + ": " + sr.getPos() + "\n");
			
			writer.write(indent(level + 2) + "}");
			
			if(i < elements.size() - 1)
				writer.write(",");
			
			i++;
			writer.write("\n");
			
		}
		writer.write(indent(level + 1)+ "]\n" );
	}
	
	/**
	 * Prints into JSON format given the raw data structure and path
	 * @param elements
	 * 			takes in the data structure
	 * @param path
	 * 			takes in the path
	 * @throws IOException
	 */
	public static void toSearchFormat(TreeMap<String, ArrayList<SearchResult>> elements, Path path) throws IOException
	{
		try(BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8))
		{
			writer.write("[\n");
			int level = 1;
			int i = 0;
			for(String query : elements.keySet())
			{
				writer.write(indent(level) + "{\n");
				writer.write(indent(level + 1) + quote("queries") + ": " + quote(query) + ",\n");
				
				resultArray(writer, elements.get(query), level);
				
				writer.write(indent(level) + "}");
				
				if(i < elements.size() - 1)
					writer.write(",");
				
				i++;
				writer.write("\n");
			}
			writer.write("]");
			writer.flush();
		}
	}
	
}
