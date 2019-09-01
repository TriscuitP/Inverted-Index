import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class InvertedIndexBuilder 
{

	/**
	 * Creates and returns a new word index built from the file located at the
	 * path provided.
	 *
	 * @param path
	 *            path to file to parse
	 * @return inverted index containing words from the path
	 * @throws IOException
	 *
	 * @see {@link #buildIndex(Path, WordIndex)}
	 */
	public static InvertedIndex buildIndex(Path path) throws IOException 
	{
		InvertedIndex index = new InvertedIndex();
		buildIndex(path, index);
		return index;
	}
	
	/**
	 * Opens the file located at the path provided, parses each line in the file
	 * into words, and stores those words in an inverted index.
	 *
	 * @param path
	 *            path to file to parse
	 * @param index
	 *            word index to add words
	 * @throws IOException
	 *
	 * @see WordParser#parseWords(String, int)
	 *
	 * @see Files#newBufferedReader(Path, java.nio.charset.Charset)
	 * @see StandardCharsets#UTF_8
	 */
	public static void buildIndex(Path path, InvertedIndex indexMap) throws IOException 
	{
		List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
		String result = String.join(" ", lines);
		
		// Strip html/htm to get all words
		String cleanRegex = HTMLCleaner.stripHTML(result);
		
		// Parse text (not HTML) into words, including converting that text to lowercase, replacing
		// special characters and digits with a space, and finally splitting that text by spaces
		String[] allWords = WordParser.parseWords(cleanRegex);
		
		// Add those words to the index
		indexMap.addAll(allWords, path.toString());
	}
}
