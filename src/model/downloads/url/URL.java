package model.downloads.url;

public class URL
{
	private final String name;
	private final String url;
	
	public URL(String url, String name)
	{
		this.url = url;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public String getUrl() {
		return url;
	}
	
	@Override
	public String toString()
	{
		String uri;
		if(getName() == null || getName().equals(""))
			uri = getUrl();
		else
			uri = getName();
		
		return uri;
	}
}
