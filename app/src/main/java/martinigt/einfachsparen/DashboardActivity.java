package martinigt.einfachsparen;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import martinigt.einfachsparen.data.DatabaseHelper;
import martinigt.einfachsparen.data.ExpenseDbHelper;
import martinigt.einfachsparen.data.IncomeDbHelper;
import martinigt.einfachsparen.data.PeriodDbHelper;
import martinigt.einfachsparen.model.Dashboard;
import martinigt.einfachsparen.model.Period;

public class DashboardActivity extends AppCompatActivity {

    DatabaseHelper dbHelper;

    Dashboard dashboard;

    TextView allCurrentExpensesDisplay;

    TextView plannedExpensesDisplay;

    TextView budgetDisplay;

    TextView budgetPerDayDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bindListeners();

        findReferencesToDisplayControls();
    }

    private void bindListeners() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.addExpenseButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_newPeriod:
                Intent goToCreateNewPeriod = new Intent(getApplicationContext(),
                        CreatePeriodActivity.class);
                startActivity(goToCreateNewPeriod);
                return true;
            case R.id.action_settings:
                //showHelp();
                return true;
            case R.id.action_manageIncome:

                return true;
            case R.id.action_manageExpenses:

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void loadCurrentPeriodOrShowNotAvailableDialog() {
        PeriodDbHelper periodHelper = new PeriodDbHelper(dbHelper);
        ExpenseDbHelper expenseHelper = new ExpenseDbHelper(dbHelper);
        IncomeDbHelper incomeHelper = new IncomeDbHelper(dbHelper);

        Period currentPeriod = periodHelper.getCurrentPeriod();

        if (currentPeriod == null) {
            showNoPeriodFoundDialog();
        }
        else {
            dashboard.setPeriod(currentPeriod);
            dashboard.setPeriodExpenses(expenseHelper.getAllExpensesForPeriod(currentPeriod.getId()));
            dashboard.setPeriodIncome(incomeHelper.getAllExpensesForPeriod(currentPeriod.getId()));
        }
        dashboard.recalculate();
        updateDisplay();
    }

    private void updateDisplay() {
        allCurrentExpensesDisplay.setText("" + dashboard.getCumulatedExpenses());
        budgetDisplay.setText( ""+  dashboard.getBudget());
        budgetPerDayDisplay.setText("" + dashboard.getBudgetPerDay());
        plannedExpensesDisplay.setText("" + dashboard.getPlannedExpenses());
    }

    private void findReferencesToDisplayControls() {
        allCurrentExpensesDisplay = (TextView) findViewById(R.id.currentExpensesDisplay);
        plannedExpensesDisplay = (TextView) findViewById(R.id.plannedExpensesDisplay);
        budgetDisplay = (TextView) findViewById(R.id.budgetDisplay);
        budgetPerDayDisplay = (TextView) findViewById(R.id.budgetPerDayDisplay);
    }

    private void showNoPeriodFoundDialog() {
        FragmentManager fm = getSupportFragmentManager();
        NoCurrentPeriodFoundDialogFragment noPeriodDialogFragment =
                NoCurrentPeriodFoundDialogFragment.newInstance(
                        getString(R.string.noCurrentPeriodFoundTitle));
        noPeriodDialogFragment.show(fm, "fragment_alert");
    }

    @Override
    public void onResume() {
        super.onResume();
        dbHelper = new DatabaseHelper(this.getApplicationContext());
        dashboard = new Dashboard();

        loadCurrentPeriodOrShowNotAvailableDialog();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);
        return true;
    }

}
