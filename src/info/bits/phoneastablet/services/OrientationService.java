/**
 * 
 */
package info.bits.phoneastablet.services;

import info.bits.phoneastablet.receivers.OrientationReceiver;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

/**
 * @author little
 * 
 */
public class OrientationService extends Service {

    private static final String BCAST_CONFIGCHANGED = "android.intent.action.CONFIGURATION_CHANGED";
    private OrientationReceiver receiver;

    @Override
    public void onCreate() {
	// TODO Auto-generated method stub
	super.onCreate();

	receiver = new OrientationReceiver();
	IntentFilter filter = new IntentFilter();
	filter.addAction(BCAST_CONFIGCHANGED);
	registerReceiver(receiver, filter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
	// TODO Auto-generated method stub
	return START_STICKY;
    }
    
    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.app.Service#onBind(android.content.Intent)
     */
    @Override
    public IBinder onBind(Intent arg0) {
	// TODO Auto-generated method stub
	return null;
    }

}
