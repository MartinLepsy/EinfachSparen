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

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

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

    private PieChart chart;

    private DatabaseHelper dbHelper;

    private TransactionDbHelper transactionDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_period_category_overview);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Verteilung der Ausgaben");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dbHelper = new DatabaseHelper(getApplicationContext());

        getReferencesToWidgets();

    }

    private void getReferencesToWidgets() {
        periodNameTextView = (TextView) findViewById(R.id.periodDetailsName);
        groupedExpenseListView = (ListView) findViewById(R.id.periodExpensesGroupedByCategoryList);
        chart = (PieChart) findViewById(R.id.periodExpensesGroupedByCategoryPie);
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
        fillChart(itemsToShow);
    }

    private void fillChart(ArrayList<GroupedTransactionByCategory> data) {
        double totalExpenses = 0d;
        List<PieEntry> dataEntries = new ArrayList<>();
        for (GroupedTransactionByCategory group: data
             ) {
            totalExpenses += group.getValue();
        }
        for (GroupedTransactionByCategory group: data
             ) {
            String groupLabel = group.getCategoryName().equals("") ? "Keine Kategorie" :
                    group.getCategoryName();
            float groupPercentage = (float) (group.getValue() / totalExpenses) * 100f;
            //PieEntry entry = new PieEntry(groupPercentage, groupLabel);
            PieEntry entry = new PieEntry((float)group.getValue(), groupLabel);
            dataEntries.add(entry);
        }
        PieDataSet set = new PieDataSet(dataEntries, "");
        set.setHighlightEnabled(true);
        set.setColors(ColorTemplate.COLORFUL_COLORS);
        set.setValueTextSize(14f);
        //set.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        PieData pieData = new PieData(set);
        chart.setData(pieData);
        Description descr = new Description();
        descr.setText("Verteilung der Ausgaben");
        chart.setDescription(descr);
        chart.setDrawHoleEnabled(false);
        chart.invalidate();
        chart.getLegend().setWordWrapEnabled(true);



    }

}
