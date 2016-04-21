package martinigt.einfachsparen.lists;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import martinigt.einfachsparen.R;
import martinigt.einfachsparen.data.DatabaseHelper;
import martinigt.einfachsparen.data.ExpenseDbHelper;
import martinigt.einfachsparen.data.IncomeDbHelper;
import martinigt.einfachsparen.data.PeriodDbHelper;
import martinigt.einfachsparen.data.TransactionAdapter;
import martinigt.einfachsparen.forms.CreateExpenseActivity;
import martinigt.einfachsparen.forms.CreateIncomeActivity;
import martinigt.einfachsparen.forms.EditTransactionActivity;
import martinigt.einfachsparen.helper.Helper;
import martinigt.einfachsparen.model.Expense;
import martinigt.einfachsparen.model.Period;
import martinigt.einfachsparen.model.Transaction;

public class ExpenseListActivity extends AppCompatActivity {

    private ListView expenseListView;

    private FloatingActionButton addExpenseButton;

    private TransactionAdapter expenseAdapter;

    private DatabaseHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dbHelper = new DatabaseHelper(this.getApplicationContext());

        getReferencesToWidgets();

        bindListeners();

        refreshData();
    }

    private void getReferencesToWidgets() {
        addExpenseButton = (FloatingActionButton) findViewById(R.id.fab);
        expenseListView = (ListView) findViewById(R.id.expenseList);
    }

    private void bindListeners() {
        addExpenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToCreateExpense = new Intent(getApplicationContext(),
                        CreateExpenseActivity.class);
                startActivity(goToCreateExpense);
            }
        });
        expenseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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

    private void refreshData() {
        ExpenseDbHelper expenseDbHelper = new ExpenseDbHelper(dbHelper);
        PeriodDbHelper periodDbHelper = new PeriodDbHelper(dbHelper);

        Period currentPeriod = periodDbHelper.getCurrentPeriod();
        ArrayList<Expense> expenses = expenseDbHelper.getAllExpensesForPeriod(currentPeriod.getId());
        expenseAdapter = new TransactionAdapter(this, Helper.castToTransactionList(expenses));
        expenseListView.setAdapter(expenseAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshData();
    }

}
