package dahgooogle.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.StringTokenizer;


public class GetPageRankIndex {

	/**
	 * Removes the char at the position 'pos' of string 's'
	 * @param s - considered string
	 * @param pos - considered position
	 * @return
	 */
	private static String removeCharAtPosition(String s,int pos)
	{
		if(s.length()==(pos-1))
			return s.substring(0,pos);
		else
			return s.substring(0,pos) + s.substring(pos+1);
	}
	
	/**
	 * Parses the result of Python rpc service
	 * @param service_result - service output
	 * @return
	 */
	private static LinkedList<WebPage> parseRPCResponse(String service_result)
	{
		//remove the index part
		StringTokenizer t = new StringTokenizer(service_result,"[");
		ArrayList<String> str = new ArrayList<String>();
		
		while(t.hasMoreTokens()==true)
			str.add(t.nextToken());
		
		service_result = str.get(1);
		
		//remove the last two characters - ]}
		
		service_result = removeCharAtPosition(service_result,service_result.length()-2);
		service_result = removeCharAtPosition(service_result, service_result.length()-2);
				
		//parse the information that represents each web page info (name + page_rank)
		
		t = new StringTokenizer(service_result,"{"); //separate web page info
		LinkedList<WebPage> parsed_output = new LinkedList<WebPage>();
		int index=1;
		int SIZE=10;
		while(t.hasMoreTokens()==true)
		{
			String token = t.nextToken();
			
			//remove the last two chars of the string ",}"
			token = removeCharAtPosition(token,token.length()-2);
			token = removeCharAtPosition(token, token.length()-2);
			
			//separate the string into name and page_rank substrings
			String separator = ",";
			String[] splited_token = token.split(separator);
			
			separator = "\"";
			
			//////get page name
			String[] splited_token_1 = splited_token[0].split(separator);
			
			//delete the " around name
			if(splited_token_1[1].indexOf("\"")!= -1)
				splited_token_1[3] = removeCharAtPosition(splited_token_1[3],splited_token_1[3].indexOf("\""));
			
			//delete space
			if(splited_token_1[3].indexOf(" ")!= -1)
				splited_token_1[3] = removeCharAtPosition(splited_token_1[3],splited_token_1[3].indexOf(" "));
			
			String page_name = splited_token_1[3];
			
			//////get page' PageRank
			separator=":";
			String[] splited_token_2 = splited_token[1].split(separator);
			
			//eleminate spaces
			
			if(splited_token_2.length==1)
			{
				index++;
				continue;
			}
			
			if(splited_token_2[1].indexOf(" ")!= -1)
				splited_token_2[1] = removeCharAtPosition(splited_token_2[1],splited_token_2[1].indexOf(" "));

			if(splited_token_2[1].indexOf(" ")!= -1)
				splited_token_2[1] = removeCharAtPosition(splited_token_2[1],splited_token_2[1].indexOf(" "));
			
			//on the last element we need to remove the '}' if it exists
			if(splited_token_2[1].indexOf("}")!=-1)
				splited_token_2[1] = removeCharAtPosition(splited_token_2[1],splited_token_2[1].indexOf("}"));
			
			float page_rank = Float.valueOf(splited_token_2[1]);
			
			//creates a WebPage object with the desired information and put it into the list
			parsed_output.add(new WebPage(page_name, page_rank));
			index++;
		}		
		
		return parsed_output;
	}
	
	/**
	 * Gets a list of webpages ordered by PageRank
	 * @return 
	 * @throws IOException
	 */
	public static LinkedList<WebPage> run() throws IOException
	{
		String app_link = "http://1.cn2201112.appspot.com/getindex.get";//"http://localhost:8080/getindex.get";
		String content_type = "application/json";
		 
		URL url = new URL(app_link);
		String data = "";
		URLConnection conn = url.openConnection();
		
		//HTTP POST
		conn.setDoOutput(true);
		conn.setRequestProperty("Content-type",content_type);
		
		OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
		wr.write(data);
		wr.flush();
		
		//Get response
		BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String line;
		StringBuilder bd = new StringBuilder();
		
		while((line=rd.readLine())!=null)
		{
			bd.append(line);
		}
		
		String service_result = bd.toString();
		
		//Parse the response and return a list of WebPages
		LinkedList<WebPage> output = parseRPCResponse(service_result);	
		
		return output;
	}

}
