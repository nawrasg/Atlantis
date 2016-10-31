package fr.nawrasg.atlantis.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import fr.nawrasg.atlantis.App;
import fr.nawrasg.atlantis.R;
import fr.nawrasg.atlantis.type.Scenario;

/**
 * Created by Nawras on 31/10/2016.
 */

public class ScenarioAdapter extends RecyclerView.Adapter<ScenarioAdapter.ScenarioViewHolder> {
    private Context mContext;
    private ArrayList<Scenario> mList;

    public ScenarioAdapter(Context context, ArrayList<Scenario> list){
        mContext = context;
        mList = list;
    }

    @Override
    public ScenarioViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View nView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_scenario, parent, false);
        return new ScenarioViewHolder(nView);
    }

    @Override
    public void onBindViewHolder(final ScenarioViewHolder holder, int position) {
        Scenario nScenario = mList.get(position);
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

    static class ScenarioViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.imgScenarioPlay)
        ImageView action;
        @Bind(R.id.txtScenarioLabel)
        TextView label;

        public ScenarioViewHolder(View itemView){
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
