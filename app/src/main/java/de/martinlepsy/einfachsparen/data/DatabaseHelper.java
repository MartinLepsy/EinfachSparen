package de.martinlepsy.einfachsparen.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import de.martinlepsy.einfachsparen.model.Transaction;

/**
 * Created by martin on 16.04.16.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "EinfachSparen.db";
    private static final int DATABASE_VERSION = 14;

    private ArrayList<Transaction> allTransactions;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        allTransactions = new ArrayList<>();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        createDatabaseFromScratch(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        recreateDatabase();
    }

    public void recreateDatabase() {
        deleteDatabases(getWritableDatabase());
        createDatabaseFromScratch(getWritableDatabase());
    }

    private void deleteDatabases(SQLiteDatabase sqLiteDatabase){
        sqLiteDatabase.execSQL(TransactionDbHelper.TRANSACTION_DROP_TABLE);
        sqLiteDatabase.execSQL(PeriodDbHelper.PERIOD_DROP_TABLE);
    }


    private void createDatabaseFromScratch(SQLiteDatabase sqlLiteDatabase) {
        sqlLiteDatabase.execSQL(TransactionDbHelper.TRANSACTION_SCHEMA_CREATE);
        sqlLiteDatabase.execSQL(PeriodDbHelper.PERIODEN_SCHEMA_CREATE);
    }

}
