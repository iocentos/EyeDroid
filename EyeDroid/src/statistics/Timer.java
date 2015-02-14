package statistics;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import android.os.Environment;
import android.util.Log;

public class Timer {

	private static Timer mInstance;
	public static final String FILE_NAME = "/EyeDroid";
	public static final String TAG = "Statistics";
	public static final String STATISTICS_FULL_PATH = Environment
			.getExternalStorageDirectory().getAbsolutePath().concat(FILE_NAME);
	private final boolean mFileExists;
	private long mStartTime;

	private Timer() {
		mFileExists = createFile();
		mStartTime = System.currentTimeMillis();
		if (mFileExists)
			Log.i(TAG, "File was created successully");
		else
			Log.i(TAG, "Could not create file");
	}

	public static Timer getInstance() {
		if (mInstance == null)
			mInstance = new Timer();

		return mInstance;
	}

	public boolean createFile() {
		File file = new File(STATISTICS_FULL_PATH);
		try {
			if (!file.exists())
				file.createNewFile();
			else {
				file.delete();
				file.createNewFile();
			}

			if (file.exists())
				return true;
			else
				return false;
		} catch (IOException e) {
			return false;
		}
	}

	synchronized public void computeTimes(long finalTime) {

		if (mFileExists) {
			File file = new File(STATISTICS_FULL_PATH);

			PrintWriter writer = null;
			try {

				writer = new PrintWriter(new FileWriter(file));
				writer.println("Time : " + (finalTime - mStartTime));

				writer.close();
			} catch (IOException e) {
			}
		}
	}
}
