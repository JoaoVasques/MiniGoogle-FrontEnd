package dahgooogle.client.remotereq;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;

public class RemoteResource
{
	private String resourcePath;
	
	public RemoteResource(String resourcePath)
	{
		if(resourcePath == null) throw new IllegalArgumentException("The resource path must be defined (null pointer)");
		
		this.resourcePath = resourcePath;
	}
	
	public RequestHandler get(ResourceReceptionCallback callback)
	{
		return this.get(null, callback);
	}
	
	public RequestHandler get(String requestData, final ResourceReceptionCallback callback)
	{
		if(callback == null) throw new IllegalArgumentException("The callback must be defined so that the resource can be received correctly (null pointer)");
		
		Request request = null;
		try
		{
			request = new RequestBuilder(RequestBuilder.GET, URL.encode(this.resourcePath)).sendRequest(requestData, new RequestCallback()
			{	
				@Override
				public void onResponseReceived(Request request, Response response) 
				{
					if(response.getStatusCode() / 100 != 2)
					{
						callback.onFailure(response.getStatusCode(), response.getStatusText());
					}
					else
					{
						callback.onSuccess(response.getStatusCode(), response.getStatusText(), response.getText());
					}
				}
				
				@Override
				public void onError(Request request, Throwable exception)
				{
					callback.onError(exception);
				}
			});
		} 
		catch(RequestException e)
		{
			// This exception would be thrown if the RequestCallback is null. The callback will never be null because it's defined as a
			// new object in the method call up there
		}
		
		return new RequestHandler(request);
	}
}
