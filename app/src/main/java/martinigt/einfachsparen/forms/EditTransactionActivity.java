package martinigt.einfachsparen.forms;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;

import martinigt.einfachsparen.R;
import martinigt.einfachsparen.model.Transaction;

public class EditTransactionActivity extends AppCompatActivity {

    private EditText transactionNameInput;

    private EditText transactionValueInput;

    private CheckBox transactionRecurringInput;

    private Transaction transactionToEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_transaction);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getReferencesToWidgets();

        Intent sourceIntent = getIntent();
        transactionToEdit = (Transaction) sourceIntent.getSerializableExtra("TransactionToEdit");

        readValuesFromTransaction();

    }

    private void getReferencesToWidgets() {
        transactionNameInput = (EditText) findViewById(R.id.editTransactionNameInput);
        transactionValueInput = (EditText) findViewById(R.id.editTransactionValueInput);
        transactionRecurringInput = (CheckBox) findViewById(R.id.editTransactionRecurringInput);
    }

    private void readValuesFromTransaction() {
        transactionNameInput.setText(transactionToEdit.getName());
        transactionValueInput.setText(""+ transactionToEdit.getValue());
        transactionRecurringInput.setChecked(transactionToEdit.isStandard());
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_saveTransaction:
                updateTransaction();
                return true;
            case R.id.action_deleteTransaction:
                askAndDeleteTransaction();
                return true;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void askAndDeleteTransaction() {

    }

    private void updateTransaction() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_transaction, menu);
        return true;
    }

}
