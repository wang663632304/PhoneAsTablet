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

package info.bits.phoneastablet;

import java.io.IOException;

import info.bits.phoneastablet.listeners.MyOnCheckedListener;
import info.bits.phoneastablet.listeners.MyTextWatcher;
import info.bits.phoneastablet.services.OrientationService;
import info.bits.phoneastablet.utils.DatabaseHandler;
import info.bits.phoneastablet.utils.NotificationHandler;
import info.bits.phoneastablet.utils.SuCommands;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;

/**
 * @author LiTTle
 * It is the main activity for the application.
 */
public class Resolution extends Activity {

    private EditText portraitWidth, portraitHeight, landscapeWidth,
	    landscapeHeight;
    private CheckBox keepAnalogy;
    private Spinner resolutionPolicy;
    private String[] dimens;
    private DatabaseHandler dbHandler;
    private NotificationHandler notificationHandler;

    /**
     * Creates the activity.
     * 
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_resolution);

	createDB();
	
	setupViews();
    }

    /**
     * Sets up graphical objects of this activity.
     * Applies any initialization and listeners needed for the graphical objects.
     * 
     */
    private final void setupViews() {
	portraitWidth = (EditText) findViewById(R.id.portrait_resolution_width);
	portraitHeight = (EditText) findViewById(R.id.portrait_resolution_height);
	landscapeWidth = (EditText) findViewById(R.id.landscape_resolution_width);
	landscapeHeight = (EditText) findViewById(R.id.landscape_resolution_height);
	keepAnalogy = (CheckBox) findViewById(R.id.keep_analogy_checkBox);
	resolutionPolicy = (Spinner) findViewById(R.id.resolution_policy);

	//initialize fields
	portraitWidth.setText(dbHandler.getDimension(DatabaseHandler.KEY_PORTRAIT_WIDTH, 
		DatabaseHandler.TABLE_LATEST_RESOLUTIONS));
	portraitHeight.setText(dbHandler.getDimension(DatabaseHandler.KEY_PORTRAIT_HEIGHT, 
		DatabaseHandler.TABLE_LATEST_RESOLUTIONS));
	landscapeWidth.setText(portraitWidth.getText().toString());
	landscapeHeight.setText(portraitHeight.getText().toString());
	ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
	        R.array.policies, R.layout.spinner_inner_textview);
	adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	resolutionPolicy.setAdapter(adapter);
	resolutionPolicy.setSelection(dbHandler.getResolutionPolicy());

	//apply listeners
	keepAnalogy.setOnCheckedChangeListener(new MyOnCheckedListener(dbHandler, portraitWidth, 
		landscapeWidth, portraitHeight, landscapeHeight));
	portraitWidth.addTextChangedListener(new MyTextWatcher(keepAnalogy,
		portraitWidth, landscapeWidth));
	portraitHeight.addTextChangedListener(new MyTextWatcher(keepAnalogy,
		portraitHeight, landscapeHeight));
	
	//initialize other fields
	notificationHandler = new NotificationHandler(this);
	
	final int policy = resolutionPolicy.getSelectedItemPosition();
	if (policy == DatabaseHandler.SERVICE_POLICY) {
	    notificationHandler.disableNotification();
	    startService(new Intent(this, OrientationService.class));
	}
	else if (policy == DatabaseHandler.NOTIFICATION_POLICY) {
	    notificationHandler.enableNotification();
	    stopService(new Intent(this, OrientationService.class));
	}
	else{
	    stopService(new Intent(this, OrientationService.class));
	    notificationHandler.disableNotification();
	}
    }

    /**
     * Creates the database used by the application.
     * Also adds to the DB the default values for the resolution.
     */
    private final void createDB(){
	//get the resolutions
	try {
	    dimens = SuCommands.getAvailableResolutions();
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	    //TODO show a dialog
	    return;
	}
	dbHandler = new DatabaseHandler(this);
	dbHandler.saveDefaultResolution(dimens[0], dimens[1]);
    }
    
    /**
     * Resets the device's resolution to default.
     * @param view
     */
    public void resetToDefault(View view){
	String[] defaultResolution = dbHandler.getDefaultResolution();
	portraitWidth.setText(defaultResolution[0]);
	portraitHeight.setText(defaultResolution[1]);
	keepAnalogy.setChecked(true);
	resolutionPolicy.setSelection(DatabaseHandler.APPLICATION_POLICY);
	String help = getResources().getString(R.string.default_button_help, getResources().getString(R.string.apply));
	Toast.makeText(this, help, Toast.LENGTH_LONG).show();
    }
    
    /**
     * Applies the specified settings to the device.
     * @param view
     */
    public void applySettings(View view){
	dbHandler.saveLatestResolutions(portraitWidth.getText().toString(), portraitHeight.getText().toString(),
		landscapeWidth.getText().toString(), landscapeHeight.getText().toString());
	dbHandler.saveResolutionPolicy(String.valueOf(resolutionPolicy.getSelectedItemId()));
	Toast.makeText(this, getResources().getString(R.string.success), Toast.LENGTH_LONG).show();
	int orientation = getResources().getConfiguration().orientation;
	try{
	    if (orientation == Configuration.ORIENTATION_PORTRAIT)
		SuCommands.changeResolution(portraitWidth.getText().toString(), portraitHeight.getText().toString());
	    else if (orientation == Configuration.ORIENTATION_LANDSCAPE)
		SuCommands.changeResolution(landscapeWidth.getText().toString(), landscapeHeight.getText().toString());
	}catch (IOException ioe){}
	int policy = resolutionPolicy.getSelectedItemPosition();
	if (policy == DatabaseHandler.SERVICE_POLICY) {
	    notificationHandler.disableNotification();
	    startService(new Intent(this, OrientationService.class));
	}
	else if (policy == DatabaseHandler.NOTIFICATION_POLICY) {
	    notificationHandler.enableNotification();
	    stopService(new Intent(this, OrientationService.class));
	}
	else{
	    stopService(new Intent(this, OrientationService.class));
	    notificationHandler.disableNotification();
	}
	    
    }
    /*
     * @Override public boolean onCreateOptionsMenu(Menu menu) { // Inflate the
     * menu; this adds items to the action bar if it is present.
     * getMenuInflater().inflate(R.menu.resolution, menu); return true; }
     */

}
