package fr.nawrasg.atlantis.fragments;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
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
import fr.nawrasg.atlantis.adapters.spinner.CuisinePlaceAdapter;
import fr.nawrasg.atlantis.async.DataGET;
import fr.nawrasg.atlantis.async.DataPOST;

public class CuisineAddFragment extends Fragment{
	private Calendar mCalendar = Calendar.getInstance();
	private DateFormat fmtDate = DateFormat.getDateInstance();
	private SimpleDateFormat nDate = new SimpleDateFormat("yyyy-MM-dd", Locale.FRANCE);
	@Bind(R.id.txtCuisineAddNom)
	EditText txtNom;
	@Bind(R.id.txtCuisineDate)
	EditText txtDate;
	@Bind(R.id.txtCuisineQte)
	EditText txtQte;
	private String mEAN;
	private Context mContext;
	private boolean eanFound = false;
	private Spinner nSpinner;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View nView = inflater.inflate(R.layout.fragment_cuisine_add, container, false);
		ButterKnife.bind(this, nView);
		mContext = getActivity();
		setHasOptionsMenu(true);
		return nView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		nSpinner = (Spinner) view.findViewById(R.id.spinnerEndroit);
		nSpinner.setAdapter(new CuisinePlaceAdapter(mContext));
	}

	public void onCreateOptionsMenu(android.view.Menu menu, android.view.MenuInflater inflater) {
		inflater.inflate(R.menu.fragment_cuisine_add, menu);
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.itemCuisineAddSave:
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
			mCalendar.set(Calendar.YEAR, year);
			mCalendar.set(Calendar.MONTH, monthOfYear);
			mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
			updatelblDate();
		}
	};

	private void updatelblDate() {
		txtDate.setText(fmtDate.format(mCalendar.getTime()));
	}

	public void setEAN(String ean) {
		mEAN = ean;
		new EanGET(mContext, true).execute(App.EAN, "ean=" + ean);
	}

	@OnClick(R.id.btnCuisineAddScan)
	public void scanProduit(){
		IntentIntegrator scanIntegrator = new IntentIntegrator(getActivity());
		scanIntegrator.initiateScan();
	}

	@OnClick(R.id.btnCuisineAddDate)
	public void chooseDate(){
		new DatePickerDialog(mContext, d, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH),
				mCalendar.get(Calendar.DAY_OF_MONTH)).show();
	}

	private void save() {
		if (txtNom.getText().toString().equals("") || txtDate.getText().toString().equals("")) {
			Toast.makeText(mContext, "Merci de préciser le nom du produit ainsi que sa date de péremption !", Toast.LENGTH_LONG).show();
			return;
		}
		try {
			String nom = URLEncoder.encode(txtNom.getText().toString(), "UTF-8");
			String qte = txtQte.getText().toString();
			String date = nDate.format(mCalendar.getTime());
			String nURL = "peremption=" + date + "&quantite=" + qte;
			if(eanFound){
				nURL += "&element=" + mEAN;
			}else{
				if(mEAN == ""){
					nURL += "&element=" + nom;
				}else{
					nURL += "&element=" + mEAN + "&ean=" + nom;
				}
			}
			new DataPOST(mContext).execute(App.CUISINE, nURL);
			setNew();
		} catch (UnsupportedEncodingException e) {
			Toast.makeText(mContext, e.toString(), Toast.LENGTH_LONG).show();
		}
	}

	private void setNew() {
		txtNom.setText("");
		txtQte.setText("");
		txtDate.setText("");
		mEAN = "";
		((MainFragmentActivity) getActivity()).refreshFragment();
	}
	
	private class EanGET extends DataGET{

		public EanGET(Context context, boolean progressbar) {
			super(context, progressbar);
		}
		
		@Override
		protected void onPostExecute(String result) {
			try {
				if (result.equals("404")) {
					eanFound = false;
				} else {
					txtNom.setText(result);
					eanFound = true;
				}
			} catch (Exception e) {
				Toast.makeText(mContext, e.toString(), Toast.LENGTH_LONG).show();
			}
			super.onPostExecute(result);
		}
		
	}

}
