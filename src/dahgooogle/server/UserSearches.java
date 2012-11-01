package dahgooogle.server;

import java.util.ArrayList;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable
public class UserSearches 
{
	/*
	 * user searches
	 */
	
	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;
	
	@Persistent
	private String userID;
	
	@Persistent
	private ArrayList<String> queries;
	
	public UserSearches(String userID)
	{
		this.userID = userID;
		this.queries = new ArrayList<String>();
	}
	
	public String getKey()
	{
		return key.toString();
	}

	public String getUserID() 
	{
		return userID;
	}

	public void setUserID(String userID) 
	{
		this.userID = userID;
	}

	public ArrayList<String> getQueries() 
	{
		return queries;
	}

	public void addQuery(String queries) 
	{
		this.queries.add(queries);
	}
	
	
}
