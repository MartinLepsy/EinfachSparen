package martinigt.einfachsparen.admin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import martinigt.einfachsparen.R;
import martinigt.einfachsparen.data.DatabaseHelper;
import martinigt.einfachsparen.data.ExpenseDbHelper;
import martinigt.einfachsparen.data.IncomeDbHelper;
import martinigt.einfachsparen.data.PeriodDbHelper;

public class AdminActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        dbHelper = new DatabaseHelper(this.getApplicationContext());

        Button deleteAllIncome = (Button) findViewById(R.id.adminDeleteAllIncome);
        Button deleteAllExpenses = (Button) findViewById(R.id.adminDeleteAllExpensesButton);
        Button deleteAllPeriods = (Button) findViewById(R.id.adminDeleteAllPeriods);

        deleteAllIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IncomeDbHelper incomeHelper = new IncomeDbHelper(dbHelper);
                incomeHelper.cleanTable();
            }
        });

        deleteAllExpenses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ExpenseDbHelper expenseHelper = new ExpenseDbHelper(dbHelper);
                expenseHelper.cleanTable();
            }
        });

        deleteAllPeriods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PeriodDbHelper periodHelper = new PeriodDbHelper(dbHelper);
                periodHelper.cleanTable();
            }
        });


    }
}
