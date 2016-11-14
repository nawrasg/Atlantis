package fr.nawrasg.atlantis.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import fr.nawrasg.atlantis.App;
import fr.nawrasg.atlantis.R;
import fr.nawrasg.atlantis.activities.MainActivity;
import fr.nawrasg.atlantis.adapters.CoursesAdapter;
import fr.nawrasg.atlantis.type.Element;

/**
 * Created by Nawras on 04/11/2016.
 */

public class CoursesFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private Context mContext;
    private Handler mHandler;
    private CoursesAdapter mAdapter;
    private ArrayList<Element> mList;

    @Bind(R.id.rvCourses)
    RecyclerView mRecyclerView;
    @Bind(R.id.swipeCourses)
    SwipeRefreshLayout mSwipeLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View nView = inflater.inflate(R.layout.fragment_courses, container, false);
        ButterKnife.bind(this, nView);
        mContext = getActivity();
        mHandler = new Handler();
        mSwipeLayout.setOnRefreshListener(this);
        return nView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(getResources().getInteger(R.integer.courses_layout), StaggeredGridLayoutManager.VERTICAL));
        ((MainActivity) getActivity()).setProgressBar(true);
        getCourses();
    }

    private void getCourses() {
        String nURL = App.getUri(mContext, App.COURSES);
        Request nRequest = new Request.Builder()
                .url(nURL)
                .build();
        App.httpClient.newCall(nRequest).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                mList = new ArrayList<>();
                try {
                    JSONArray arr = new JSONArray(response.body().string());
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject jdata = arr.getJSONObject(i);
                        Element nElement = new Element(jdata);
                        mList.add(nElement);
                    }
                    mAdapter = new CoursesAdapter(mList);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mRecyclerView.setAdapter(mAdapter);
                            mSwipeLayout.setRefreshing(false);
                            ((MainActivity) getActivity()).setProgressBar(false);
                        }
                    });
                } catch (JSONException e) {
                    Log.e("Atlantis", e.toString());
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        mSwipeLayout.setRefreshing(false);
    }
}
