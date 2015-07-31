package it.squallstar.go.api;


public class GoLink {
	public int id;
	public String title;
	public String url;
	public String date_upload;
	public String bookmark;
	public int views;
	public int label;
	
	private GoLabel label_data;
	
	public boolean hasLabel() {
		return label != 0;
	}
	
	public GoLabel getLabel() {
		return label_data;
	}
}
