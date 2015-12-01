package fr.nawrasg.atlantis;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import fr.nawrasg.atlantis.adapters.DrawerAdapter;
import fr.nawrasg.atlantis.fragments.CameraFragment;
import fr.nawrasg.atlantis.fragments.ConnectedDevicesFragment;
import fr.nawrasg.atlantis.fragments.CoursesFragment;
import fr.nawrasg.atlantis.fragments.CuisineAddFragment;
import fr.nawrasg.atlantis.fragments.CuisineFragment;
import fr.nawrasg.atlantis.fragments.EntretienAddFragment;
import fr.nawrasg.atlantis.fragments.EntretienFragment;
import fr.nawrasg.atlantis.fragments.GPSFragment;
import fr.nawrasg.atlantis.fragments.HistoryFragment;
import fr.nawrasg.atlantis.fragments.HomeFragment;
import fr.nawrasg.atlantis.fragments.LightFragment;
import fr.nawrasg.atlantis.fragments.MapsFragment;
import fr.nawrasg.atlantis.fragments.MusicFragment;
import fr.nawrasg.atlantis.fragments.PINFragment;
import fr.nawrasg.atlantis.fragments.PharmacieAddFragment;
import fr.nawrasg.atlantis.fragments.PharmacieFragment;
import fr.nawrasg.atlantis.fragments.PlantFragment;
import fr.nawrasg.atlantis.fragments.ScenarioFragment;
import fr.nawrasg.atlantis.fragments.SensorsFragment;
import fr.nawrasg.atlantis.interfaces.DrawerItemInterface;
import fr.nawrasg.atlantis.preferences.MainPreferenceFragment;
import fr.nawrasg.atlantis.type.DrawerItem;
import fr.nawrasg.atlantis.type.DrawerSection;

@SuppressWarnings("deprecation")
public class MainFragmentActivity extends Activity implements OnItemClickListener {
	private DrawerLayout nDrawerLayout;
	private ListView nDrawerList;
	private ActionBarDrawerToggle nDrawerToggle;
	private Context nContext;
	DrawerItemInterface[] nSections;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_main);
		nContext = this;
		nDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		nDrawerList = (ListView) findViewById(R.id.left_drawer);
		DrawerItemInterface[] nSections = createNavigationMenu();
		nDrawerList.setAdapter(new DrawerAdapter(nContext, R.layout.layout_drawer_item, nSections));
		nDrawerList.setOnItemClickListener(this);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		nDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
		nDrawerLayout, /* DrawerLayout object */
		R.drawable.drawer, /* nav drawer image to replace 'Up' caret */
		R.string.drawer_open, /* "open drawer" description for accessibility */
		R.string.drawer_close /* "close drawer" description for accessibility */
		) {
			public void onDrawerClosed(View view) {
				super.onDrawerClosed(view);
				getActionBar().setTitle("Atlantis");
			}

			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				getActionBar().setTitle("Atlantis");
			}
		};
		nDrawerLayout.setDrawerListener(nDrawerToggle);
		loadFragment(new HomeFragment(), true);
		loadFragment(new PlantFragment(), false);
		handleIntent(getIntent());
	}
	
	private DrawerItemInterface[] createNavigationMenu() {
		if (findViewById(R.id.main_fragment2) != null) {
			DrawerItemInterface[] nSections = { DrawerItem.create(0, "Accueil", "ng_screen", false, nContext),
					DrawerItem.create(2, "Lumières et Capteurs", "ng_bulb", false, nContext),
					DrawerSection.create(100, "Bases de données"),
					DrawerItem.create(101, "Liste des courses", "ng_todo", false, nContext),
					DrawerItem.create(102, "Cuisine", "ng_kittle", false, nContext),
					DrawerItem.create(103, "Pharmacie", "ng_medicine", false, nContext),
					DrawerItem.create(104, "Hygiène et Entretien", "ng_soap", false, nContext),
					DrawerSection.create(200, "Services"),
					DrawerItem.create(204, "Musique", "ng_player", false, nContext),
					DrawerItem.create(201, "Plantes", "ng_plant", false, nContext),
					DrawerItem.create(202, "Géolocalisation", "ng_satellite", false, nContext),
					DrawerItem.create(203, "Appareils Connectés", "ng_connected", false, nContext),
					DrawerSection.create(300, "Général"),
					DrawerItem.create(301, "Historique", "ng_graph", false, nContext),
					DrawerItem.create(302, "Paramètres", "ng_settings", false, nContext),
					DrawerItem.create(303, "Quitter", "ng_exit", false, nContext) };
			return nSections;
		} else {
			DrawerItemInterface[] nSections = { DrawerItem.create(0, "Accueil", "ng_screen", false, nContext),
					DrawerItem.create(2, "Lumières", "ng_bulb", false, nContext),
					DrawerItem.create(1, "Capteurs", "ng_device", false, nContext),
					DrawerItem.create(3, "Caméras", "ng_camera", false, nContext),
					DrawerItem.create(4, "Scénarios", "ng_scenario", false, nContext),
					DrawerSection.create(100, "Bases de données"),
					DrawerItem.create(101, "Liste des courses", "ng_todo", false, nContext),
					DrawerItem.create(102, "Cuisine", "ng_kittle", false, nContext),
					DrawerItem.create(103, "Pharmacie", "ng_medicine", false, nContext),
					DrawerItem.create(104, "Hygiène et Entretien", "ng_soap", false, nContext),
					DrawerSection.create(200, "Services"),
					DrawerItem.create(204, "Musique", "ng_player", false, nContext),
					DrawerItem.create(201, "Plantes", "ng_plant", false, nContext),
					DrawerItem.create(202, "Géolocalisation", "ng_satellite", false, nContext),
					DrawerItem.create(203, "Appareils Connectés", "ng_connected", false, nContext),
					DrawerSection.create(300, "Général"),
					DrawerItem.create(301, "Historique", "ng_graph", false, nContext),
					DrawerItem.create(302, "Paramètres", "ng_settings", false, nContext),
					DrawerItem.create(303, "Quitter", "ng_exit", false, nContext) };
			return nSections;
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
				((EntretienAddFragment) nFragment).setEAN(nEAN);
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
		if (nFragment instanceof CuisineFragment) {
			((CuisineFragment) nFragment).getItems();
		} else if (nFragment instanceof PharmacieFragment) {
			((PharmacieFragment) nFragment).getItems();
		} else if (nFragment instanceof EntretienFragment) {
			((EntretienFragment) nFragment).getItems();
		}
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		nDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		nDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (nDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void scanProduct() {
		IntentIntegrator scanIntegrator = new IntentIntegrator(this);
		scanIntegrator.initiateScan();
	}
	
	public void startFragment(Fragment fragment, boolean first, boolean remove) {
		if(remove){
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
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		nDrawerLayout.closeDrawers();
		removeFragments();
		int nID = ((DrawerItem) nDrawerList.getItemAtPosition(position)).getId();
		switch (nID) {
			case 0:
				loadFragment(new HomeFragment(), true);
				loadFragment(new PlantFragment(), false);
				break;
			case 1:
				loadFragment(new SensorsFragment(), true);
				break;
			case 2:
				loadFragment(new LightFragment(), true);
				loadFragment(new SensorsFragment(), false);
				break;
			case 3:
				loadFragment(new CameraFragment(), true);
				break;
			case 4:
				loadFragment(new ScenarioFragment(), true);
				break;
			case 101:
				loadFragment(new CoursesFragment(), true);
				break;
			case 102:
				loadFragment(new CuisineFragment(), true);
				loadFragment(new CuisineAddFragment(), false);
				break;
			case 103:
				loadFragment(new PharmacieFragment(), true);
				loadFragment(new PharmacieAddFragment(), false);
				break;
			case 104:
				loadFragment(new EntretienFragment(), true);
				loadFragment(new EntretienAddFragment(), false);
				break;
			case 201:
				loadFragment(new PlantFragment(), true);
				break;
			case 202:
				loadFragment(new GPSFragment(), true);
				loadFragment(new MapsFragment(), false);
				break;
			case 203:
				loadFragment(new ConnectedDevicesFragment(), true);
				break;
			case 204:
				loadFragment(new MusicFragment(), true);
				break;
			case 301:
				loadFragment(new HistoryFragment(), true);
				break;
			case 302:
				if (App.isPIN(nContext)) {
					loadFragment(new PINFragment(), true);
				} else {
					loadFragment(new MainPreferenceFragment(), true);
				}
				break;
			case 303:
				finish();
				break;
		}
	}

}

