package dahgooogle.server;


public class WebPage {

	private String name;
	private float page_rank;
	
	public WebPage(String name, float page_rank)
	{
		this.name = name;
		this.page_rank = page_rank;
	}


	public String getName() {
		return name;
	}

	public float getPage_rank() {
		return page_rank;
	}
	
	@Override
	public String toString()
	{
		return "Name: "+ this.getName() + "\nPageRank: " + this.getPage_rank();
	}
}
