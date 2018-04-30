import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Map.Entry;
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
		Object[] e = elements.toArray();
		
		for(int i = 0; i < e.length; i++)
			{
			writer.write(indent(level + 1) + e[i]);
			
			if(i < e.length - 1)
				writer.write(",");
			
			writer.write("\n");
			}

		writer.write(indent(level) + "]");
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
	
	}
