package fr.nawrasg.atlantis;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import fr.nawrasg.atlantis.adapters.TabsAdapter;
import fr.nawrasg.atlantis.preferences.ConnectionPreferenceFragment;
import fr.nawrasg.atlantis.preferences.DiversPreferenceFragment;
import fr.nawrasg.atlantis.preferences.SecurityPreferenceFragment;

/**
 * Created by Nawras on 14/07/2016.
 */

public class SettingsActivity extends AppCompatActivity {

    @Bind(R.id.tabsSettingsTabs)
    TabLayout mTabs;
    @Bind(R.id.vpSettingsViewPager)
    ViewPager mViewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setupViewPager();
        mTabs.setupWithViewPager(mViewPager);
    }

    private void setupViewPager() {
        TabsAdapter nAdapter = new TabsAdapter(getFragmentManager());
        nAdapter.addTab(new ConnectionPreferenceFragment(), getString(R.string.xml_pref_main_connection_title));
        nAdapter.addTab(new SecurityPreferenceFragment(), getString(R.string.xml_pref_main_security_title));
        nAdapter.addTab(new DiversPreferenceFragment(), getString(R.string.xml_pref_main_divers_title));
        mViewPager.setAdapter(nAdapter);
    }
}