package fr.nawrasg.atlantis.preferences;

import android.app.Fragment;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;

import fr.nawrasg.atlantis.MainFragmentActivity;
import fr.nawrasg.atlantis.R;

public class MainPreferenceFragment extends PreferenceFragment implements OnPreferenceClickListener{
	private Preference mConnection, mSecurity, mOthers;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.pref_main);
		mConnection = findPreference("main_connection");
		mConnection.setOnPreferenceClickListener(this);
		mSecurity = findPreference("main_security");
		mSecurity.setOnPreferenceClickListener(this);
		mOthers = findPreference("main_divers");
		mOthers.setOnPreferenceClickListener(this);
	}

	@Override
	public boolean onPreferenceClick(Preference preference) {
		switch(preference.getKey()){
			case "main_connection":
				loadPreferences(new ConnectionPreferenceFragment());
				return true;
			case "main_security":
				loadPreferences(new SecurityPreferenceFragment());
				return true;
			case "main_divers":
				loadPreferences(new DiversPreferenceFragment());
				return true;
		}
		return false;
	}
	
	private void loadPreferences(Fragment fragment){
		if(((MainFragmentActivity)getActivity()).findViewById(R.id.main_fragment2) == null){
			((MainFragmentActivity)getActivity()).startFragment(fragment, true, true);			
		}else{
			((MainFragmentActivity)getActivity()).startFragment(fragment, false, false);
		}
	}
}
