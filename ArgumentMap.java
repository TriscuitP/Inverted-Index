import java.util.HashMap;
import java.util.Map;

/**
 * Parses command-line arguments into flag/value pairs, and stores those pairs
 * in a map for easy access.
 */
public class ArgumentMap 
{

	private final Map<String, String> map;

	/**
	 * Initializes the argument map.
	 */
	public ArgumentMap() 
	{
		map = new HashMap<>();
	}

	/**
	 * Initializes the argument map and parses the specified arguments into
	 * key/value pairs.
	 *
	 * @param args 
	 *            command line arguments
	 *
	 * @see #parse(String[])
	 */
	public ArgumentMap(String[] args) 
	{
		this();
		parse(args);
	}

	/**
	 * Parses the specified arguments into key/value pairs and adds them to the
	 * argument map.
	 *
	 * @param args 
	 *            command line arguments
	 */
	public void parse(String[] args) 
	{
		for(int i = 0; i < args.length; i++)
		{
			if(isFlag(args[i]))
			{
				if(i != args.length - 1 && isValue(args[i + 1]))
					map.put(args[i], args[i + 1]);
				else
					map.put(args[i], null);
			}
		}
	}

	/**
	 * @param arg
	 * @return
	 */
	public static boolean isFlag(String arg) 
	{
		if(arg == null)
			return false;
		
		arg = arg.trim();
		
		if(arg.startsWith("-") && arg.length() > 1)
			return true;
		
		return false;
	}

	/**
	 * @param arg
	 * @return
	 */
	public static boolean isValue(String arg) 
	{
		if(arg == null)
			return false;
		
		arg = arg.trim();
		
		if(!arg.startsWith("-") && !arg.isEmpty())
			return true;
		
		return false;
	}

	/**
	 * Returns the number of unique flags stored in the argument map.
	 *
	 * @return number of flags
	 */
	public int numFlags() 
	{
		return this.map.size();
	}

	/**
	 * Determines whether the specified flag is stored in the argument map.
	 *
	 * @param flag
	 *            flag to test
	 *
	 * @return true if the flag is in the argument map
	 */
	public boolean hasFlag(String flag) 
	{
		return this.map.containsKey(flag);
	}

	/**
	 * Determines whether the specified flag is stored in the argument map and
	 * has a non-null value stored with it.
	 *
	 * @param flag
	 *            flag to test
	 *
	 * @return true if the flag is in the argument map and has a non-null value
	 */
	public boolean hasValue(String flag) 
	{
		if(this.map.get(flag) != null)
			return true;
		
		return false;
	}

	/**
	 * Returns the value for the specified flag as a String object.
	 *
	 * @param flag
	 *            flag to get value for
	 *
	 * @return value as a String or null if flag or value was not found
	 */
	public String getString(String flag) 
	{	
		return this.map.get(flag);
	}

	/**
	 * Returns the value for the specified flag as a String object. If the flag
	 * is missing or the flag does not have a value, returns the specified
	 * default value instead.
	 *
	 * @param flag
	 *            flag to get value for
	 * @param defaultValue
	 *            value to return if flag or value is missing
	 * @return value of flag as a String, or the default value if the flag or
	 *         value is missing
	 */
	public String getString(String flag, String defaultValue) 
	{
		if(!this.map.containsKey(flag) || this.map.get(flag) == null)
			return defaultValue;
		
		return this.map.get(flag);
	}
	
	/**
	 * Returns the flag value if it contains a value
	 * @param flag
	 * @return
	 */
	public String getValue(String flag)
	{
		if(!this.map.containsKey(flag) && this.map.get(flag) == null && this.hasValue(flag) != true)	
			return null;
		
		return this.map.get(flag);
	}

	/**
	 * Returns the value for the specified flag as an int value. If the flag is
	 * missing or the flag does not have a value, returns the specified default
	 * value instead.
	 *
	 * @param flag
	 *            flag to get value for
	 * @param defaultValue
	 *            value to return if the flag or value is missing
	 * @return value of flag as an int, or the default value if the flag or
	 *         value is missing
	 */
	public int getInteger(String flag, int defaultValue) 
	{		
		try
		{
			return Integer.valueOf(this.map.get(flag));
		}
		catch(NumberFormatException e)
		{
			return defaultValue;
		}		
	}

	@Override
	public String toString() 
	{
		return map.toString();
	}
}
