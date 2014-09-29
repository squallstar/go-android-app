package it.squallstar.go;

import it.squallstar.go.api.API;
import it.squallstar.go.api.GoResponseSignIn;
import it.squallstar.go.api.GoUser;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;


public class LoginActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        setContentView(R.layout.activity_links);
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	
    	if (GoUser.CurrentUser(getApplicationContext()) != null) {
    		loadMainActivity();
    	} else {
    		API.getApiClient().signIn("1629485853", "michela2", new Callback<GoResponseSignIn>() {

				@Override
				public void failure(RetrofitError arg0) {
					new AlertDialog.Builder(LoginActivity.this)
 	    		    .setTitle("Oops...")
 	    		    .setMessage("The provided uid or password didn't match. Please check your login details again.")
 	    		    .setPositiveButton(android.R.string.ok, null)
 	    		    .setIcon(android.R.drawable.ic_dialog_alert)    		    
 	    		    .show();
				}

				@Override
				public void success(GoResponseSignIn response, Response arg1) {
					GoUser.SetCurrentUser(response, getApplicationContext());
					loadMainActivity();
				}
    			
    		});
    	}
    }
    
    private void loadMainActivity()
    {
    	Intent mainActivity = new Intent(this, MainActivity.class);
		mainActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); 
		startActivity(mainActivity);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.links, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
