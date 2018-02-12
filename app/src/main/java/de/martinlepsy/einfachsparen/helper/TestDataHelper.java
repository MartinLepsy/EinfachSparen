package de.martinlepsy.einfachsparen.helper;

import android.content.Context;

import java.util.ArrayList;
import java.util.Date;

import de.martinlepsy.einfachsparen.data.DatabaseHelper;
import de.martinlepsy.einfachsparen.data.PeriodDbHelper;
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

    public void createTestData(boolean recreateDatabase, Date periodStartDate, int durationDays) {
        if (recreateDatabase) {
            dbHelper.recreateDatabase();
        }
        Period samplePeriod = createAndStoreSamplePeriod(periodStartDate, durationDays);
        ArrayList<Transaction> sampleExpenses = createSampleExpenses(samplePeriod.getId());
        ArrayList<Transaction> sampleIncome = createSampleIncome(samplePeriod.getId());
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

    private ArrayList<Transaction> createSampleExpenses(long periodId) {
        ArrayList<Transaction> result = new ArrayList<>();

        return result;
    }

    private ArrayList<Transaction> createSampleIncome(long periodId) {
        ArrayList<Transaction> result = new ArrayList<>();

        return result;
    }


}
