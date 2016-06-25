package fr.nawrasg.atlantis.fragments;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
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
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
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
import fr.nawrasg.atlantis.other.AtlantisContract;

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
	private OkHttpClient mClient;
	ContentResolver mResolver;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View nView = inflater.inflate(R.layout.fragment_cuisine_add, container, false);
		ButterKnife.bind(this, nView);
		mContext = getActivity();
		mClient = new OkHttpClient();
		mResolver = mContext.getContentResolver();
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
		getEan(ean);
	}

	private void getEan(String ean){
		Cursor nCursor = mResolver.query(Uri.withAppendedPath(AtlantisContract.Ean.CONTENT_URI, ean), null, null, null, null);
		if(nCursor.getCount() > 0){
			nCursor.moveToFirst();
			txtNom.setText(nCursor.getString(1));
			eanFound = true;
		}else{
			eanFound = false;
		}
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
			postItem(nURL);
			setNew();
		} catch (UnsupportedEncodingException e) {
			Toast.makeText(mContext, e.toString(), Toast.LENGTH_LONG).show();
		}
	}

	private void postItem(String data){
		String nURL = App.getFullUrl(mContext) + App.CUISINE + "?api=" + App.getAPI(mContext) + "&" + data;
		Request nRequest = new Request.Builder()
				.url(nURL)
				.post(RequestBody.create(MediaType.parse("text/x-markdown; charset=utf-8"), ""))
				.build();
		mClient.newCall(nRequest).enqueue(new Callback() {
			@Override
			public void onFailure(Request request, IOException e) {

			}

			@Override
			public void onResponse(Response response) throws IOException {

			}
		});
	}

	private void setNew() {
		txtNom.setText("");
		txtQte.setText("");
		txtDate.setText("");
		mEAN = "";
		((MainFragmentActivity) getActivity()).refreshFragment();
	}

}
