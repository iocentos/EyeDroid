package dk.itu.eyedroid.filters;

import org.opencv.core.Mat;

import android.util.Log;
import dk.itu.eyedroid.Constants;
import dk.itu.spcl.jlpf.common.Bundle;
import dk.itu.spcl.jlpf.core.Filter;

/**
 * Algorithm step: 1 Eye Region of interest(ROI). The first time a frame is
 * received, a constant ROI is defined in the center of the image (400x350 px),
 * covering the whole eye of the user. This region is used to look for the
 * existence of the user pupil on a smaller image than the original one in order
 * to minimize the processing overhead. This constant initial ROI is later
 * reduced (200x150px) and moved closer to a previously position where the pupil
 * was found than the center of the image
 */
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
