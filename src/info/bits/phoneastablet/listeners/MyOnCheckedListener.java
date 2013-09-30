package info.bits.phoneastablet.listeners;

import info.bits.phoneastablet.utils.DatabaseHandler;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;

/**
 * @author LiTTle
 * This is a listener which is applied to enhance the behavior of the checkbox.
 *
 */
public class MyOnCheckedListener implements OnCheckedChangeListener {

    private final EditText portraitTransmitter, portraitReceiver, 
    	landscapeTransimitter, landscapeReceiver;
    private final DatabaseHandler dbHandler;
    /**
     * Constructs a new listener and prepare it for use.
     */
    public MyOnCheckedListener(DatabaseHandler handler, EditText...fields) {
	portraitTransmitter = fields[0];
	portraitReceiver = fields[1];
	landscapeTransimitter = fields[2];
	landscapeReceiver = fields[3];
	dbHandler = handler;
    }

    /**
     * It is called every time the checkbox changes its state.
     * It is enhanced to change the DB data also. 
     * @see android.widget.CompoundButton.OnCheckedChangeListener#onCheckedChanged(android.widget.CompoundButton, boolean)
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
	portraitReceiver.setEnabled(!isChecked);
	landscapeReceiver.setEnabled(!isChecked);
	if(isChecked){
	    portraitReceiver.setText(portraitTransmitter.getText().toString());
	    landscapeReceiver.setText(landscapeTransimitter.getText().toString());
	}
	else{
	    portraitReceiver.setText(dbHandler.getDimension(DatabaseHandler.KEY_LANDSCAPE_WIDTH, 
			DatabaseHandler.TABLE_LATEST_RESOLUTIONS));
	    landscapeReceiver.setText(dbHandler.getDimension(DatabaseHandler.KEY_LANDSCAPE_HEIGHT, 
			DatabaseHandler.TABLE_LATEST_RESOLUTIONS));
	}
    }

}
