package dk.itu.eyedroid.filters;

import org.opencv.core.Mat;

import android.util.Log;
import dk.itu.eyedroid.Constants;
import dk.itu.spcl.jlpf.common.Bundle;
import dk.itu.spcl.jlpf.core.Filter;

public class RGB2GRAYFilter extends Filter {

	private long mPupilRoiRect;

	public static final String TAG = "Filter";

	@Override
	protected Bundle execute(Bundle arg0) {

		Mat rgba = (Mat) arg0.get(Constants.SOURCE_MAT_RGB);
		Mat gray = new Mat();
		arg0 = null;

		mPupilRoiRect = rgb2gray(rgba.getNativeObjAddr(),
				gray.getNativeObjAddr());

		Bundle newBundle = new Bundle();
		newBundle.put(Constants.SOURCE_MAT_RGB, rgba);
		newBundle.put(Constants.SOURCE_MAT_GRAY, gray);
		newBundle.put(Constants.PUPIL_ROI_RECT, mPupilRoiRect);

		Log.i(RGB2GRAYFilter.TAG, this.getFilterName());

		return newBundle;
	}

	private static native long rgb2gray(long inputFrame, long outputFrame);

}
