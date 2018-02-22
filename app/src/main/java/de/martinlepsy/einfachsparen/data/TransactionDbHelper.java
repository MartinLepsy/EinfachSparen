package de.martinlepsy.einfachsparen.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;

import de.martinlepsy.einfachsparen.model.GroupedTransactionByCategory;
import de.martinlepsy.einfachsparen.model.Period;
import de.martinlepsy.einfachsparen.model.Transaction;
import de.martinlepsy.einfachsparen.model.TransactionType;

/**
 * Created by martin on 27.04.16.
 */
public class TransactionDbHelper {

    public static final String TRANSACTION_TABLE_NAME = "Trans";

    public static final String TRANSACTION_ID = "_id";
    public static final String TRANSACTION_PERIOD_ID = "periodId";
    public static final String TRANSACTION_IS_STANDARD = "standard";
    public static final String TRANSACTION_NAME = "name";
    public static final String TRANSACTION_VALUE = "value";
    public static final String TRANSACTION_DATE = "date";
    public static final String TRANSACTION_TAG = "tag";
    public static final String TRANSACTION_TYPE = "type";
    public static final String TRANSACTION_COPIED_FROM_STANDARD_ID = "fromStandardId";

    public static final String TRANSACTION_SCHEMA_CREATE = "CREATE TABLE " + TRANSACTION_TABLE_NAME + " (" + TRANSACTION_ID + " INTEGER " +
            "PRIMARY KEY, " + TRANSACTION_PERIOD_ID + " INTEGER, " + TRANSACTION_IS_STANDARD + " INTEGER, " +
            TRANSACTION_NAME + " TEXT, " + TRANSACTION_VALUE + " real, " + TRANSACTION_TAG + " TEXT, " +
            TRANSACTION_DATE + " INTEGER, " + TRANSACTION_TYPE + " INTEGER, " + TRANSACTION_COPIED_FROM_STANDARD_ID +
            " INTEGER)";

    public static final String TRANSACTION_DROP_TABLE = "DROP TABLE IF EXISTS " + TRANSACTION_TABLE_NAME;


    public static final String GET_ALL_DEFAULT_TRANSACTIONS = "SELECT * FROM " + TRANSACTION_TABLE_NAME + " WHERE " +
            TRANSACTION_IS_STANDARD + " >= 1";

    private static final String TRANSACTION_DELETE_TABLE = "DELETE FROM " + TRANSACTION_TABLE_NAME;

    private static final String TRANSACTION_GET_ALL_TAGS = "SELECT DISTINCT " + TRANSACTION_TAG + " FROM " +
            TRANSACTION_TABLE_NAME;

    private static final String TRANSACTION_GET_TAGS_FOR_TITLE = "SELECT * FROM " + TRANSACTION_TABLE_NAME +
            " WHERE " + TRANSACTION_NAME + " = ?  ORDER BY " + TRANSACTION_ID + " DESC";

    private static final String TRANSACTION_GET_DISTINCT_EXPENSES = "SELECT DISTINCT " + TRANSACTION_NAME +
            " FROM " + TRANSACTION_TABLE_NAME;

    private static final String TRANSACTION_GET_SUM_OF_EXPENSES_IN_PERIOD_UNTIL = "SELECT sum(" + TRANSACTION_VALUE +
            ") as Total FROM " + TRANSACTION_TABLE_NAME + " WHERE " + TRANSACTION_TYPE + " = " + TransactionType.EXPENSE.ordinal() +
            " AND " + TRANSACTION_COPIED_FROM_STANDARD_ID + " = 0 AND " + TRANSACTION_IS_STANDARD +
            " = 0 AND " + TRANSACTION_PERIOD_ID + " = ? AND " + TRANSACTION_DATE + " < ?";

    private DatabaseHelper dbHelper;

    public TransactionDbHelper(DatabaseHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public void cleanTable() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL(TRANSACTION_DELETE_TABLE);
    }

    public void storeListOfTransactions(ArrayList<Transaction> transactionsToStore) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        storeListOfTransactions(transactionsToStore, db);
    }

    public void storeListOfTransactions(ArrayList<Transaction> transactionsToStore,
                                        SQLiteDatabase db) {
        for (Transaction currentTransaction : transactionsToStore
                ) {
            addTransaction(currentTransaction, db);
        }
        if (db.isOpen()) {
            db.close();
        }
    }

    public boolean addTransaction(Transaction transactionToAdd, SQLiteDatabase db) {
        boolean result = false;
        ContentValues contentValues = new ContentValues();
        contentValues.put(TRANSACTION_NAME, transactionToAdd.getName());
        contentValues.put(TRANSACTION_TAG, transactionToAdd.getTag());
        contentValues.put(TRANSACTION_VALUE, transactionToAdd.getValue());
        contentValues.put(TRANSACTION_DATE, transactionToAdd.getDate().getTime());
        contentValues.put(TRANSACTION_IS_STANDARD, transactionToAdd.isStandard());
        contentValues.put(TRANSACTION_PERIOD_ID, transactionToAdd.getPeriodId());
        contentValues.put(TRANSACTION_TYPE, transactionToAdd.getType().ordinal());
        contentValues.put(TRANSACTION_COPIED_FROM_STANDARD_ID, transactionToAdd.getFromStandardId());
        try {
            db.insert(TRANSACTION_TABLE_NAME, null, contentValues);
            result = true;
        } catch (Exception e) {
            result = false;
        }
        return result;
    }

    public boolean addTransaction(Transaction transactionToAdd) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return addTransaction(transactionToAdd, db);
    }

    public ArrayList<Transaction> getAllIncomesForPeriod(long periodId) {
        return getAllTransactionsForPeriod(periodId, TransactionType.INCOME, false);
    }

    public ArrayList<Transaction> getAllExpensesForPeriod(long periodId) {
        return getAllTransactionsForPeriod(periodId, TransactionType.EXPENSE, false);
    }

    public ArrayList<Transaction> getAllExpensesForPeriod(long periodId, boolean mostRecentFirst) {
        return getAllTransactionsForPeriod(periodId, TransactionType.EXPENSE, mostRecentFirst);
    }

    public ArrayList<Transaction> getAllTransactionsForPeriod(long periodId, TransactionType type,
                                                              boolean mostRecentFirst) {
        ArrayList<Transaction> result = new ArrayList<Transaction>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + TRANSACTION_TABLE_NAME + " WHERE " +
                periodId + " = " + TRANSACTION_PERIOD_ID + " AND " + TRANSACTION_TYPE +
                " = " + type.ordinal();
        if (mostRecentFirst) {
            query = query + " ORDER BY " + TRANSACTION_ID + " DESC";
        }
        Cursor dbResults = db.rawQuery(query, null);
        while (dbResults.moveToNext()) {
            result.add(getTransactionFromCursor(dbResults, periodId));
        }
        dbResults.close();
        return result;
    }

    public float getSumOfExpensesForPeriodUntil(long periodId, long dateValue) {
        float result = 0f;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] selArgs = {""+periodId, ""+ dateValue};
        Cursor dbResult = db.rawQuery(TRANSACTION_GET_SUM_OF_EXPENSES_IN_PERIOD_UNTIL, selArgs);
        if (dbResult.moveToNext()) {
            result = dbResult.getFloat(dbResult.getColumnIndex("Total"));
        }
        return result;
    }

    public ArrayList<GroupedTransactionByCategory> getAllExpensesForPeriodGroupedByCategory(long periodId) {
        ArrayList<GroupedTransactionByCategory> result = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sumColumnName = "CatValue";
        String query = "SELECT SUM(" + TRANSACTION_VALUE +") AS " + sumColumnName + ", " + TRANSACTION_TAG + " FROM " +
                TRANSACTION_TABLE_NAME + " WHERE " + periodId + " = " + TRANSACTION_PERIOD_ID + " AND " +
                TRANSACTION_TYPE + " = " + TransactionType.EXPENSE.ordinal() + " GROUP BY " + TRANSACTION_TAG;
        Cursor dbResults = db.rawQuery(query, null);
        while (dbResults.moveToNext()) {
            GroupedTransactionByCategory newResult = new GroupedTransactionByCategory();
            newResult.setCategoryName(dbResults.getString(dbResults.getColumnIndex(TRANSACTION_TAG)));
            newResult.setValue(dbResults.getDouble(dbResults.getColumnIndex(sumColumnName)));
            result.add(newResult);
        }
        return result;
    }

    public ArrayList<Transaction> getAllDefaultTransactions() {
        ArrayList<Transaction> result = new ArrayList<Transaction>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor allResults = db.rawQuery(GET_ALL_DEFAULT_TRANSACTIONS, null);
        while (allResults.moveToNext()) {
            result.add(getTransactionFromCursor(allResults));
        }
        allResults.close();
        return result;
    }

    private Transaction getTransactionFromCursor(Cursor cursor) {
        return this.getTransactionFromCursor(cursor, -1);
    }

    public boolean deleteTRansaction(Transaction transactionToDelete) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.delete(TRANSACTION_TABLE_NAME, TRANSACTION_ID + " = " + transactionToDelete.getId(), null) > 0;
    }

    public boolean deleteAllTransactionsForPeriod(Period periodToDeleteFor) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.delete(TRANSACTION_TABLE_NAME, TRANSACTION_PERIOD_ID + " = " + periodToDeleteFor.getId(), null) > 0;
    }

    public boolean updateTransaction(Transaction transactionToUpdate) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TRANSACTION_NAME, transactionToUpdate.getName());
        contentValues.put(TRANSACTION_TAG, transactionToUpdate.getTag());
        contentValues.put(TRANSACTION_VALUE, transactionToUpdate.getValue());
        contentValues.put(TRANSACTION_IS_STANDARD, transactionToUpdate.isStandard());
        contentValues.put(TRANSACTION_TYPE, transactionToUpdate.getType().ordinal());
        contentValues.put(TRANSACTION_COPIED_FROM_STANDARD_ID, transactionToUpdate.getFromStandardId());
        return db.update(TRANSACTION_TABLE_NAME, contentValues, TRANSACTION_ID + " = " + transactionToUpdate.getId(), null) > 0;
    }

    private Transaction getTransactionFromCursor(Cursor cursor, long periodId) {
        Transaction result = new Transaction();
        result.setPeriodId(periodId);
        result.setId(cursor.getInt(cursor.getColumnIndex(TRANSACTION_ID)));
        result.setStandard(cursor.getInt(cursor.getColumnIndex(TRANSACTION_IS_STANDARD)) > 0);
        result.setName(cursor.getString(cursor.getColumnIndex(TRANSACTION_NAME)));
        result.setValue(cursor.getDouble(cursor.getColumnIndex(TRANSACTION_VALUE)));
        result.setDate(new Date(cursor.getLong(cursor.getColumnIndex(TRANSACTION_DATE))));
        result.setTag(cursor.getString(cursor.getColumnIndex(TRANSACTION_TAG)));
        result.setType(TransactionType.values()[cursor.getInt(cursor.getColumnIndex(TRANSACTION_TYPE))]);
        result.setFromStandardId(cursor.getInt(cursor.getColumnIndex(TRANSACTION_COPIED_FROM_STANDARD_ID)));
        return result;
    }

    public String[] getAllAvailableTags() {
        ArrayList<String> tempResult = new ArrayList<String>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor tagCursor = db.rawQuery(TRANSACTION_GET_ALL_TAGS, null);
        while (tagCursor.moveToNext()) {
            tempResult.add(tagCursor.getString(tagCursor.getColumnIndex(TRANSACTION_TAG)));
        }
        tagCursor.close();
        return  tempResult.toArray(new String[0]);
    }

    public String[] getAllAvailableExpenses() {
        ArrayList<String> tempResults = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor expenseCursor = db.rawQuery(TRANSACTION_GET_DISTINCT_EXPENSES, null);
        while (expenseCursor.moveToNext()) {
            tempResults.add(expenseCursor.getString(expenseCursor.getColumnIndex(TRANSACTION_NAME)));
        }
        expenseCursor.close();
        return tempResults.toArray(new String[0]);
    }

    public String getAssociatedTagForTransactionTitle(String title) {
        String result = "";
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor resultsWithTitle = db.rawQuery(TRANSACTION_GET_TAGS_FOR_TITLE,new  String[] { title});
        while (resultsWithTitle.moveToNext() && result.length() == 0) {
            result = resultsWithTitle.getString(resultsWithTitle.getColumnIndex(TRANSACTION_TAG));
        }
        resultsWithTitle.close();
        return result;
    }
}
