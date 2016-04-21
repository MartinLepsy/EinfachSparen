package martinigt.einfachsparen.lists;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import martinigt.einfachsparen.R;
import martinigt.einfachsparen.data.DatabaseHelper;
import martinigt.einfachsparen.data.IncomeDbHelper;
import martinigt.einfachsparen.data.PeriodDbHelper;
import martinigt.einfachsparen.data.TransactionAdapter;
import martinigt.einfachsparen.forms.CreateIncomeActivity;
import martinigt.einfachsparen.forms.EditTransactionActivity;
import martinigt.einfachsparen.helper.Helper;
import martinigt.einfachsparen.model.Income;
import martinigt.einfachsparen.model.Period;
import martinigt.einfachsparen.model.Transaction;

public class IncomeListActivity extends AppCompatActivity {

    private ListView incomeListView;

    private FloatingActionButton addIncomeButton;

    private TransactionAdapter incomeAdapter;

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getReferencesToWidgets();

        dbHelper = new DatabaseHelper(this.getApplicationContext());

        bindListeners();

        refreshData();

    }

    private void getReferencesToWidgets() {
        addIncomeButton = (FloatingActionButton) findViewById(R.id.fab);
        incomeListView = (ListView) findViewById(R.id.incomeList);
    }

    private void bindListeners() {
        addIncomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToCreateIncome = new Intent(getApplicationContext(),
                        CreateIncomeActivity.class);
                startActivity(goToCreateIncome);
            }
        });

        incomeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Transaction selectedIncome = incomeAdapter.getValues().get(position);
                Intent editIncomeIntent = new Intent(getApplicationContext(),
                        EditTransactionActivity.class);
                editIncomeIntent.putExtra("TransactionToEdit", selectedIncome);
                startActivity(editIncomeIntent);
            }
        });
    }

    private void refreshData() {
        IncomeDbHelper incomeDbHelper = new IncomeDbHelper(dbHelper);
        PeriodDbHelper periodDbHelper = new PeriodDbHelper(dbHelper);

        Period currentPeriod = periodDbHelper.getCurrentPeriod();
        ArrayList<Income> incomes = incomeDbHelper.getAllIncomesForPeriod(currentPeriod.getId());
        incomeAdapter = new TransactionAdapter(this, Helper.castToTransactionList(incomes));
        incomeListView.setAdapter(incomeAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshData();
    }


}
