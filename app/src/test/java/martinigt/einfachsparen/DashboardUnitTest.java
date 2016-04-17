package martinigt.einfachsparen;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;

import martinigt.einfachsparen.model.Expense;
import martinigt.einfachsparen.model.Dashboard;
import martinigt.einfachsparen.model.Income;
import martinigt.einfachsparen.model.Period;

import static org.junit.Assert.assertEquals;

/**
 * Created by martin on 16.04.16.
 */
public class DashboardUnitTest {

    private Period testPeriode;

    private ArrayList<Expense> ausgabenTestPeriode;

    private ArrayList<Income> einnahmenTestPeriode;

    private  Dashboard d;

    public  DashboardUnitTest() {
        testPeriode = new Period();
        testPeriode.setStart(new Date());
        testPeriode.setEnd(new Date(testPeriode.getStart().getTime() + 1000 * 60 * 60 * 24 * 10));
        testPeriode.setPlannedSaving(100);

        ausgabenTestPeriode = new ArrayList<Expense>();
        ausgabenTestPeriode.add(new Expense(1, 1, 755, "Miete", "", new Date(), true));
        ausgabenTestPeriode.add(new Expense(2, 1, 85, "Strom", "", new Date(), true));

        einnahmenTestPeriode = new ArrayList<Income>();
        einnahmenTestPeriode.add(new Income(1, 1, 2300, "Lohn", "", new Date(), true));

        d = new Dashboard();
        d.setPeriod(testPeriode);
        d.setPeriodExpenses(ausgabenTestPeriode);
        d.setPeriodIncome(einnahmenTestPeriode);
        d.recalculate();
    }

    @Test
    public void budgetBerechnungKorrekt() throws Exception {

        double expectedBudget = 2300 - 755 - 85 - 100;

        assertEquals(expectedBudget, d.getBudget(), 0.0);
    }

    @Test
    public void budgetBerechnungProTagKorrekt() throws Exception {
        double expectedResult = (2300 - 755 - 85 - 100) / 10;
        assertEquals(expectedResult, d.getBudgetPerDay(), 0.0);
    }

    @Test
    public void sumOfExpensesCorrect() throws Exception {
        double expectedResult = 755 + 85;
        assertEquals(expectedResult, d.getCumulatedExpenses(), 0.0);
    }

    @Test
    public void nullPeriodDashboardTest() throws Exception {
        Dashboard sut = new Dashboard();
        sut.setPeriod(null);
        sut.recalculate();

        assertEquals(0, sut.getBudget(), 0.0);
        assertEquals(0, sut.getBudgetPerDay(), 0.0);
        assertEquals(0, sut.getCumulatedExpenses(), 0.0);
    }



}
