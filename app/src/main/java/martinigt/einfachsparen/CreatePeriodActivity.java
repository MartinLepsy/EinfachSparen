package martinigt.einfachsparen;

import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ThemedSpinnerAdapter;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import martinigt.einfachsparen.data.DatabaseHelper;
import martinigt.einfachsparen.data.PeriodDbHelper;
import martinigt.einfachsparen.helper.DatePickerFragment;
import martinigt.einfachsparen.helper.Helper;
import martinigt.einfachsparen.model.Period;
import martinigt.einfachsparen.model.PeriodWizard;

public class CreatePeriodActivity extends AppCompatActivity {

    private EditText periodNameInput;

    private EditText periodStartInput;

    private EditText periodEndInput;

    private EditText plannedSavingInput;

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_period);

        Toolbar toolbar = (Toolbar) findViewById(R.id.createPeriodToolbar);
        setSupportActionBar(toolbar);

        getReferenceToInputWidgets();

        addPickers();

        Button savePeriodButton = (Button) findViewById(R.id.periodSaveButton);
        savePeriodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                    preparePeriodAndSave(newPeriod);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        dbHelper = new DatabaseHelper(this.getApplicationContext());

        prefillWidgets();
    }

    private void preparePeriodAndSave(Period periodToSave) {
        PeriodWizard wizz = new PeriodWizard(periodToSave, dbHelper);
        wizz.assignDefaultIncomeAndExpenses();
        wizz.savePeriod();

    }

    private void getReferenceToInputWidgets() {
        periodNameInput = (EditText) findViewById(R.id.periodNameInput);
        periodStartInput = (EditText) findViewById(R.id.periodStartInput);
        periodEndInput = (EditText) findViewById(R.id.periodEndInput);
        plannedSavingInput = (EditText) findViewById(R.id.periodPlannedSavingInput);
    }

    private void addPickers() {
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
        PeriodDbHelper periodHelper = new PeriodDbHelper(dbHelper);
        Period mostRecentPeriod = periodHelper.getMostRecentPeriod();
        if (mostRecentPeriod != null) {
            plannedSavingInput.setText(""+mostRecentPeriod.getPlannedSaving());
        }
        periodNameInput.setText(Helper.getMonthForDate(new Date()));
        periodStartInput.setText(new Date().toString());
    }
}
