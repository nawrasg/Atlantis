package fr.nawrasg.atlantis.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import fr.nawrasg.atlantis.R;
import fr.nawrasg.atlantis.type.User;

public class UserAdapter extends ArrayAdapter<User> {
	private Context mContext;
	private List<User> mItemsList;
	private User mUser;

	static class UserViewHolder {
		public TextView name;
	}

	public UserAdapter(Context context, List<User> objects) {
		super(context, R.layout.row_user, objects);
		mContext = context;
		mItemsList = objects;
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		mUser = mItemsList.get(position);
		View nView = convertView;
		if (nView == null) {
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			nView = inflater.inflate(R.layout.row_user, parent, false);
			final UserViewHolder nHolder = new UserViewHolder();
			nHolder.name = (TextView) nView.findViewById(R.id.lblUserName);
			nView.setTag(nHolder);
		}
		UserViewHolder nHolder = new UserViewHolder();
		nHolder = (UserViewHolder) nView.getTag();
		nHolder.name.setText(mUser.getName());
		switch (mUser.getType()) {
			case -1:
				nHolder.name.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ng_user_red, 0, 0, 0);
				break;
			case 0:
				nHolder.name.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ng_user_blue, 0, 0, 0);
				break;
			case 1:
				nHolder.name.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ng_user_green, 0, 0, 0);
				break;
			case 2:
				nHolder.name.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ng_user_orange, 0, 0, 0);
				break;
			default:
				nHolder.name.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
				break;

		}
		return nView;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		mUser = mItemsList.get(position);
		View nView = convertView;
		if (nView == null) {
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			nView = inflater.inflate(R.layout.row_user, parent, false);
			final UserViewHolder nHolder = new UserViewHolder();
			nHolder.name = (TextView) nView.findViewById(R.id.lblUserName);
			nView.setTag(nHolder);
		}
		UserViewHolder nHolder = new UserViewHolder();
		nHolder = (UserViewHolder) nView.getTag();
		nHolder.name.setText(mUser.getName());
		switch (mUser.getType()) {
			case -1:
				nHolder.name.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ng_user_red, 0, 0, 0);
				break;
			case 0:
				nHolder.name.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ng_user_blue, 0, 0, 0);
				break;
			case 1:
				nHolder.name.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ng_user_green, 0, 0, 0);
				break;
			case 2:
				nHolder.name.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ng_user_orange, 0, 0, 0);
				break;
			default:
				nHolder.name.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
				break;

		}
		return nView;
	}

}
