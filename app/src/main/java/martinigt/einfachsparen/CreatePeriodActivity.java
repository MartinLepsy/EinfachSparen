package martinigt.einfachsparen;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import martinigt.einfachsparen.data.DatabaseHelper;
import martinigt.einfachsparen.data.PeriodDbHelper;
import martinigt.einfachsparen.helper.DatePickerFragment;
import martinigt.einfachsparen.helper.Helper;
import martinigt.einfachsparen.model.Period;
import martinigt.einfachsparen.model.PeriodUtility;
import martinigt.einfachsparen.model.PeriodWizard;

public class CreatePeriodActivity extends AppCompatActivity implements TextWatcher {

    private EditText periodNameInput;

    private EditText periodStartInput;

    private EditText periodEndInput;

    private EditText plannedSavingInput;

    private TextView createPeriodInfoLabel;

    private FloatingActionButton saveButton;

    private DatabaseHelper dbHelper;

    private PeriodUtility periodUtility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_period);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dbHelper = new DatabaseHelper(this.getApplicationContext());
        periodUtility = new PeriodUtility(dbHelper);

        getReferenceToWidgets();

        addPickers();

        bindListeners();

        prefillWidgets();
    }

    private void bindListeners() {
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savePeriod();
            }
        });
        periodNameInput.addTextChangedListener(this);
        periodStartInput.addTextChangedListener(this);
        periodEndInput.addTextChangedListener(this);
        plannedSavingInput.addTextChangedListener(this);
    }

    private void savePeriod() {
        Period newPeriod = new Period();
        Double plannedSaving = Double.parseDouble(plannedSavingInput.getText().toString());
        newPeriod.setPlannedSaving(plannedSaving);
        newPeriod.setName(periodNameInput.getText().toString());
        Calendar c = Calendar.getInstance();
        DateFormat df = DateFormat.getDateInstance();
        try {
            Date periodStart = df.parse(periodStartInput.getText().toString());
            Date periodEnd = df.parse(periodEndInput.getText().toString());
            newPeriod.setStart(periodStart);
            newPeriod.setEnd(periodEnd);
            periodUtility.savePeriodAndAssignDefaultValues(newPeriod);
            Helper.updateDesktopWidgets(getApplicationContext());
            finish();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void getReferenceToWidgets() {
        createPeriodInfoLabel = (TextView) findViewById(R.id.createPeriodInfoLabel);
        saveButton = (FloatingActionButton) findViewById(R.id.newPeriodSaveButton);
        periodNameInput = (EditText) findViewById(R.id.periodNameInput);
        periodStartInput = (EditText) findViewById(R.id.periodStartInput);
        periodEndInput = (EditText) findViewById(R.id.periodEndInput);
        plannedSavingInput = (EditText) findViewById(R.id.periodPlannedSavingInput);

        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromInputMethod(periodStartInput.getWindowToken(),0);
        inputManager.hideSoftInputFromInputMethod(periodEndInput.getWindowToken(),0);
    }

    private void addPickers() { //TODO: Shorten this
        periodStartInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    DialogFragment datePickerFragment = new DatePickerFragment() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int day) {
                            //Log.d(TAG, "onDateSet");
                            Calendar c = Calendar.getInstance();
                            c.set(year, month, day);
                            //periodStartInput.setText(df.format(c.getTime()));
                            DateFormat df = DateFormat.getDateInstance();
                            periodStartInput.setText(df.format(c.getTime()));
                            //nextField.requestFocus(); //moves the focus to something else after dialog is closed
                        }
                    };
                    datePickerFragment.show(getSupportFragmentManager(), "datePicker");
                }
            }
        });
        periodEndInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    DialogFragment datePickerFragment = new DatePickerFragment() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int day) {
                            //Log.d(TAG, "onDateSet");
                            Calendar c = Calendar.getInstance();
                            c.set(year, month, day);
                            //periodStartInput.setText(df.format(c.getTime()));
                            DateFormat df = DateFormat.getDateInstance();
                            periodEndInput.setText(df.format(c.getTime()));
                            //nextField.requestFocus(); //moves the focus to something else after dialog is closed
                        }
                    };
                    datePickerFragment.show(getSupportFragmentManager(), "datePicker");
                }
            }
        });
    }

    private void prefillWidgets() {
        periodNameInput.setText(Helper.getNextMonthName());
        DateFormat df = DateFormat.getDateInstance();
        Date startDate = new Date();
        periodStartInput.setText(df.format(startDate.getTime()));
        if (periodUtility.currentPeriodAvailable()) {
            createPeriodInfoLabel.setText(getString(R.string.createPeriodExistingCurrentPeriodInfoText));
        }
        if (periodUtility.mostRecentPeriodAvailable()) {
            plannedSavingInput.setText(""+periodUtility.getMostRecentPeriod().getPlannedSaving());
            periodEndInput.setText(df.format(periodUtility.predictEndDateFromMostRecentPeriod(startDate).getTime()));
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        boolean validationResult = validateInputFields();
        Helper.formatFloatingButton(validationResult, saveButton);
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    private boolean validateInputFields() {
        boolean result = true;
        result &= Helper.validateMandatoryTextField(periodNameInput);
        result &= validateDateField(periodStartInput);
        result &= validateDateField(periodEndInput);
        result &= Helper.validatePositiveDoubleField(plannedSavingInput);
        return result;
    }

    private boolean validateDateField(EditText dateField) {
        boolean result = true;
        try {
            DateFormat df = DateFormat.getDateInstance();
            df.parse(dateField.getText().toString());
            //removeWrongMark(dateField); //TODO: Improve UI
        }
        catch (Exception ex) {
            result = false;
            //markFieldAsWrong(dateField); //TODO: Improve UI
        }
        return result;
    }

    private void markFieldAsWrong(EditText textFieldToMark) {
        Drawable drawable = DrawableCompat.wrap(textFieldToMark.getBackground());
        DrawableCompat.setTintList(drawable, ColorStateList.valueOf(Color.parseColor("#cc0000")));
    }

    private void removeWrongMark(EditText textFieldToUnMark) {
        Drawable drawable = DrawableCompat.wrap(textFieldToUnMark.getBackground());
    }



}
