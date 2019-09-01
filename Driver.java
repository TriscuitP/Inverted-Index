import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Driver 
{
	
	private static void traverseDirectory(Path path, InvertedIndex indexMap) throws IOException
	{		
		if(Files.isDirectory(path))
		{
			try(DirectoryStream<Path> listing = Files.newDirectoryStream(path))
			{
				for(Path extension : listing)	
					traverseDirectory(extension, indexMap);
			}
		}
		else if(path.toString().toLowerCase().endsWith("html") || path.toString().endsWith("htm"))
		{
			InvertedIndexBuilder.buildIndex(path, indexMap);
		}
	}

	public static void main(String[] args) throws IOException
	{
		ArgumentMap commdLine = new ArgumentMap(args);
		InvertedIndex indexMap = new InvertedIndex();	
		
		// Building inverted index from files in subdirectory of the current working directory
		if(commdLine.hasFlag("-path") && commdLine.hasValue("-path"))
			traverseDirectory(Paths.get(commdLine.getString("-path")), indexMap);
		
		// Write that index as JSON
		if(commdLine.hasFlag("-index"))
		{
			// Getting JSON file if command line contains '-index'
			String jsonFile = commdLine.getString("-index", "index.json");
			JSONWriter.asInvertedIndexObject(indexMap.getIndex(), Paths.get(jsonFile));
		}
		
		// Do query search, partial or exact
		QuerySearch query = new QuerySearch(indexMap);
		if(commdLine.hasFlag("-query") && commdLine.hasValue("-query"))
		{
			try
			{
				query.parseQuery(Paths.get(commdLine.getValue("-query")), commdLine.hasFlag("-exact"));
			}
			catch(IOException e)
			{
				System.out.println("Unable to read query file");
			}
		}
		
		// Search results
		if(commdLine.hasFlag("-results"))
		{
			String searchResult = commdLine.getString("-results", "results.json");
			query.toJSON(Paths.get(searchResult));			
		}
	}
}
