package de.martinlepsy.einfachsparen.lists;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import de.martinlepsy.einfachsparen.R;
import de.martinlepsy.einfachsparen.data.DatabaseHelper;
import de.martinlepsy.einfachsparen.data.PeriodAdapter;
import de.martinlepsy.einfachsparen.data.PeriodDbHelper;
import de.martinlepsy.einfachsparen.forms.PeriodDetailsActivity;
import de.martinlepsy.einfachsparen.model.Period;

public class PeriodListActivity extends AppCompatActivity {

    private ListView periodListView;

    private PeriodAdapter periodAdapter;

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_period_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getReferencesToWidgets();

        dbHelper = new DatabaseHelper(this.getApplicationContext());

        bindListeners();

        refreshData();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        refreshData();
    }

    private void refreshData() {
        PeriodDbHelper periodDbHelper = new PeriodDbHelper(dbHelper);
        ArrayList<Period> periods = periodDbHelper.getAllPeriods(true);

        if (periods != null) {
            periodAdapter = new PeriodAdapter(this, periods);
            periodListView.setAdapter(periodAdapter);
        }
    }

    private void bindListeners() {
        periodListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Period selectedPeriod = periodAdapter.getValues().get(position);
                Intent editTransactionIntent = new Intent(getApplicationContext(),
                        PeriodDetailsActivity.class);
                editTransactionIntent.putExtra("PeriodToShow", selectedPeriod);
                startActivity(editTransactionIntent);
            }
        });
    }

    public void getReferencesToWidgets() {
        periodListView = (ListView) findViewById(R.id.periodList);
    }
}
