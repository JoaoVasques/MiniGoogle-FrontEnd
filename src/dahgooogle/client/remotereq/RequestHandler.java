package dahgooogle.client.remotereq;

import com.google.gwt.http.client.Request;

public class RequestHandler
{
	private Request request;
	
	protected RequestHandler(Request request)
	{
		this.request = request;
	}
	
	public void cancelRequest()
	{
		this.request.cancel();
	}
	
	public boolean isPending()
	{
		return this.request.isPending();
	}
}
