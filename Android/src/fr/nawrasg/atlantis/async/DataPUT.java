package fr.nawrasg.atlantis.async;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import fr.nawrasg.atlantis.App;
import fr.nawrasg.atlantis.other.CheckConnection;

public class DataPUT extends AsyncTask<String, Void, String> {
	private Context mContext;
	private ProgressDialog mPD;
	private CheckConnection nIC;
	private Toast mToast;
	private int mResultCode;
	private boolean mShowPB = true;

	private String mURL, mAPI;

	public DataPUT(Context context) {
		mContext = context;
		mPD = new ProgressDialog(mContext);
		mPD.setCancelable(true);
		mPD.setMessage("Chargement...");
		nIC = new CheckConnection(mContext);
		mToast = Toast.makeText(mContext, "Pas de connexion Internet !", Toast.LENGTH_SHORT);
		mURL = App.getURL(mContext);
		mAPI = App.getAPI(mContext);
	}

	public DataPUT(Context context, boolean progressbar) {
		mContext = context;
		mShowPB = progressbar;
		mPD = new ProgressDialog(mContext);
		mPD.setCancelable(true);
		mPD.setMessage("Chargement...");
		nIC = new CheckConnection(mContext);
		mToast = Toast.makeText(mContext, "Pas de connexion Internet !", Toast.LENGTH_SHORT);
		mURL = App.getURL(mContext);
		mAPI = App.getAPI(mContext);
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
		if (mPD != null)
			mPD.dismiss();
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if (nIC.checkConnection() == CheckConnection.TYPE_NOT_CONNECTED) {
			this.cancel(true);
			mToast.show();
		}
		if(mShowPB) mPD.show();
	}

	protected String doInBackground(String... x) {
		BufferedReader nBR = null;
		String data = null;
		try {
			String nLink = "http://" + mURL + "/" + x[0] + "?api=" + mAPI;
			if(x.length > 1) nLink += "&" + x[1];
			URL nURL = new URL(nLink);
			HttpURLConnection nConnection = (HttpURLConnection) nURL.openConnection();
			nConnection.setDoOutput(true);
			nConnection.setRequestMethod("PUT");
			mResultCode = nConnection.getResponseCode();
			nBR = new BufferedReader(new InputStreamReader(nConnection.getInputStream()));
			StringBuffer nSB = new StringBuffer("");
			String l = "";
			while ((l = nBR.readLine()) != null) {
				nSB.append(l);
			}
			nBR.close();
			data = nSB.toString();
			nConnection.disconnect();
			return data;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		switch(getResultCode()){
			case 400:
				break;
			case 403:
				break;
		}
		if(mPD != null) mPD.dismiss();
	}

	protected int getResultCode(){
		return mResultCode;
	}
}