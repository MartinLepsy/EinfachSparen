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

import martinigt.einfachsparen.R;
import martinigt.einfachsparen.forms.CreateIncomeActivity;

public class IncomeListActivity extends AppCompatActivity {

    private ListView incomeListView;

    private FloatingActionButton addIncomeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getReferencesToWidgets();

        bindListeners();
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

}
