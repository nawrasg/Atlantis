package fr.nawrasg.atlantis.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import fr.nawrasg.atlantis.App;
import fr.nawrasg.atlantis.R;
import fr.nawrasg.atlantis.type.Scenario;

/**
 * Created by Nawras GEORGI on 01/12/2015.
 */
public class ScenarioSimpleAdapter extends ArrayAdapter<Scenario> {
	private Context mContext;
	private List<Scenario> mList;
	private Scenario mScenario;

	static class ScenarioViewHolder {
		@Bind(R.id.imgScenarioPlay)
		ImageView action;
		@Bind(R.id.txtScenarioLabel)
		TextView label;

		public ScenarioViewHolder(View view) {
			ButterKnife.bind(this, view);
		}
	}

	public ScenarioSimpleAdapter(Context context, List<Scenario> objects) {
		super(context, R.layout.row_scenario_simple, objects);
		mContext = context;
		mList = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		mScenario = mList.get(position);
		View nView = convertView;
		if (nView == null) {
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			nView = inflater.inflate(R.layout.row_scenario_simple, parent, false);
			ScenarioViewHolder nHolder = new ScenarioViewHolder(nView);
			nView.setTag(nHolder);
			nHolder.action.setTag(mScenario);
		}
		final ScenarioViewHolder nHolder = (ScenarioViewHolder) nView.getTag();
		nHolder.label.setText(mScenario.getLabel());
		nHolder.action.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Scenario nScenario = (Scenario) nHolder.action.getTag();
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
		return nView;
	}
}
