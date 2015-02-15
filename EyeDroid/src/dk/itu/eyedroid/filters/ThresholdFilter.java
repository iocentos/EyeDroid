package dk.itu.eyedroid.filters;

import org.opencv.core.Mat;

import android.util.Log;
import dk.itu.eyedroid.Constants;
import dk.itu.spcl.jlpf.common.Bundle;
import dk.itu.spcl.jlpf.core.Filter;

/**
 * Algorithm step: 3 Thresholding. The exact type of thresholding used was
 * binary inverted. The result of this operation was a new image where the most
 * dark parts of the original image were converted to black while the most
 * bright parts of the image were converted to white. This way the pupil is
 * represented now as a black blob in the image while removing any unnecessary
 * data in the rest of the image.
 */
public class ThresholdFilter extends Filter {

	@Override
	protected Bundle execute(Bundle arg0) {

		Mat rgba = (Mat) arg0.get(Constants.SOURCE_MAT_RGB);
		Mat gray = (Mat) arg0.get(Constants.SOURCE_MAT_GRAY);
		long pupilRoiRect = (Long) arg0.get(Constants.PUPIL_ROI_RECT);
		arg0 = null;

		thresholdFrame(gray.getNativeObjAddr());

		Bundle newBundle = new Bundle();
		newBundle.put(Constants.SOURCE_MAT_RGB, rgba);
		newBundle.put(Constants.SOURCE_MAT_GRAY, gray);
		newBundle.put(Constants.PUPIL_ROI_RECT, pupilRoiRect);
		Log.i(RGB2GRAYFilter.TAG, this.getFilterName());

		return newBundle;
	}

	public static native void thresholdFrame(long frame);

}
