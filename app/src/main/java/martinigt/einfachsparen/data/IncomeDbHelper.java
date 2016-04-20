package martinigt.einfachsparen.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;

import martinigt.einfachsparen.model.Income;

/**
 * Created by martin on 16.04.16.
 */
public class IncomeDbHelper {

    public static final String INCOME_TABLE_NAME = "Income";

    public static final String INCOME_ID = "_id";
    public static final String INCOME_PERIOD_ID = "periodId";
    public static final String INCOME_IS_STANDARD = "standard";
    public static final String INCOME_NAME = "name";
    public static final String INCOME_VALUE = "value";
    public static final String INCOME_DATE = "date";
    public static final String INCOME_TAG = "tag";

    public static final String EINNAHMEN_SCHEMA_CREATE = "CREATE TABLE " + INCOME_TABLE_NAME +" (" + INCOME_ID + " INTEGER " +
            "PRIMARY KEY, " + INCOME_PERIOD_ID + " INTEGER, " + INCOME_IS_STANDARD +" INTEGER, " +
            INCOME_NAME + " TEXT, " + INCOME_VALUE + " real, "+ INCOME_TAG + " TEXT, " +
            INCOME_DATE + " INTEGER)";

    public static final String INCOME_DROP_TABLE = "DROP TABLE IF EXISTS " + INCOME_TABLE_NAME;

    public static final String INCOME_GET_ALL_DEFAULT = "SELECT * FROM " + INCOME_TABLE_NAME + " WHERE " +
            INCOME_IS_STANDARD + " >= 1";

    private static final String INCOME_DELETE_TABLE = "DELETE FROM " + INCOME_TABLE_NAME;

    private DatabaseHelper dbHelper;

    public IncomeDbHelper(DatabaseHelper helper) {
        dbHelper = helper;
    }

    public void cleanTable() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL(INCOME_DELETE_TABLE);
    }

    public void storeListOfIncomes(ArrayList<Income> incomesToStore) {
        for (Income currentIncome: incomesToStore
                ) {
            addIncome(currentIncome);
        }
    }

    public boolean addIncome(Income incomeToAdd) {
        boolean result = false;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(INCOME_NAME, incomeToAdd.getName());
        contentValues.put(INCOME_TAG, incomeToAdd.getTag());
        contentValues.put(INCOME_VALUE, incomeToAdd.getValue());
        contentValues.put(INCOME_DATE, incomeToAdd.getDate().getTime());
        contentValues.put(INCOME_IS_STANDARD, incomeToAdd.isStandard());
        contentValues.put(INCOME_PERIOD_ID, incomeToAdd.getPeriodId());
        try {
            db.insert(INCOME_TABLE_NAME, null, contentValues);
            result = true;
        }
        catch (Exception e) {
            result = false;
        }
        return result;
    }

    public ArrayList<Income> getAllIncomesForPeriod(int periodId) {
        ArrayList<Income> result = new ArrayList<Income>();
        SQLiteDatabase db =  dbHelper.getReadableDatabase();
        Cursor dbResults = db.rawQuery("SELECT * FROM " +  INCOME_TABLE_NAME + " WHERE " +
                periodId + " = " + INCOME_PERIOD_ID, null);
        while (dbResults.moveToNext()) {
            result.add(getIncomeFromCursor(dbResults, periodId));
        }
        return result;
    }

    public ArrayList<Income> getAllDefaultIncomes() {
        ArrayList<Income> result = new ArrayList<Income>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor allResults = db.rawQuery(INCOME_GET_ALL_DEFAULT, null);
        while (allResults.moveToNext()) {
            result.add(getIncomeFromCursor(allResults));
        }
        return result;
    }

    private Income getIncomeFromCursor(Cursor cursor) {
        return this.getIncomeFromCursor(cursor, -1);
    }

    private Income getIncomeFromCursor(Cursor cursor, int periodId) {
        Income result = new Income();
        result.setPeriodId(periodId);
        result.setId(cursor.getInt(cursor.getColumnIndex(INCOME_ID)));
        result.setStandard(cursor.getInt(cursor.getColumnIndex(INCOME_IS_STANDARD)) > 0);
        result.setName(cursor.getString(cursor.getColumnIndex(INCOME_NAME)));
        result.setValue(cursor.getDouble(cursor.getColumnIndex(INCOME_VALUE)));
        result.setDate(new Date(cursor.getInt(cursor.getColumnIndex(INCOME_DATE))));
        result.setTag(cursor.getString(cursor.getColumnIndex(INCOME_TAG)));
        return result;
    }

}
