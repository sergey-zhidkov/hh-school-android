package ru.hh.school.android;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        super(context, "resumeDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table resume ("
                + "id integer primary key autoincrement,"
                + "name text,"
                + "birthday text,"
                + "gender text,"
                + "position text,"
                + "salary text,"
                + "phone text,"
                + "email text"
                + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
