/**
 * 
 */
package info.bits.phoneastablet.receivers;

import info.bits.phoneastablet.utils.DatabaseHandler;
import info.bits.phoneastablet.utils.SuCommands;

import java.io.IOException;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;

/**
 * @author little
 *
 */
public class OrientationReceiver extends BroadcastReceiver {

    private int orientation = Configuration.ORIENTATION_UNDEFINED;
    private DatabaseHandler dbHandler;
    private int policy;
    private Context context;
    /* (non-Javadoc)
     * @see android.content.BroadcastReceiver#onReceive(android.content.Context, android.content.Intent)
     */
    @Override
    public void onReceive(Context ctx, Intent intent) {
	// TODO Auto-generated method stub
	context = ctx;
	orientation = ctx.getResources().getConfiguration().orientation;
	new Thread(){
	    public void run(){
		try{
		    dbHandler = new DatabaseHandler(context);
		    policy = dbHandler.getResolutionPolicy();
		    if (policy == DatabaseHandler.SERVICE_POLICY){
			if(orientation == Configuration.ORIENTATION_PORTRAIT){
			    String width = dbHandler.getDimension(DatabaseHandler.KEY_PORTRAIT_WIDTH, DatabaseHandler.TABLE_LATEST_RESOLUTIONS);
			    String height = dbHandler.getDimension(DatabaseHandler.KEY_PORTRAIT_HEIGHT, DatabaseHandler.TABLE_LATEST_RESOLUTIONS);
			    SuCommands.changeResolution(width, height);
			}
			else if (orientation == Configuration.ORIENTATION_LANDSCAPE){
			    String width = dbHandler.getDimension(DatabaseHandler.KEY_LANDSCAPE_WIDTH, DatabaseHandler.TABLE_LATEST_RESOLUTIONS);
			    String height = dbHandler.getDimension(DatabaseHandler.KEY_LANDSCAPE_HEIGHT, DatabaseHandler.TABLE_LATEST_RESOLUTIONS);
			    SuCommands.changeResolution(width, height);
			}
			else{}
		    }
		} catch (IOException ioe){}
	    }
	}.start();
    }
    
    
}
