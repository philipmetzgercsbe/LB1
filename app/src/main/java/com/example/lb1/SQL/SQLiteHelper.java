package com.example.lb1.SQL;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.location.Location;

import com.example.lb1.Models.ImageData;

import java.io.ByteArrayOutputStream;

import static android.graphics.Bitmap.CompressFormat.JPEG;

public class SQLiteHelper extends SQLiteOpenHelper implements DBHelper{
    private static final String DATABASE_NAME = "localwalkdb";
    private static final String TABLE_IMAGE = "Images";
    private static final String TABLE_IMAGE_LOCATION = "Image_location";
    private static final String TABLE_LOCATION = "Location";
    private static final String TABLE_IMAGE_COLUMN_NAME = "imagename";
    private static final String TABLE_IMAGE_COLUMN_IMAGE = "image";
    private static final String TABLE_LOCATION_COLUMN_LATITUDE = "latitude";
    private static final String TABLE_LOCATION_COLUMN_LONGITUDE = "longitude";
    private static final String TABLE_IMAGELOCATION_COLUMN_FKIMAGE = "fkimage";
    private static final String TABLE_IMAGELOCATION_COLUMN_FKLOCATION="fklocation";






    public SQLiteHelper(Context context) {
        super(context,DATABASE_NAME, null, 1);
    }




    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuilder DbStmnt = new StringBuilder();
        DbStmnt.append("CREATE DATABASE" + DATABASE_NAME +";");
        db.execSQL(DbStmnt.toString());
        CreateDB(db);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        resetDB(db);
        CreateDB(db);

    }




    @Override
    public void selectAll(SQLiteDatabase db) {
        String Stmnt = "SELECT * FROM "  + TABLE_IMAGE + ", " + TABLE_LOCATION  +", " + TABLE_IMAGE_LOCATION;
        db.execSQL(Stmnt);

    }

    @Override
    public void resetDB(SQLiteDatabase db) {
        String Tables[] = {TABLE_IMAGE,TABLE_LOCATION,TABLE_IMAGE_LOCATION};
        for(int i = 0; i<3; i++){
            String Drop = "DROP TABLE IF EXISTS " + Tables[i] + ";";
            db.execSQL(Drop);
        }
        CreateDB(db);



    }

    private void CreateDB(SQLiteDatabase DB){

        StringBuilder ImageTable = new StringBuilder();
        ImageTable.append("CREATE TABLE IF NOT EXISTS" + TABLE_IMAGE + "( id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"+ TABLE_IMAGE_COLUMN_NAME +" TEXT NOT NULL,"+ TABLE_IMAGE_COLUMN_IMAGE +" BLOB NOT NULL);");
        String ImageTbl = ImageTable.toString();
        DB.execSQL(ImageTbl);
        StringBuilder LocationTable = new StringBuilder();
        LocationTable.append("CREATE TABLE IF NOT EXISTS" + TABLE_LOCATION + "(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "+ TABLE_LOCATION_COLUMN_LATITUDE+"DOUBLE NOT NULL, "+TABLE_LOCATION_COLUMN_LONGITUDE+" DOUBLE NOT NULL);");
        String LocationTbl = LocationTable.toString();
        DB.execSQL(LocationTbl);
        StringBuilder ImageLocationTable = new StringBuilder();
        ImageLocationTable.append("CREATE TABLE IF NOT EXISTS" + TABLE_IMAGE_LOCATION + "(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "+ TABLE_IMAGELOCATION_COLUMN_FKIMAGE+"INTEGER NOT NULL,"+TABLE_IMAGELOCATION_COLUMN_FKLOCATION +"INTEGER NOT NULL, FOREIGN KEY (fkimage) REFERENCES ImageData(id), FOREIGN KEY (fklocation) REFERENCES Location(id));");
        String ImageLocationTbl = ImageLocationTable.toString();
        DB.execSQL(ImageLocationTbl);

    }
    public void Insert(ImageData imageData){
        long Imgid = addImage(imageData);
        long Locid = addLocation(imageData);
        long unused = addImage_location(Imgid,Locid);

    }

   private long addImage(ImageData imageData){
        SQLiteDatabase db = this.getWritableDatabase();
       ContentValues ImgValues = new ContentValues();
       ImgValues.put(TABLE_IMAGE_COLUMN_NAME,imageData.Name);
       ByteArrayOutputStream stream = new ByteArrayOutputStream();
       imageData.Image.compress(JPEG,50,stream);
       ImgValues.put(TABLE_IMAGE_COLUMN_IMAGE,stream.toByteArray());
       long id = db.insert(TABLE_IMAGE,null,ImgValues);
       db.close();
       return id ;



   }
   private long addLocation(ImageData imageData){
       SQLiteDatabase db = this.getWritableDatabase();
       ContentValues LocValues = new ContentValues();
       LocValues.put("Longitude",imageData.getLongitude());
       LocValues.put("Latitude",imageData.getLatitude());
       long id = db.insert(TABLE_LOCATION,null,LocValues);
       db.close();
       return id ;
   }

   private long addImage_location(long imgid, long locid){
       SQLiteDatabase db = this.getWritableDatabase();

       ContentValues ImgLocValues = new ContentValues();
       ImgLocValues.put(TABLE_IMAGELOCATION_COLUMN_FKIMAGE,imgid);
       ImgLocValues.put(TABLE_IMAGELOCATION_COLUMN_FKLOCATION,locid);
       long id = db.insert(TABLE_IMAGE_LOCATION,null,ImgLocValues);
       db.close();
       return id;

   }
    private int getLastAddedRowId() {
        SQLiteDatabase db = this.getWritableDatabase();
        String queryLastRowInserted = "select last_insert_rowid()";

        final Cursor cursor = db.rawQuery(queryLastRowInserted, null);
        int lastinsertedrow = 0;
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    lastinsertedrow = cursor.getInt(0);
                }
            } finally {
                cursor.close();
            }
        }

        return lastinsertedrow;
    }


    }
