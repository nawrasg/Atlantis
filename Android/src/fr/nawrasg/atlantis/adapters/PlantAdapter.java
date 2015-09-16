package fr.nawrasg.atlantis.adapters;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import fr.nawrasg.atlantis.App;
import fr.nawrasg.atlantis.R;
import fr.nawrasg.atlantis.fragments.dialogs.PlantInfoDialogFragment;
import fr.nawrasg.atlantis.type.Plant;

public class PlantAdapter extends RecyclerView.Adapter<PlantAdapter.PlantViewHolder> {
	private Context mContext;
	private ArrayList<Plant> mList;

	static class PlantViewHolder extends RecyclerView.ViewHolder{
		@Bind(R.id.imgPlantPlant) ImageView imgPlant;
		@Bind(R.id.lblPlantTitle) TextView lblTitle;
		@Bind(R.id.lblPlantMoisture) TextView lblMoisture;
		@Bind(R.id.lblPlantLight) TextView lblLum;
		@Bind(R.id.lblPlantSoilTemperature) TextView lblSoilTemp;
		@Bind(R.id.lblPlantBattery) TextView lblBattery;
		@Bind(R.id.lblPlantAirTemperature) TextView lblAirTemp;
		@Bind(R.id.lblPlantTimestamp) TextView lblTimestamp;
		@Bind(R.id.lblPlantConductivity) TextView lblConductivity;
		@Bind(R.id.cvPlant) CardView cvPlant;

		public PlantViewHolder(View itemView) {
			super(itemView);
			ButterKnife.bind(this, itemView);
		}

	}

	public PlantAdapter(Context context, ArrayList<Plant> list) {
		mContext = context;
		mList = list;
	}

	@Override
	public int getItemCount() {
		return mList.size();
	}
	
	private Bundle setArgs(Plant plant) {
		Bundle nBundle = new Bundle();
		nBundle.putParcelable("plant", plant);
		return nBundle;
	}
	
	private void openPlantInfo(Plant plant){
		PlantInfoDialogFragment nPlantDialog = new PlantInfoDialogFragment();
		Bundle nArgs = setArgs(plant);
		nPlantDialog.setArguments(nArgs);
		nPlantDialog.show(((Activity)mContext).getFragmentManager(), "plant");
	}

	@Override
	public void onBindViewHolder(PlantViewHolder arg0, int arg1) {
		final Plant nPlant = mList.get(arg1);
		Picasso.with(mContext).load(App.getFullUrl(mContext) + "backend/home/plants/" + nPlant.getId() + ".png").into(arg0.imgPlant);
		if(!nPlant.getTitle().equals("null")) arg0.lblTitle.setText(nPlant.getTitle());
		arg0.lblAirTemp.setText("Air : " + nPlant.getAirTemperature() + "°C");
		arg0.lblSoilTemp.setText("Terreau : " + nPlant.getSoilTemperature() + "°C");
		arg0.lblBattery.setText(nPlant.getBatteryLevel() + "%");
		arg0.lblLum.setText(nPlant.getLight() + "");
		arg0.lblMoisture.setText(nPlant.getMoisture() + "%");
		arg0.lblTimestamp.setText(nPlant.getDate() + " " + nPlant.getTime());
		arg0.lblConductivity.setText(nPlant.getConductivity() + "");
		arg0.cvPlant.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				openPlantInfo(nPlant);
				return true;
			}
		});
	}

	@Override
	public PlantViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
		View nView = LayoutInflater.from(arg0.getContext()).inflate(R.layout.row_plant, arg0, false);
		return new PlantViewHolder(nView);
	}
}
