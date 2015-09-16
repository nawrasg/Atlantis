package fr.nawrasg.atlantis.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import fr.nawrasg.atlantis.R;
import fr.nawrasg.atlantis.type.Element;

public class CoursesAdapter extends ArrayAdapter<Element>{
	private Context mContext;
	private List<Element> mList;
	private Element mElement;
	
	static class CoursesViewHolder{
		public TextView title, quantity;
	}
	
	public CoursesAdapter(Context context, List<Element> objects) {
		super(context, R.layout.row_course, objects);
		mContext = context;
		mList = objects;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		mElement = mList.get(position); 
		View nView = convertView;
		if(nView == null){
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			nView = inflater.inflate(R.layout.row_course, parent, false);
			final CoursesViewHolder nHolder = new CoursesViewHolder();
			nHolder.title = (TextView)nView.findViewById(R.id.lblCoursesTitle);
			nHolder.quantity = (TextView)nView.findViewById(R.id.lblCoursesQuantity);
			nView.setTag(nHolder);
		}
		CoursesViewHolder nHolder = new CoursesViewHolder();
		nHolder = (CoursesViewHolder) nView.getTag();
		nHolder.title.setText(mElement.getName());
		nHolder.quantity.setText(mElement.getQuantity() + "");
		return nView;
	}

}
