package fr.nawrasg.atlantis.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import fr.nawrasg.atlantis.App;
import fr.nawrasg.atlantis.R;
import fr.nawrasg.atlantis.async.DataGET;
import fr.nawrasg.atlantis.type.Scenario;

/**
 * Created by Nawras GEORGI on 01/12/2015.
 */
public class ScenarioAdapter extends ArrayAdapter<Scenario> {
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

	public ScenarioAdapter(Context context, List<Scenario> objects) {
		super(context, R.layout.row_scenario, objects);
		mContext = context;
		mList = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		mScenario = mList.get(position);
		View nView = convertView;
		if (nView == null) {
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			nView = inflater.inflate(R.layout.row_scenario, parent, false);
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
					new DataGET(mContext).execute(App.SCENARIOS, "scenario=" + nLabel);
				} catch (UnsupportedEncodingException e) {
					Log.e("Atlantis", e.toString());
				}
			}
		});
		return nView;
	}
}
