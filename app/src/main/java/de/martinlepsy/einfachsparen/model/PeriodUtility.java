package de.martinlepsy.einfachsparen.model;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import de.martinlepsy.einfachsparen.data.DatabaseHelper;
import de.martinlepsy.einfachsparen.data.PeriodDbHelper;

/**
 * Created by martin on 25.04.16.
 */
public class PeriodUtility {

    private DatabaseHelper dbHelper;

    private PeriodDbHelper periodDbHelper;

    private Period currentPeriod;

    private Period mostRecentPeriod;

    public PeriodUtility(DatabaseHelper dbHelper) {
        this.dbHelper = dbHelper;
        periodDbHelper = new PeriodDbHelper(dbHelper);
        currentPeriod = periodDbHelper.getCurrentPeriod();
        mostRecentPeriod = periodDbHelper.getMostRecentPeriod();
    }

    public void savePeriodAndAssignDefaultValues(Period periodToSave) {
        PeriodWizard wizz = new PeriodWizard(periodToSave, dbHelper);
        wizz.savePeriodAndAssignDefaultTransactions();
        if (currentPeriodAvailable()) {
            setNewEndDateOfCurrentPeriod(new Date(periodToSave.getStart().getTime() - 1000));
        }
    }

    private void setNewEndDateOfCurrentPeriod(Date newEndDate) {
        currentPeriod.setEnd(newEndDate);
        periodDbHelper.updatePeriod(currentPeriod);
    }

    public boolean currentPeriodAvailable(){
        return currentPeriod != null;
    }

    public boolean mostRecentPeriodAvailable() {
        return mostRecentPeriod != null;
    }

    public Period getMostRecentPeriod() {
        return mostRecentPeriod;
    }

    public Date predictEndDateFromMostRecentPeriod(Date currentStartDate) {
        Date result = null;
        long diff = mostRecentPeriod.getEnd().getTime() - mostRecentPeriod.getStart().getTime();
        long days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentStartDate);
        cal.add(Calendar.DAY_OF_YEAR, (int) days);
        result = cal.getTime();
        return result;
    }



}
