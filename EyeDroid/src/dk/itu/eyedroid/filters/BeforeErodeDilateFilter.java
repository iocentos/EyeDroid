package dk.itu.eyedroid.filters;

import org.opencv.core.Mat;

import android.util.Log;
import dk.itu.eyedroid.Constants;
import dk.itu.spcl.jlpf.common.Bundle;
import dk.itu.spcl.jlpf.core.Filter;

/**
 * Algorithm step: 2 Erode-dilation. Edore (3 times) and dilation (2 times) is
 * performed both before and after the threshold step. This step is used in
 * order to smooth the corners in the image blobs.
 */
public class BeforeErodeDilateFilter extends Filter {

	@Override
	protected Bundle execute(Bundle arg0) {

		Mat rgba = (Mat) arg0.get(Constants.SOURCE_MAT_RGB);
		Mat gray = (Mat) arg0.get(Constants.SOURCE_MAT_GRAY);
		long pupilRoiRect = (Long) arg0.get(Constants.PUPIL_ROI_RECT);
		arg0 = null;

		beforeErodeDilate(gray.getNativeObjAddr());

		Bundle newBundle = new Bundle();
		newBundle.put(Constants.SOURCE_MAT_RGB, rgba);
		newBundle.put(Constants.SOURCE_MAT_GRAY, gray);
		newBundle.put(Constants.PUPIL_ROI_RECT, pupilRoiRect);
		Log.i(RGB2GRAYFilter.TAG, this.getFilterName());

		return newBundle;
	}

	public static native void beforeErodeDilate(long frame);
}
