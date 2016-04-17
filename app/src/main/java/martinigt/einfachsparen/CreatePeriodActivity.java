package martinigt.einfachsparen;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class CreatePeriodActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_period);

        Toolbar toolbar = (Toolbar) findViewById(R.id.createPeriodToolbar);
        setSupportActionBar(toolbar);
    }
}
