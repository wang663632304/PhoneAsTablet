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

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author LiTTle
 * A handler of the database. This handler communicate to the database and stores the user's preferences. 
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    public static final String DB_NAME = "phone_as_tablet",
	    KEY_ID = "_id",
	    //default resolution table
	    TABLE_DEFAULT_RESOLUTION = "default_resolution",
	    KEY_DEFAULT_WIDTH = "default_width",
	    KEY_DEFAULT_HEIGHT = "default_height",
	    //latest resolution table
	    TABLE_LATEST_RESOLUTIONS = "latest_resolutions",
	    KEY_PORTRAIT_WIDTH = "portrait_width",
	    KEY_PORTRAIT_HEIGHT = "portrait_height",
	    KEY_LANDSCAPE_WIDTH = "landscape_width",
	    KEY_LANDSCAPE_HEIGHT = "landscape_height",
	    //settings table
	    TABLE_SETTINGS = "settings",
	    KEY_TYPE = "type",
	    KEY_VALUE = "value";
    public static final int APPLICATION_POLICY = 0,
	    SERVICE_POLICY = 1,
	    NOTIFICATION_POLICY = 2;
	    
    /**
     * Triggers the creation of the database.
     * @param context
     */
    public DatabaseHandler(Context context) {
	super(context, DB_NAME, null, DB_VERSION);
    }

    /**
     * Creates the database.
     * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
	String defaultResolutionsCmd = "CREATE TABLE " + TABLE_DEFAULT_RESOLUTION + 
		"(" + KEY_ID + " INTEGER PRIMARY KEY," + 
		KEY_DEFAULT_WIDTH + " TEXT," + 
		KEY_DEFAULT_HEIGHT + " TEXT" + ")";
	String resolutionsCmd = "CREATE TABLE " + TABLE_LATEST_RESOLUTIONS + 
		"(" + KEY_ID + " INTEGER PRIMARY KEY," + 
		KEY_PORTRAIT_WIDTH + " TEXT," + 
		KEY_PORTRAIT_HEIGHT + " TEXT," + 
		KEY_LANDSCAPE_WIDTH + " TEXT," + 
		KEY_LANDSCAPE_HEIGHT + " TEXT" + ")";
	String settingsCmd = "CREATE TABLE " + TABLE_SETTINGS + 
		"(" + KEY_ID + " INTEGER PRIMARY KEY," + 
		KEY_TYPE + " TEXT," + 
		KEY_VALUE + " TEXT" + ")";
	db.execSQL(defaultResolutionsCmd);
	db.execSQL(resolutionsCmd);
	db.execSQL(settingsCmd);
    }

    /**
     * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	db.execSQL("DROP TABLE IF EXISTS " + TABLE_DEFAULT_RESOLUTION);
	db.execSQL("DROP TABLE IF EXISTS " + TABLE_LATEST_RESOLUTIONS);
	db.execSQL("DROP TABLE IF EXISTS " + TABLE_SETTINGS);
	 
        // Create table again
        onCreate(db);
    }
    
    /**
     * Stores the default resolution settings for the device.
     * @param dimens default resolution dimension for the device
     */
    public void saveDefaultResolution(String... dimens){
	SQLiteDatabase db = this.getWritableDatabase();
	ContentValues defaultValues = new ContentValues();
	defaultValues.put(KEY_ID, 1);
	defaultValues.put(KEY_DEFAULT_WIDTH, dimens[0]);
	defaultValues.put(KEY_DEFAULT_HEIGHT, dimens[1]);
	db.insert(TABLE_DEFAULT_RESOLUTION, null, defaultValues);
	
	db.close();
    }
    
    /**
     * Returns the defautl resolution values for the device.
     * @return the default resolution values
     */
    public String[] getDefaultResolution(){
	String countQuery = "SELECT * FROM " + TABLE_DEFAULT_RESOLUTION + " WHERE _id=1";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.moveToFirst();
        String[] result = new String[2];
        result[0] = cursor.getString(cursor.getColumnIndex(KEY_DEFAULT_WIDTH));
        result[1] = cursor.getString(cursor.getColumnIndex(KEY_DEFAULT_HEIGHT));
        db.close();
        return result;
    }
    
    /**
     * Stores the latest resolution values according to user's preferences.
     * @param dimens user defined resolution values
     */
    public void saveLatestResolutions(String... dimens){
	SQLiteDatabase db = this.getWritableDatabase();
	ContentValues customValues = new ContentValues();
	customValues.put(KEY_ID, 1);
	customValues.put(KEY_PORTRAIT_WIDTH, dimens[0]);
	customValues.put(KEY_PORTRAIT_HEIGHT, dimens[1]);
	customValues.put(KEY_LANDSCAPE_WIDTH, dimens[2]);
	customValues.put(KEY_LANDSCAPE_HEIGHT, dimens[3]);
	 
	// updating row
	int rows = db.update(TABLE_LATEST_RESOLUTIONS, customValues, KEY_ID + " = ?", new String[] {"1"});
	if (rows == 0)
	    db.insert(TABLE_LATEST_RESOLUTIONS, null, customValues);
	db.close();
    }
    
    /**
     * Stores the policy for resolution settings.
     * The policy defines when the resolution will be applied, what values will be used, etc.
     * @param value the policy for resolution settings
     */
    public void saveResolutionPolicy(String value){
	SQLiteDatabase db = this.getWritableDatabase();
	ContentValues policy = new ContentValues();
	policy.put(KEY_ID, 1);
	policy.put(KEY_TYPE, "Policy");
	policy.put(KEY_VALUE, value);
	 
	// updating row
	int rows = db.update(TABLE_SETTINGS, policy, KEY_ID + " = ?", new String[] {"1"});
	if (rows == 0)
	    db.insert(TABLE_SETTINGS, null, policy);
	db.close();
    }

    /**
     * Returns the policy for resolution settings.
     * The policy defines when the resolution will be applied, what values will be used, etc.
     * @return the policy for resolution settings
     */
    public int getResolutionPolicy(){
	int result;
	String countQuery = "SELECT * FROM " + TABLE_SETTINGS + " WHERE _id=1";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        if (cursor.getCount() == 0) {
            result = APPLICATION_POLICY;
        }
        else{
            cursor.moveToFirst();
            result = cursor.getInt(cursor.getColumnIndex(KEY_VALUE));
            cursor.close();
        }
        return result;
    }
    
    /**
     * Returns the user defined value of the width or height depending the orientation of the device.
     * If there is no record it returns the default values.
     * @param dimension width/height of the current orientation
     * @param table the database table from which retrieves the values.
     * @return
     */
    public String getDimension(String dimension, String table){
	String result = null;
	String countQuery = "SELECT * FROM " + table;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        if (cursor.getCount() == 0) {
            if(dimension.equals(KEY_PORTRAIT_WIDTH) || dimension.equals(KEY_LANDSCAPE_WIDTH)){
        	result = getDimension(KEY_DEFAULT_WIDTH, TABLE_DEFAULT_RESOLUTION);
            }
            else if(dimension.equals(KEY_PORTRAIT_HEIGHT) || dimension.equals(KEY_LANDSCAPE_HEIGHT)){
        	result = getDimension(KEY_DEFAULT_HEIGHT, TABLE_DEFAULT_RESOLUTION);
            }
        }
        else{
            cursor.moveToFirst();
            result = cursor.getString(cursor.getColumnIndex(dimension));
            cursor.close();
        }
        return result;
    }

}
