package dk.itu.eyedroid.demo;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import dk.itu.eyedroid.R;
import dk.itu.eyedroid.settings.SettingsActivity;

/**
 * EyeDroid demo main activity. This activity is loaded when the application
 * starts
 */
public class MainActivity extends Activity implements
		NavigationDrawerFragment.NavigationDrawerCallbacks {

	private NavigationDrawerFragment mNavigationDrawerFragment; // Fragment
																// managing the
																// behavior,
																// interactions
																// and
																// presentation
																// of the
																// navigation
																// drawer.
	private CharSequence mTitle; // Used to store the last screen title. For use
									// in {@link #restoreActionBar()}.
	BaseLoaderCallback mLoaderCallbacks; // Library loader callbacks

	// Loading libraries
	static {
		System.loadLibrary("opencv_java");
		System.loadLibrary("EyeDroid");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();

		// Set up the navigation drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));
		// Setup loader callbacks
		mLoaderCallbacks = new BaseLoaderCallback(this) {
			@Override
			public void onManagerConnected(int status) {
				switch (status) {
				// Open CV loaded succesfully
				case LoaderCallbackInterface.SUCCESS: {
					MainActivity.this.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(MainActivity.this,
									"OpenCV library is loaded",
									Toast.LENGTH_SHORT).show();
						}
					});
					break;
				}
				// Open CV failed to load
				case LoaderCallbackInterface.INIT_FAILED: {
					MainActivity.this.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(MainActivity.this,
									"Could not load OpenCV", Toast.LENGTH_SHORT)
									.show();
						}
					});
					break;
				}

				default: {
					super.onManagerConnected(status);
					break;
				}
				}
			}
		};
		// Initialize OpenCv library
		OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_9, this,
				mLoaderCallbacks);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {

		// Select input camera and update the main content by replacing
		// fragments
		Bundle bundle = new Bundle();
		switch (position) {
		case MainFragment.FRONT_CAMERA:
			bundle.putInt(MainFragment.CAMERA_OPTION, MainFragment.FRONT_CAMERA);
			break;
		case MainFragment.BACK_CAMERA:
			bundle.putInt(MainFragment.CAMERA_OPTION, MainFragment.BACK_CAMERA);
			break;
		case MainFragment.USB_CAMERA:
			bundle.putInt(MainFragment.CAMERA_OPTION, MainFragment.USB_CAMERA);
			break;
		default:
			break;
		}

		Fragment frag = new MainFragment();
		frag.setArguments(bundle);
		this.getFragmentManager().beginTransaction()
				.replace(R.id.container, frag).commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			// Only show items in the action bar relevant to this screen if the
			// drawer is not showing.
			// Otherwise, let the drawer decide what to show in the action bar.
			getMenuInflater().inflate(R.menu.main, menu);
			restoreActionBar();
			return true;
		}
		return super.onCreateOptionsMenu(menu);
	}

	public void restoreActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will automatically
		// handle clicks on the Home/Up button,
		// so long as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			Intent intent = new Intent(this, SettingsActivity.class);
			this.startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
