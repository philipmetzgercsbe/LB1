package com.example.lb1.SQL;

import android.database.sqlite.SQLiteDatabase;

public interface DBHelper {


    void selectAll(SQLiteDatabase Db);
    void resetDB(SQLiteDatabase Db);

}
