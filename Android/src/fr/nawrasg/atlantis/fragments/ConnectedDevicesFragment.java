package fr.nawrasg.atlantis.fragments;

import android.app.ListFragment;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fr.nawrasg.atlantis.App;
import fr.nawrasg.atlantis.MainFragmentActivity;
import fr.nawrasg.atlantis.R;
import fr.nawrasg.atlantis.adapters.DeviceAdapter;
import fr.nawrasg.atlantis.fragments.dialogs.DeviceDialogFragment;
import fr.nawrasg.atlantis.fragments.dialogs.DeviceInfoDialogFragment;
import fr.nawrasg.atlantis.other.AtlantisContract;
import fr.nawrasg.atlantis.type.Device;
import fr.nawrasg.atlantis.type.User;

public class ConnectedDevicesFragment extends ListFragment {
    private Context mContext;
    private List<Device> nList;
    private DeviceAdapter mAdapter;
    private Device mDevice;
    private ArrayList<User> mUserList;
    private Handler mHandler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View nView = inflater.inflate(R.layout.fragment_devices, container, false);
        mContext = getActivity();
        mHandler = new Handler();
        setHasOptionsMenu(true);
        return nView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getItems();
        getStatus();
    }

    private void getItems() {
        ContentResolver nResolver = mContext.getContentResolver();
        Cursor nCursor = nResolver.query(AtlantisContract.Devices.CONTENT_URI, null, null, null, null);
        if (nCursor.moveToFirst()) {
            nList = new ArrayList<Device>();
            do {
                Device nDevice = new Device(nCursor);
                nList.add(nDevice);
            } while (nCursor.moveToNext());
            mAdapter = new DeviceAdapter(mContext, nList);
            setListAdapter(mAdapter);
        }
    }

    private void getStatus() {
        String nURL = App.getUri(mContext, App.DEVICES);
        Request nRequest = new Request.Builder().url(nURL).build();
        App.httpClient.newCall(nRequest).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {

                mUserList = new ArrayList<User>();
                try {
                    JSONObject nJson = new JSONObject(response.body().string());
                    JSONArray nDeviceArr = nJson.getJSONArray("devices");
                    for (int i = 0; i < nDeviceArr.length(); i++) {
                        JSONObject json = nDeviceArr.getJSONObject(i);
                        Device nDevice = new Device(json);
                        int nIndex = nList.indexOf(nDevice);
                        nList.get(nIndex).update(nDevice);
                    }
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.notifyDataSetChanged();
                        }
                    });
                    JSONArray nUserArr = nJson.getJSONArray("users");
                    mUserList.add(new User());
                    for (int i = 0; i < nUserArr.length(); i++) {
                        mUserList.add(new User(nUserArr.getJSONObject(i)));
                    }
                } catch (JSONException e) {
                    Log.e("Atlantis", e.toString());
                }
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_devices, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itemDevicesAdd:
                ((MainFragmentActivity) getActivity()).loadFragment(new DevicesAddFragment(), true);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater nMI = getActivity().getMenuInflater();
        nMI.inflate(R.menu.context_devices, menu);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        registerForContextMenu(getListView());
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        int position = info.position;
        mDevice = mAdapter.getItem(position);
        switch (item.getItemId()) {
            case R.id.menuDeviceInfo:
                openDeviceInfo(mDevice);
                return true;
            case R.id.menuDeviceDelete:
                deleteItem(mDevice);
                return true;
        }
        return super.onContextItemSelected(item);
    }

    private void openDeviceInfo(Device device) {
        DeviceInfoDialogFragment nDeviceDialog = new DeviceInfoDialogFragment();
        Bundle nArgs = setArgs(device);
        nDeviceDialog.setArguments(nArgs);
        nDeviceDialog.show(getFragmentManager(), "device");
    }

    private void openDeviceCommands(Device device) {
        DeviceDialogFragment nDeviceDialog = new DeviceDialogFragment();
        Bundle nArgs = setArgs(device);
        nDeviceDialog.setArguments(nArgs);
        nDeviceDialog.show(getFragmentManager(), "device");
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Device nDevice = mAdapter.getItem(position);
        if (nDevice.getNom().equals("Serveur")) {

        } else {
            openDeviceCommands(nDevice);
        }
    }


    private Bundle setArgs(Device device) {
        Bundle nBundle = new Bundle();
        nBundle.putParcelable("device", device);
        nBundle.putParcelableArrayList("users", mUserList);
        return nBundle;
    }

    private void deleteItem(Device device) {
        String nURL = App.getUri(mContext, App.DEVICES) + "&id=" + device.getID();
        Request nRequest = new Request.Builder().url(nURL).delete().build();
        App.httpClient.newCall(nRequest).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.body().string().equals("200")) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.remove(mDevice);
                            mAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        });
    }
}
