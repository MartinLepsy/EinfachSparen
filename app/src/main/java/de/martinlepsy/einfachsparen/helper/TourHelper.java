package de.martinlepsy.einfachsparen.helper;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;

import de.martinlepsy.einfachsparen.R;
import de.martinlepsy.einfachsparen.TourActivity;


public class TourHelper implements DialogInterface.OnClickListener {

    private Context context;

    public TourHelper(Context context) {
        this.context = context;
    }

    public boolean isTourAlreadyTaken() {
        boolean result = false;
        SharedPreferences sPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        result = sPrefs.getBoolean(context.getString(R.string.tour_taken_key), false);
        return result;
    }

    public void markTourAsTaken() {
        SharedPreferences sPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sPrefs.edit();
        editor.putBoolean(context.getString(R.string.tour_taken_key), true);
        editor.apply();
    }

    public void askForTour() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(R.string.tourQuestion).setPositiveButton(R.string.tourYes, this)
                .setNegativeButton(R.string.tourNo, this).show();
    }

    private void takeTour() {
        Intent startTourIntent = new Intent(context, TourActivity.class);
        context.startActivity(startTourIntent);
    }


    @Override
    public void onClick(DialogInterface dialogInterface, int which) {
        switch (which){
            case DialogInterface.BUTTON_POSITIVE:
                takeTour();
                break;
            case DialogInterface.BUTTON_NEGATIVE:
                markTourAsTaken();
                break;
        }
    }
}
