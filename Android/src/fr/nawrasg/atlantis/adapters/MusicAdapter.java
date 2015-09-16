package fr.nawrasg.atlantis.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import fr.nawrasg.atlantis.R;
import fr.nawrasg.atlantis.type.Song;

public class MusicAdapter extends ArrayAdapter<Song>{
	private Context mContext;
	private ArrayList<Song> mList;
	private int mWelcomeMusic;
	private Song mSong;
	
	static class MusicViewHolder {
		public ImageView imgMusicIcon;
		public TextView lblMusicName;
		public TextView lblMusicLength;		
	}
	
	public MusicAdapter(Context context, int welcome, ArrayList<Song> list){
		super(context, R.layout.row_music, list);
		mContext = context;
		mList = list;
		mWelcomeMusic = welcome;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		mSong = mList.get(position);
		View nView = convertView;
		if (nView == null) {
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			nView = inflater.inflate(R.layout.row_music, parent, false);
			final MusicViewHolder nHolder = new MusicViewHolder();
			nHolder.lblMusicName = (TextView) nView.findViewById(R.id.lblSongTitle);
			nHolder.imgMusicIcon = (ImageView) nView.findViewById(R.id.imgSongIcon);
			nHolder.lblMusicLength = (TextView) nView.findViewById(R.id.lblSongLength);
			nView.setTag(nHolder);
		}

		MusicViewHolder nHolder = (MusicViewHolder) nView.getTag();
		String nName = mSong.getTitle();
		nHolder.lblMusicName.setText(nName);
		nHolder.lblMusicLength.setText(mSong.getLength());
		if(mSong.getType().equals("playlist")){
			nHolder.imgMusicIcon.setImageResource(R.drawable.ng_note_double);						
		}else{
			if(mSong.getID() == mWelcomeMusic){
				nHolder.imgMusicIcon.setImageResource(R.drawable.ng_note_white);									
			}else{
				nHolder.imgMusicIcon.setImageResource(R.drawable.ng_note);									
			}
		}
		return nView;
	}
}
