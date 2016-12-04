package fr.nawrasg.atlantis.adapters;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import fr.nawrasg.atlantis.App;
import fr.nawrasg.atlantis.R;
import fr.nawrasg.atlantis.other.AtlantisContract;
import fr.nawrasg.atlantis.type.Scenario;
import fr.nawrasg.atlantis.type.Widget;

/**
 * Created by Nawras on 31/10/2016.
 */

public class ScenarioAdapter extends RecyclerView.Adapter<ScenarioAdapter.ScenarioViewHolder> {
    private Context mContext;
    private ArrayList<Scenario> mList;
    private boolean mDashboard = false;
    private ContentResolver mResolver;

    public ScenarioAdapter(Context context, ArrayList<Scenario> list) {
        mContext = context;
        mList = list;
        mResolver = mContext.getContentResolver();
    }

    @Override
    public ScenarioViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View nView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_scenario, parent, false);
        return new ScenarioViewHolder(nView);
    }

    @Override
    public void onBindViewHolder(final ScenarioViewHolder holder, int position) {
        final Scenario nScenario = mList.get(position);
        holder.label.setText(nScenario.getLabel());
        holder.action.setTag(nScenario);
        holder.dashboard.setChecked(isDashboard(nScenario));
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
        if (mDashboard) {
            holder.dashboard.setVisibility(View.VISIBLE);
        } else {
            holder.dashboard.setVisibility(View.GONE);
        }
        holder.dashboard.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    addToDashboard(nScenario);
                } else {
                    removeFromDashboard(nScenario);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    private void addToDashboard(Scenario scenario) {
        ContentValues nValues = new ContentValues();
        nValues.put(AtlantisContract.Widgets.COLUMN_TYPE, Widget.WIDGET_SCENARIO);
        nValues.put(AtlantisContract.Widgets.COLUMN_ITEM, scenario.getLabel());
        mResolver.insert(AtlantisContract.Widgets.CONTENT_URI, nValues);
    }

    private void removeFromDashboard(Scenario scenario) {
        String nWhere = AtlantisContract.Widgets.COLUMN_ITEM + " = ? AND " + AtlantisContract.Widgets.COLUMN_TYPE + " = ?";
        String[] nArgs = {scenario.getLabel(), Widget.WIDGET_SCENARIO + ""};
        int i = mResolver.delete(AtlantisContract.Widgets.CONTENT_URI, nWhere, nArgs);
    }

    private boolean isDashboard(Scenario scenario) {
        String nWhere = AtlantisContract.Widgets.COLUMN_ITEM + " = ? AND " + AtlantisContract.Widgets.COLUMN_TYPE + " = ?";
        String[] nArgs = {scenario.getLabel(), Widget.WIDGET_SCENARIO + ""};
        Cursor nCursor = mResolver.query(AtlantisContract.Widgets.CONTENT_URI, null, nWhere, nArgs, null);
        if (nCursor != null && nCursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public void modeDashboard() {
        mDashboard = !mDashboard;
        notifyDataSetChanged();
    }

    static class ScenarioViewHolder extends WidgetAdapter.ViewHolder {
        @Bind(R.id.imgScenarioPlay)
        ImageView action;
        @Bind(R.id.txtScenarioLabel)
        TextView label;
        @Bind(R.id.cbScenarioDashboard)
        CheckBox dashboard;

        public ScenarioViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
