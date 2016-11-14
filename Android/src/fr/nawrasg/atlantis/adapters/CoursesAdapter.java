package fr.nawrasg.atlantis.adapters;

import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import fr.nawrasg.atlantis.App;
import fr.nawrasg.atlantis.R;
import fr.nawrasg.atlantis.type.Element;

/**
 * Created by Nawras on 04/11/2016.
 */

public class CoursesAdapter extends RecyclerView.Adapter<CoursesAdapter.CoursesViewHolder> {
    private ArrayList<Element> mList;
    private Handler mHandler;

    public CoursesAdapter(ArrayList<Element> list) {
        mList = list;
        mHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public CoursesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View nView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_course, parent, false);
        return new CoursesViewHolder(nView, this);
    }

    @Override
    public void onBindViewHolder(final CoursesViewHolder holder, int position) {
        Element nElement = mList.get(position);
        holder.title.setText(nElement.getName());
        holder.quantity.setText(nElement.getQuantity() + "");
        holder.cv.setTag(nElement);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public Element getItem(int position) {
        return mList.get(position);
    }

    public void update(final Element element) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (element != null) {
                    mList.remove(element);
                }
                notifyDataSetChanged();
            }
        });
    }

    public void add(Element element){
        mList.add(element);
        update(null);
    }

    static class CoursesViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {
        private CoursesAdapter mAdapter;
        @Bind(R.id.lblCoursesTitle)
        TextView title;
        @Bind(R.id.lblCoursesQuantity)
        TextView quantity;
        @Bind(R.id.cbCoursesDone)
        CheckBox done;
        @Bind(R.id.cvCourse)
        CardView cv;

        public CoursesViewHolder(View itemView, CoursesAdapter adapter) {
            super(itemView);
            mAdapter = adapter;
            ButterKnife.bind(this, itemView);
            itemView.setOnCreateContextMenuListener(this);
            done.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        done.setChecked(false);
                        deleteCourses((Element) cv.getTag());
                    }
                }
            });
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            MenuItem nIncrementItem = menu.add(0, R.string.menu_increment, 0, R.string.menu_increment);
            nIncrementItem.setOnMenuItemClickListener(this);
            MenuItem nDecrementItem = menu.add(0, R.string.menu_decrement, 1, R.string.menu_decrement);
            nDecrementItem.setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.string.menu_increment:
                    modifyCourses((Element) cv.getTag(), '+');
                    return true;
                case R.string.menu_decrement:
                    modifyCourses((Element) cv.getTag(), '-');
                    return true;
            }
            return false;
        }

        private void modifyCourses(final Element element, final char mode) {
            String nURL = App.getUri(App.getContext(), App.COURSES) + "&id=" + element.getID();
            switch (mode) {
                case '+':
                    nURL += "&quantity=" + (element.getQuantity() + 1);
                    break;
                case '-':
                    nURL += "&quantity=" + (element.getQuantity() - 1);
                    break;
            }
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
                    if (response.code() == 202) {
                        switch (mode) {
                            case '+':
                                element.increment();
                                break;
                            case '-':
                                element.decrement();
                                break;
                        }
                        mAdapter.update(null);
                    }
                }
            });
        }

        private void deleteCourses(final Element element) {
            String nURL = App.getUri(App.getContext(), App.COURSES) + "&id=" + element.getID();
            Request nRequest = new Request.Builder()
                    .url(nURL)
                    .delete()
                    .build();
            App.httpClient.newCall(nRequest).enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {

                }

                @Override
                public void onResponse(Response response) throws IOException {
                    if (response.code() == 202) {
                        mAdapter.update(element);
                    }
                }
            });
        }
    }
}
