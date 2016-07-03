package fr.nawrasg.atlantis.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import fr.nawrasg.atlantis.other.AtlantisSyncAdapter;

/**
 * Created by Nawras on 24/06/2016.
 */

public class SyncService extends Service {
    private static AtlantisSyncAdapter mSyncAdapter = null;
    private static final Object mSyncAdapterLock = new Object();

    @Override
    public void onCreate() {
        synchronized (mSyncAdapterLock){
            if(mSyncAdapter == null){
                mSyncAdapter = new AtlantisSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mSyncAdapter.getSyncAdapterBinder();
    }
}
