package fr.nawrasg.atlantis.preferences;

import android.content.Context;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;

import fr.nawrasg.atlantis.MainFragmentActivity;
import fr.nawrasg.atlantis.R;
import fr.nawrasg.atlantis.fragments.HomeFragment;

public class SecurityPreferenceFragment extends PreferenceFragment implements OnPreferenceClickListener{
	private Context mContext;
	private Preference mAdminPreference;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.pref_security);
		mContext = getActivity();
	}

	@Override
	public boolean onPreferenceClick(Preference preference) {
		switch(preference.getKey()){
			case "admin":
				((MainFragmentActivity)getActivity()).loadFragment(new HomeFragment(), true);

				return true;
		}
		return false;
	}

}
