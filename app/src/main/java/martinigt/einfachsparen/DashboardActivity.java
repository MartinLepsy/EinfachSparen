package martinigt.einfachsparen;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Currency;
import java.util.Locale;

import martinigt.einfachsparen.admin.AdminActivity;
import martinigt.einfachsparen.data.DatabaseHelper;
import martinigt.einfachsparen.data.PeriodDbHelper;
import martinigt.einfachsparen.data.TransactionAdapter;
import martinigt.einfachsparen.data.TransactionDbHelper;
import martinigt.einfachsparen.forms.CreateTransactionActivity;
import martinigt.einfachsparen.forms.EditTransactionActivity;
import martinigt.einfachsparen.helper.Helper;
import martinigt.einfachsparen.lists.PeriodListActivity;
import martinigt.einfachsparen.lists.TransactionListActivity;
import martinigt.einfachsparen.model.Dashboard;
import martinigt.einfachsparen.model.Period;
import martinigt.einfachsparen.model.Transaction;
import martinigt.einfachsparen.model.TransactionType;

public class DashboardActivity extends AppCompatActivity {

    DatabaseHelper dbHelper;

    Dashboard dashboard;

    TextView allCurrentExpensesDisplay;

    TextView plannedExpensesDisplay;

    TextView budgetDisplay;

    TextView budgetPerDayDisplay;

    private TextView daysRemainingDisplay;

    private TextView approximatedSavingsDisplay;

    private ListView expenseList;

    private FloatingActionButton addExpenseButton;

    private TransactionAdapter expenseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        findReferencesToDisplayControls();

        bindListeners();

    }

    private void bindListeners() {
        addExpenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToCreateExpenseActivity = new Intent(getApplicationContext(),
                        CreateTransactionActivity.class);
                goToCreateExpenseActivity.putExtra(CreateTransactionActivity.TRANSACTION_TYPE_INTENT_EXTRA,
                        TransactionType.EXPENSE.ordinal());
                startActivity(goToCreateExpenseActivity);
            }
        });

        expenseList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Transaction selectedExpense = expenseAdapter.getValues().get(position);
                Intent editIncomeIntent = new Intent(getApplicationContext(),
                        EditTransactionActivity.class);
                editIncomeIntent.putExtra("TransactionToEdit", selectedExpense);
                startActivity(editIncomeIntent);
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
                Intent goToPrefs = new Intent(getApplicationContext(),
                        SettingsActivity.class);
                startActivity(goToPrefs);
                return true;
            case R.id.action_manageIncome:
                Intent goToIncomeList = new Intent(getApplicationContext(),
                        TransactionListActivity.class);
                goToIncomeList.putExtra(TransactionListActivity.TRANSACTION_TYPE_INTENT_EXTRA,
                        TransactionType.INCOME.ordinal());
                startActivity(goToIncomeList);
                return true;
            case R.id.action_manageExpenses:
                Intent goToExpenseList = new Intent(getApplicationContext(),
                        TransactionListActivity.class);
                goToExpenseList.putExtra(TransactionListActivity.TRANSACTION_TYPE_INTENT_EXTRA,
                        TransactionType.EXPENSE.ordinal());
                startActivity(goToExpenseList);
                return true;
            case R.id.action_showPeriods:
                Intent goToPeriodList = new Intent(getApplicationContext(),
                        PeriodListActivity.class);
                startActivity(goToPeriodList);
                return true;
            case R.id.action_adminArea:
                Intent goToAdminArea = new Intent(getApplicationContext(),
                        AdminActivity.class);
                startActivity(goToAdminArea);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void loadCurrentPeriodOrShowNotAvailableDialog() {
        PeriodDbHelper periodHelper = new PeriodDbHelper(dbHelper);
        TransactionDbHelper transactionDbHelper = new TransactionDbHelper(dbHelper);

        Period currentPeriod = periodHelper.getCurrentPeriod();

        if (currentPeriod == null) {
            showNoPeriodFoundDialog();
        }
        else {
            dashboard.setPeriod(currentPeriod);
            dashboard.setPeriodExpenses(transactionDbHelper.getAllExpensesForPeriod(currentPeriod.getId()));
            dashboard.setPeriodIncome(transactionDbHelper.getAllIncomesForPeriod(currentPeriod.getId()));
        }
        dashboard.recalculate();
        updateDisplay();
    }

    private void updateDisplay() {
        Locale currentLocale = Locale.getDefault();
        Currency myCurrency = Currency.getInstance((currentLocale));
        allCurrentExpensesDisplay.setText(String.format("%1.2f %s", dashboard.getCumulatedExpenses(), myCurrency.getSymbol()));
        budgetDisplay.setText(String.format("%1.2f %s", dashboard.getBudget(), myCurrency.getSymbol()));
        budgetPerDayDisplay.setText(String.format("%1.2f %s", dashboard.getBudgetPerDay(), myCurrency.getSymbol()));
        plannedExpensesDisplay.setText(String.format("%1.2f %s", dashboard.getPlannedExpenses(), myCurrency.getSymbol()));
        daysRemainingDisplay.setText(String.format("%s", dashboard.getDaysRemaining()));
        approximatedSavingsDisplay.setText(String.format("%1.2f %s", dashboard.getApproximatedSaving(), myCurrency.getSymbol()));
    }

    private void findReferencesToDisplayControls() {
        expenseList = (ListView) findViewById(R.id.dashboardExpenseList);
        addExpenseButton = (FloatingActionButton) findViewById(R.id.fab);
        allCurrentExpensesDisplay = (TextView) findViewById(R.id.currentExpensesDisplay);
        plannedExpensesDisplay = (TextView) findViewById(R.id.plannedExpensesDisplay);
        budgetDisplay = (TextView) findViewById(R.id.budgetDisplay);
        budgetPerDayDisplay = (TextView) findViewById(R.id.budgetPerDayDisplay);
        daysRemainingDisplay = (TextView) findViewById(R.id.remainingDaysDisplay);
        approximatedSavingsDisplay = (TextView) findViewById(R.id.approximatedSavingsDisplay);
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

        refreshExpenseList();
    }

    private void refreshExpenseList() {
        TransactionDbHelper transactionDbHelper = new TransactionDbHelper(dbHelper);
        PeriodDbHelper periodDbHelper = new PeriodDbHelper(dbHelper);

        Period currentPeriod = periodDbHelper.getCurrentPeriod();
        if (currentPeriod != null) {
            ArrayList<Transaction> expenses = transactionDbHelper.getAllExpensesForPeriod(currentPeriod.getId(), true);
            expenseAdapter = new TransactionAdapter(this, Helper.castToTransactionList(expenses));
            expenseList.setAdapter(expenseAdapter);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);
        return true;
    }

}
