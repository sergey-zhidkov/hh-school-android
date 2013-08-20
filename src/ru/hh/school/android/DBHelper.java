package ru.hh.school.android;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "resumeDB";
    public static final String TABLE_RESUME = "resume";

    public static final String COL_ROWID = "rowid";
    public static final String COL_NAME = "name";
    public static final String COL_BIRTHDAY = "birthday";
    public static final String COL_GENDER = "gender";
    public static final String COL_POSITION = "position";
    public static final String COL_SALARY = "salary";
    public static final String COL_PHONE = "phone";
    public static final String COL_EMAIL = "email";


    public DBHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_RESUME + " ("
                + COL_ROWID + " integer primary key autoincrement,"
                + COL_NAME + " text not null,"
                + COL_BIRTHDAY + " integer not null,"
                + COL_GENDER + " text not null,"
                + COL_POSITION + " text not null,"
                + COL_SALARY + " text not null,"
                + COL_PHONE + " text not null,"
                + COL_EMAIL + " text not null"
                + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
