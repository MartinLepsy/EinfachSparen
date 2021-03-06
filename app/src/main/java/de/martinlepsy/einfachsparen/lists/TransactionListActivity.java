package de.martinlepsy.einfachsparen.lists;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import de.martinlepsy.einfachsparen.R;
import de.martinlepsy.einfachsparen.data.DatabaseHelper;
import de.martinlepsy.einfachsparen.data.PeriodDbHelper;
import de.martinlepsy.einfachsparen.data.TransactionAdapter;
import de.martinlepsy.einfachsparen.data.TransactionDbHelper;
import de.martinlepsy.einfachsparen.forms.CreateTransactionActivity;
import de.martinlepsy.einfachsparen.forms.EditTransactionActivity;
import de.martinlepsy.einfachsparen.helper.Helper;
import de.martinlepsy.einfachsparen.model.Period;
import de.martinlepsy.einfachsparen.model.Transaction;
import de.martinlepsy.einfachsparen.model.TransactionType;

public class TransactionListActivity extends AppCompatActivity {

    private ListView transactionListView;

    private FloatingActionButton addIncomeButton;

    private TransactionAdapter transactionAdapter;

    private ImageView spyView;

    private TextView incomeListHiddenHint;

    private DatabaseHelper dbHelper;

    private TransactionType transactionListType;

    public static final String TRANSACTION_TYPE_INTENT_EXTRA = "TransactionType";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent sourceIntent = getIntent();

        transactionListType = TransactionType.values()[sourceIntent.getIntExtra(TRANSACTION_TYPE_INTENT_EXTRA,
                TransactionType.INCOME.ordinal())];
        int titleId = transactionListType == TransactionType.EXPENSE ? R.string.title_expense_list :
                R.string.title_income_list;
        setTitle(titleId);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getReferencesToWidgets();

        dbHelper = new DatabaseHelper(this.getApplicationContext());

        bindListeners();

        refreshData();
    }

    private void getReferencesToWidgets() {
        addIncomeButton = (FloatingActionButton) findViewById(R.id.fab);
        transactionListView = (ListView) findViewById(R.id.transactionList);
        spyView = (ImageView) findViewById(R.id.spyIcon);
        incomeListHiddenHint = (TextView) findViewById(R.id.incomeListHiddenHint);
    }

    private void bindListeners() {
        addIncomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToCreateTransaction = new Intent(getApplicationContext(),
                        CreateTransactionActivity.class);
                goToCreateTransaction.putExtra(CreateTransactionActivity.TRANSACTION_TYPE_INTENT_EXTRA,
                        transactionListType.ordinal());
                startActivity(goToCreateTransaction);
            }
        });

        transactionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Transaction selectedTransaction = transactionAdapter.getValues().get(position);
                Intent editTransactionIntent = new Intent(getApplicationContext(),
                        EditTransactionActivity.class);
                editTransactionIntent.putExtra("TransactionToEdit", selectedTransaction);
                startActivity(editTransactionIntent);
            }
        });
    }

    private void refreshData() {
        TransactionDbHelper transactionDbHelper = new TransactionDbHelper(dbHelper);
        PeriodDbHelper periodDbHelper = new PeriodDbHelper(dbHelper);

        Period currentPeriod = periodDbHelper.getCurrentPeriod();

        ArrayList<Transaction> transactions = null;
        if (transactionListType == TransactionType.EXPENSE) {
            transactions = transactionDbHelper.getAllExpensesForPeriod(currentPeriod.getId());
            spyView.setVisibility(View.GONE);
            incomeListHiddenHint.setVisibility(View.GONE);
        }
        else {
            SharedPreferences sPrefs = PreferenceManager.getDefaultSharedPreferences(this);
            boolean showIncome= sPrefs.getBoolean(getString(R.string.pref_key_showIncomeList), true);
            if (showIncome) {
                transactions = transactionDbHelper.getAllIncomesForPeriod(currentPeriod.getId());
                spyView.setVisibility(View.GONE);
                incomeListHiddenHint.setVisibility(View.GONE);
            }
            else {
                transactions = new ArrayList<>();
            }
        }
        if (transactions != null) {
            transactionAdapter = new TransactionAdapter(this, Helper.castToTransactionList(transactions));
            transactionListView.setAdapter(transactionAdapter);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        refreshData();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
