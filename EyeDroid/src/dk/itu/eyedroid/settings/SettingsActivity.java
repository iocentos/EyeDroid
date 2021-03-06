package dk.itu.eyedroid.settings;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import dk.itu.eyedroid.R;

/*
 * The class provides a screen with sliders for all the configuration
 * parameters of the image processing methods. This can be used in order
 * to change the configuration at runtime . Once the optimum configuration 
 * has been found the values should be hardcoded and the settings activity 
 * should be removed from the project. 
 * 
 * It is only used for development purposes.
 * 
 * WARNING
 * There is no checking for the values before they are set in the
 * image processing methods. The settings activity is using sliders to 
 * set all these values usually ranging from 0 to max.
 * The real ranges of these values are not the same so if not set
 * to correct value it might cause the application to crash.
 * 
 * The default configuration of EyeDroid has been configured with these
 * values based on experimentation. If you wish to experiment to tune 
 * the image processing methods use the settings activity to change the values
 * at runtime and once you find your configuration you can hardcode it both 
 * in the Config file and in the jni/Config.h file.
 */
public class SettingsActivity extends Activity {

	public static final String TAG = "Settings"; // Logging TAG

	/**
	 * Set application settings
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_settings);
		ListView list = (ListView) this.findViewById(R.id.settings_root_layout);
		list.setAdapter(new CustomAdapter(this, 1));
	}

	/**
	 * Settings adapter
	 */
	public class CustomAdapter extends ArrayAdapter<String> {

		private Context mContext;

		public CustomAdapter(Context context, int resource) {
			super(context, resource);
			mContext = context;
		}

		@Override
		public int getCount() {
			return 18;
		}

		@SuppressLint("ViewHolder")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.settings_list_item, parent, false);
			Holder holder = new Holder();
			holder.title = (TextView) convertView
					.findViewById(R.id.settings_list_item_title);
			holder.current = (TextView) convertView
					.findViewById(R.id.settings_list_item_current_value);
			holder.lower = (TextView) convertView
					.findViewById(R.id.settings_list_item_lower_limit);
			holder.upper = (TextView) convertView
					.findViewById(R.id.settings_list_item_upper_limit);
			holder.seekBar = (SeekBar) convertView
					.findViewById(R.id.settings_seekbar);
			holder.key = position;

			switch (position) {
			case Config.BEFORE_THRESHOLD_ERODE:
				setUpListItem(holder, "Before thres erode",
						Config.getConfigValue(position), 0, 16);
				break;
			case Config.BEFORE_THRESHOLD_DILATE:
				setUpListItem(holder, "Before thres dilate",
						Config.getConfigValue(position), 0, 16);
				break;
			case Config.AFTER_THRESHOLD_ERODE:
				setUpListItem(holder, "After thres erode",
						Config.getConfigValue(position), 0, 16);
				break;
			case Config.AFTER_THRESHOLD_DILATE:
				setUpListItem(holder, "After thres dilate",
						Config.getConfigValue(position), 0, 16);
				break;
			case Config.DIAMETER_FACTOR:
				setUpListItem(holder, "Diameter factor",
						Config.getConfigValue(position), 0, 8);
				break;
			case Config.ROI_CONSTANT_Y:
				setUpListItem(holder, "Constant roi Y",
						Config.getConfigValue(position), 0, 300);
				break;
			case Config.ROI_CONSTANT_X:
				setUpListItem(holder, "Constant roi X",
						Config.getConfigValue(position), 0, 300);
				break;
			case Config.ROI_CONSTANT_W:
				setUpListItem(holder, "Constant roi W",
						Config.getConfigValue(position), 0, 600);
				break;
			case Config.ROI_CONSTANT_H:
				setUpListItem(holder, "Constant roi H",
						Config.getConfigValue(position), 0, 600);
				break;
			case Config.ROI_PUPIL_FOUND_W:
				setUpListItem(holder, "Pupil found roi W",
						Config.getConfigValue(position), 0, 500);
				break;
			case Config.ROI_PUPIL_FOUND_H:
				setUpListItem(holder, "Pupil found roi H",
						Config.getConfigValue(position), 0, 500);
				break;
			case Config.THRESHOLD_LOWER_LIMIT:
				setUpListItem(holder, "Threshold limit",
						Config.getConfigValue(position), 0, 255);
				break;
			case Config.MIN_NEIGHBOR_DISTANCE_FACTOR:
				setUpListItem(holder, "Min neighbor dist factor",
						Config.getConfigValue(position), 0, 8);
				break;
			case Config.MIN_BLOB_SIZE:
				setUpListItem(holder, "Min blob size",
						Config.getConfigValue(position), 0, 200);
				break;
			case Config.MAX_BLOB_SIZE:
				setUpListItem(holder, "Max blob size",
						Config.getConfigValue(position), 0, 200);
				break;
			case Config.UPPER_THRESHOLD:
				setUpListItem(holder, "Upper threshold",
						Config.getConfigValue(position), 0, 300);
				break;
			case Config.THRESHOLD_CENTER:
				setUpListItem(holder, "Threshold center",
						Config.getConfigValue(position), 0, 250);
				break;
			case Config.SCALE_FACTOR:
				setUpListItem(holder, "Scale factor",
						Config.getConfigValue(position), 0, 8);
				break;
			default:
				break;
			}
			return convertView;
		}

		public class Holder {
			public TextView title;
			public TextView current;
			public TextView lower;
			public TextView upper;
			public SeekBar seekBar;
			public int key;
		}

		private void setUpListItem(final Holder holder, final String message,
				int defaultValue, int lower, int max) {

			final int key = holder.key;

			holder.title.setText(message);
			holder.lower.setText(String.valueOf(lower));
			holder.upper.setText(String.valueOf(max));
			holder.current.setText(String.valueOf(defaultValue));

			holder.seekBar.setMax(max);
			holder.seekBar.setProgress(defaultValue);
			holder.seekBar
					.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

						@Override
						public void onStopTrackingTouch(SeekBar seekBar) {
							// Do nothing...
						}

						@Override
						public void onStartTrackingTouch(SeekBar seekBar) {
							// Do nothing...
						}

						@Override
						public void onProgressChanged(SeekBar seekBar,
								int progress, boolean fromUser) {
							Log.i(TAG, message + " values " + progress);
							holder.current.setText(String.valueOf(progress));
							Config.setConfigValue(key, progress);
						}
					});
		}
	}
}
