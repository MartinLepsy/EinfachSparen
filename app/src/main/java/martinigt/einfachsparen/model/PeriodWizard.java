package martinigt.einfachsparen.model;

import java.util.ArrayList;

import martinigt.einfachsparen.data.DatabaseHelper;
import martinigt.einfachsparen.data.ExpenseDbHelper;
import martinigt.einfachsparen.data.IncomeDbHelper;
import martinigt.einfachsparen.data.PeriodDbHelper;

/**
 * Created by martin on 16.04.16.
 */
public class PeriodWizard {

    private Period periodToPrepare;

    private IncomeDbHelper incomeDbHelper;

    private ExpenseDbHelper expenseDbHelper;

    private PeriodDbHelper periodDbHelper;

    public PeriodWizard(Period newPeriod, DatabaseHelper dbHelper) {
        periodToPrepare = newPeriod;
        incomeDbHelper = new IncomeDbHelper(dbHelper);
        expenseDbHelper = new ExpenseDbHelper(dbHelper);
        periodDbHelper = new PeriodDbHelper(dbHelper);
    }

    private void assignDefaultIncomeAndExpenses() {
        assignDefaultIncome();
        assignDefaultExpenses();
    }

    private void assignDefaultExpenses() {
        ArrayList<Expense> allDefaultIncomes = expenseDbHelper.getAllDefaultExpenses();
        ArrayList<Expense> expensesToAdd = new ArrayList<Expense>();
        for (Expense currentDefaultExpense : allDefaultIncomes) {
            Expense clonedExpense = currentDefaultExpense.cloneAsNonDefault();
            clonedExpense.setPeriodId(periodToPrepare.getId());
            expensesToAdd.add(clonedExpense);
        }
        expenseDbHelper.storeListOfExpenses(expensesToAdd);
    }

    private void assignDefaultIncome() {
        ArrayList<Income> allDefaultIncomes = incomeDbHelper.getAllDefaultIncomes();
        ArrayList<Income> incomesToAdd = new ArrayList<Income>();
        for (Income currentDefaultIncome : allDefaultIncomes) {
            Income clonedIncome = currentDefaultIncome.cloneAsNonDefault();
            clonedIncome.setPeriodId(periodToPrepare.getId());
            incomesToAdd.add(clonedIncome);
        }
        incomeDbHelper.storeListOfIncomes(incomesToAdd);
    }

    private void savePeriod() {
        long newPeriodId = periodDbHelper.savePeriod(periodToPrepare);
        periodToPrepare.setId(newPeriodId);
    }

    public void savePeriodAndAssignDefaultTransactions() {
        savePeriod();
        assignDefaultIncomeAndExpenses();
    }

}
