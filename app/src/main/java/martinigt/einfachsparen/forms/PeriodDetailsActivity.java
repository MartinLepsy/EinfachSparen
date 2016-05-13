package martinigt.einfachsparen.forms;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ListViewCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.ArrayList;

import martinigt.einfachsparen.R;
import martinigt.einfachsparen.data.DatabaseHelper;
import martinigt.einfachsparen.data.PeriodDbHelper;
import martinigt.einfachsparen.data.TransactionAdapter;
import martinigt.einfachsparen.data.TransactionDbHelper;
import martinigt.einfachsparen.model.Period;
import martinigt.einfachsparen.model.Transaction;

public class PeriodDetailsActivity extends AppCompatActivity implements DialogInterface.OnClickListener {

    private Period periodToShow;

    private ListView incomeList;

    private ListView expenseList;

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

        Intent sourceIntent = getIntent();
        periodToShow = (Period) sourceIntent.getSerializableExtra("PeriodToShow");

        showData();

    }

    private void showData() {
        periodNameTextView.setText(periodToShow.getName());
        periodDurationTextView.setText(df.format(periodToShow.getStart().getTime()) +
                " - " + df.format(periodToShow.getEnd().getTime()));
        TransactionDbHelper transactionDbHelper = new TransactionDbHelper(dbHelper);
        ArrayList<Transaction> incomes = transactionDbHelper.getAllIncomesForPeriod(periodToShow.getId());
        TransactionAdapter incomeAdapter = new TransactionAdapter(this, incomes);
        ArrayList<Transaction> expenses = transactionDbHelper.getAllExpensesForPeriod(periodToShow.getId());
        TransactionAdapter expenseAdapter = new TransactionAdapter(this, expenses);
        incomeList.setAdapter(incomeAdapter);
        expenseList.setAdapter(expenseAdapter);
    }

    public void getReferencesToWidgets() {
        incomeList = (ListView) findViewById(R.id.periodDetailsIncomeList);
        expenseList = (ListView) findViewById(R.id.periodDetailsExpensesList);
        periodNameTextView = (TextView) findViewById(R.id.periodDetailsName);
        periodDurationTextView = (TextView) findViewById(R.id.periodDetailsTime);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_deletePeriod:
                askDeletionConfirmation();
                return true;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
