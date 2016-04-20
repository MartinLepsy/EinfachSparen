package martinigt.einfachsparen.forms;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import java.util.Date;

import martinigt.einfachsparen.R;
import martinigt.einfachsparen.data.DatabaseHelper;
import martinigt.einfachsparen.data.ExpenseDbHelper;
import martinigt.einfachsparen.data.IncomeDbHelper;
import martinigt.einfachsparen.data.PeriodDbHelper;
import martinigt.einfachsparen.helper.Helper;
import martinigt.einfachsparen.model.Expense;
import martinigt.einfachsparen.model.Period;

public class CreateExpenseActivity extends AppCompatActivity  implements TextWatcher {

    private EditText expenseTitleInput;

    private EditText expenseValueInput;

    private CheckBox expenseRecurringInput;

    private FloatingActionButton saveButton;

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_expense);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getReferenceToWidgets();

        bindListeners();

        dbHelper = new DatabaseHelper(getApplicationContext());
    }

    private void getReferenceToWidgets() {
        expenseTitleInput = (EditText) findViewById(R.id.createExpenseTitleInput);
        expenseValueInput = (EditText) findViewById(R.id.createExpenseValueInput);
        expenseRecurringInput = (CheckBox) findViewById(R.id.createExpenseRecurringInput);
        saveButton = (FloatingActionButton) findViewById(R.id.fab);
    }

    private void bindListeners() {
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateInput()) {
                    saveExpense();
                }
            }
        });
        expenseTitleInput.addTextChangedListener(this);
        expenseValueInput.addTextChangedListener(this);
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
        result &= Helper.validateMandatoryTextField(expenseTitleInput);
        result &= Helper.validatePositiveDoubleField(expenseValueInput);
        return result;
    }

    private void saveExpense() {
        PeriodDbHelper periodHelper = new PeriodDbHelper(dbHelper);
        ExpenseDbHelper expenseHelper = new ExpenseDbHelper(dbHelper);
        Period currentPeriod = periodHelper.getCurrentPeriod();
        Expense expenseToAdd = new Expense();
        expenseToAdd.setPeriodId(currentPeriod.getId());
        expenseToAdd.setDate(new Date());
        expenseToAdd.setTag("");
        expenseToAdd.setName(expenseTitleInput.getText().toString());
        expenseToAdd.setValue(Double.parseDouble(expenseValueInput.getText().toString()));
        expenseToAdd.setStandard(expenseRecurringInput.isChecked());
        expenseHelper.addExpense(expenseToAdd);
        finish();
    }

}
