package de.martinlepsy.einfachsparen.forms;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;

import java.util.Date;

import de.martinlepsy.einfachsparen.R;
import de.martinlepsy.einfachsparen.data.DatabaseHelper;
import de.martinlepsy.einfachsparen.data.PeriodDbHelper;
import de.martinlepsy.einfachsparen.data.TransactionDbHelper;
import de.martinlepsy.einfachsparen.helper.Helper;
import de.martinlepsy.einfachsparen.model.Period;
import de.martinlepsy.einfachsparen.model.Transaction;
import de.martinlepsy.einfachsparen.model.TransactionType;

public class CreateTransactionActivity extends AppCompatActivity implements TextWatcher, View.OnFocusChangeListener {

    private AutoCompleteTextView transactionTitleInput;

    private EditText transactionValueInput;

    private AutoCompleteTextView transactionTagInput;

    private CheckBox transactionRecurringInput;

    private FloatingActionButton saveButton;

    private DatabaseHelper dbHelper;

    private TransactionType transactionType;

    public static final String TRANSACTION_TYPE_INTENT_EXTRA = "TransactionType";

    private boolean useTags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_transaction);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dbHelper = new DatabaseHelper(getApplicationContext());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences sPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        useTags = sPrefs.getBoolean(getApplicationContext().getString(R.string.pref_key_useTags), false);

        Intent sourceIntent = getIntent();
        transactionType = TransactionType.values()[sourceIntent.getIntExtra(TRANSACTION_TYPE_INTENT_EXTRA,
                TransactionType.INCOME.ordinal())];

        int titleId = transactionType == TransactionType.EXPENSE ? R.string.title_create_expense :
                R.string.title_create_income;
        setTitle(titleId);

        getReferenceToWidgets();

        Helper.addTransactionTitleAutoComplete(transactionTitleInput);
        Helper.hideOrConfigureTagInput(transactionTagInput);

        bindListeners();

    }

    private void getReferenceToWidgets() {
        transactionTitleInput = (AutoCompleteTextView) findViewById(R.id.createTransactionTitleInput);
        transactionValueInput = (EditText) findViewById(R.id.createTransactionValueInput);
        transactionTagInput = (AutoCompleteTextView) findViewById(R.id.createTransactionTagInput);
        transactionRecurringInput = (CheckBox) findViewById(R.id.createTransactionRecurringInput);
        saveButton = (FloatingActionButton) findViewById(R.id.fab);
    }

    private void bindListeners() {
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateInput()) {
                    saveTransaction();
                }
            }
        });
        transactionTitleInput.addTextChangedListener(this);
        transactionValueInput.addTextChangedListener(this);
        transactionTitleInput.setOnFocusChangeListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        boolean validationResult = validateInput();
        Helper.formatFloatingButton(validationResult, saveButton);
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    private boolean validateInput() {
        boolean result = true;
        result &= Helper.validateMandatoryTextField(transactionTitleInput);
        result &= Helper.validatePositiveDoubleField(transactionValueInput);
        return result;
    }

    private void saveTransaction() {
        PeriodDbHelper periodHelper = new PeriodDbHelper(dbHelper);
        TransactionDbHelper transactionDbHelper = new TransactionDbHelper(dbHelper);
        Period currentPeriod = periodHelper.getCurrentPeriod();
        Transaction transactionToAdd = new Transaction();
        transactionToAdd.setPeriodId(currentPeriod.getId());
        transactionToAdd.setDate(new Date());
        if (transactionTagInput.getVisibility() == View.VISIBLE) {
            transactionToAdd.setTag(transactionTagInput.getText().toString().trim());
        }
        else {
            transactionToAdd.setTag("");
        }
        transactionToAdd.setName(transactionTitleInput.getText().toString().trim());
        transactionToAdd.setValue(Double.parseDouble(transactionValueInput.getText().toString().trim()));
        transactionToAdd.setStandard(transactionRecurringInput.isChecked());
        transactionToAdd.setType(transactionType);
        transactionToAdd.setFromStandardId(0);
        transactionDbHelper.addTransaction(transactionToAdd);
        Helper.updateDesktopWidgets(getApplicationContext());
        finish();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        Helper.suggestTagForTransaction(useTags, view, hasFocus, transactionTitleInput,
                transactionTagInput, dbHelper);
    }
}
