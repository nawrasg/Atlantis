package fr.nawrasg.atlantis.activities;

import android.accounts.Account;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import butterknife.Bind;
import butterknife.ButterKnife;
import fr.nawrasg.atlantis.App;
import fr.nawrasg.atlantis.R;
import fr.nawrasg.atlantis.adapters.TabsAdapter;
import fr.nawrasg.atlantis.fragments.preferences.ConnectionPreferenceFragment;
import fr.nawrasg.atlantis.fragments.preferences.DiversPreferenceFragment;
import fr.nawrasg.atlantis.fragments.preferences.SecurityPreferenceFragment;
import fr.nawrasg.atlantis.other.AtlantisContract;

/**
 * Created by Nawras on 14/07/2016.
 */

public class SettingsActivity extends AppCompatActivity {
    private Context mContext;
    private boolean mLaunchSync;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.tabsSettingsTabs)
    TabLayout mTabs;
    @Bind(R.id.vpSettingsViewPager)
    ViewPager mViewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mContext = this;
        mLaunchSync = false;
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setupViewPager();
        mTabs.setupWithViewPager(mViewPager);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (App.isPIN(mContext)) {
            enterPIN();
        }
    }

    @Override
    protected void onDestroy() {
        if (mLaunchSync) {
            Account nAccount = new Account(getString(R.string.app_name), AtlantisContract.AUTHORITY);
            ContentResolver.requestSync(nAccount, AtlantisContract.AUTHORITY, Bundle.EMPTY);
        }
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Fragment nFragment = getFragmentManager().findFragmentByTag("android:switcher:" + R.id.vpSettingsViewPager + ":" + mViewPager.getCurrentItem());
        switch (requestCode) {
            case App.PERMISSION_LOCATION:
                boolean nResult = false;
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    nResult = true;
                }
                if (nFragment instanceof SecurityPreferenceFragment) {
                    ((SecurityPreferenceFragment) nFragment).setPermissionResult(App.PERMISSION_LOCATION, nResult);
                }
                break;
        }
    }

    public void launchSync() {
        mLaunchSync = true;
    }

    private void setupViewPager() {
        TabsAdapter nAdapter = new TabsAdapter(getFragmentManager());
        nAdapter.addTab(new ConnectionPreferenceFragment(), getString(R.string.xml_pref_main_connection_title));
        nAdapter.addTab(new SecurityPreferenceFragment(), getString(R.string.xml_pref_main_security_title));
        nAdapter.addTab(new DiversPreferenceFragment(), getString(R.string.xml_pref_main_divers_title));
        mViewPager.setAdapter(nAdapter);
    }

    public void enterPIN() {
        View view = getLayoutInflater().inflate(R.layout.fragment_dialog_pin, null);
        final EditText txtCode = (EditText) view.findViewById(R.id.txtPin);
        final TextInputLayout txtCodeLayout = (TextInputLayout) view.findViewById(R.id.txtPinLayout);
        AlertDialog.Builder inputBoxBuilder = new AlertDialog.Builder(this);
        inputBoxBuilder.setView(view);
        inputBoxBuilder.setCancelable(false);
        inputBoxBuilder.setPositiveButton(getResources().getString(R.string.fragment_settings_pin_login), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setNegativeButton(getResources().getString(R.string.fragment_settings_pin_cancel), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        final AlertDialog inputBox = inputBoxBuilder.create();
        inputBox.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        inputBox.show();
        inputBox.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtCode.getText().toString().equals(App.getString(mContext, "pinCode"))) {
                    inputBox.dismiss();
                } else {
                    txtCode.setText("");
                    txtCodeLayout.setError(getString(R.string.fragment_settings_pin_password_incorrect));
                }
            }
        });
        txtCode.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            if (txtCode.getText().toString().equals(App.getString(mContext, "pinCode"))) {
                                inputBox.dismiss();
                            } else {
                                txtCode.setText("");
                                txtCodeLayout.setError(getString(R.string.fragment_settings_pin_password_incorrect));
                            }
                            return true;
                    }
                }
                return false;
            }
        });
    }
}
