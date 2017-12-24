package database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.manutastic.android.simplydo.Task;

import java.util.Date;
import java.util.UUID;

import database.TaskDbSchema.CrimeTable;

public class TaskCursorWrapper extends CursorWrapper {
    public TaskCursorWrapper(Cursor cursor) {
        super(cursor);
    }
    public Task getCrime() {
        String uuidString = getString(getColumnIndex(CrimeTable.Cols.UUID));
        String title = getString(getColumnIndex(CrimeTable.Cols.TITLE));
        long date = getLong(getColumnIndex(CrimeTable.Cols.DATE));
        int isSolved = getInt(getColumnIndex(CrimeTable.Cols.SOLVED));
        String contact = getString(getColumnIndex(CrimeTable.Cols.CONTACT));

        Task crime = new Task(UUID.fromString(uuidString));
        crime.setTitle(title);
        crime.setDate(new Date(date));
        crime.setSolved(isSolved != 0);
        crime.setContact(contact);

        return crime;
    }
}
