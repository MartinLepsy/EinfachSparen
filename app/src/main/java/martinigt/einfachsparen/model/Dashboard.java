package martinigt.einfachsparen.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import martinigt.einfachsparen.helper.Helper;

/**
 * Created by martin on 16.04.16.
 */
public class Dashboard {

    private Period currentPeriod;

    private ArrayList<Transaction> periodExpenses;

    private ArrayList<Transaction> periodIncome;

    private  double budget;

    private  double budgetPerDay;

    private double cumulatedExpenses;

    private double cumulatedIncome;

    private double plannedExpenses;

    private int daysRemaining;

    private double approximatedSaving;

    private Date dateToCalculateFrom;

    public Dashboard() {
        setDateToCalculateFrom(new Date());
    }

    public void setPeriod(Period periode) {
        currentPeriod = periode;
    }

    public void setPeriodExpenses(ArrayList<Transaction> periodExpenses) {
        this.periodExpenses = periodExpenses;
    }

    public void setPeriodIncome(ArrayList<Transaction> periodIncome) {
        this.periodIncome = periodIncome;
    }

    public void recalculate() {
        cumulatedExpenses = 0;
        cumulatedIncome = 0;
        if (currentPeriod != null) {
            daysRemaining = (int) Helper.getDateDiff(dateToCalculateFrom, currentPeriod.getEnd(),
                    TimeUnit.DAYS);
            if (periodExpenses != null) {
                for (Transaction currentExpense : periodExpenses) {
                    cumulatedExpenses += currentExpense.getValue();
                }
            }
            if (periodIncome != null) {
                for (Transaction currentIncome : periodIncome) {
                    cumulatedIncome += currentIncome.getValue();
                }
            }
            budget = cumulatedIncome - cumulatedExpenses - currentPeriod.getPlannedSaving();
            plannedExpenses = cumulatedIncome - currentPeriod.getPlannedSaving();
            calculateDailyBudget();
            approximatedSaving = currentPeriod.getPlannedSaving() + budget;
        }
        else {
            budgetPerDay = 0;
            budget = 0;
            plannedExpenses = 0;
        }

    }

    private void calculateDailyBudget() {

        if (currentPeriod.getEnd() == null) {
            budgetPerDay = -1;
        }
        else {
            if (daysRemaining >= 1) {
                budgetPerDay = budget / daysRemaining;
            }
            else {
                budgetPerDay = -1;
            }
        }
    }



    public double getBudget() {
        return budget;
    }

    public double getBudgetPerDay() {
        return budgetPerDay;
    }

    public double getCumulatedExpenses() {
        return cumulatedExpenses;
    }

    public double getPlannedExpenses() { return  plannedExpenses; }

    public Date getDateToCalculateFrom() {
        return dateToCalculateFrom;
    }

    public void setDateToCalculateFrom(Date dateToCalculateFrom) {
        this.dateToCalculateFrom = dateToCalculateFrom;
    }

    public int getDaysRemaining() {
        return daysRemaining;
    }

    public void setDaysRemaining(int daysRemaining) {
        this.daysRemaining = daysRemaining;
    }

    public double getApproximatedSaving() {
        return approximatedSaving;
    }

    public void setApproximatedSaving(double approximatedSaving) {
        this.approximatedSaving = approximatedSaving;
    }
}
