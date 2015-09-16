package fr.nawrasg.atlantis.fragments;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fr.nawrasg.atlantis.App;
import fr.nawrasg.atlantis.MainFragmentActivity;
import fr.nawrasg.atlantis.R;
import fr.nawrasg.atlantis.async.DataGET;
import fr.nawrasg.atlantis.async.DataPOST;

public class EntretienAddFragment extends Fragment {
	@Bind(R.id.txtEntretienNom)
	EditText txtNom;
	@Bind(R.id.txtEntretienQte)
	EditText txtQte;
	@Bind(R.id.txtEntretienDate)
	EditText txtDate;
	private Calendar nCalendar = Calendar.getInstance();
	private DateFormat fmtDate = DateFormat.getDateInstance();
	private SimpleDateFormat nDate = new SimpleDateFormat("yyyy-MM-dd", Locale.FRANCE);
	private Context mContext;
	private String mEAN;
	private boolean mEANFound = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View nView = inflater.inflate(R.layout.fragment_entretien_add, container, false);
		ButterKnife.bind(this, nView);
		mContext = getActivity();
		setHasOptionsMenu(true);
		return nView;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.fragment_entretien_add, menu);
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.itemEntretienAddSave:
				save();
				return true;
		}
		return false;
	}

	DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			GregorianCalendar g = new GregorianCalendar();
			view.setMinDate(g.getTimeInMillis());
			nCalendar.set(Calendar.YEAR, year);
			nCalendar.set(Calendar.MONTH, monthOfYear);
			nCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
			updatelblDate();
		}
	};

	private void updatelblDate() {
		txtDate.setText(fmtDate.format(nCalendar.getTime()));
	}

	private void save() {
		if (txtNom.getText().toString().equals("") || txtDate.getText().toString().equals("")) {
			Toast.makeText(mContext, "Merci de préciser le nom du produit ainsi que sa date de péremption !", Toast.LENGTH_LONG).show();
			return;
		}
		try {
			String nom = URLEncoder.encode(txtNom.getText().toString(), "UTF-8");
			String qte = txtQte.getText().toString();
			String date = nDate.format(nCalendar.getTime());
			new DataPOST(mContext).execute(App.ENTRETIEN, "nom=" + nom + "&peremption=" + date + "&qte=" + qte);
			setNew();
		} catch (UnsupportedEncodingException e) {
			Toast.makeText(mContext, "Erreur : " + e.getMessage(), Toast.LENGTH_LONG).show();
		}
	}

	@OnClick(R.id.btnEntretienAddScan)
	public void scanProduit(){
		IntentIntegrator scanIntegrator = new IntentIntegrator(getActivity());
		scanIntegrator.initiateScan();
	}

	@OnClick(R.id.btnEntretienAddDate)
	public void chooseDate(){
		new DatePickerDialog(mContext, d, nCalendar.get(Calendar.YEAR), nCalendar.get(Calendar.MONTH),
				nCalendar.get(Calendar.DAY_OF_MONTH)).show();
	}

	public void setEAN(String ean) {
		mEAN = ean;
		new EanGET(mContext).execute(App.ENTRETIEN, "ean=" + ean);
	}

	private void setNew() {
		txtNom.setText("");
		txtQte.setText("");
		txtDate.setText("");
		mEAN = "";
		((MainFragmentActivity) getActivity()).refreshFragment();
	}

	private class EanGET extends DataGET {

		public EanGET(Context context) {
			super(context);
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			try {
				if (result.equals("404")) {
					mEANFound = false;
				} else {
					txtNom.setText(result);
					mEANFound = true;
				}
			} catch (Exception e) {

			}
		}
	}

}