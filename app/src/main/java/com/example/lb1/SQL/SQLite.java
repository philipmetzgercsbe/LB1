package com.example.lb1.SQL;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;

public class SQLite extends Service {
    private static final SQLite SQ_LITE = new SQLite();

    public static SQLite getInstance() {

        return SQ_LITE;
    }

    private SQLite() {
         final SQLiteHelper Sqlite = new SQLiteHelper(getApplicationContext());
    }

     @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
