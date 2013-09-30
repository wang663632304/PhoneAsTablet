/**
 * 
 */
package info.bits.phoneastablet.receivers;

import java.io.IOException;

import info.bits.phoneastablet.services.OrientationService;
import info.bits.phoneastablet.utils.DatabaseHandler;
import info.bits.phoneastablet.utils.NotificationHandler;
import info.bits.phoneastablet.utils.SuCommands;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * @author LiTTle
 * This is a receiver which is called every time the device starts.
 */
public class BootCompletedReceiver extends BroadcastReceiver {

    /**
     * Change the device's settings according to user's preferences.
     * @see android.content.BroadcastReceiver#onReceive(android.content.Context, android.content.Intent)
     */
    @Override
    public void onReceive(Context context, Intent intent) {
	// TODO Auto-generated method stub
	DatabaseHandler dbHandler = new DatabaseHandler(context);
	NotificationHandler notificationHandler = new NotificationHandler(context);
	int policy = dbHandler.getResolutionPolicy();
	if (policy == DatabaseHandler.SERVICE_POLICY) {
	    notificationHandler.disableNotification();
	    context.startService(new Intent(context, OrientationService.class));
	}
	else if (policy == DatabaseHandler.NOTIFICATION_POLICY) {
	    notificationHandler.enableNotification();
	    context.stopService(new Intent(context, OrientationService.class));
	}
	else
	{
	    try {
		SuCommands.fallbackToDefaultResolution();
	    } catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	}
    }

}
