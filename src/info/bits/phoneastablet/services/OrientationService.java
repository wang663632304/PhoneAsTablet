package info.bits.phoneastablet.services;

import info.bits.phoneastablet.receivers.OrientationReceiver;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

/**
 * @author LiTTle
 * A service running at the background and inspects the device's state
 */
public class OrientationService extends Service {

    private static final String BCAST_CONFIG_CHANGED = "android.intent.action.CONFIGURATION_CHANGED";
    private OrientationReceiver receiver;

    /**
     * Registers the receiver for orientation changes.
     * @see android.app.Service#onCreate()
     */
    @Override
    public void onCreate() {
	// TODO Auto-generated method stub
	super.onCreate();

	receiver = new OrientationReceiver();
	IntentFilter filter = new IntentFilter();
	filter.addAction(BCAST_CONFIG_CHANGED);
	registerReceiver(receiver, filter);
    }

    /**
     * Starts the service and keep it running.
     * @see android.app.Service#onStartCommand(android.content.Intent, int, int)
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
	// TODO Auto-generated method stub
	return START_STICKY;
    }
    
    /**
     * Unregisters the receiver and stops the service.
     * @see android.app.Service#onDestroy()
     */
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
