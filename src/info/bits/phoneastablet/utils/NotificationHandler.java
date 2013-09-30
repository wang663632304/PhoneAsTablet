package info.bits.phoneastablet.utils;

import info.bits.phoneastablet.R;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

/**
 * @author LiTTle
 * A handler of the notification. This handler enables/disables the notification at the notification area. 
 */
public class NotificationHandler {

    private static final int NOTIFICATION_ID = 550220;
    private final Context context;
    private NotificationManager nm;
    /**
     * Prepares the notification to be used.
     */
    public NotificationHandler(Context ctx) {
	// TODO Auto-generated constructor stub
	context = ctx;
    }

    /**
     * Shows the notification at the notification area.
     */
    public void enableNotification(){
	NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
	builder.setTicker(context.getResources().getString(R.string.notification_ticker))
	.setAutoCancel(false)
	.setContentIntent(PendingIntent.getActivity(context, 0, new Intent(), 0))
	.setContentTitle(context.getString(R.string.app_name))
	.setContentText("NOTIFICATION")
	.setSmallIcon(R.drawable.ic_launcher);
	nm = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
	Notification notification = builder.build();
	notification.flags |= Notification.FLAG_ONGOING_EVENT;
	nm.notify(NOTIFICATION_ID, notification);
    }
    
    /**
     * Hides the notification from the notification area.
     */
    public void disableNotification(){
	if(nm != null)
	    nm.cancel(NOTIFICATION_ID);
    }
}
