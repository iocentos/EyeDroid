package dk.itu.eyedroid.filters;

import statistics.Timer;
import android.content.Context;
import android.widget.Toast;
import dk.itu.eyedroid.demo.MainActivity;
import dk.itu.spcl.jlpf.common.Bundle;
import dk.itu.spcl.jlpf.core.Filter;

public class TestFilter extends Filter {

	private int counter = 0;
	private Context mContext;

	public TestFilter(Context context) {
		this.mContext = context;
	}

	@Override
	protected Bundle execute(Bundle arg0) {
		if (counter++ == 500) {
			Timer.getInstance().computeTimes(System.currentTimeMillis());
			((MainActivity) mContext).runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(mContext, "Done", Toast.LENGTH_LONG).show();
				}
			});

		}

		arg0 = null;

		return null;
	}

}
