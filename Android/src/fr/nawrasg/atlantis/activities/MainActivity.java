package fr.nawrasg.atlantis.activities;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Fragment;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.squareup.okhttp.OkHttpClient;

import butterknife.Bind;
import butterknife.ButterKnife;
import fr.nawrasg.atlantis.App;
import fr.nawrasg.atlantis.R;
import fr.nawrasg.atlantis.fragments.CameraFragment;
import fr.nawrasg.atlantis.fragments.ConnectedDevicesFragment;
import fr.nawrasg.atlantis.fragments.CoursesFragment;
import fr.nawrasg.atlantis.fragments.CuisineAddFragment;
import fr.nawrasg.atlantis.fragments.EntretienAddFragment;
import fr.nawrasg.atlantis.fragments.EntretienFragment;
import fr.nawrasg.atlantis.fragments.GPSFragment;
import fr.nawrasg.atlantis.fragments.HistoryFragment;
import fr.nawrasg.atlantis.fragments.HomeFragment;
import fr.nawrasg.atlantis.fragments.KitchenFragment;
import fr.nawrasg.atlantis.fragments.LightFragment;
import fr.nawrasg.atlantis.fragments.MapsFragment;
import fr.nawrasg.atlantis.fragments.MusicFragment;
import fr.nawrasg.atlantis.fragments.PharmacieAddFragment;
import fr.nawrasg.atlantis.fragments.PharmacieFragment;
import fr.nawrasg.atlantis.fragments.PlantFragment;
import fr.nawrasg.atlantis.fragments.ScenarioFragment;
import fr.nawrasg.atlantis.fragments.SensorsFragment;
import fr.nawrasg.atlantis.fragments.WidgetsFragment;
import fr.nawrasg.atlantis.other.AtlantisContract;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Context mContext;
    @Bind(R.id.drawer_layout)
    DrawerLayout nDrawerLayout;
    @Bind(R.id.navigation_view)
    NavigationView mNavigation;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.mainProgressBar)
    ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main);
        ButterKnife.bind(this);
        mContext = this;
        if (App.httpClient == null) {
            App.httpClient = new OkHttpClient();
        }
        if (!App.getPrefBoolean(mContext, "first_launch")) {
            createSyncAccount();
            startActivity(new Intent(this, IntroActivity.class));
        }
        mNavigation.setItemIconTintList(null);
        mNavigation.setNavigationItemSelectedListener(this);
        setNavigationHeader();
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_dehaze_white_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        loadHomeScreen();
        loadFragment(new PlantFragment(), false);
        setFrameWeight(App.FRAME_LAYOUT_25);
        handleIntent(getIntent());
    }

    private void loadHomeScreen() {
        switch (App.getPrefString(this, "home_screen")) {
            case "widgets":
                loadFragment(new WidgetsFragment(), true);
                break;
            case "plan":
                loadFragment(new HomeFragment(), true);
                break;
        }
    }

    public void setProgressBar(boolean visible) {
        if (mProgressBar == null) {
            return;
        }
        if (visible) {
            mProgressBar.setVisibility(View.VISIBLE);
        } else {
            mProgressBar.setVisibility(View.GONE);
        }
    }

    private void setNavigationHeader() {
        View nView = mNavigation.getRootView();
        TextView txtUsername = (TextView) nView.findViewById(R.id.txtNavigationUsername);
        txtUsername.setText(App.getString(this, "username"));
        TextView txtUserType = (TextView) nView.findViewById(R.id.txtNavigationUserType);
        switch (App.getInt(this, "usertype")) {
            case 0:
                txtUserType.setText(getString(R.string.user_account_type_admin));
                break;
            case 1:
                txtUserType.setText(getString(R.string.user_account_type_user));
                break;
            case 2:
                txtUserType.setText(getString(R.string.user_account_type_visitor));
                break;
            case -1:
                txtUserType.setText(getString(R.string.user_account_type_blocked));
                break;
        }
    }

    private void createSyncAccount() {
        Account nAccount = new Account(getString(R.string.app_name), AtlantisContract.AUTHORITY);
        AccountManager nManager = (AccountManager) getSystemService(ACCOUNT_SERVICE);
        boolean nResult = nManager.addAccountExplicitly(nAccount, null, null);
        if (nResult) {
            ContentResolver.addPeriodicSync(nAccount, AtlantisContract.AUTHORITY, Bundle.EMPTY, 21600L);
            ContentResolver.setSyncAutomatically(nAccount, AtlantisContract.AUTHORITY, true);
        }
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            // String query = intent.getStringExtra(SearchManager.QUERY);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                nDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment nFragment;
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (findViewById(R.id.main_fragment2) == null) {
            nFragment = getFragmentManager().findFragmentByTag("f1");
        } else {
            nFragment = getFragmentManager().findFragmentByTag("f2");
        }
        String nEAN;
        if (scanningResult != null) {
            nEAN = scanningResult.getContents();
            if (nEAN == null || nEAN.equals("")) {
                return;
            }

            if (nFragment instanceof CuisineAddFragment) {
                ((CuisineAddFragment) nFragment).setEAN(nEAN);
            } else if (nFragment instanceof EntretienAddFragment) {
                //((EntretienAddFragment) nFragment).setEAN(nEAN);
            }
        } else {
            nEAN = null;
        }

    }

    private void removeFragments() {
        Fragment nFragment = getFragmentManager().findFragmentByTag("f1");
        if (nFragment != null) {
            getFragmentManager().beginTransaction().remove(nFragment).commit();
        }
        nFragment = getFragmentManager().findFragmentByTag("f2");
        if (nFragment != null) {
            getFragmentManager().beginTransaction().remove(nFragment).commit();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Fragment nFragment;
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                nFragment = getFragmentManager().findFragmentByTag("f1");
                if (nFragment != null && nFragment instanceof MusicFragment) {
                    ((MusicFragment) nFragment).setVolume(true);
                }
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                nFragment = getFragmentManager().findFragmentByTag("f1");
                if (nFragment != null && nFragment instanceof MusicFragment) {
                    ((MusicFragment) nFragment).setVolume(false);
                }
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void refreshFragment() {
        Fragment nFragment = getFragmentManager().findFragmentByTag("f1");
        if (nFragment instanceof KitchenFragment) {
            ((KitchenFragment) nFragment).get();
        } else if (nFragment instanceof PharmacieFragment) {
            ((PharmacieFragment) nFragment).getItems();
        } else if (nFragment instanceof EntretienFragment) {
            ((EntretienFragment) nFragment).getItems();
        }
    }

    public void scanProduct() {
        IntentIntegrator scanIntegrator = new IntentIntegrator(this);
        scanIntegrator.initiateScan();
    }

    public void startFragment(Fragment fragment, boolean first, boolean remove) {
        if (remove) {
            removeFragments();
        }
        if (first) {
            getFragmentManager().beginTransaction().replace(R.id.main_fragment, fragment, "f1").commit();
        } else {
            if (findViewById(R.id.main_fragment2) != null) {
                getFragmentManager().beginTransaction().replace(R.id.main_fragment2, fragment, "f2").commit();
            }
        }
    }

    public void loadFragment(Fragment fragment, boolean first) {
        removeFragments();
        if (first) {
            getFragmentManager().beginTransaction().replace(R.id.main_fragment, fragment, "f1").commit();
        } else {
            if (findViewById(R.id.main_fragment2) != null) {
                getFragmentManager().beginTransaction().replace(R.id.main_fragment2, fragment, "f2").commit();
            }
        }
        nDrawerLayout.closeDrawers();
    }

    private void setFrameWeight(float weight) {
        FrameLayout nFrame2 = (FrameLayout) findViewById(R.id.main_fragment2);
        if (nFrame2 != null) {
            LinearLayout.LayoutParams nParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, weight);
            nFrame2.setLayoutParams(nParams);
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.itemNavigationHome:
                loadHomeScreen();
                loadFragment(new PlantFragment(), false);
                setFrameWeight(App.FRAME_LAYOUT_25);
                return true;
            case R.id.itemNavigationLights:
                loadFragment(new LightFragment(), true);
                loadFragment(new SensorsFragment(), false);
                setFrameWeight(App.FRAME_LAYOUT_50);
                return true;
            case R.id.itemNavigationSensors:
                loadFragment(new SensorsFragment(), true);
                setFrameWeight(App.FRAME_LAYOUT_50);
                return true;
            case R.id.itemNavigationCameras:
                loadFragment(new CameraFragment(), true);
                loadFragment(new ScenarioFragment(), false);
                setFrameWeight(App.FRAME_LAYOUT_50);
                return true;
            case R.id.itemNavigationScenarios:
                loadFragment(new ScenarioFragment(), true);
                setFrameWeight(App.FRAME_LAYOUT_50);
                return true;
            case R.id.itemNavigationCourses:
                loadFragment(new CoursesFragment(), true);
                setFrameWeight(App.FRAME_LAYOUT_50);
                return true;
            case R.id.itemNavigationCuisine:
                loadFragment(new KitchenFragment(), true);
                loadFragment(new CuisineAddFragment(), false);
                setFrameWeight(App.FRAME_LAYOUT_33);
                return true;
            case R.id.itemNavigationPharmacie:
                loadFragment(new PharmacieFragment(), true);
                loadFragment(new PharmacieAddFragment(), false);
                setFrameWeight(App.FRAME_LAYOUT_33);
                return true;
            case R.id.itemNavigationEntretien:
                loadFragment(new EntretienFragment(), true);
                loadFragment(new EntretienAddFragment(), false);
                setFrameWeight(App.FRAME_LAYOUT_33);
                return true;
            case R.id.itemNavigationMusic:
                loadFragment(new MusicFragment(), true);
                setFrameWeight(App.FRAME_LAYOUT_50);
                return true;
            case R.id.itemNavigationPlants:
                loadFragment(new PlantFragment(), true);
                setFrameWeight(App.FRAME_LAYOUT_50);
                return true;
            case R.id.itemNavigationGeo:
                loadFragment(new GPSFragment(), true);
                loadFragment(new MapsFragment(), false);
                setFrameWeight(App.FRAME_LAYOUT_66);
                return true;
            case R.id.itemNavigationDevices:
                loadFragment(new ConnectedDevicesFragment(), true);
                setFrameWeight(App.FRAME_LAYOUT_50);
                return true;
            case R.id.itemNavigationHistory:
                loadFragment(new HistoryFragment(), true);
                setFrameWeight(App.FRAME_LAYOUT_50);
                return true;
            case R.id.itemNavigationSettings:
                startActivity(new Intent(this, SettingsActivity.class));
                nDrawerLayout.closeDrawers();
                return true;
            case R.id.itemNavigationExit:
                finish();
                return true;
        }
        return false;
    }
}

