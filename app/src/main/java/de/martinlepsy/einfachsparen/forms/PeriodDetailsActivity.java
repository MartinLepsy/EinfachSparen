package de.martinlepsy.einfachsparen.forms;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.ArrayList;

import de.martinlepsy.einfachsparen.R;
import de.martinlepsy.einfachsparen.data.DatabaseHelper;
import de.martinlepsy.einfachsparen.data.PeriodDbHelper;
import de.martinlepsy.einfachsparen.data.TransactionAdapter;
import de.martinlepsy.einfachsparen.data.TransactionDbHelper;
import de.martinlepsy.einfachsparen.model.Period;
import de.martinlepsy.einfachsparen.model.Transaction;

public class PeriodDetailsActivity extends AppCompatActivity implements DialogInterface.OnClickListener {

    private Period periodToShow;

    private ListView incomeList;

    private ListView expenseList;

    private ImageView spyView;

    private TextView incomeListHiddenHint;

    private TextView periodNameTextView;

    private TextView periodDurationTextView;

    private DatabaseHelper dbHelper;

    private DateFormat df;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_period_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        df = DateFormat.getDateInstance();

        dbHelper = new DatabaseHelper(this);

        getReferencesToWidgets();

    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent sourceIntent = getIntent();
        Period periodFromIntent = (Period) sourceIntent.getSerializableExtra("PeriodToShow");

        if(periodFromIntent != null) {
            DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
            PeriodDbHelper periodDbHelper = new PeriodDbHelper(dbHelper);
            this.periodToShow = periodDbHelper.getPeriod(periodFromIntent.getId());
            showData();
        }
    }

    private void showData() {
        periodNameTextView.setText(periodToShow.getName());
        periodDurationTextView.setText(df.format(periodToShow.getStart().getTime()) +
                " - " + df.format(periodToShow.getEnd().getTime()));
        SharedPreferences sPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean showIncome= sPrefs.getBoolean(getString(R.string.pref_key_showIncomeList), true);
        TransactionDbHelper transactionDbHelper = new TransactionDbHelper(dbHelper);
        ArrayList<Transaction> expenses = transactionDbHelper.getAllExpensesForPeriod(periodToShow.getId());
        TransactionAdapter expenseAdapter = new TransactionAdapter(this, expenses);
        expenseList.setAdapter(expenseAdapter);
        if (showIncome) {
            ArrayList<Transaction> incomes = transactionDbHelper.getAllIncomesForPeriod(periodToShow.getId());
            TransactionAdapter incomeAdapter = new TransactionAdapter(this, incomes);
            incomeList.setAdapter(incomeAdapter);
            spyView.setVisibility(View.GONE);
            incomeListHiddenHint.setVisibility(View.GONE);
        }
        else {
            incomeList.setVisibility(View.GONE);
        }
    }

    public void getReferencesToWidgets() {
        incomeList = (ListView) findViewById(R.id.periodDetailsIncomeList);
        expenseList = (ListView) findViewById(R.id.periodDetailsExpensesList);
        periodNameTextView = (TextView) findViewById(R.id.periodDetailsName);
        periodDurationTextView = (TextView) findViewById(R.id.periodDetailsTime);
        spyView = (ImageView) findViewById(R.id.spyIcon);
        incomeListHiddenHint = (TextView) findViewById(R.id.incomeListHiddenHint);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_deletePeriod:
                askDeletionConfirmation();
                return true;
            case R.id.action_editPeriod:
                editDisplayedPeriod();
                return true;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void editDisplayedPeriod() {
        Intent openEditPeriodIntent = new Intent(getApplicationContext(), EditPeriodActivity.class);
        openEditPeriodIntent.putExtra(EditPeriodActivity.PERIOD_TO_EDIT_EXTRA_NAME, periodToShow);
        startActivity(openEditPeriodIntent);
    }

    private void askDeletionConfirmation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.confirmQuestion).setPositiveButton(R.string.confirmYes, this)
                .setNegativeButton(R.string.confirmNo, this).show();
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int which) {
        switch (which){
            case DialogInterface.BUTTON_POSITIVE:
                deletePeriod();
                break;
            case DialogInterface.BUTTON_NEGATIVE:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_perioddetails, menu);
        return true;
    }

    private void deletePeriod() {
        PeriodDbHelper periodDbHelper = new PeriodDbHelper(dbHelper);
        TransactionDbHelper transactionDbHelper = new TransactionDbHelper(dbHelper);

        transactionDbHelper.deleteAllTransactionsForPeriod(periodToShow);
        periodDbHelper.deletePeriod(periodToShow);
        finish();
    }
}
