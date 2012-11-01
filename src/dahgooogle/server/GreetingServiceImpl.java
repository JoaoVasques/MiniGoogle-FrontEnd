package dahgooogle.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.jdo.Extent;
import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import dahgooogle.client.GreetingService;
import dahgooogle.shared.FieldVerifier;

@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements GreetingService 
{
	public static final int RESULTS_LIST_SIZE = 5;

	public static PersistenceManagerFactory PERSISTENCE_MANAGER = JDOHelper.getPersistenceManagerFactory("transactions-optional");

	//	private static final String BASE_URL = "http://np.hopto.org/~NP/index.php?";
	//	private static final String SEARCH_URL_TOKEN = "search=";

	// executar o run() e obter uma lista com os URL dos WebSiteInfo
	// percorrer a lista e procurar o termo que vem do search e comparar com as keywords
	// retorna uma lista com os WebSiteInfo que tem as keywords ou descricoes iguais

	public List<String> filterWebSiteInfos(String userQuery)
	{
		List<String> result = new ArrayList<String>();
		PersistenceManager manager = PERSISTENCE_MANAGER.getPersistenceManager();

		try 
		{
			List<WebPage> initialList = GetPageRankIndex.run();

			for(WebPage webpage : initialList)
			{
				if(result.size() == RESULTS_LIST_SIZE) break;

				if(webpage.getName().contains(userQuery))
				{
					result.add(webpage.getPage_rank() + " | " + webpage.getName() + "<br><br>");
				}
				else
				{
					Query query = manager.newQuery(WebSiteInfo.class);
					query.setFilter("url == webpageName");
					query.declareParameters("String webpageName");

					@SuppressWarnings("unchecked")
					Collection<WebSiteInfo> results = (Collection<WebSiteInfo>) query.execute(webpage.getName());
					
					if(results.size() == 0) continue;
					
					for(WebSiteInfo websiteInfo : results)
					{
						if(websiteInfo.getDescription().contains(userQuery) || websiteInfo.getKeyWords().contains(userQuery))
						{
							StringBuilder resultToAdd = new StringBuilder();

							resultToAdd.append(webpage.getPage_rank() + " | ");
							resultToAdd.append(webpage.getName() + "<br>");
							resultToAdd.append(websiteInfo.getDescription() + "<br>");
							resultToAdd.append(websiteInfo.getKeyWords() + "<br><br>");
							result.add(resultToAdd.toString());
						}
						
						break;
					}
				}
			}

			return result;
		}
		catch(ArrayIndexOutOfBoundsException bounds)
		{
			bounds.printStackTrace();
			return null;
		}
		catch(IOException e) 
		{
			e.printStackTrace();
			return null;
		}
	}

	public String greetServer(String input) throws IllegalArgumentException {
		// Verify that the input is valid. 
		if (!FieldVerifier.isValidName(input)) {
			// If the input is not valid, throw an IllegalArgumentException back to
			// the client.
			throw new IllegalArgumentException(
					"Name must be at least 4 characters long");
		}

		String serverInfo = getServletContext().getServerInfo();
		String userAgent = getThreadLocalRequest().getHeader("User-Agent");

		// Escape data from the client to avoid cross-site script vulnerabilities.
		input = escapeHtml(input);
		userAgent = escapeHtml(userAgent);

		return "Hello, " + input + "!<br><br>I am running " + serverInfo
				+ ".<br><br>It looks like you are using:<br>" + userAgent;
	}

	/**
	 * Escape an html string. Escaping data received from the client helps to
	 * prevent cross-site script vulnerabilities.
	 * 
	 * @param html the html string to escape
	 * @return the escaped string
	 */
	private String escapeHtml(String html) {
		if (html == null) {
			return null;
		}
		return html.replaceAll("&", "&amp;").replaceAll("<", "&lt;")
				.replaceAll(">", "&gt;");
	}

	//	private String fetchRemoteUrlContent(String query)
	//	{
	//		StringBuilder builder = new StringBuilder();
	//		
	//		try
	//		{
	//			
	//			//"".split("\\|");
	//			URL url = new URL(BASE_URL + SEARCH_URL_TOKEN + query);
	//			//System.out.println("Get URL: " + url);
	//			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	//            connection.setDoOutput(false);
	//            connection.setConnectTimeout(60000);
	//            connection.setRequestMethod("GET");
	//            String line;
	//
	//            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	//            
	//            while((line = reader.readLine()) != null) 
	//            {
	//                //System.out.println(line);
	//                builder.append(line);
	//            }
	//            
	//            reader.close();
	//		}
	//		catch(Exception e) 
	//		{
	//			e.printStackTrace();
	//		} 
	//		
	//		return builder.toString();
	//	}

	@Override
	public String saveUserQuery(String userID, String query) 
	{
		List<String> resultsFromFilter = this.filterWebSiteInfos(query);
		String queryResults = null;

		if(resultsFromFilter == null || resultsFromFilter.isEmpty())
		{
			queryResults = "Term NOT FOUND";
		}
		else
		{
			StringBuilder queryResultsBuilder = new StringBuilder();
			for(String result : resultsFromFilter)
			{
				queryResultsBuilder.append(result);
			}

			queryResults = queryResultsBuilder.toString();
		}

		try
		{
			/*
			 * ver se o user existe
			 */

			PersistenceManager manager = PERSISTENCE_MANAGER.getPersistenceManager();

			boolean foundUser = false;
			Extent<UserSearches> extent = manager.getExtent(UserSearches.class);
			for(UserSearches userData : extent)
			{
				if(userData.getUserID().equals(userID))
				{
					foundUser = true;
					break;
				}
			}

			extent.closeAll();
			manager.close();

			/*
			 * se existir, adiciona, se nao cria novo
			 */

			manager = PERSISTENCE_MANAGER.getPersistenceManager();

			if(foundUser)
			{
				Query query1 = manager.newQuery(UserSearches.class);
				query1.setFilter("userID == userParam");
				query1.declareParameters("String userParam");

				@SuppressWarnings("unchecked")
				List<UserSearches> results = (List<UserSearches>) query1.execute(userID);

				results.get(0).addQuery(query);
				manager.close();
			}
			else
			{
				UserSearches newUserSearches = new UserSearches(userID);
				newUserSearches.addQuery(query);

				manager.makePersistent(newUserSearches);

				manager.close();
			}

			return queryResults;
		}
		catch(Exception e)
		{
			//e.printStackTrace();
			return null;
		}
	}

	@Override
	public String getUserSearches(String userID) 
	{
		StringBuilder builder = new StringBuilder();

		PersistenceManager manager = PERSISTENCE_MANAGER.getPersistenceManager();

		Extent<UserSearches> extent = manager.getExtent(UserSearches.class);
		for(UserSearches user : extent)
		{
			System.out.println("user: " + user);
			builder.append(user.getKey() + "|" + user.getUserID() + "|");
			for(String query : user.getQueries())
			{
				System.out.println("query: " + query);
				builder.append(query + "|");
			}

			builder.append("<br>");
		}

		return builder.toString();
	}
}
