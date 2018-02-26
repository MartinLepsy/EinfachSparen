package de.martinlepsy.einfachsparen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.test.TouchUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;

import java.util.ArrayList;
import java.util.Currency;
import java.util.Locale;

import de.martinlepsy.einfachsparen.admin.AdminActivity;
import de.martinlepsy.einfachsparen.data.DatabaseHelper;
import de.martinlepsy.einfachsparen.data.PeriodDbHelper;
import de.martinlepsy.einfachsparen.data.TransactionAdapter;
import de.martinlepsy.einfachsparen.data.TransactionDbHelper;
import de.martinlepsy.einfachsparen.forms.CreateTransactionActivity;
import de.martinlepsy.einfachsparen.forms.EditTransactionActivity;
import de.martinlepsy.einfachsparen.helper.BudgetBurndownChartConverter;
import de.martinlepsy.einfachsparen.helper.BudgetBurndownChartFormatter;
import de.martinlepsy.einfachsparen.helper.Helper;
import de.martinlepsy.einfachsparen.helper.TourHelper;
import de.martinlepsy.einfachsparen.lists.PeriodListActivity;
import de.martinlepsy.einfachsparen.lists.TransactionListActivity;
import de.martinlepsy.einfachsparen.model.Dashboard;
import de.martinlepsy.einfachsparen.model.Period;
import de.martinlepsy.einfachsparen.model.Transaction;
import de.martinlepsy.einfachsparen.model.TransactionType;

public class DashboardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener  {

    DatabaseHelper dbHelper;

    Dashboard dashboard;

    TextView budgetDisplay;

    TextView budgetPerDayDisplay;

    private TextView remainingBudgetPerTodayDisplay;

    private LineChart budgetBurndownChart;

    private FloatingActionButton addExpenseButton;

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

        TourHelper tourHelper = new TourHelper(this);
        if (!tourHelper.isTourAlreadyTaken()) {
            tourHelper.askForTour();
        }
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
        dashboard.recalculate(transactionDbHelper);
        updateDisplay();
    }

    private void updateDisplay() {
        Locale currentLocale = Locale.getDefault();
        Currency myCurrency = Currency.getInstance((currentLocale));
        budgetDisplay.setText(String.format("%1.2f %s", dashboard.getBudget(), myCurrency.getSymbol()));
        budgetPerDayDisplay.setText(String.format("%1.2f %s", dashboard.getBudgetPerDay(), myCurrency.getSymbol()));
        remainingBudgetPerTodayDisplay.setText(String.format("%1.2f %s", dashboard.getDifferenceToPlannedExpense(), myCurrency.getSymbol()));
        formatRemainingBudgetPerToday();
        if(dashboard.hasPeriod()) {
            updateBurndownChart();
        }
    }

    private void updateBurndownChart() {
        budgetBurndownChart.setData(BudgetBurndownChartConverter.getChartDataFromDashboard(dashboard.getBudgetBurndownChartDataPoints()));
        BudgetBurndownChartFormatter.configureBurndownChart(budgetBurndownChart);
        budgetBurndownChart.invalidate();
    }

    private void formatRemainingBudgetPerToday() {
        if (dashboard.getDifferenceToPlannedExpense() < 0) {
            if (dashboard.getDifferenceToPlannedExpense() < (dashboard.getBudgetPerDay() * -1)) {
                remainingBudgetPerTodayDisplay.setTextColor(ContextCompat.getColor(getApplicationContext(),
                        R.color.colorCannotSave));
            }
            else {
                remainingBudgetPerTodayDisplay.setTextColor(ContextCompat.getColor(getApplicationContext(),
                        R.color.colorAccent));
            }
        }
    }

    private void findReferencesToDisplayControls() {
        addExpenseButton = (FloatingActionButton) findViewById(R.id.fab);
        budgetDisplay = (TextView) findViewById(R.id.budgetDisplay);
        budgetPerDayDisplay = (TextView) findViewById(R.id.budgetPerDayDisplay);
        remainingBudgetPerTodayDisplay = (TextView) findViewById(R.id.remainingBudgetPerTodayDisplay);
        budgetBurndownChart = (LineChart) findViewById(R.id.budgetBurndownChart);
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

        updateDisplay();
    }



    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_adminArea:
                Intent goToAdminArea = new Intent(getApplicationContext(), AdminActivity.class);
                startActivity(goToAdminArea);
                break;
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
                break;
            case R.id.action_about:
                Intent goToInfo = new Intent(getApplicationContext(),
                        AboutActivity.class);
                startActivity(goToInfo);
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
