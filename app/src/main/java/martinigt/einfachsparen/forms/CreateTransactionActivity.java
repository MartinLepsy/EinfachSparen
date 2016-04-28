package martinigt.einfachsparen.forms;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import java.util.Date;

import martinigt.einfachsparen.R;
import martinigt.einfachsparen.data.DatabaseHelper;
import martinigt.einfachsparen.data.PeriodDbHelper;
import martinigt.einfachsparen.data.TransactionDbHelper;
import martinigt.einfachsparen.helper.Helper;
import martinigt.einfachsparen.model.Period;
import martinigt.einfachsparen.model.Transaction;
import martinigt.einfachsparen.model.TransactionType;

public class CreateTransactionActivity extends AppCompatActivity implements TextWatcher {

    private EditText transactionTitleInput;

    private EditText transactionValueInput;

    private CheckBox transactionRecurringInput;

    private FloatingActionButton saveButton;

    private DatabaseHelper dbHelper;

    private TransactionType transactionType;

    public static final String TRANSACTION_TYPE_INTENT_EXTRA = "TransactionType";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_transaction);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent sourceIntent = getIntent();
        transactionType = TransactionType.values()[sourceIntent.getIntExtra(TRANSACTION_TYPE_INTENT_EXTRA,
                TransactionType.INCOME.ordinal())];

        int titleId = transactionType == TransactionType.EXPENSE ? R.string.title_create_expense :
                R.string.title_create_income;
        setTitle(titleId);

        getReferenceToWidgets();

        bindListeners();

        dbHelper = new DatabaseHelper(getApplicationContext());
    }

    private void getReferenceToWidgets() {
        transactionTitleInput = (EditText) findViewById(R.id.createTransactionTitleInput);
        transactionValueInput = (EditText) findViewById(R.id.createTransactionValueInput);
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
        transactionToAdd.setTag("");
        transactionToAdd.setName(transactionTitleInput.getText().toString());
        transactionToAdd.setValue(Double.parseDouble(transactionValueInput.getText().toString()));
        transactionToAdd.setStandard(transactionRecurringInput.isChecked());
        transactionToAdd.setType(transactionType);
        transactionToAdd.setFromStandardId(0);
        transactionDbHelper.addTransaction(transactionToAdd);
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

}
