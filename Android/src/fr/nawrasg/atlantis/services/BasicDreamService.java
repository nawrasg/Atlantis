package fr.nawrasg.atlantis.services;

import android.service.dreams.DreamService;

import fr.nawrasg.atlantis.R;

/**
 * Created by Nawras GEORGI on 15/12/2015.
 */
public class BasicDreamService extends DreamService {

	@Override
	public void onDreamingStarted() {
		super.onDreamingStarted();
		setFullscreen(true);
		setScreenBright(false);
		setContentView(R.layout.dream_basic);
	}
}
