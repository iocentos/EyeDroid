package dk.itu.eyedroid.statistics;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import android.os.Environment;
import android.util.Log;

/**
 * Timer implementation class. Logs timing into a file.
 */
public class Timer {

	private static Timer mInstance;												//Timer instance
	public static final String FILE_NAME = "/EyeDroid";							//File name to store statisctics
	public static final String TAG = "Statistics";								//Log Tag
	public static final String STATISTICS_FULL_PATH = Environment				//File path
			.getExternalStorageDirectory().getAbsolutePath().concat(FILE_NAME);

	private final boolean mFileExists;											//Flag
	private long mStartTime;													//TImer start time
	
	/**
	 * Timer default constructor. Starts a new timer
	 */
	private Timer() {
		mFileExists = createFile();
		mStartTime = System.currentTimeMillis();
		if (mFileExists)
			Log.i(TAG, "File was created successully");
		else
			Log.i(TAG, "Could not create file");
	}

	/**
	 * Timer singleton instance
	 * @return Timer instance
	 */
	public static Timer getInstance() {
		if (mInstance == null)
			mInstance = new Timer();
		return mInstance;
	}

	/**
	 * Create timing file
	 * @return Is file created?
	 */
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

	/**
	 * Computes time and writes it into a file
	 * @param finalTime Ending time
	 */
	synchronized public void computeTimes( long finalTime ) {

		if (mFileExists) {
			File file = new File(STATISTICS_FULL_PATH);

			PrintWriter writer = null;
			try {	
				writer = new PrintWriter(new FileWriter(file));
				writer.println("Time : " + (finalTime - mStartTime));
				writer.close();
			} catch (IOException e) {}
		}
	}
}
