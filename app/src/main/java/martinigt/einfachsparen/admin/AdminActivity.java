package martinigt.einfachsparen.admin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import martinigt.einfachsparen.R;
import martinigt.einfachsparen.data.DatabaseHelper;
import martinigt.einfachsparen.data.PeriodDbHelper;
import martinigt.einfachsparen.data.TransactionDbHelper;

public class AdminActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        dbHelper = new DatabaseHelper(this.getApplicationContext());

        Button deleteAllTransactions = (Button) findViewById(R.id.adminDeleteAllExpensesButton);
        Button deleteAllPeriods = (Button) findViewById(R.id.adminDeleteAllPeriods);


        deleteAllTransactions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TransactionDbHelper expenseHelper = new TransactionDbHelper(dbHelper);
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
