package fr.nawrasg.atlantis.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import fr.nawrasg.atlantis.App;
import fr.nawrasg.atlantis.R;
import fr.nawrasg.atlantis.fragments.dialogs.LightDialogFragment;
import fr.nawrasg.atlantis.type.Hue;
import fr.nawrasg.atlantis.type.Light;
import fr.nawrasg.atlantis.type.Room;

/**
 * Created by Nawras on 29/10/2016.
 */

public class LightAdapter extends RecyclerView.Adapter<LightAdapter.LightViewHolder> {
    private Context mContext;
    private ArrayList<Light> mList;
    private ArrayList<Room> mRoomList;

    public LightAdapter(Context context, ArrayList<Light> list) {
        mContext = context;
        mList = list;
    }

    public LightAdapter(Context context, ArrayList<Light> list, ArrayList<Room> rooms) {
        mContext = context;
        mList = list;
        mRoomList = rooms;
    }

    @Override
    public LightViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View nView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_light, parent, false);
        return new LightViewHolder(nView);
    }

    @Override
    public void onBindViewHolder(final LightViewHolder holder, int position) {
        Light nLight = mList.get(position);
        String nName = nLight.getName();
        nName += getRoom(nLight);
        holder.lblLightName.setText(nName);
        holder.swtLightToggle.setTag(nLight);
        holder.swtLightToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Light nLight = (Hue) v.getTag();
                setPowerStatus(nLight, holder.swtLightToggle.isChecked());
            }
        });
        holder.sbLightIntensity.setTag(nLight);
        holder.sbLightIntensity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Light nLight = (Hue) holder.sbLightIntensity.getTag();
                setLightIntensity(nLight, seekBar.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        holder.imgLightIcon.setTag(nLight);
        holder.imgLightIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleLight((Light) holder.imgLightIcon.getTag());
            }
        });
        holder.imgLightIcon.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                LightDialogFragment nLightDialog = new LightDialogFragment();
                Bundle nArgs;
                nArgs = setArgs((Light) holder.imgLightIcon.getTag());
                nLightDialog.setArguments(nArgs);
                nLightDialog.show(((AppCompatActivity) mContext).getFragmentManager(), "light");
                return true;
            }
        });
        switch (nLight.getProtocol()) {
            case Light.HUE:
                holder.imgLightIcon.setImageResource(R.drawable.ng_bulb);
                holder.swtLightToggle.setChecked(getPowerStatus(nLight));
                int nValue = getLightIntensity(nLight);
                holder.sbLightIntensity.setProgress(nValue);
                break;
        }
    }

    private void toggleLight(Light light) {
        String nURL = App.getUri(mContext, App.LIGHTS) + "&toggle=" + ((Hue) light).getUID();
        Request nRequest = new Request.Builder()
                .url(nURL)
                .put(RequestBody.create(MediaType.parse("text/x-markdown; charset=utf-8"), ""))
                .build();
        App.httpClient.newCall(nRequest).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {

            }
        });
    }

    private void setPowerStatus(Light light, boolean status) {
        String nURL = App.getUri(mContext, App.LIGHTS) + "&on=" + ((Hue) light).getUID() + "&protocol=" + light.getProtocol() + "&value=" + status;
        Request nRequest = new Request.Builder()
                .url(nURL)
                .put(RequestBody.create(MediaType.parse("text/x-markdown; charset=utf-8"), ""))
                .build();
        App.httpClient.newCall(nRequest).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {

            }
        });
    }

    private void setLightIntensity(Light light, int value) {
        String nURL = App.getUri(mContext, App.LIGHTS) + "&bri=" + ((Hue) light).getUID() + "&protocol=" + light.getProtocol() + "&value=" + value;
        Request nRequest = new Request.Builder()
                .url(nURL)
                .put(RequestBody.create(MediaType.parse("text/x-markdown; charset=utf-8"), ""))
                .build();
        App.httpClient.newCall(nRequest).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {

            }
        });
    }

    private int getLightIntensity(Light light) {
        switch (light.getProtocol()) {
            case Light.HUE:
                return ((Hue) light).getBrightness();
        }
        return 0;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    private boolean getPowerStatus(Light light) {
        Hue nLight = (Hue) light;
        if (nLight.isReachable()) {
            return nLight.isOn();
        } else {
            return false;
        }
    }

    private String getRoom(Light light) {
        Room nRoom;
        if (mRoomList != null) {
            for (int i = 0; i < mRoomList.size(); i++) {
                nRoom = mRoomList.get(i);
                if (light.getRoom() != null && light.getRoom().equals(nRoom.getID())) {
                    return " (" + nRoom.getRoom() + ")";
                }
            }
        }
        return "";
    }

    private Bundle setArgs(Light light) {
        Bundle nBundle = new Bundle();
        nBundle.putParcelableArrayList("rooms", mRoomList);
        nBundle.putParcelable("light", light);
        return nBundle;
    }

    static class LightViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.cvLight)
        CardView cvLight;
        @Bind(R.id.imgLightIcon)
        ImageView imgLightIcon;
        @Bind(R.id.lblLightName)
        TextView lblLightName;
        @Bind(R.id.swtLightToggle)
        Switch swtLightToggle;
        @Bind(R.id.sbLightIntensity)
        SeekBar sbLightIntensity;

        public LightViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
