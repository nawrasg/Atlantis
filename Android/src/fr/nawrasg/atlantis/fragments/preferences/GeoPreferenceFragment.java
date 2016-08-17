package fr.nawrasg.atlantis.fragments.preferences;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import fr.nawrasg.atlantis.R;

/**
 * Created by Nawras GEORGI on 03/12/2015.
 */
public class GeoPreferenceFragment extends PreferenceFragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.pref_geo);
	}
}
