package martinigt.einfachsparen.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by martin on 16.04.16.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "EinfachSparen.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(IncomeDbHelper.EINNAHMEN_SCHEMA_CREATE);
        sqLiteDatabase.execSQL(ExpenseDbHelper.EXPENSE_SCHEMA_CREATE);
        sqLiteDatabase.execSQL(PeriodDbHelper.PERIODEN_SCHEMA_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

}
