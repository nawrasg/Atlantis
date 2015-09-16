package fr.nawrasg.atlantis.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fr.nawrasg.atlantis.App;
import fr.nawrasg.atlantis.R;
import fr.nawrasg.atlantis.preferences.MainPreferenceFragment;

public class PINFragment extends Fragment{
	@Bind(R.id.txtPin)
	EditText txtPIN;
	private Button btnLogin;
	String pin;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View nView = inflater.inflate(R.layout.fragment_pin, container, false);
		ButterKnife.bind(this, nView);
		pin = App.getString(getActivity(), "pinCode");
		txtPIN.requestFocus();
		InputMethodManager nIMM = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		nIMM.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
		return nView;
	}

	@OnClick(R.id.btnLogin)
	public void login(){
		if(txtPIN.getText().toString().equals(pin)){
			closeKeyboard(getActivity(), txtPIN.getWindowToken());
			getFragmentManager().beginTransaction().replace(R.id.main_fragment, new MainPreferenceFragment()).commit();
		}else{
			Toast.makeText(getActivity(), "Mot de passe incorrect !", Toast.LENGTH_SHORT).show();
		}
	}
	
	private void closeKeyboard(Context c, IBinder windowToken) {
	    InputMethodManager nIMM = (InputMethodManager) c.getSystemService(Context.INPUT_METHOD_SERVICE);
	    nIMM.hideSoftInputFromWindow(windowToken, 0);
	}
}
