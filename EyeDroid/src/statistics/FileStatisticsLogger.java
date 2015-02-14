package statistics;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import android.os.Environment;
import android.util.Log;
import dk.itu.spcl.jlpf.core.CoreStatistics;
import dk.itu.spcl.jlpf.core.ProcessingCore;

public class FileStatisticsLogger implements ProcessingCore.StatisticsCallback {

	private String mFileName;
	public static final String FILE_NAME = "/Skata";
	public static final String TAG = "Statistics";
	public static final String STATISTICS_FULL_PATH = Environment
			.getExternalStorageDirectory().getAbsolutePath().concat(FILE_NAME);

	private final boolean mFileExists;

	public FileStatisticsLogger(String fileName) {
		this.mFileName = fileName;
		mFileExists = createFile();
		if (mFileExists)
			Log.i(TAG, "File was created successully");
		else
			Log.i(TAG, "Could not create file");
	}

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
