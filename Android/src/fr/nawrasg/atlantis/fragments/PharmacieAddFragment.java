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

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
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
import fr.nawrasg.atlantis.activities.MainActivity;
import fr.nawrasg.atlantis.R;

public class PharmacieAddFragment extends Fragment{
	@Bind(R.id.txtPharmacieNom)
	EditText txtNom;
	@Bind(R.id.txtPharmacieQte)
	EditText txtQte;
	@Bind(R.id.txtPharmacieDate)
	EditText txtDate;
	private Calendar mCalendar = Calendar.getInstance();
	private DateFormat fmtDate = DateFormat.getDateInstance();
	private SimpleDateFormat mDate = new SimpleDateFormat("yyyy-MM-dd", Locale.FRANCE);
	private Context mContext;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View nView = inflater.inflate(R.layout.fragment_pharmacie_add, container, false);
		ButterKnife.bind(this, nView);
		mContext = getActivity();
		setHasOptionsMenu(true);
		return nView;
	}

	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.fragment_pharmacie_add, menu);
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.itemPharmacieAddSave:
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

	private void save() {
		if (txtNom.getText().toString().equals("") || txtDate.getText().toString().equals("")) {
			Toast.makeText(mContext, getResources().getString(R.string.fragment_pharmacie_add_save_warning), Toast.LENGTH_LONG).show();
			return;
		}
		try {
			String nom = URLEncoder.encode(txtNom.getText().toString(), "UTF-8");
			String qte = txtQte.getText().toString();
			String date = mDate.format(mCalendar.getTime());
			post("nom=" + nom + "&peremption=" + date + "&qte=" + qte);
			setNew();
		} catch (UnsupportedEncodingException e) {
			Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_LONG).show();
		}
	}

	private void setNew() {
		txtNom.setText("");
		txtQte.setText("");
		txtDate.setText("");
		((MainActivity) getActivity()).refreshFragment();
	}

	@OnClick(R.id.btnPharmacieAddDate)
	public void chooseDate(){
		new DatePickerDialog(mContext, d, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH),
				mCalendar.get(Calendar.DAY_OF_MONTH)).show();
	}

	private void post(String data){
		String nURL = App.getUri(mContext, App.PHARMACIE) + "&" + data;
		Request nRequest = new Request.Builder()
				.url(nURL)
				.post(RequestBody.create(MediaType.parse("text/x-markdown; charset=utf-8"), ""))
				.build();
		App.httpClient.newCall(nRequest).enqueue(new Callback() {
			@Override
			public void onFailure(Request request, IOException e) {

			}

			@Override
			public void onResponse(Response response) throws IOException {

			}
		});
	}
}
