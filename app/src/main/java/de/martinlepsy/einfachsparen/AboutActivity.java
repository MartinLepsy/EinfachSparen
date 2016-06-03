package de.martinlepsy.einfachsparen;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class AboutActivity extends AppCompatActivity {

    private TextView versionTextView;

    private Button sendFeedbackButton;

    private Button rateAppButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getReferencesToWidgets();

        bindListeners();

        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String versionName = " " + packageInfo.versionName;
            versionTextView.append(versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void bindListeners() {
        sendFeedbackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendFeedbackClicked();
            }
        });
        rateAppButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rateAppClicked();
            }
        });
    }

    private void getReferencesToWidgets() {
        versionTextView = (TextView) findViewById(R.id.aboutVersion);
        sendFeedbackButton = (Button) findViewById(R.id.aboutSendFeedBackButton);
        rateAppButton = (Button) findViewById(R.id.aboutRateAppButton);
    }

    private void sendFeedbackClicked() {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{getString(R.string.about_mailto)});
        i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.about_mailsubject));
        i.putExtra(Intent.EXTRA_TEXT   , getString(R.string.about_mail_placeholder));
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(AboutActivity.this, getString(R.string.about_no_mail_client_found),
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void rateAppClicked() {
        Uri uri = Uri.parse("market://details?id=" + getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
        }
    }

}
