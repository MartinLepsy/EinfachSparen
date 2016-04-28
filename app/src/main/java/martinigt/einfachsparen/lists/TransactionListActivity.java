package martinigt.einfachsparen.lists;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import martinigt.einfachsparen.R;
import martinigt.einfachsparen.data.DatabaseHelper;
import martinigt.einfachsparen.data.PeriodDbHelper;
import martinigt.einfachsparen.data.TransactionAdapter;
import martinigt.einfachsparen.data.TransactionDbHelper;
import martinigt.einfachsparen.forms.CreateTransactionActivity;
import martinigt.einfachsparen.forms.EditTransactionActivity;
import martinigt.einfachsparen.helper.Helper;
import martinigt.einfachsparen.model.Period;
import martinigt.einfachsparen.model.Transaction;
import martinigt.einfachsparen.model.TransactionType;

public class TransactionListActivity extends AppCompatActivity {

    private ListView transactionListView;

    private FloatingActionButton addIncomeButton;

    private TransactionAdapter transactionAdapter;

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
        }
        else {
            transactions = transactionDbHelper.getAllIncomesForPeriod(currentPeriod.getId());
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
