package martinigt.einfachsparen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

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

public class DashboardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener  {

    DatabaseHelper dbHelper;

    Dashboard dashboard;

    TextView allCurrentExpensesDisplay;

    TextView plannedExpensesDisplay;

    TextView budgetDisplay;

    TextView budgetPerDayDisplay;

    private TextView daysRemainingDisplay;

    private TextView approximatedSavingsDisplay;

    private TextView plannedExpensesDisplayTitle;

    private TextView allCurrentExpensesDisplayTitle;

    private ListView expenseList;

    private FloatingActionButton addExpenseButton;

    private TransactionAdapter expenseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_with_nav);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        findReferencesToDisplayControls();

        bindListeners();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

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
        SharedPreferences sPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean hideExpenses = sPrefs.getBoolean(getString(R.string.pref_key_expensesOnDashboard), false);
        allCurrentExpensesDisplay.setText(String.format("%1.2f %s", dashboard.getCumulatedExpenses(), myCurrency.getSymbol()));
        budgetDisplay.setText(String.format("%1.2f %s", dashboard.getBudget(), myCurrency.getSymbol()));
        budgetPerDayDisplay.setText(String.format("%1.2f %s", dashboard.getBudgetPerDay(), myCurrency.getSymbol()));
        plannedExpensesDisplay.setText(String.format("%1.2f %s", dashboard.getPlannedExpenses(), myCurrency.getSymbol()));
        daysRemainingDisplay.setText(String.format("%s", dashboard.getDaysRemaining()));
        approximatedSavingsDisplay.setText(String.format("%1.2f %s", dashboard.getApproximatedSaving(), myCurrency.getSymbol()));
        if (hideExpenses) {
            allCurrentExpensesDisplay.setVisibility(View.GONE);
            allCurrentExpensesDisplayTitle.setVisibility(View.GONE);
            plannedExpensesDisplay.setVisibility(View.GONE);
            plannedExpensesDisplayTitle.setVisibility(View.GONE);
        }
    }

    private void findReferencesToDisplayControls() {
        expenseList = (ListView) findViewById(R.id.dashboardExpenseList);
        addExpenseButton = (FloatingActionButton) findViewById(R.id.fab);
        allCurrentExpensesDisplay = (TextView) findViewById(R.id.currentExpensesDisplay);
        allCurrentExpensesDisplayTitle = (TextView) findViewById(R.id.currentExpensesLabel);
        plannedExpensesDisplay = (TextView) findViewById(R.id.plannedExpensesDisplay);
        plannedExpensesDisplayTitle = (TextView) findViewById(R.id.plannedExpensesLabel);
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

        updateDisplay();
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
    public boolean onNavigationItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_newPeriod:
                Intent goToCreateNewPeriod = new Intent(getApplicationContext(),
                        CreatePeriodActivity.class);
                startActivity(goToCreateNewPeriod);
                break;
            case R.id.action_manageIncome:
                Intent goToIncomeList = new Intent(getApplicationContext(),
                        TransactionListActivity.class);
                goToIncomeList.putExtra(TransactionListActivity.TRANSACTION_TYPE_INTENT_EXTRA,
                        TransactionType.INCOME.ordinal());
                startActivity(goToIncomeList);
                break;
            case R.id.action_manageExpenses:
                Intent goToExpenseList = new Intent(getApplicationContext(),
                        TransactionListActivity.class);
                goToExpenseList.putExtra(TransactionListActivity.TRANSACTION_TYPE_INTENT_EXTRA,
                        TransactionType.EXPENSE.ordinal());
                startActivity(goToExpenseList);
                break;
            case R.id.action_showPeriods:
                Intent goToPeriodList = new Intent(getApplicationContext(),
                        PeriodListActivity.class);
                startActivity(goToPeriodList);
                break;
            case R.id.action_settings:
                Intent goToSettings = new Intent(getApplicationContext(),
                        SettingsActivity.class);
                startActivity(goToSettings);
                break;
            case R.id.action_tour:
                Intent goToTour = new Intent(getApplicationContext(),
                        TourActivity.class);
                startActivity(goToTour);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


}
