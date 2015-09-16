package fr.nawrasg.atlantis.other;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

public class GCMReceiver extends WakefulBroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		ComponentName comp = new ComponentName(context.getPackageName(),
                GCMService.class.getName());
		startWakefulService(context, (intent.setComponent(comp)));
		
        setResultCode(Activity.RESULT_OK);
	}

}
