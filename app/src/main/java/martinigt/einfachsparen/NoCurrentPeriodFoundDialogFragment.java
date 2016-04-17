package martinigt.einfachsparen;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

/**
 * Created by martin on 17.04.16.
 */
public class NoCurrentPeriodFoundDialogFragment extends DialogFragment{

    public NoCurrentPeriodFoundDialogFragment() {
        // muss da sein
    }

    public static NoCurrentPeriodFoundDialogFragment newInstance(String title) {
        NoCurrentPeriodFoundDialogFragment frag = new NoCurrentPeriodFoundDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = getString(R.string.noCurrentPeriodFoundTitle);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(getString(R.string.noCurrentPeriodFound));
        alertDialogBuilder.setPositiveButton(getString(R.string.createNewPeriodButtonText),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent goToCreateNewPeriod = new Intent(getActivity().getApplicationContext(),
                                CreatePeriodActivity.class);
                        startActivity(goToCreateNewPeriod);
                    }
                });
        alertDialogBuilder.setNegativeButton(getString(android.R.string.cancel),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        return alertDialogBuilder.create();
    }

}
