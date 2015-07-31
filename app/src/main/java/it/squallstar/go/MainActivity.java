package it.squallstar.go;

import it.squallstar.go.adapters.LinksAdapter;
import it.squallstar.go.api.API;
import it.squallstar.go.api.API.OnFetchListener;
import it.squallstar.go.api.GoLink;
import it.squallstar.go.api.GoLinks;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.Toast;

public class MainActivity extends Activity implements OnRefreshListener {

	SwipeRefreshLayout swipeLayout;
	LinksAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);

	    setContentView(R.layout.activity_links);

	    this.setTitle(" Links");

	    adapter = new LinksAdapter(getApplicationContext(), new GoLinks());

	    GridView itemsview = (GridView) findViewById(R.id.itemsview);
	    itemsview.setAdapter(adapter);

	    swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
	    swipeLayout.setOnRefreshListener(this);
	    swipeLayout.setColorSchemeColors(Color.parseColor("#EEBB09"), Color.parseColor("#F08B08"), Color.parseColor("#4BBFFF"), Color.parseColor("#FFFFFF"));

	    onRefresh();

	    registerForContextMenu(itemsview);

	    itemsview.setOnItemClickListener(new OnItemClickListener()
	    {
	        @Override
	        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	        {
	        	GoLink link = adapter.getItem(position);

	        	// Native intent
	        	Intent i = new Intent(Intent.ACTION_VIEW);
	        	i.setData(Uri.parse(link.url));
	        	startActivity(i);
	        }
	    });
	}


	@Override
	public void onRefresh() {
		API.Current().fetchLinks(adapter.getLinks(), new OnFetchListener() {

			@Override
			public void onComplete(boolean success, int newLinksCount) {
				swipeLayout.setRefreshing(false);

				if (success) {
					adapter.notifyDataSetChanged();

					if (adapter.getLinks().size() == 0 && adapter.getLinks().hasSearchQuery()) {
						Toast.makeText(getApplicationContext(), "No results", Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(getApplicationContext(), "Could not get the links", Toast.LENGTH_LONG).show();
				}
			}
		});
	}
}
