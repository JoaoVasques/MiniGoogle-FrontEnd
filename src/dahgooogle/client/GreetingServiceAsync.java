package dahgooogle.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface GreetingServiceAsync {
	void greetServer(String input, AsyncCallback<String> callback)
			throws IllegalArgumentException;
	
	void saveUserQuery(String userID, String query, AsyncCallback<String> callback);

	void getUserSearches(String userID, AsyncCallback<String> callback);
}
