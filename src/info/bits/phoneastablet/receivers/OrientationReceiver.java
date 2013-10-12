/*
 * Copyright (C) 2013  Tsapalos Vasilios
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
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
 * @author LiTTle
 * This receiver is called every time the orientation for the device changes.
 */
public class OrientationReceiver extends BroadcastReceiver {

    private int orientation = Configuration.ORIENTATION_UNDEFINED;
    private DatabaseHandler dbHandler;
    private int policy;
    private Context context;
    
    /**
     * Changes the device settings according to user's preferences.
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
