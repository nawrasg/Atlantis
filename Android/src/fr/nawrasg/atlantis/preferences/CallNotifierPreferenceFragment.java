package fr.nawrasg.atlantis.preferences;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import fr.nawrasg.atlantis.R;

public class CallNotifierPreferenceFragment extends PreferenceFragment{

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.pref_callnotifier);
	}
}
