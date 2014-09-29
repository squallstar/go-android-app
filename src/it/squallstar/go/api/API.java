package it.squallstar.go.api;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
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
	
    public interface ApiInterface {
        
        @GET("/v2/login/")
    	void signIn(
    			@Query("uid") String user_id,
    			@Query("password") String password,
    			Callback<GoResponseSignIn> callback
    	);
        
        @GET("/v2/save/")
    	void saveLink(
    			@Query("url") String url,
    			Callback<GoResponseSaveLink> callback
    	);
    }	
}