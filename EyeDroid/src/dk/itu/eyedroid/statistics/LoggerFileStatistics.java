package dk.itu.eyedroid.statistics;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import android.os.Environment;
import android.util.Log;
import dk.itu.spcl.jlpf.core.CoreStatistics;
import dk.itu.spcl.jlpf.core.ProcessingCore;

/*
 *A logger that can be user with the processing core.
 *The logger will print in a nice format the state of the core at a specific moment.
 *It will print the size of the queues in every step, the number of
 *executions of each filter and the average execution time of each filter 
 *in an external file that can be reviewed later.
 *
 *It is enabled as follows with 5000 being the interval for the statistics thread
 *		core.enableStatistics(new LoggerFileStatistics(
 *		LoggerFileStatistics.STATISTICS_FULL_PATH), 5000);
 */
public class LoggerFileStatistics implements ProcessingCore.StatisticsCallback {

	private String mFileName;
	public static final String FILE_NAME = "/statistics"; // File prefix
	public static final String TAG = "Statistics"; // Log TAG
	public static final String STATISTICS_FULL_PATH = // File system path
	Environment.getExternalStorageDirectory().getAbsolutePath()
			.concat(FILE_NAME);
	private final boolean mFileExists; // Flag

	/**
	 * Default constructor. Creates a new statistics file
	 * 
	 * @param fileName
	 *            File name to create
	 */
	public LoggerFileStatistics(String fileName) {
		this.mFileName = fileName;
		mFileExists = createFile();
		if (mFileExists)
			Log.i(TAG, "File was created successully");
		else
			Log.i(TAG, "Could not create file");
	}

	/**
	 * Create new file
	 * 
	 * @return Is file created?
	 */
	public boolean createFile() {
		File file = new File(mFileName);
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
	 * Update statistics log file
	 */
	@Override
	public void onStatisticsUpdates(CoreStatistics statistics) {
		if (mFileExists) {
			PrintWriter writer = null;
			try {
				File file = new File(mFileName);
				writer = new PrintWriter(new BufferedWriter(new FileWriter(
						file, true)));

				writer.println("**********************************************************");
				writer.printf("Filter name\t\tExecution Time\t\tFrame counter\n");
				for (int i = 0; i < statistics.filtersCount; i++) {
					writer.printf("%s\t\t%2f\t\t%d\n",
							statistics.filterNames[i],
							statistics.filterExecutionTimes[i],
							statistics.filterExecutionCounter[i]);
				}

				writer.println();
				writer.println("**Pipes**");
				StringBuilder b = new StringBuilder();
				b.append("Source  ");

				for (int i = 0; i < statistics.pipesCount - 1; i++) {
					b.append(statistics.pipeSizes[i] + "----->");
				}

				b.append(statistics.pipeSizes[statistics.pipesCount - 1]
						+ "  Sink");

				writer.println(b.toString());
				writer.println();
				writer.println();
				writer.println();
				writer.close();
			} catch (IOException e) {
			}

		}// end of if
	}
}
