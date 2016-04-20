package martinigt.einfachsparen.martinigt.einfachsparen.lists;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ListViewCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

import martinigt.einfachsparen.R;
import martinigt.einfachsparen.data.DatabaseHelper;
import martinigt.einfachsparen.data.IncomeAdapter;
import martinigt.einfachsparen.data.IncomeDbHelper;
import martinigt.einfachsparen.data.PeriodDbHelper;
import martinigt.einfachsparen.forms.CreateIncomeActivity;
import martinigt.einfachsparen.model.Income;
import martinigt.einfachsparen.model.Period;

public class IncomeListActivity extends AppCompatActivity {

    private ListView incomeListView;

    private FloatingActionButton addIncomeButton;

    private IncomeAdapter incomeAdapter;

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
    }

    private void refreshData() {
        IncomeDbHelper incomeDbHelper = new IncomeDbHelper(dbHelper);
        PeriodDbHelper periodDbHelper = new PeriodDbHelper(dbHelper);

        Period currentPeriod = periodDbHelper.getCurrentPeriod();
        ArrayList<Income> incomes = incomeDbHelper.getAllExpensesForPeriod(currentPeriod.getId());
        incomeAdapter = new IncomeAdapter(this, incomes);
        incomeListView.setAdapter(incomeAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshData();
    }


}
