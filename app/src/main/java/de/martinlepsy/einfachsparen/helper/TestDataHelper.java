package de.martinlepsy.einfachsparen.helper;

import android.content.Context;

import java.util.ArrayList;
import java.util.Date;

import de.martinlepsy.einfachsparen.data.DatabaseHelper;
import de.martinlepsy.einfachsparen.data.PeriodDbHelper;
import de.martinlepsy.einfachsparen.data.TransactionDbHelper;
import de.martinlepsy.einfachsparen.model.Period;
import de.martinlepsy.einfachsparen.model.Transaction;
import de.martinlepsy.einfachsparen.model.TransactionType;

/**
 * Created by LM on 08.02.2018.
 */
public class TestDataHelper {

    private DatabaseHelper dbHelper;

    public TestDataHelper(Context context) {
        this.dbHelper = new DatabaseHelper(context);
    }

    public void createTestData(boolean recreateDatabase, Date periodStartDate, int durationDays) {
        if (recreateDatabase) {
            dbHelper.recreateDatabase();
        }
        Period samplePeriod = createAndStoreSamplePeriod(periodStartDate, durationDays);
        ArrayList<Transaction> sampleExpenses = createSampleExpenses(samplePeriod);
        ArrayList<Transaction> sampleIncome = createSampleIncome(samplePeriod);
        TransactionDbHelper transactionDbHelper = new TransactionDbHelper(dbHelper);
        transactionDbHelper.storeListOfTransactions(sampleExpenses);
        transactionDbHelper.storeListOfTransactions(sampleIncome);
    }

    private Period createAndStoreSamplePeriod(Date periodStartDate, int durationDays) {
        Period result = new Period();
        result.setStart(periodStartDate);
        result.setEnd(Helper.addDaysToDate(periodStartDate, durationDays));
        result.setName("Testperiode");
        result.setPlannedSaving(300);
        PeriodDbHelper periodDbHelper = new PeriodDbHelper(dbHelper);
        long samplePeriodId = periodDbHelper.savePeriod(result);
        result.setId(samplePeriodId);
        return result;
    }

    private ArrayList<Transaction> createSampleExpenses(Period samplePeriod) {
        ArrayList<Transaction> result = new ArrayList<>();
        Transaction rent = new Transaction(true, "Miete", "Wohnen",
                500, Helper.addDaysToDate(samplePeriod.getStart(),1), TransactionType.EXPENSE, 0, samplePeriod.getId());
        Transaction power = new Transaction(true, "Strom", "Wohnen",
                70, Helper.addDaysToDate(samplePeriod.getStart(),3), TransactionType.EXPENSE, 0, samplePeriod.getId());
        Transaction food = new Transaction(false, "Essen", "Haushalt",
                300, Helper.addDaysToDate(samplePeriod.getStart(),5), TransactionType.EXPENSE, 0, samplePeriod.getId());
        result.add(rent);
        result.add(power);
        result.add(food);
        return result;
    }

    private ArrayList<Transaction> createSampleIncome(Period samplePeriod) {
        ArrayList<Transaction> result = new ArrayList<>();
        Transaction paycheck = new Transaction(true, "Lohn", "",
                2000, Helper.addDaysToDate(samplePeriod.getStart(),1), TransactionType.INCOME, 0, samplePeriod.getId());
        Transaction power = new Transaction(true, "Einnahme", "",
                120, Helper.addDaysToDate(samplePeriod.getStart(),10), TransactionType.INCOME, 0, samplePeriod.getId());
        return result;
    }


}
