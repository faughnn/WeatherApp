package com.example.android.sunshine.app.test;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.util.Log;

import com.example.android.sunshine.app.data.WeatherContract;
import com.example.android.sunshine.app.data.WeatherDbHelper;

/**
 * Created by NF on 15/07/2015.
 */
public class TestDb extends AndroidTestCase {

    private static final String LOG_TAG = TestDb.class.getSimpleName();

    public void testCreateDb() throws Throwable {
        mContext.deleteDatabase(WeatherDbHelper.DATABASE_NAME);
        SQLiteDatabase db = new WeatherDbHelper(
                this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());
        db.close();
    }

    public void testInsertReadDb(){
        String testName = "North Pole";
        String testLocationSetting = "Dublin,IE";
        double testLatitude = 55;
        double testLongitude = 131;

        WeatherDbHelper dbHelper = new WeatherDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(WeatherContract.LocationEntry.COLUMN_CITY_NAME, testName);
        values.put(WeatherContract.LocationEntry.COLUMN_LOCATION_SETTING, testLocationSetting);
        values.put(WeatherContract.LocationEntry.COLUMN_COORD_LAT, testLatitude);
        values.put(WeatherContract.LocationEntry.COLUMN_COORD_LONG, testLongitude);

        long locationRowId;
        locationRowId = db.insert(WeatherContract.LocationEntry.TABLE_NAME, null, values);

        assertTrue(locationRowId != -1);
        Log.d(LOG_TAG, "New row id: " + locationRowId);

        String [] columns = {
                WeatherContract.LocationEntry._ID,
                WeatherContract.LocationEntry.COLUMN_LOCATION_SETTING,
                WeatherContract.LocationEntry.COLUMN_CITY_NAME,
                WeatherContract.LocationEntry.COLUMN_COORD_LAT,
                WeatherContract.LocationEntry.COLUMN_COORD_LONG
        };

        Cursor cursor = db.query(
                WeatherContract.LocationEntry.TABLE_NAME,
                columns,
                null,
                null,
                null,
                null,
                null
        );

        if(cursor.moveToFirst()){
            int locationIndex = cursor.getColumnIndex(WeatherContract.LocationEntry.COLUMN_LOCATION_SETTING);
            String location = cursor.getString(locationIndex);

            int nameIndex = cursor.getColumnIndex(WeatherContract.LocationEntry.COLUMN_CITY_NAME);
            String name = cursor.getString(nameIndex);

            int latIndex = cursor.getColumnIndex(WeatherContract.LocationEntry.COLUMN_COORD_LAT);
            double latitude = cursor.getDouble(latIndex);

            int longIndex = cursor.getColumnIndex(WeatherContract.LocationEntry.COLUMN_COORD_LONG);
            double longitude = cursor.getDouble(longIndex);

            assertEquals(testName, name);
            assertEquals(testLocationSetting, location);
            assertEquals(testLatitude, latitude);
            assertEquals(testLongitude, longitude);

        } else {
            fail("Oop, no values returned");
        }

    }
}
