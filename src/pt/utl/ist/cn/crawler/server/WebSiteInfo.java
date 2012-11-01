package pt.utl.ist.cn.crawler.server;

import java.math.BigDecimal;
import java.util.ArrayList;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable
public class WebSiteInfo {

	@PrimaryKey
	private String url;
	
	@Persistent
	private BigDecimal pageRank;

	@Persistent
	private String title;

	@Persistent
	private String description;

	@Persistent
	private ArrayList<String> keyWords;

	@Persistent
	private ArrayList<String> links;
	
	@Persistent
	private String blobstorePath;

	
	public WebSiteInfo() {
		super();
		this.url = new String();
		this.pageRank = new BigDecimal(1.0);
		this.title = new String();
		this.description = new String();
		this.keyWords = new ArrayList<String>();
		this.links = new ArrayList<String>();
		this.blobstorePath = new String();
	}
	
	public WebSiteInfo(String url, BigDecimal pageRank, String title,
			String description, ArrayList<String> keyWords,
			ArrayList<String> links, String blobstorePath) {
		super();
		this.url = url;
		this.pageRank = pageRank;
		this.title = title;
		this.description = description;
		this.keyWords = keyWords;
		this.links = links;
		this.blobstorePath = blobstorePath;
	}
	

	public String getUrl() {
		return url;
	}

	public BigDecimal getPageRank() {
		return pageRank;
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public ArrayList<String> getKeyWords() {
		return keyWords;
	}

	public ArrayList<String> getLinks() {
		return links;
	}

	public String getBlobstorePath() {
		return blobstorePath;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setPageRank(BigDecimal pageRank) {
		this.pageRank = pageRank;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setKeyWords(ArrayList<String> keyWords) {
		this.keyWords = keyWords;
	}

	public void setLinks(ArrayList<String> links) {
		this.links = links;
	}

	public void setBlobstorePath(String blobstorePath) {
		this.blobstorePath = blobstorePath;
	}

	
}
