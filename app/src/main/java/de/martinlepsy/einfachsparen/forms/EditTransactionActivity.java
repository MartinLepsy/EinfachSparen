package de.martinlepsy.einfachsparen.forms;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;

import de.martinlepsy.einfachsparen.R;
import de.martinlepsy.einfachsparen.data.DatabaseHelper;
import de.martinlepsy.einfachsparen.data.TransactionDbHelper;
import de.martinlepsy.einfachsparen.helper.Helper;
import de.martinlepsy.einfachsparen.model.Transaction;

public class EditTransactionActivity extends AppCompatActivity implements TextWatcher, DialogInterface.OnClickListener {

    private AutoCompleteTextView transactionNameInput;

    private EditText transactionValueInput;

    private AutoCompleteTextView transactionTagInput;

    private CheckBox transactionRecurringInput;

    private MenuItem saveMenuItem;

    private Transaction transactionToEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_transaction);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getReferencesToWidgets();

        Helper.addTransactionTitleAutoComplete(transactionNameInput);
        Helper.hideOrConfigureTagInput(transactionTagInput);

        Intent sourceIntent = getIntent();
        transactionToEdit = (Transaction) sourceIntent.getSerializableExtra("TransactionToEdit");

        readValuesFromTransaction();

        bindListeners();

    }

    private void bindListeners() {
        transactionNameInput.addTextChangedListener(this);
        transactionValueInput.addTextChangedListener(this);
    }

    private void getReferencesToWidgets() {
        transactionNameInput = (AutoCompleteTextView) findViewById(R.id.editTransactionNameInput);
        transactionValueInput = (EditText) findViewById(R.id.editTransactionValueInput);
        transactionTagInput = (AutoCompleteTextView) findViewById(R.id.editTransactionTagInput);
        transactionRecurringInput = (CheckBox) findViewById(R.id.editTransactionRecurringInput);
    }

    private void readValuesFromTransaction() {
        transactionNameInput.setText(transactionToEdit.getName());
        transactionValueInput.setText(""+ transactionToEdit.getValue());
        transactionRecurringInput.setChecked(transactionToEdit.isStandard());
        transactionTagInput.setText(transactionToEdit.getTag());
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_saveTransaction:
                if (validateInput()) {
                    updateTransaction();
                }
                return true;
            case R.id.action_deleteTransaction:
                askDeletionConfirmation();
                return true;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        boolean validationResult = validateInput();
        saveMenuItem.setEnabled(validationResult);
        saveMenuItem.setVisible(validationResult);
    }

    public void afterTextChanged(Editable editable) {

    }

    private boolean validateInput() {
        boolean result = true;
        result &= Helper.validateMandatoryTextField(transactionNameInput);
        result &= Helper.validatePositiveDoubleField(transactionValueInput);
        return result;
    }

    private void askDeletionConfirmation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.confirmQuestion).setPositiveButton(R.string.confirmYes, this)
                .setNegativeButton(R.string.confirmNo, this).show();
    }

    private void deleteTransaction() {
        boolean result = true;
        DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
        TransactionDbHelper transactionDbHelper = new TransactionDbHelper(dbHelper);
        result = transactionDbHelper.deleteTRansaction(transactionToEdit);
        if (result) {
            Helper.updateDesktopWidgets(getApplicationContext());
            finish();
        }
    }

    private void updateTransaction() {
        boolean result = true;
        transactionToEdit.setName(transactionNameInput.getText().toString());
        transactionToEdit.setValue(Double.parseDouble(transactionValueInput.getText().toString()));
        if (transactionTagInput.getVisibility() == View.VISIBLE) {
            transactionToEdit.setTag(transactionTagInput.getText().toString());
        }
        transactionToEdit.setStandard(transactionRecurringInput.isChecked());
        DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
        TransactionDbHelper transactionDbHelper = new TransactionDbHelper(dbHelper);
        result = transactionDbHelper.updateTransaction(transactionToEdit);
        if (result) {
            Helper.updateDesktopWidgets(getApplicationContext());
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_transaction, menu);
        saveMenuItem = menu.findItem(R.id.action_saveTransaction);
        return true;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which){
            case DialogInterface.BUTTON_POSITIVE:
                deleteTransaction();
                break;
            case DialogInterface.BUTTON_NEGATIVE:
                break;
        }
    }

}
