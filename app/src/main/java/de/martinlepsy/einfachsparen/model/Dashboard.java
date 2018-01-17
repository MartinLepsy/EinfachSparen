package de.martinlepsy.einfachsparen.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import de.martinlepsy.einfachsparen.helper.Helper;

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

    private double differenceToPlannedExpense;

    private double plannedExpenses;

    private double sumOfRecurringExpenses;

    private double approximatedSaving;

    private double linearExpensePerDay;

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
        sumOfRecurringExpenses = 0;
        if (currentPeriod != null) {
            calculateData();
            calculateChartData();
        }
        else {
            budgetPerDay = 0;
            budget = 0;
            plannedExpenses = 0;
            setDifferenceToPlannedExpense(0);
        }
    }

    private void calculateData() {
        calculateCumulatedAndRecurringExpenses();
        calculateCumulatedIncome();
        budget = cumulatedIncome - cumulatedExpenses - currentPeriod.getPlannedSaving();
        plannedExpenses = cumulatedIncome - currentPeriod.getPlannedSaving();
        calculateDailyBudget();
        calculateDifferenceToPlannedBudget();
        approximatedSaving = currentPeriod.getPlannedSaving() + budget;
    }

    private void calculateChartData() {
        Calendar start = Calendar.getInstance();
        start.setTime(currentPeriod.getStart());
        Calendar end = Calendar.getInstance();
        end.setTime(currentPeriod.getEnd());
        for (Date date = start.getTime(); start.before(end); start.add(Calendar.DATE, 1), date = start.getTime()) {
            DashboardChartDataPoint currentDataPoint = new DashboardChartDataPoint();
            currentDataPoint.setDate(date);
            currentDataPoint.setPlannedBudget((float)(cumulatedIncome - sumOfRecurringExpenses) -
                    ((float)currentPeriod.getRemainingDays(date) * (float)linearExpensePerDay));
        }
    }

    private void calculateCumulatedIncome() {
        if (periodIncome != null) {
            for (Transaction currentIncome : periodIncome) {
                cumulatedIncome += currentIncome.getValue();
            }
        }
    }

    private void calculateCumulatedAndRecurringExpenses() {
        if (periodExpenses != null) {
            for (Transaction currentExpense : periodExpenses) {
                cumulatedExpenses += currentExpense.getValue();
                if (currentExpense.isStandard() || currentExpense.getFromStandardId() > 0) {
                    sumOfRecurringExpenses += currentExpense.getValue();
                }
            }
        }
    }

    private void calculateDifferenceToPlannedBudget() {
        double startingPoint = cumulatedIncome - sumOfRecurringExpenses;
        double differenceToPlannedSaving = startingPoint - currentPeriod.getPlannedSaving();
        if (currentPeriod.getPeriodDays() > 0) {
            linearExpensePerDay = differenceToPlannedSaving / (double) currentPeriod.getPeriodDays();
            double optimalExpensesPerToday = startingPoint - ((double)(currentPeriod.getPeriodDays() - currentPeriod.getRemainingDays(dateToCalculateFrom)))
                    * linearExpensePerDay;
            setDifferenceToPlannedExpense(cumulatedIncome - cumulatedExpenses - optimalExpensesPerToday);
        }
    }

    private void calculateDailyBudget() {

        if (currentPeriod.getEnd() == null) {
            budgetPerDay = -1;
        }
        else {
            if (currentPeriod.getRemainingDays(dateToCalculateFrom) >= 1) {
                budgetPerDay = budget / currentPeriod.getRemainingDays(dateToCalculateFrom);
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

    public double getApproximatedSaving() {
        return approximatedSaving;
    }

    public void setApproximatedSaving(double approximatedSaving) {
        this.approximatedSaving = approximatedSaving;
    }

    public double getDifferenceToPlannedExpense() {
        return differenceToPlannedExpense;
    }

    public void setDifferenceToPlannedExpense(double differenceToPlannedExpense) {
        this.differenceToPlannedExpense = differenceToPlannedExpense;
    }

    public int getDaysRemaining() {
        return currentPeriod != null ? currentPeriod.getRemainingDays(dateToCalculateFrom) : 0;
    }
}
