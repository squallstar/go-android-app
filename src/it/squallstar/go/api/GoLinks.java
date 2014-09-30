package it.squallstar.go.api;

import java.util.ArrayList;

public class GoLinks extends ArrayList<GoLink> {

	private static final long serialVersionUID = 6903924228062691051L;
	
	private String searchQuery;
	
	public void setSearchQuery(String query){
		if (query != null && query.length() > 0) {
			this.searchQuery = query;
		} else this.searchQuery = null;
	}
	
	public boolean hasSearchQuery() {
		return this.searchQuery != null;
	}
	
	public String getSearchQuery() {
		return this.searchQuery;
	}
	
	public int getLastId() {
		return get(size()-1).id;
	}
}