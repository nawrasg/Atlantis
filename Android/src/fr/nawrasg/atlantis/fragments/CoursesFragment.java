package fr.nawrasg.atlantis.fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

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
    private List<String> list = new ArrayList<String>();

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
        setHasOptionsMenu(true);
        return nView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(getResources().getInteger(R.integer.courses_layout), StaggeredGridLayoutManager.VERTICAL));
        ((MainActivity) getActivity()).setProgressBar(true);
        getCourses();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_courses, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itemCoursesAdd:
                addItem();
                return true;
            case R.id.itemCoursesNotify:
                sendNotification();
                return true;
        }
        return super.onOptionsItemSelected(item);
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

    public void sendNotification() {
        try {
            String nMsg = URLEncoder.encode(getResources().getString(R.string.fragment_courses_notification), "UTF-8");
            String nURL = App.getUri(mContext, App.NOTIFY) + "&msg=" + nMsg;
            Request nRequest = new Request.Builder()
                    .url(nURL)
                    .post(RequestBody.create(MediaType.parse("text/x-markdown; charset=utf-8"), ""))
                    .build();
            App.httpClient.newCall(nRequest).enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {

                }

                @Override
                public void onResponse(Response response) throws IOException {

                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void addItem() {
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_dialog_courses, null);
        AlertDialog.Builder inputBoxBuilder = new AlertDialog.Builder(mContext);
        inputBoxBuilder.setView(view);
        final AutoCompleteTextView txtAdd = (AutoCompleteTextView) view.findViewById(R.id.txtAutoAdd);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, list);
        txtAdd.setAdapter(adapter);
        inputBoxBuilder.setPositiveButton(getResources().getString(R.string.fragment_courses_button_add), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                String res = txtAdd.getText().toString();
                String result[] = res.split(",");
                if (result.length > 1) {
                    int n = result.length;
                    result[n - 1] = result[n - 1].replace(" ", "");
                    try {
                        Long l = Long.parseLong(result[n - 1]);
                        String name = result[0];
                        for (int i = 1; i < n - 2; i++) {
                            name += result[i];
                        }
                        name = URLEncoder.encode(name, "UTF-8");
                        postCourses(name, l);
                    } catch (Exception e) {
                        Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    try {
                        res = URLEncoder.encode(res, "UTF-8");
                        postCourses(res, 1);
                    } catch (Exception f) {
                        Toast.makeText(mContext, f.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        }).setNegativeButton(getResources().getString(R.string.fragment_courses_button_cancel), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        }).setTitle(getResources().getString(R.string.fragment_courses_add_product));
        AlertDialog inputBox = inputBoxBuilder.create();
        inputBox.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        inputBox.show();
    }

    private void postCourses(String name, long quantity) {
        String nURL = App.getUri(mContext, App.COURSES) + "&name=" + name + "&quantity=" + quantity;
        Request nRequest = new Request.Builder()
                .url(nURL)
                .post(RequestBody.create(MediaType.parse("text/x-markdown; charset=utf-8"), ""))
                .build();
        App.httpClient.newCall(nRequest).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.code() == 202) {
                    try {
                        JSONObject nItem = new JSONObject(response.body().string());
                        final Element nElement = new Element(nItem);
                        mAdapter.add(nElement);
                    } catch (JSONException e) {

                    }
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        mSwipeLayout.setRefreshing(false);
    }
}
