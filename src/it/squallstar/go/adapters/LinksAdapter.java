package it.squallstar.go.adapters;

import it.squallstar.go.R;
import it.squallstar.go.api.API;
import it.squallstar.go.api.API.OnFetchListener;
import it.squallstar.go.api.GoLink;
import it.squallstar.go.api.GoLinks;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LinksAdapter extends BaseAdapter {
	
	private Context mContext;
	private GoLinks links;
	
	private boolean isFetching = false;
	
	public LinksAdapter(Context c, GoLinks links) {
        this.mContext = c;
        this.links = links;
    }
	
	public GoLinks getLinks() {
		return this.links;
	}
	
	@Override
	public int getCount() {
		return this.links.size();
	}

	@Override
	public GoLink getItem(int position) {
		return this.links.get(position);
	}

	@Override
	public long getItemId(int position) {
		return this.links.get(position).id;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		GoLink link = getItem(position);
		
		if (!isFetching && (position > links.size() - 13)) {
    		
    		isFetching = true;
    		
    		API.Current().fetchLinks(links, new OnFetchListener() {
    			
    			@Override
    			public void onComplete(boolean success, int newLinksCount) {

					isFetching = false;
					
    				if (success && newLinksCount > 0) {
    					notifyDataSetChanged();
    				}
    			}
    		});
    	}
		
		View item;
		
		if (convertView == null) {
        	LayoutInflater inflater = LayoutInflater.from(mContext);
        	
        	item = inflater.inflate(R.layout.link, parent, false);
        } else {
        	item = (View) convertView;
        }
		
		TextView title = (TextView) item.findViewById(R.id.title);
        title.setText(link.title);
        
        TextView description = (TextView) item.findViewById(R.id.description);
        description.setText(link.url);
        
        LinearLayout label = (LinearLayout) item.findViewById(R.id.label);
        
        if (link.hasLabel()) {
        	TextView labelName = (TextView) item.findViewById(R.id.labelName);
        	labelName.setText(link.getLabel().name);
        	
        	label.setBackgroundColor(Color.parseColor(link.getLabel().color));
        	label.setVisibility(View.VISIBLE);
        	
        	LinearLayout content = (LinearLayout)item.findViewById(R.id.content);

        	GradientDrawable shapeDrawable = (GradientDrawable)content.getBackground();
        	shapeDrawable.setStroke(2, Color.parseColor(link.getLabel().color));
      

        } else {
        	label.setVisibility(View.GONE);
        }
		
		return item;
	}

}
