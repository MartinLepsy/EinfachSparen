package martinigt.einfachsparen;

import org.junit.Test;

import java.util.Date;

import martinigt.einfachsparen.model.Expense;
import martinigt.einfachsparen.model.Income;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by martin on 16.04.16.
 */
public class TransactionUnitTest {

    @Test
    public void expenseClonedCorrectly() throws Exception {

        Expense defaultExpoense = new Expense(1, 1, 755, "Miete", "", new Date(), true);
        Expense clonedExpense = defaultExpoense.cloneAsNonDefault();

        assertEquals(defaultExpoense.getValue(), clonedExpense.getValue(), 0.0);
        assertEquals(defaultExpoense.getName(), clonedExpense.getName());
        assertEquals(defaultExpoense.getTag(), clonedExpense.getTag());
        assertNotEquals(defaultExpoense.isStandard(), clonedExpense.isStandard());
    }

    @Test
    public void incomeClonedCorrectly() throws Exception {

        Income defaultIncome = new Income(1, 1, 1500, "Lohn", "", new Date(), true);
        Income clonedIncome = defaultIncome.cloneAsNonDefault();

        assertEquals(defaultIncome.getValue(), clonedIncome.getValue(), 0.0);
        assertEquals(defaultIncome.getName(), clonedIncome.getName());
        assertEquals(defaultIncome.getTag(), clonedIncome.getTag());
        assertNotEquals(defaultIncome.isStandard(), clonedIncome.isStandard());
    }
}
