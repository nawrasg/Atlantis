package fr.nawrasg.atlantis.adapters;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by Nawras on 14/07/2016.
 */

public class TabsAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> mFragmentList;
    private ArrayList<String> mFragmentLabelList;

    public TabsAdapter(FragmentManager fm) {
        super(fm);
        mFragmentLabelList = new ArrayList<>();
        mFragmentList = new ArrayList<>();
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentLabelList.get(position);
    }

    public void addTab(Fragment fragment, String label){
        mFragmentList.add(fragment);
        mFragmentLabelList.add(label);
    }
}
