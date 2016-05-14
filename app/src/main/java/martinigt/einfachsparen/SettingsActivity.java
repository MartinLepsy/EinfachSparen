package martinigt.einfachsparen;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

/**
 * Created by martin on 13.05.16.
 */
public class SettingsActivity extends PreferenceActivity
        implements Preference.OnPreferenceChangeListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.pref_general);

        //SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        //String gespeicherteAktienliste = sharedPrefs.getString(aktienlistePref.getKey(), "");
        //onPreferenceChange(aktienlistePref, gespeicherteAktienliste);

    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object value) {
        preference.setSummary(value.toString());
        return true;
    }
}
