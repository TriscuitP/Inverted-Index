import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.TreeSet;

public class Driver 
	{
	
	private static void traverseDirectory(Path path, InvertedIndex indexMap) throws IOException
		{		
		if(Files.isDirectory(path))
			{
			try(DirectoryStream<Path> listing = Files.newDirectoryStream(path))
				{
				for (Path extension : listing)
					{	
					traverseDirectory(extension, indexMap);
					}
				}
			}
		else if(path.toString().toLowerCase().endsWith("html") || path.toString().endsWith("htm"))
			{
//			System.out.println("Found html file: " + path.toString());
			InvertedIndexBuilder.buildIndex(path, indexMap);
			}
		}

	public static void main(String[] args) throws IOException
		{
		ArgumentMap commdLine = new ArgumentMap(args);
		InvertedIndex indexMap = new InvertedIndex();	
		
		// Getting JSON file if commmand line contains '-index'
		String jsonFile = commdLine.getString("-index", "index.json");
		
		// Building inverted index from files in subdirectory of the current working directory
		if(commdLine.hasFlag("-path") && commdLine.hasValue("-path"))
			traverseDirectory(Paths.get(commdLine.getString("-path")), indexMap);
		
		// Write that index as JSON
		if(commdLine.hasFlag("-index"))
			JSONWriter.asInvertedIndexObject(indexMap.getIndex(), Paths.get(jsonFile));
		
		}

	}
