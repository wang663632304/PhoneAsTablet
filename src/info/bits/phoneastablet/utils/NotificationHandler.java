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

package info.bits.phoneastablet.utils;

import info.bits.phoneastablet.R;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

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
	.setContentText(context.getString(R.string.notification_private_text))
	.setSmallIcon(R.drawable.ic_launcher);
	nm = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
	Notification notification = builder.build();
	notification.flags |= Notification.FLAG_ONGOING_EVENT;
	
	RemoteViews contentView=new RemoteViews(context.getPackageName(), R.layout.notification);	
	//set the button listeners
	setListeners(contentView);	
	notification.contentView = contentView;
	nm.notify(NOTIFICATION_ID, notification);
	
    }
    
    /**
     * Hides the notification from the notification area.
     */
    public void disableNotification(){
	if(nm != null)
	    nm.cancel(NOTIFICATION_ID);
    }
    
    /**
     * Sets the listeners for the buttons at the notification area.
     * 
     * @param view
     */
    public void setListeners(RemoteViews view){
	//App start listener
	Intent app=new Intent(context, NotificationButtonsHandler.class);
	app.putExtra("DO", "app");
	PendingIntent pApp = PendingIntent.getActivity(context, 0, app, 0);
	view.setOnClickPendingIntent(R.id.app, pApp);
	
	//default screen size listener
	Intent defaultResolution=new Intent(context, NotificationButtonsHandler.class);
	defaultResolution.putExtra("DO", "default");
	PendingIntent pDefaultResolution = PendingIntent.getActivity(context, 1, defaultResolution, 0);
	view.setOnClickPendingIntent(R.id.default_resolution, pDefaultResolution);
	
	//custom screen size listener
	Intent customResolution=new Intent(context, NotificationButtonsHandler.class);
	customResolution.putExtra("DO", "custom");
	PendingIntent pCustomResolution = PendingIntent.getActivity(context, 2, customResolution, 0);
	view.setOnClickPendingIntent(R.id.custom_resolution, pCustomResolution);
}
}
