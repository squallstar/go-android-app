package it.squallstar.go.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.google.gson.Gson;


public class GoUser {
	
	private static GoUser currentUserInstance;
	
	private static String PREFERENCES_KEY = "current_user";

    private int id;
    private String auth_token;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAuthToken(String token) {
    	this.auth_token = token;
    }
    
    public String getAuthToken() {
    	return auth_token;
    }
    
    public void SaveToPreferences(Context ctx)
    {
    	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
    	
    	Editor prefsEditor = prefs.edit();
    	Gson gson = new Gson();
    	String json = gson.toJson(this);
    	prefsEditor.putString(PREFERENCES_KEY, json);
        prefsEditor.commit();
    }
    
    public static GoUser CurrentUser(Context ctx)
    {
    	if (currentUserInstance == null)
    	{
    		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
    		
    		if (prefs.contains(PREFERENCES_KEY))
        	{
        		String json = prefs.getString(PREFERENCES_KEY, "");
        		Gson gson = new Gson();
        		currentUserInstance = gson.fromJson(json, GoUser.class);
        	}
    	}
    	
    	return currentUserInstance;
    }
    
    public static GoUser CurrentUser()
    {
    	return currentUserInstance;
    }
    
    public static void SetCurrentUser(GoResponseSignIn userData, Context ctx) {
    	userData.user.setAuthToken(userData.auth_token);
    	userData.user.SaveToPreferences(ctx);

		Toast.makeText(ctx, "Logged in", Toast.LENGTH_LONG).show();
    }

}