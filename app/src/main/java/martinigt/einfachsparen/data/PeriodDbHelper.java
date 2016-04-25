package martinigt.einfachsparen.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.Date;

import martinigt.einfachsparen.model.Period;

/**
 * Created by martin on 16.04.16.
 */
public class PeriodDbHelper {

    public static final String PERIOD_TABLE_NAME = "Period";

    public static final String PERIOD_ID = "_id";
    public static final String PERIOD_START = "start";
    public static final String PERIOD_END = "end";
    public static final String PERIOD_NAME = "name";
    public static final String PERIOD_PLANNED_SAVING = "plannedSaving";

    public static final String PERIODEN_SCHEMA_CREATE = "CREATE TABLE " + PERIOD_TABLE_NAME + " (" + PERIOD_ID +" INTEGER " +
            "PRIMARY KEY, " + PERIOD_START + " INTEGER, " + PERIOD_END + " INTEGER, " + PERIOD_NAME + " TEXT, " +
            PERIOD_PLANNED_SAVING + " REAL)";

    public static final String PERIOD_DROP_TABLE = "DROP TABLE IF EXISTS " + PERIOD_TABLE_NAME;

    private static final String PERIOD_DELETE_TABLE = "DELETE FROM " + PERIOD_TABLE_NAME;

    private static final String GET_MOST_RECENT_PERIOD_QUERY = "SELECT * FROM " + PERIOD_TABLE_NAME +
            " ORDER BY " + PERIOD_ID + " DESC LIMIT 1";

    private DatabaseHelper dbHelper;

    public PeriodDbHelper(DatabaseHelper helper){
        dbHelper = helper;
    }

    public long savePeriod(Period period)
    {
        long result = -1;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PERIOD_START, period.getStart().getTime());
        contentValues.put(PERIOD_END, period.getEnd().getTime());
        contentValues.put(PERIOD_NAME, period.getName());
        contentValues.put(PERIOD_PLANNED_SAVING, period.getPlannedSaving());
        try {
            result = db.insert(PERIOD_TABLE_NAME, null, contentValues);
        }
        catch (Exception e) {
        }
        return result;
    }

    public Period getMostRecentPeriod() {
        Period result = null;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor dbResults = db.rawQuery(GET_MOST_RECENT_PERIOD_QUERY, null);
        while (dbResults.moveToNext()) {
            result = getPeriodFromCursor(dbResults);
        }
        return result;
    }

    public void cleanTable() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL(PERIOD_DELETE_TABLE);
    }

    public Period getCurrentPeriod() {
        return getPeriodForDate(new Date());
    }

    public Period getPeriodForDate(Date datum) {
        Period result = null;
        long ticks = datum.getTime();
        SQLiteDatabase db =  dbHelper.getReadableDatabase();
        Cursor dbResults = db.rawQuery("SELECT * FROM " + PERIOD_TABLE_NAME +" WHERE " + ticks + " >= " + PERIOD_START +
                " AND " +ticks +" < " + PERIOD_END, null);
        while (dbResults.moveToNext()) {
            result = getPeriodFromCursor(dbResults);
        }
        return result;
    }

    private Period getPeriodFromCursor(Cursor cursor){
        Period result = new Period();
        result.setId(cursor.getLong(cursor.getColumnIndex(PERIOD_ID)));
        result.setStart(new Date(cursor.getLong(cursor.getColumnIndex(PERIOD_START))));
        result.setEnd(new Date(cursor.getLong(cursor.getColumnIndex(PERIOD_END))));
        result.setName(cursor.getString(cursor.getColumnIndex(PERIOD_NAME)));
        result.setPlannedSaving(cursor.getDouble(cursor.getColumnIndex(PERIOD_PLANNED_SAVING)));
        return result;
    }

    public boolean updatePeriod(Period periodToUpdate) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PERIOD_NAME, periodToUpdate.getName());
        contentValues.put(PERIOD_PLANNED_SAVING, periodToUpdate.getPlannedSaving());
        contentValues.put(PERIOD_START, periodToUpdate.getStart().getTime());
        contentValues.put(PERIOD_END, periodToUpdate.getEnd().getTime());
        return db.update(PERIOD_TABLE_NAME, contentValues, PERIOD_ID + " = " + periodToUpdate.getId(), null) > 0;
    }

}
