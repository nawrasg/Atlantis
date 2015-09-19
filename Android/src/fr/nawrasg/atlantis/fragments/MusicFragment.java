package fr.nawrasg.atlantis.fragments;

import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import fr.nawrasg.atlantis.App;
import fr.nawrasg.atlantis.R;
import fr.nawrasg.atlantis.adapters.MusicAdapter;
import fr.nawrasg.atlantis.async.DataDELETE;
import fr.nawrasg.atlantis.async.DataGET;
import fr.nawrasg.atlantis.async.DataPOST;
import fr.nawrasg.atlantis.async.DataPUT;
import fr.nawrasg.atlantis.fragments.dialogs.SpeechDialogFragment;
import fr.nawrasg.atlantis.type.Song;

public class MusicFragment extends ListFragment implements OnClickListener, OnSeekBarChangeListener{
	private Context mContext;
	private ArrayList<Song> nList;
	private boolean isPlay, isOn;
	@Bind(R.id.sbMusicVolume)
	SeekBar nSB;
	@Bind(R.id.btnMusicPause)
	ImageButton btnPause;
	@Bind(R.id.btnMusicPlay)
	ImageButton btnPlay;
	@Bind(R.id.btnMusicRepeat)
	ImageButton btnRepeat;
	@Bind(R.id.btnMusicPrevious)
	ImageButton btnPrevious;
	@Bind(R.id.btnMusicNext)
	ImageButton btnNext;
	@Bind(R.id.btnMusicShuffle)
	ImageButton btnShuffle;
	private int mWelcomeMusic, mVolume;
	private MusicAdapter mAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View nView = inflater.inflate(R.layout.fragment_music, container, false);
		ButterKnife.bind(this, nView);
		getActivity().getActionBar().setIcon(R.drawable.ng_player);
		mContext = getActivity();
		mWelcomeMusic = -1;
		setHasOptionsMenu(true);
		return nView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		btnPlay.setOnClickListener(this);
		btnPause.setOnClickListener(this);
		btnRepeat.setOnClickListener(this);
		btnPrevious.setOnClickListener(this);
		btnNext.setOnClickListener(this);
		btnShuffle.setOnClickListener(this);
		nSB.setOnSeekBarChangeListener(this);
		getItems();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.fragment_music, menu);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		registerForContextMenu(getListView());
	}

	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);
		MenuItem nItemOn = menu.findItem(R.id.itemMusicOn);
		if (isOn) {
			nItemOn.setChecked(true);
		} else {
			nItemOn.setChecked(false);
		}
		MenuItem nItemWelcome = menu.findItem(R.id.itemMusicWelcomeMusic);
		if (mWelcomeMusic < 0) {
			nItemWelcome.setChecked(false);
		} else {
			nItemWelcome.setChecked(true);
		}
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater nMI = getActivity().getMenuInflater();
		nMI.inflate(R.menu.fragment_music_context, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.itemContextMusicWelcome:
				AdapterContextMenuInfo nInfo = (AdapterContextMenuInfo) item.getMenuInfo();
				int nPosition = nInfo.position;
				Song nSong = mAdapter.getItem(nPosition);
				if (nSong.getType().equals("song")) {
					new RefreshPUT(mContext).execute(App.MUSIC, "welcome=" + nSong.getID());
				} else {
					Toast.makeText(mContext, "Impossible de définir une playlist comme musique de bienvenue !",
							Toast.LENGTH_SHORT).show();
				}
				return true;
		}
		return super.onContextItemSelected(item);
	}

	public void setVolume(boolean increase) {
		if (increase) {
			nSB.incrementProgressBy(1);
		} else {
			nSB.incrementProgressBy(-1);
		}
	}

	private void getItems() {
		new MusicGET(mContext).execute(App.MUSIC);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.itemMusicOn:
				String nStatus = "";
				if (isOn) {
					isOn = false;
					nStatus = "off";
				} else {
					isOn = true;
					nStatus = "on";
				}
				new RefreshPUT(mContext).execute(App.MUSIC, "action=" + nStatus);
				return true;
			case R.id.itemMusicRefresh:
				new DataPOST(mContext).execute(App.MUSIC);
				return true;
			case R.id.itemMusicSpeech:
				SpeechDialogFragment nDialog = new SpeechDialogFragment();
				nDialog.show(getFragmentManager(), "speech");
				return true;
			case R.id.itemMusicWelcomeMusic:
				if (mWelcomeMusic < 0) {
					Toast.makeText(mContext, "Pour définir un morceau de bienvenue, maintenez l'appui sur le morceau désiré !",
							Toast.LENGTH_LONG).show();
					return true;
				}
				new DataDELETE(mContext).execute(App.MUSIC, "welcome=1");
				mWelcomeMusic = -1;
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		RefreshPUT nCmd = new RefreshPUT(mContext);
		Song nSong = nList.get(position);
		if (nSong.getType().equals("song")) {
			nCmd.execute(App.MUSIC, "action=play&id=" + nSong.getID());
		} else {
			nCmd.execute(App.MUSIC, "action=playlistplay&playlist=" + nSong.getID());
		}
	}

	@Override
	public void onClick(View v) {
		RefreshPUT nCmd = new RefreshPUT(mContext);
		switch (v.getId()) {
			case R.id.btnMusicPlay:
				if (isPlay) {
					nCmd.execute(App.MUSIC, "action=stop");
					isPlay = false;
					btnPlay.setImageResource(R.drawable.ic_action_play);
				} else {
					nCmd.execute(App.MUSIC, "action=start");
					isPlay = true;
					btnPlay.setImageResource(R.drawable.ic_action_stop);
				}
				break;
			case R.id.btnMusicPause:
				nCmd.execute(App.MUSIC, "action=pause");
				break;
			case R.id.btnMusicRepeat:
				nCmd.execute(App.MUSIC, "action=repeat");
				break;
			case R.id.btnMusicPrevious:
				nCmd.execute(App.MUSIC, "action=previous");
				break;
			case R.id.btnMusicNext:
				nCmd.execute(App.MUSIC, "action=next");
				break;
			case R.id.btnMusicShuffle:
				nCmd.execute(App.MUSIC, "action=shuffle");
				break;
		}
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		if (!fromUser) {
			new DataPUT(mContext).execute(App.MUSIC, "action=vol&level=" + seekBar.getProgress() + "&source=1");
		}
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		new DataPUT(mContext).execute(App.MUSIC, "action=vol&level=" + seekBar.getProgress() + "&source=1");
	}

	private void changeVolume(int vol) {
		nSB.setOnSeekBarChangeListener(null);
		nSB.setProgress(vol);
		nSB.setOnSeekBarChangeListener(this);
	}

	private class RefreshPUT extends DataPUT {

		public RefreshPUT(Context context) {
			super(context);
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			getItems();
		}

	}

	private class MusicGET extends DataGET {

		public MusicGET(Context context) {
			super(context);
		}

		@Override
		protected void onPostExecute(String result) {
			nList = new ArrayList<Song>();
			mWelcomeMusic = -1;
			try {
				JSONObject nJson = new JSONObject(result);
				int nVal = nJson.getInt("on");
				if (nVal == 1) {
					isOn = true;
				} else {
					isOn = false;
				}
				nVal = nJson.getInt("play");
				if (nVal == 1) {
					isPlay = true;
					btnPlay.setImageResource(R.drawable.ic_action_stop);
				} else {
					isPlay = false;
					btnPlay.setImageResource(R.drawable.ic_action_play);
				}
				mVolume = nJson.getInt("vol");
				changeVolume(mVolume);
				mWelcomeMusic = nJson.getJSONObject("welcome").getInt("music");
				JSONArray nArr = nJson.getJSONArray("songs");
				Song nSong;
				for (int i = 0; i < nArr.length(); i++) {
					nSong = new Song(nArr.getJSONObject(i));
					nList.add(nSong);
				}
				mAdapter = new MusicAdapter(mContext, mWelcomeMusic, nList);
				setListAdapter(mAdapter);
			} catch (JSONException e) {
				Toast.makeText(mContext, e.toString(), Toast.LENGTH_SHORT).show();
			}
			super.onPostExecute(result);
		}

	}

}