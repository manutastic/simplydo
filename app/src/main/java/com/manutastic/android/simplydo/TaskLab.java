package com.manutastic.android.simplydo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import database.TaskBaseHelper;
import database.TaskCursorWrapper;
import database.TaskDbSchema.CrimeTable;

public class TaskLab
{
    public static TaskLab sCrimeLab;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static TaskLab get(Context context) {
        if (sCrimeLab == null) {
            sCrimeLab = new TaskLab(context);
        }

        return sCrimeLab;
    }

    private TaskLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new TaskBaseHelper(mContext).getWritableDatabase();
    }

    public void addCrime(Task c) {
        ContentValues values = getContentValues(c);
        mDatabase.insert(CrimeTable.NAME, null, values);
    }

    public void deleteCrime(Task c) {
        String uuidString = c.getId().toString();
        mDatabase.delete(CrimeTable.NAME, CrimeTable.Cols.UUID + " = ?", new String[] {uuidString});
    }

    public List<Task> getCrimes() {
        List<Task> crimes = new ArrayList<>();

        TaskCursorWrapper cursor = queryCrimes(null, null);
        try {
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                crimes.add(cursor.getCrime());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return crimes;
    }

    public Task getCrime(UUID id) {
        TaskCursorWrapper cursor = queryCrimes(
                CrimeTable.Cols.UUID + " = ?",
                new String[] { id.toString() }
        );

        try {
            if (cursor.getCount() == 0){
                return null;
            }
            cursor.moveToFirst();
            return cursor.getCrime();
        } finally {
            cursor.close();
        }
    }

    public void updateCrime(Task crime) {
        String uuidString = crime.getId().toString();
        ContentValues values = getContentValues(crime);

        mDatabase.update(CrimeTable.NAME, values,
                CrimeTable.Cols.UUID + " = ?",
                new String[] { uuidString });
    }

    private static ContentValues getContentValues(Task crime) {
        ContentValues values = new ContentValues();
        values.put(CrimeTable.Cols.UUID, crime.getId().toString());
        values.put(CrimeTable.Cols.TITLE, crime.getTitle());
        values.put(CrimeTable.Cols.DATE, crime.getDate().getTime());
        values.put(CrimeTable.Cols.SOLVED, crime.isSolved() ? 1 : 0);
        values.put(CrimeTable.Cols.CONTACT, crime.getContact());

        return values;
    }

    private TaskCursorWrapper queryCrimes(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                CrimeTable.NAME,
                null, //null selects all columns
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new TaskCursorWrapper(cursor);
    }
}
