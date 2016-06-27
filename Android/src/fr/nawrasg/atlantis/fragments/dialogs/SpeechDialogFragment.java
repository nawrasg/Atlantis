package fr.nawrasg.atlantis.fragments.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import fr.nawrasg.atlantis.App;
import fr.nawrasg.atlantis.R;

public class SpeechDialogFragment extends DialogFragment {
	private Context mContext;
	private EditText txtText;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		mContext = getActivity();
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View nView = inflater.inflate(R.layout.fragment_speech, null);
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setPositiveButton("Envoyer", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				try {
					String nText = URLEncoder.encode(txtText.getText().toString(), "UTF-8");
					String nURL = App.getFullUrl(mContext) + App.SPEECH + "?api=" + App.getAPI(mContext) + "&speaker&text=" + nText;
					Request nRequest = new Request.Builder().url(nURL).build();
					App.httpClient.newCall(nRequest).enqueue(new Callback() {
						@Override
						public void onFailure(Request request, IOException e) {

						}

						@Override
						public void onResponse(Response response) throws IOException {

						}
					});
				} catch (UnsupportedEncodingException e) {

				}
				dismiss();
			}
		}).setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dismiss();
			}
		}).setView(nView);
		onViewCreated(nView, savedInstanceState);
		return builder.create();
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		txtText = (EditText) view.findViewById(R.id.txtSpeechDialogText);
	}

}
