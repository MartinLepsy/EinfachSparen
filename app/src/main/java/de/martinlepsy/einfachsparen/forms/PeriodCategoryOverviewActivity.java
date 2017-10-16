package de.martinlepsy.einfachsparen.forms;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import de.martinlepsy.einfachsparen.R;
import de.martinlepsy.einfachsparen.data.DatabaseHelper;
import de.martinlepsy.einfachsparen.data.GroupedExpenseAdapter;
import de.martinlepsy.einfachsparen.data.PeriodDbHelper;
import de.martinlepsy.einfachsparen.data.TransactionAdapter;
import de.martinlepsy.einfachsparen.data.TransactionDbHelper;
import de.martinlepsy.einfachsparen.model.GroupedTransactionByCategory;
import de.martinlepsy.einfachsparen.model.Period;

public class PeriodCategoryOverviewActivity extends AppCompatActivity {

    private Period periodToShow;

    private TextView periodNameTextView;

    private ListView groupedExpenseListView;

    private DatabaseHelper dbHelper;

    private TransactionDbHelper transactionDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_period_category_overview);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dbHelper = new DatabaseHelper(getApplicationContext());

        getReferencesToWidgets();

    }

    private void getReferencesToWidgets() {
        periodNameTextView = (TextView) findViewById(R.id.periodDetailsName);
        groupedExpenseListView = (ListView) findViewById(R.id.periodExpensesGroupedByCategoryList);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent sourceIntent = getIntent();
        Period periodFromIntent = (Period) sourceIntent.getSerializableExtra("PeriodToShow");

        if (periodFromIntent != null) {
            DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
            PeriodDbHelper periodDbHelper = new PeriodDbHelper(dbHelper);
            this.periodToShow = periodDbHelper.getPeriod(periodFromIntent.getId());
            showData();
        }
    }

    private void showData() {
        periodNameTextView.setText(periodToShow.getName());

        TransactionDbHelper transactionDbHelper = new TransactionDbHelper(dbHelper);
        ArrayList<GroupedTransactionByCategory> itemsToShow =
                transactionDbHelper.getAllExpensesForPeriodGroupedByCategory(periodToShow.getId());
        GroupedExpenseAdapter adapter = new GroupedExpenseAdapter(this, itemsToShow);
        groupedExpenseListView.setAdapter(adapter);
    }

}
