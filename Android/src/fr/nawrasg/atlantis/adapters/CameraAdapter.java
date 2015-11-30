package fr.nawrasg.atlantis.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import fr.nawrasg.atlantis.App;
import fr.nawrasg.atlantis.R;
import fr.nawrasg.atlantis.type.Camera;

/**
 * Created by Nawras GEORGI on 17/11/2015.
 */
public class CameraAdapter extends RecyclerView.Adapter<CameraAdapter.CameraViewHolder> {
	private Context mContext;
	private ArrayList<Camera> mList;

	public CameraAdapter(Context context, ArrayList<Camera> list) {
		mContext = context;
		mList = list;
	}

	@Override
	public CameraViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View nView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_camera, parent, false);
		return new CameraViewHolder(nView);
	}

	@Override
	public void onBindViewHolder(CameraViewHolder holder, int position) {
		final Camera nCamera = mList.get(position);
		Picasso.with(mContext).load(App.getFullUrl(mContext) + App.Images + "?api=" + App.getAPI(mContext) + "&id=" + nCamera.getID()).into(holder.image);
		String nAlias = "";
		if(nCamera.getAlias().equals("null") || nCamera.getAlias().equals("")){
			nAlias = nCamera.getType();
		}else{
			nAlias = nCamera.getAlias();
		}
		String nRoomLabel = nCamera.getRoomLabel();
		if(!nRoomLabel.equals("null") && !nRoomLabel.equals("")){
			nAlias += " (" + nRoomLabel + ")";
		}
		holder.alias.setText(nAlias);
		holder.type.setText("Type : " + nCamera.getType());
		holder.imageUrl.setText("Image : " + nCamera.getImageUrl());
		holder.videoUrl.setText("Video : " + nCamera.getVideoUrl());
		holder.ipAddress.setText("Adresse IP : " + nCamera.getIP());
	}

	@Override
	public int getItemCount() {
		return mList.size();
	}

	static class CameraViewHolder extends RecyclerView.ViewHolder {
		@Bind(R.id.imgCameraImage) ImageView image;
		@Bind(R.id.txtCameraAlias) TextView alias;
		@Bind(R.id.txtCameraType) TextView type;
		@Bind(R.id.txtCameraIpAddress) TextView ipAddress;
		@Bind(R.id.txtCameraImageUrl) TextView imageUrl;
		@Bind(R.id.txtCameraVideoUrl) TextView videoUrl;

		public CameraViewHolder(View itemView) {
			super(itemView);
			ButterKnife.bind(this, itemView);
		}
	}
}
