package fr.nawrasg.atlantis.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import fr.nawrasg.atlantis.R;
import fr.nawrasg.atlantis.type.Element;

public class CoursesAdapter extends ArrayAdapter<Element> {
	private Context mContext;
	private List<Element> mList;
	private Element mElement;

	static class CoursesViewHolder {
		@Bind(R.id.lblCoursesTitle)
		TextView title;
		@Bind(R.id.lblCoursesQuantity)
		TextView quantity;
		@Bind(R.id.cbCoursesDone)
		CheckBox done;

		public CoursesViewHolder(View view) {
			ButterKnife.bind(this, view);
		}
	}

	public CoursesAdapter(Context context, List<Element> objects) {
		super(context, R.layout.row_course, objects);
		mContext = context;
		mList = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		mElement = mList.get(position);
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.row_course, parent, false);
			CoursesViewHolder nHolder = new CoursesViewHolder(convertView);
			nHolder.done.setTag(position);
			convertView.setTag(nHolder);
		}
		final CoursesViewHolder nHolder = (CoursesViewHolder) convertView.getTag();
		nHolder.title.setText(mElement.getName());
		nHolder.quantity.setText(mElement.getQuantity() + "");
		nHolder.done.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				nHolder.done.setChecked(false);
				int nPosition = ((Integer) nHolder.done.getTag()).intValue();
				mList.remove(nPosition);
				notifyDataSetChanged();
			}
		});
		return convertView;
	}
}
