package fr.nawrasg.atlantis.fragments.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fr.nawrasg.atlantis.App;
import fr.nawrasg.atlantis.R;
import fr.nawrasg.atlantis.async.DataPUT;
import fr.nawrasg.atlantis.type.Plant;

public class PlantInfoDialogFragment extends DialogFragment{
	private Context mContext;
	@Bind(R.id.txtPlantDialogTitle)
	EditText txtTitle;
	@Bind(R.id.imgPlantDialogPicture)
	ImageView imgPlant;
	private Plant nPlant;
	
	final int PICTURE_GET = 1;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		mContext = getActivity();
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View nView = inflater.inflate(R.layout.fragment_dialog_plant_info, null);
		ButterKnife.bind(this, nView);
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setPositiveButton("Enregistrer", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				String nTitle;
				try {
					nTitle = URLEncoder.encode(txtTitle.getText().toString(), "UTF-8");
					String nURL = "id=" + nPlant.getId() + "&title=" + nTitle;
					new DataPUT(mContext).execute(App.PLANTE, nURL);
					dismiss();
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
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
		getSettings();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			switch(requestCode){
				case PICTURE_GET:
					try {
						Bitmap nPicture = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), data.getData());
						imgPlant.setImageBitmap(nPicture);
						ByteArrayOutputStream baos = new ByteArrayOutputStream();  
						nPicture.compress(Bitmap.CompressFormat.PNG, 100, baos); //bm is the bitmap object   
						byte[] b = baos.toByteArray(); 
						String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
					} catch (FileNotFoundException e) {
						Toast.makeText(mContext, e.toString(), Toast.LENGTH_LONG).show();
					} catch (IOException e) {
						Toast.makeText(mContext, e.toString(), Toast.LENGTH_LONG).show();
					}
					break;
			}
		}
	}

	private void getSettings() {
		nPlant = getArguments().getParcelable("plant");
		if (!nPlant.getTitle().equals("null"))
			txtTitle.setText(nPlant.getTitle());
		imgPlant.setImageBitmap(nPlant.getPicture());
	}

	@OnClick(R.id.btnPlantDialogPicture)
	public void takePicture() {
		Intent intent = new Intent(Intent.ACTION_PICK, Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(intent, PICTURE_GET);
	}
}
