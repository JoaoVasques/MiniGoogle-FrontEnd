package dahgooogle.client.remotereq;

public interface ResourceReceptionCallback 
{
	/**
	 * To be called when the request returned successfully
	 * 
	 * @param statusCode the HTTP status code of the request
	 * @param statusDescription the returned description of the HTTP request
	 * @param resource the requested resource as a string
	 */
	public void onSuccess(int statusCode, String statusDescription, String resource);
	
	/**
	 * To be called when the request's status code doesn't represent success
	 * 
	 * @param statusCode the HTTP status code of the request
	 * @param statusDescription the returned description of the HTTP request
	 */
	public void onFailure(int statusCode, String statusDescription);
	
	/**
	 * To be called when an unexpected exception happens
	 * 
	 * @param error the exceptions representing the error
	 */
	public void onError(Throwable error);
}