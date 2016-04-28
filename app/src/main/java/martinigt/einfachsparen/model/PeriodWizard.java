package martinigt.einfachsparen.model;

import java.util.ArrayList;

import martinigt.einfachsparen.data.DatabaseHelper;
import martinigt.einfachsparen.data.PeriodDbHelper;
import martinigt.einfachsparen.data.TransactionDbHelper;

/**
 * Created by martin on 16.04.16.
 */
public class PeriodWizard {

    private Period periodToPrepare;

    private PeriodDbHelper periodDbHelper;

    private TransactionDbHelper transactionDbHelper;

    public PeriodWizard(Period newPeriod, DatabaseHelper dbHelper) {
        periodToPrepare = newPeriod;
        transactionDbHelper = new TransactionDbHelper(dbHelper);
        periodDbHelper = new PeriodDbHelper(dbHelper);
    }

    private void assignDefaultTransactions() {
        ArrayList<Transaction> allDefaultTransactions = transactionDbHelper.getAllDefaultTransactions();
        ArrayList<Transaction> transactionToAdd = new ArrayList<Transaction>();
        for (Transaction currentDefaultTransaction : allDefaultTransactions) {
            Transaction clonedTransaction = currentDefaultTransaction.cloneAsNonDefault();
            clonedTransaction.setPeriodId(periodToPrepare.getId());
            transactionToAdd.add(clonedTransaction);
        }
        transactionDbHelper.storeListOfTransactions(transactionToAdd);
    }

    private void savePeriod() {
        long newPeriodId = periodDbHelper.savePeriod(periodToPrepare);
        periodToPrepare.setId(newPeriodId);
    }

    public void savePeriodAndAssignDefaultTransactions() {
        savePeriod();
        assignDefaultTransactions();
    }

}
