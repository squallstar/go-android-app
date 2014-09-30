package it.squallstar.go.api;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Query;
import android.util.Log;

public class API {
	
	private static ApiInterface sService;
	private static API instance;

	public static API Current() {
		if (instance == null) instance = new API();
		return instance;
	}
	
    public static ApiInterface getApiClient() {
        if (sService == null) {
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint("http://api.squallstar.it/go")
                    .setRequestInterceptor(new RequestInterceptor() {
						
						@Override
						public void intercept(RequestFacade request) {
							if (GoUser.CurrentUser() != null) {
								request.addQueryParam("auth_token", GoUser.CurrentUser().getAuthToken());
							}
						}
					})
                    .build();
            
            Log.i("API", "REST Service created");

            sService = restAdapter.create(ApiInterface.class);
        }

        return sService;
    }
    
    public interface OnFetchListener {
        void onComplete(boolean success, int newLinksCount);
    }
    
    public void fetchLinks(final GoLinks links, final OnFetchListener cb) {
    	if (links.size() > 0) {
    		if (links.hasSearchQuery()) {
    			getApiClient().getLinksForQuery(links.getSearchQuery(), links.getLastId(), new Callback<GoResponseLinks>() {

					@Override
					public void failure(RetrofitError error) {
						cb.onComplete(false, 0);
					}

					@Override
					public void success(GoResponseLinks response, Response arg1) {
						links.addAll(response.data);
						cb.onComplete(true, response.data.size());
					}
				});
    		}
    		else {
				getApiClient().getLinks(links.getLastId(), new Callback<GoResponseLinks>() {

					@Override
					public void failure(RetrofitError error) {
						cb.onComplete(false, 0);
					}

					@Override
					public void success(GoResponseLinks response, Response arg1) {
						links.addAll(response.data);
						cb.onComplete(true, response.data.size());
					}
				});
			}
    	} else {
    		if (links.hasSearchQuery()) {
    			getApiClient().getLinksForQuery(links.getSearchQuery(), new Callback<GoResponseLinks>() {

					@Override
					public void failure(RetrofitError error) {
						cb.onComplete(false, 0);
					}

					@Override
					public void success(GoResponseLinks response, Response arg1) {
						links.clear();
						links.addAll(response.data);
						cb.onComplete(true, response.data.size());
					}
				});
    		}
    		else {
				getApiClient().getLinks(new Callback<GoResponseLinks>() {

					@Override
					public void failure(RetrofitError error) {
						cb.onComplete(false, 0);
					}

					@Override
					public void success(GoResponseLinks response, Response arg1) {
						links.clear();
						links.addAll(response.data);
						cb.onComplete(true, response.data.size());
					}
				});
			}
    	}
    }
	
    public interface ApiInterface {
        
        @GET("/v2/login/")
    	void signIn(
    			@Query("uid") String user_id,
    			@Query("password") String password,
    			Callback<GoResponseSignIn> callback
    	);
        
        @GET("/v2/links/")
    	void getLinks(
    			Callback<GoResponseLinks> callback
    	);
        
        @GET("/v2/links/")
    	void getLinks(
    			@Query("maxId") int max_id,
    			Callback<GoResponseLinks> callback
    	);
        
        @GET("/v2/links/")
    	void getLinksForQuery(
    			@Query("search") String search,
    			Callback<GoResponseLinks> callback
    	);
        
        @GET("/v2/links/")
    	void getLinksForQuery(
    			@Query("search") String search,
    			@Query("maxId") int max_id,
    			Callback<GoResponseLinks> callback
    	);
        
        @GET("/v2/save/")
    	void saveLink(
    			@Query("url") String url,
    			Callback<GoResponseSaveLink> callback
    	);
    }	
}