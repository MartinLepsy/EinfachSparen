package martinigt.einfachsparen.model;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import martinigt.einfachsparen.helper.Helper;

/**
 * Created by martin on 16.04.16.
 */
public class Dashboard {

    private Period currentPeriod;

    private ArrayList<Expense> periodExpenses;

    private ArrayList<Income> periodIncome;

    private  double budget;

    private  double budgetPerDay;

    private double cumulatedExpenses;

    private double cumulatedIncome;

    private double plannedExpenses;

    public Dashboard() {

    }

    public void setPeriod(Period periode) {
        currentPeriod = periode;
    }

    public void setPeriodExpenses(ArrayList<Expense> periodExpenses) {
        this.periodExpenses = periodExpenses;
    }

    public void setPeriodIncome(ArrayList<Income> periodIncome) {
        this.periodIncome = periodIncome;
    }

    public void recalculate() {
        cumulatedExpenses = 0;
        cumulatedIncome = 0;
        if (currentPeriod != null) {
            if (periodExpenses != null) {
                for (Expense currentExpense : periodExpenses) {
                    cumulatedExpenses += currentExpense.getValue();
                }
            }
            if (periodIncome != null) {
                for (Income aktuelleEinnahme : periodIncome) {
                    cumulatedIncome += aktuelleEinnahme.getValue();
                }
            }
            budget = cumulatedIncome - cumulatedExpenses - currentPeriod.getPlannedSaving();
            plannedExpenses = cumulatedIncome - currentPeriod.getPlannedSaving();
            calculateDailyBudget();
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
            int differenzTage = (int) Helper.getDateDiff(currentPeriod.getStart(), currentPeriod.getEnd(),
                    TimeUnit.DAYS);
            if (differenzTage >= 1) {
                budgetPerDay = budget / differenzTage;
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



}
