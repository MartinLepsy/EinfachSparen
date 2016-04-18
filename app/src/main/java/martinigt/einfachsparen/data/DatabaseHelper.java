package martinigt.einfachsparen.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by martin on 16.04.16.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "EinfachSparen.db";
    private static final int DATABASE_VERSION = 13;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        createDatabaseFromScratch(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        //if (newVersion > oldVersion) {
            deleteOldDatabase(sqLiteDatabase);
            createDatabaseFromScratch(sqLiteDatabase);
        //}
    }

    private void deleteOldDatabase(SQLiteDatabase sqlLiteDatabase) {
        sqlLiteDatabase.execSQL(ExpenseDbHelper.EXPENSE_DROP_TABLE);
        sqlLiteDatabase.execSQL(IncomeDbHelper.INCOME_DROP_TABLE);
        sqlLiteDatabase.execSQL(PeriodDbHelper.PERIOD_DROP_TABLE);
    }

    private void createDatabaseFromScratch(SQLiteDatabase sqlLiteDatabase) {
        sqlLiteDatabase.execSQL(IncomeDbHelper.EINNAHMEN_SCHEMA_CREATE);
        sqlLiteDatabase.execSQL(ExpenseDbHelper.EXPENSE_SCHEMA_CREATE);
        sqlLiteDatabase.execSQL(PeriodDbHelper.PERIODEN_SCHEMA_CREATE);
    }

}
