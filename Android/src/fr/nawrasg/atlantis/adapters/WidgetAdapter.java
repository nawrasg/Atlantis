package fr.nawrasg.atlantis.adapters;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import fr.nawrasg.atlantis.App;
import fr.nawrasg.atlantis.R;
import fr.nawrasg.atlantis.fragments.dialogs.LightDialogFragment;
import fr.nawrasg.atlantis.type.Alarm;
import fr.nawrasg.atlantis.type.Hue;
import fr.nawrasg.atlantis.type.Light;
import fr.nawrasg.atlantis.type.Scenario;

/**
 * Created by Nawras on 31/10/2016.
 */

public class WidgetAdapter extends RecyclerView.Adapter<WidgetAdapter.ViewHolder> {
    private Context mContext;

    private ArrayList<Object> mList;

    public static final int LIGHT = 0;
    public static final int SENSOR = 1;
    public static final int CAMERA = 2;
    public static final int SCENARIO = 3;

    public WidgetAdapter(Context context, ArrayList<Object> list){
        mContext = context;
        mList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View nView;
        switch(viewType){
            case LIGHT:
                nView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_light, parent, false);
                return new LightAdapter.LightViewHolder(nView);
            case SCENARIO:
                nView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_scenario, parent, false);
                return new ScenarioAdapter.ScenarioViewHolder(nView);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        switch(holder.getItemViewType()){
            case LIGHT:
                bindLightView((LightAdapter.LightViewHolder)holder, position);
                return;
            case SCENARIO:
                bindScenarioView((ScenarioAdapter.ScenarioViewHolder)holder, position);
                return;
        }
    }

    private void bindLightView(final LightAdapter.LightViewHolder holder, int position){
        Light nLight = (Light) mList.get(position);
        String nName = nLight.getName();
        //nName += getRoom(nLight);
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

    private boolean getPowerStatus(Light light) {
        Hue nLight = (Hue) light;
        if (nLight.isReachable()) {
            return nLight.isOn();
        } else {
            return false;
        }
    }

    private int getLightIntensity(Light light) {
        switch (light.getProtocol()) {
            case Light.HUE:
                return ((Hue) light).getBrightness();
        }
        return 0;
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

    private void bindScenarioView(final ScenarioAdapter.ScenarioViewHolder holder, int position){
        Scenario nScenario = (Scenario) mList.get(position);
        holder.label.setText(nScenario.getLabel());
        holder.action.setTag(nScenario);
        holder.action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Scenario nScenario = (Scenario) holder.action.getTag();
                try {
                    String nLabel = URLEncoder.encode(nScenario.getLabel(), "UTF-8");
                    String nURL = App.getUri(mContext, App.SCENARIOS) + "&scenario=" + nLabel;
                    Request nRequest = new Request.Builder().url(nURL).build();
                    App.httpClient.newCall(nRequest).enqueue(new Callback() {
                        @Override
                        public void onFailure(Request request, IOException e) {

                        }

                        @Override
                        public void onResponse(Response response) throws IOException {

                        }
                    });
                } catch (UnsupportedEncodingException e) {
                    Log.e("Atlantis", e.toString());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public int getItemViewType(int position) {
        Object nObject = mList.get(position);
        if(nObject instanceof Light || nObject instanceof Hue){
            return LIGHT;
        }else if(nObject instanceof Scenario){
            return SCENARIO;
        }
        return super.getItemViewType(position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
