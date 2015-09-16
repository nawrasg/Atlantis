package fr.nawrasg.atlantis.adapters.spinner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import fr.nawrasg.atlantis.R;

public class AudioSourceAdapter extends ArrayAdapter<String>{
	private Context mContext;
	private static String[] mList = {"Audio", "Jack"};
	
	static class AudioSourceViewHolder {
		public ImageView icon;
	}
	
	public AudioSourceAdapter(Context context) {
		super(context, R.layout.row_audio_source, mList);
		mContext = context;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return getRow(position, convertView, parent);
	}
	
	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		return getRow(position, convertView, parent);
	}
	
	private View getRow(int position, View convertView, ViewGroup parent){
		View nView = convertView;
		if(nView == null){
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			nView = inflater.inflate(R.layout.row_audio_source, parent, false);
			AudioSourceViewHolder nHolder = new AudioSourceViewHolder();
			nHolder.icon = (ImageView) nView.findViewById(R.id.imgAudioSourceIcon);
			nView.setTag(nHolder);
		}
		AudioSourceViewHolder nHolder = (AudioSourceViewHolder) nView.getTag();
		switch (position) {
			case 0:
				nHolder.icon.setImageResource(R.drawable.ic_action_volume_on);
				break;
			case 1:
				nHolder.icon.setImageResource(R.drawable.ic_action_headphones);
				break;
		}
		return nView;
	}

}
