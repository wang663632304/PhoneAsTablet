/**
 * 
 */
package info.bits.phoneastablet.utils;

import java.io.IOException;

import info.bits.phoneastablet.Resolution;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

/**
 * @author little
 * 
 */
public class NotificationButtonsHandler extends Activity {

    protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	String action = (String) getIntent().getExtras().get("DO");
	if (action.equals("custom") || action.equals("default")) {
	    try {
		applyResolution(action);
	    } catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	} else if (action.equals("app")) {
	    Intent app = new Intent(this, Resolution.class);
	    startActivity(app);
	}
	finish();
    }

    @Override
    protected void onDestroy() {
	// TODO Auto-generated method stub
	super.onDestroy();
    }
    
    private void applyResolution(String ResolutionApplied) throws IOException{
	int orientation = getResources().getConfiguration().orientation;
	DatabaseHandler dbHandler = new DatabaseHandler(this);
	if(ResolutionApplied.equals("custom")){
	    if(orientation == Configuration.ORIENTATION_PORTRAIT){
		String width = dbHandler.getDimension(DatabaseHandler.KEY_PORTRAIT_WIDTH, 
			DatabaseHandler.TABLE_LATEST_RESOLUTIONS);
		String height = dbHandler.getDimension(DatabaseHandler.KEY_PORTRAIT_HEIGHT, 
			DatabaseHandler.TABLE_LATEST_RESOLUTIONS);
		SuCommands.changeResolution(width, height);
	    } else if (orientation == Configuration.ORIENTATION_LANDSCAPE){
		String width = dbHandler.getDimension(DatabaseHandler.KEY_LANDSCAPE_WIDTH, 
			DatabaseHandler.TABLE_LATEST_RESOLUTIONS);
		String height = dbHandler.getDimension(DatabaseHandler.KEY_LANDSCAPE_HEIGHT, 
			DatabaseHandler.TABLE_LATEST_RESOLUTIONS);
		SuCommands.changeResolution(width, height);
	    }
	} else{
	    SuCommands.fallbackToDefaultResolution();
	}
    }
}
