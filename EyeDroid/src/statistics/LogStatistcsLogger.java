package statistics;

import android.util.Log;
import dk.itu.spcl.jlpf.core.CoreStatistics;
import dk.itu.spcl.jlpf.core.ProcessingCore;

public class LogStatistcsLogger implements ProcessingCore.StatisticsCallback {

	public static final String TAG = "Statistics";

	@Override
	public void onStatisticsUpdates(CoreStatistics arg0) {
		StringBuilder builder = new StringBuilder();
		builder.append("---------------------------------------------------------------------------------------------------------");

		String filterHeaders = String
				.format("Name\t\t\tExecutionTime\t\tCount");

		Log.i(TAG, builder.toString());
		Log.i(TAG, filterHeaders);
		Log.i(TAG, builder.toString());

		for (int i = 0; i < arg0.filtersCount; i++) {
			Log.i(TAG, String.format("%s\t\t\t%2f\t\t%d", arg0.filterNames[i],
					arg0.filterExecutionTimes[i],
					arg0.filterExecutionCounter[i]));
		}

		Log.i(TAG, builder.toString());
		Log.i(TAG, "Pipes");
		Log.i(TAG, "-----");
		StringBuilder b = new StringBuilder();
		b.append("Source  ");

		for (int i = 0; i < arg0.pipesCount - 1; i++) {
			b.append(arg0.pipeSizes[i] + "----->");
		}

		b.append(arg0.pipeSizes[arg0.pipesCount - 1] + "  Sink");

		Log.i(TAG, b.toString());
		Log.i(TAG, builder.toString());
		Log.i(TAG, builder.toString());
	}

}
