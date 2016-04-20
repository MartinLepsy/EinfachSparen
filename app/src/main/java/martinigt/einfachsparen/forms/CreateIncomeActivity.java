package martinigt.einfachsparen.forms;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import martinigt.einfachsparen.data.IncomeDbHelper;
import martinigt.einfachsparen.data.PeriodDbHelper;
import martinigt.einfachsparen.helper.Helper;
import martinigt.einfachsparen.model.Income;
import martinigt.einfachsparen.model.Period;

public class CreateIncomeActivity extends AppCompatActivity implements TextWatcher{

    private EditText incomeTitleInput;

    private EditText incomeValueInput;

    private CheckBox incomeRecurringInput;

    private FloatingActionButton saveButton;

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_income);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getReferenceToWidgets();

        bindListeners();

        dbHelper = new DatabaseHelper(getApplicationContext());
    }

    private void getReferenceToWidgets() {
        incomeTitleInput = (EditText) findViewById(R.id.createIncomeTitleInput);
        incomeValueInput = (EditText) findViewById(R.id.createIncomeValueInput);
        incomeRecurringInput = (CheckBox) findViewById(R.id.createIncomeRecurringInput);
        saveButton = (FloatingActionButton) findViewById(R.id.fab);
    }

    private void bindListeners() {
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateInput()) {
                    saveIncome();
                }
            }
        });
        incomeTitleInput.addTextChangedListener(this);
        incomeValueInput.addTextChangedListener(this);
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
        result &= Helper.validateMandatoryTextField(incomeTitleInput);
        result &= Helper.validatePositiveDoubleField(incomeValueInput);
        return result;
    }

    private void saveIncome() {
        PeriodDbHelper periodHelper = new PeriodDbHelper(dbHelper);
        IncomeDbHelper incomeHelper = new IncomeDbHelper(dbHelper);
        Period currentPeriod = periodHelper.getCurrentPeriod();
        Income incomeToAdd = new Income();
        incomeToAdd.setPeriodId(currentPeriod.getId());
        incomeToAdd.setDate(new Date());
        incomeToAdd.setTag("");
        incomeToAdd.setName(incomeTitleInput.getText().toString());
        incomeToAdd.setValue(Double.parseDouble(incomeValueInput.getText().toString()));
        incomeToAdd.setStandard(incomeRecurringInput.isChecked());
        incomeHelper.addIncome(incomeToAdd);
        finish();
    }


}
