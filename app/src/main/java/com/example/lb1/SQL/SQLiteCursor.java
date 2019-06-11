package com.example.lb1.SQL;

import android.database.Cursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQuery;
import android.location.Location;

import com.example.lb1.Models.ImageData;

import java.util.ArrayList;


public class SQLiteCursor extends android.database.sqlite.SQLiteCursor {

    public SQLiteCursor(SQLiteCursorDriver driver, String editTable, SQLiteQuery query) {
        super(driver, editTable, query);
    }

    public ArrayList<ImageData> getAll(SQLiteDatabase db) {
        ArrayList<ImageData> imageData = new ArrayList<>();
        Cursor ImageCursor = db.rawQuery("SELECT Image.id,image.name, image.image,location.latidude,location.longitude FROM Image  JOIN image_location on image.id = image_location.fkimage  JOIN location on image_location.fklocation = location.id  ", null);

        if (ImageCursor.moveToFirst()) {
            do {
                ImageData img = new ImageData();
                img.setId(ImageCursor.getColumnIndex("id"));
                img.setName(ImageCursor.getString(ImageCursor.getColumnIndex("name")));
                img.setLongitude(ImageCursor.getColumnIndex("longitude"));
                img.setLatitude(ImageCursor.getColumnIndex("latidude"));
                imageData.add(img);
            } while (ImageCursor.moveToNext());



        }
        return imageData;
    }
    public ArrayList<Location> getLocations(SQLiteDatabase db) {
        ArrayList<Location> Locations = new ArrayList<>();
        Cursor LocationCursor = db.rawQuery("SELECT latidude,longitude FROM Location", null);
        if (LocationCursor.moveToFirst()) {
            do {
                Location Loc = new Location("db");
                Loc.setLongitude(LocationCursor.getColumnIndex("longitude"));
                Loc.setLatitude(LocationCursor.getColumnIndex("latidude"));
                Locations.add(Loc);
            } while (LocationCursor.moveToNext());
        }
        return Locations;

    }
}
