
public class SearchResult implements Comparable<SearchResult>
{
	private int frequency;
	private int position;
	private String location;
	
	public SearchResult(int freq, int pos, String loc)
	{
		this.frequency = freq;
		this.position = pos;
		this.location = loc;
	}
	
	public int getFreq()
	{
		return frequency;
	}
	
	public int getPos()
	{
		return position;
	}
	
	public String getLoc()
	{
		return location;
	}
	
	/**
	 * Updates the position and frequency
	 * @param updatedFrequency
	 * @param updatedPosition
	 */
	public void update(int count, int pos)
	{
		frequency += count;
		if(pos < position)
			position = pos;
	}

	@Override
	/**
	 * Sorts accordingly by path, position, and frequency
	 * Ranking: frequency, position, path
	 */
	public int compareTo(SearchResult o) 
	{
		if(this.frequency != o.frequency)
			return Integer.compare(o.frequency, this.frequency);
		else
		{
			if(this.position != o.position)
				return Integer.compare(this.position, o.position);
			else
				return this.location.compareTo(o.location);
		}		
	}
}
