package fr.nawrasg.atlantis.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import fr.nawrasg.atlantis.R;
import fr.nawrasg.atlantis.type.Song;

public class MusicAdapter extends ArrayAdapter<Song>{
	private Context mContext;
	private ArrayList<Song> mList;
	private int mWelcomeMusic;
	private Song mSong;
	
	static class MusicViewHolder {
		@Bind(R.id.imgSongIcon)
		ImageView imgMusicIcon;
		@Bind(R.id.lblSongTitle)
		TextView lblMusicName;
		@Bind(R.id.lblSongLength)
		TextView lblMusicLength;

		public MusicViewHolder(View view){
			ButterKnife.bind(this, view);
		}
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
			final MusicViewHolder nHolder = new MusicViewHolder(nView);
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
