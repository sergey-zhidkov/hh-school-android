package ru.hh.school.android;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
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

    /**
     * Returns all resumes list from DB.
     *
     * @return resumes list
     */
    public List<Resume> getResumesList() {
        List<Resume> resumes = new ArrayList<Resume>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("resume", null, null, null, null, null, null);

        Resume resume;
        int colIndex;
        if (cursor.moveToFirst()) {
            do {
                resume = new Resume();

                colIndex = cursor.getColumnIndex(DBHelper.COL_ROWID);
                resume.setId(cursor.getInt(colIndex));

                colIndex = cursor.getColumnIndex(DBHelper.COL_NAME);
                resume.setLastFirstName(cursor.getString(colIndex));

                colIndex = cursor.getColumnIndex(DBHelper.COL_BIRTHDAY);
                long birthday = cursor.getLong(colIndex);
                resume.setBirthday(new Date(birthday));

                colIndex = cursor.getColumnIndex(DBHelper.COL_GENDER);
                resume.setGender(cursor.getString(colIndex));

                colIndex = cursor.getColumnIndex(DBHelper.COL_POSITION);
                resume.setDesiredJobTitle(cursor.getString(colIndex));

                colIndex = cursor.getColumnIndex(DBHelper.COL_SALARY);
                resume.setSalary(cursor.getString(colIndex));

                colIndex = cursor.getColumnIndex(DBHelper.COL_PHONE);
                resume.setPhone(cursor.getString(colIndex));

                colIndex = cursor.getColumnIndex(DBHelper.COL_EMAIL);
                resume.setEmail(cursor.getString(colIndex));

                resumes.add(resume);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return resumes;
    }

    /**
     * Returns resume from DB by rowId.
     *
     * @param rowId
     * @return resume object
     */
    public Resume getResumeByRowId(long rowId) {
        Resume resume = new Resume();
        SQLiteDatabase db = this.getReadableDatabase();
        String select = "select * from " + DBHelper.TABLE_RESUME + " where rowid = ?";
        Cursor cursor = db.rawQuery(select, new String[] {Long.toString(rowId)});

        int colIndex;
        if (cursor.moveToFirst()) {
            resume.setId((int) rowId);

            colIndex = cursor.getColumnIndex(DBHelper.COL_NAME);
            resume.setLastFirstName(cursor.getString(colIndex));

            colIndex = cursor.getColumnIndex(DBHelper.COL_BIRTHDAY);
            long birthday = cursor.getLong(colIndex);
            resume.setBirthday(new Date(birthday));

            colIndex = cursor.getColumnIndex(DBHelper.COL_GENDER);
            resume.setGender(cursor.getString(colIndex));

            colIndex = cursor.getColumnIndex(DBHelper.COL_POSITION);
            resume.setDesiredJobTitle(cursor.getString(colIndex));

            colIndex = cursor.getColumnIndex(DBHelper.COL_SALARY);
            resume.setSalary(cursor.getString(colIndex));

            colIndex = cursor.getColumnIndex(DBHelper.COL_PHONE);
            resume.setPhone(cursor.getString(colIndex));

            colIndex = cursor.getColumnIndex(DBHelper.COL_EMAIL);
            resume.setEmail(cursor.getString(colIndex));
        }

        db.close();
        cursor.close();
        return resume;
    }

    /**
     * Inserts resume into DB and returns newly created rowId.
     *
     * @param resume
     * @return new created rowId
     */
    public long insertResumeIntoDB(Resume resume) {
        ContentValues cv = new ContentValues();
        SQLiteDatabase db = this.getWritableDatabase();

        cv.put(DBHelper.COL_NAME, resume.getLastFirstName());
        cv.put(DBHelper.COL_BIRTHDAY, resume.getBirthday().getTime());
        cv.put(DBHelper.COL_GENDER, resume.getGender());
        cv.put(DBHelper.COL_POSITION, resume.getDesiredJobTitle());
        cv.put(DBHelper.COL_SALARY, resume.getSalary());
        cv.put(DBHelper.COL_PHONE, resume.getPhone());
        cv.put(DBHelper.COL_EMAIL, resume.getEmail());

        long rowId = db.insert(DBHelper.TABLE_RESUME, null, cv);
        db.close();
        return rowId;
    }

    /**
     * Remove resume from DB by rowId.
     *
     * @param rowId
     */
    public void removeResumeByRowId(long rowId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DBHelper.TABLE_RESUME, "rowid=" + rowId, null);
        db.close();
    }

    /**
     * Updates resume into DB by rowId.
     *
     * @param rowId
     * @param resume resume to update
     */
    public void updateResumeByRowId(long rowId, Resume resume) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(DBHelper.COL_NAME, resume.getLastFirstName());
        cv.put(DBHelper.COL_BIRTHDAY, resume.getBirthday().getTime());
        cv.put(DBHelper.COL_GENDER, resume.getGender());
        cv.put(DBHelper.COL_POSITION, resume.getDesiredJobTitle());
        cv.put(DBHelper.COL_SALARY, resume.getSalary());
        cv.put(DBHelper.COL_PHONE, resume.getPhone());
        cv.put(DBHelper.COL_EMAIL, resume.getEmail());

        db.update(DBHelper.TABLE_RESUME, cv, "rowid=" + rowId, null);
        db.close();
    }
}
