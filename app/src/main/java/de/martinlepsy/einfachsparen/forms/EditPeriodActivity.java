package de.martinlepsy.einfachsparen.forms;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import de.martinlepsy.einfachsparen.R;
import de.martinlepsy.einfachsparen.data.DatabaseHelper;
import de.martinlepsy.einfachsparen.data.PeriodDbHelper;
import de.martinlepsy.einfachsparen.helper.Helper;
import de.martinlepsy.einfachsparen.model.Period;

public class EditPeriodActivity extends AppCompatActivity implements TextWatcher {

    public static final String PERIOD_TO_EDIT_EXTRA_NAME = "periodToEdit";

    private Period periodToEdit;

    private EditText nameInput;

    private EditText savingInput;

    private MenuItem saveMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_period);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent sourceIntent = getIntent();
        periodToEdit = (Period) sourceIntent.getSerializableExtra(PERIOD_TO_EDIT_EXTRA_NAME);

        getReferencesToWidgets();

        bindListeners();

        readValuesFromPeriod();

    }

    private void bindListeners() {
        nameInput.addTextChangedListener(this);
        savingInput.addTextChangedListener(this);
    }

    private void getReferencesToWidgets() {
        nameInput = (EditText) findViewById(R.id.editperiod_name);
        savingInput = (EditText) findViewById(R.id.editperiod_saving);
    }

    private void updatePeriod() {
        boolean result = true;
        periodToEdit.setName(nameInput.getText().toString().trim());
        periodToEdit.setPlannedSaving(Double.parseDouble(savingInput.getText().toString().trim()));
        DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
        PeriodDbHelper periodDbHelper = new PeriodDbHelper(dbHelper);
        result = periodDbHelper.updatePeriod(periodToEdit);
        if (result) {
            Helper.updateDesktopWidgets(getApplicationContext());
            finish();
        }
    }

    private void readValuesFromPeriod() {
        nameInput.setText(periodToEdit.getName());
        savingInput.setText(""+ periodToEdit.getPlannedSaving());
    }


    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        // nop
    }

    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        boolean validationResult = validateInput();
        if (saveMenuItem != null) {
            saveMenuItem.setEnabled(validationResult);
            saveMenuItem.setVisible(validationResult);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {
        // nop
    }


    private boolean validateInput() {
        boolean result = true;
        result &= Helper.validateMandatoryTextField(nameInput);
        result &= Helper.validatePositiveDoubleField(savingInput);
        return result;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_editsavePeriod:
                if (validateInput()) {
                    updatePeriod();
                }
                return true;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_period, menu);
        saveMenuItem = menu.findItem(R.id.action_editsavePeriod);
        return true;
    }

}
