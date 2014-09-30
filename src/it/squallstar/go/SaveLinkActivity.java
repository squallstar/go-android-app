package it.squallstar.go;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import it.squallstar.go.api.API;
import it.squallstar.go.api.GoResponseSaveLink;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class SaveLinkActivity extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    Intent receivedIntent = getIntent();
        String receivedAction = receivedIntent.getAction();
        String receivedType = receivedIntent.getType();
        
        //ba262aaedf86df220c3874aa3954b32f
        
        //make sure it's an action and type we can handle
        if(receivedAction.equals(Intent.ACTION_SEND)){
            //content is being shared 
        	if(receivedType.startsWith("text/")){
        	    //handle sent text
        		//get the received text
        		String receivedText = receivedIntent.getStringExtra(Intent.EXTRA_TEXT);
        		
        		API.getApiClient().saveLink(receivedText, new Callback<GoResponseSaveLink>() {
					
					@Override
					public void success(GoResponseSaveLink response, Response arg1) {
						if (response.code == 200)
						{
							Toast.makeText(getApplicationContext(), "Link saved", Toast.LENGTH_LONG).show();
							finish();
						}
					}
					
					@Override
					public void failure(RetrofitError arg0) {
						new AlertDialog.Builder(SaveLinkActivity.this)
	 	    		    .setTitle("Error!")
	 	    		    .setMessage("The link has NOT been saved.")
	 	    		    .setPositiveButton(android.R.string.ok, null)
	 	    		    .setIcon(android.R.drawable.ic_dialog_alert)    		    
	 	    		    .show();
						
						finish();
					}
				});		
        	}
        }
        else if(receivedAction.equals(Intent.ACTION_MAIN)){
            //app has been launched directly, not from share list
        }
	}

}
