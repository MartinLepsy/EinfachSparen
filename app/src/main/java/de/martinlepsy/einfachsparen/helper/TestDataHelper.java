package de.martinlepsy.einfachsparen.helper;

import android.content.Context;

import java.util.ArrayList;

import de.martinlepsy.einfachsparen.data.DatabaseHelper;
import de.martinlepsy.einfachsparen.data.PeriodDbHelper;
import de.martinlepsy.einfachsparen.data.TransactionDbHelper;
import de.martinlepsy.einfachsparen.model.Period;
import de.martinlepsy.einfachsparen.model.Transaction;

/**
 * Created by LM on 08.02.2018.
 */
public class TestDataHelper {

    private DatabaseHelper dbHelper;

    public TestDataHelper(Context context) {
        this.dbHelper = new DatabaseHelper(context);
    }

    public void createTestDataFromNow() {
        Period samplePeriod = createSamplePeriod();
        ArrayList<Transaction> sampleExpenses = createSampleExpenses();
        ArrayList<Transaction> sampleIncome = createSampleIncome();
        writeSampleDataToDatabase(samplePeriod, sampleExpenses, sampleIncome);
    }

    private Period createSamplePeriod() {
        Period result = new Period();
        result.setName("Testperiode");
        result.setPlannedSaving(300);
        return result;
    }

    private ArrayList<Transaction> createSampleExpenses() {
        ArrayList<Transaction> result = new ArrayList<>();

        return result;
    }

    private ArrayList<Transaction> createSampleIncome() {
        ArrayList<Transaction> result = new ArrayList<>();

        return result;
    }

    private void writeSampleDataToDatabase(Period samplePeriod, ArrayList<Transaction> sampleExpenses,
                                           ArrayList<Transaction> sampleIncome) {
        PeriodDbHelper periodDbHelper = new PeriodDbHelper(dbHelper);
        TransactionDbHelper transactionDbHelper = new TransactionDbHelper(dbHelper);
        long samplePeriodId = periodDbHelper.savePeriod(samplePeriod);

    }

}
