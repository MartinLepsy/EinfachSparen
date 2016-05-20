package martinigt.einfachsparen.helper;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import martinigt.einfachsparen.R;
import martinigt.einfachsparen.data.DatabaseHelper;
import martinigt.einfachsparen.data.TransactionDbHelper;
import martinigt.einfachsparen.model.Transaction;

/**
 * Created by martin on 16.04.16.
 */
public class Helper {

    public static    String getMonthForInt(int num) {
        String month = "---";
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();
        if (num >= 0 && num <= 11 ) {
            month = months[num];
        }
        return month;
    }

    public static String getMonthForDate(Date date)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return Helper.getMonthForInt(cal.get(Calendar.MONTH));
    }

    public static String getNextMonthName() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, 1);
        return Helper.getMonthForInt(cal.get(Calendar.MONTH));
    }

    public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }

    public static boolean validatePositiveDoubleField(EditText numberField) {
        boolean result = true;
        try {
            double temp = Double.parseDouble(numberField.getText().toString());
            result = temp >= 0.0;
        }
        catch (Exception ex) {
            result = false;
        }
        return result;
    }

    public static boolean validateMandatoryTextField(EditText textField) {
        return textField.getText().length() > 0;
    }

    public static void formatFloatingButton(boolean validationResult, FloatingActionButton buttonToFormat) {
        if (validationResult) {
            buttonToFormat.setEnabled(true);
            buttonToFormat.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#458B00")));
            buttonToFormat.setImageResource(R.drawable.ic_check);

        } else {
            buttonToFormat.setEnabled(false);
            buttonToFormat.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#cc0000")));
            buttonToFormat.setImageResource(R.drawable.ic_close);
        }
    }

    public static ArrayList<Transaction> castToTransactionList(ArrayList<? extends Transaction> expenseList) {
        ArrayList<Transaction> result = new ArrayList<>();
        for (Transaction currentItem : expenseList) {
            result.add(currentItem);
        }
        return result;
    }

    public static void hideOrConfigureTagInput(AutoCompleteTextView tagInputField) {
        SharedPreferences sPrefs = PreferenceManager.getDefaultSharedPreferences(tagInputField.getContext());
        boolean useTags = sPrefs.getBoolean(tagInputField.getContext().getString(
                R.string.pref_key_useTags), false);
        if (!useTags) {
            tagInputField.setVisibility(View.GONE);
        }
        else {
            DatabaseHelper dbHelper = new DatabaseHelper(tagInputField.getContext());
            TransactionDbHelper transactionDbHelper = new TransactionDbHelper(dbHelper);
            String[] allTags = transactionDbHelper.getAllAvailableTags();
            CustomAutoSuggestAdapter tagAutoCompleteAdapter = new CustomAutoSuggestAdapter(tagInputField.getContext(),
                    android.R.layout.simple_dropdown_item_1line, new ArrayList<>(Arrays.asList(allTags)));
            tagInputField.setAdapter(tagAutoCompleteAdapter);
        }
    }
}
