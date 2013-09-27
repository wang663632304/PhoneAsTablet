/**
 * 
 */
package info.bits.phoneastablet.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author little
 *
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
     * @param context
     * @param name
     * @param factory
     * @param version
     */
    public DatabaseHandler(Context context) {
	super(context, DB_NAME, null, DB_VERSION);
    }

    /* (non-Javadoc)
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

    /* (non-Javadoc)
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
    
    public void saveDefaultResolution(String... dimens){
	SQLiteDatabase db = this.getWritableDatabase();
	ContentValues defaultValues = new ContentValues();
	defaultValues.put(KEY_ID, 1);
	defaultValues.put(KEY_DEFAULT_WIDTH, dimens[0]);
	defaultValues.put(KEY_DEFAULT_HEIGHT, dimens[1]);
	db.insert(TABLE_DEFAULT_RESOLUTION, null, defaultValues);
	
	db.close();
    }
    
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
