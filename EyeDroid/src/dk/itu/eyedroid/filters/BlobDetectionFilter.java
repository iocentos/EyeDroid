package dk.itu.eyedroid.filters;

import org.opencv.core.Mat;

import android.util.Log;
import dk.itu.eyedroid.Constants;
import dk.itu.spcl.jlpf.common.Bundle;
import dk.itu.spcl.jlpf.core.Filter;

public class BlobDetectionFilter extends Filter{
	
	private long mDetectedCirles;

	@Override
	protected Bundle execute(Bundle arg0) {

		Mat rgba = (Mat) arg0.get(Constants.SOURCE_MAT_RGB);
		Mat gray = (Mat) arg0.get(Constants.SOURCE_MAT_GRAY);
		long pupilRoiRect = (Long) arg0.get(Constants.PUPIL_ROI_RECT);
		arg0 = null;
		
		mDetectedCirles = blobDetection(gray.getNativeObjAddr());

		Bundle newBundle = new Bundle();
		newBundle.put(Constants.SOURCE_MAT_RGB, rgba);
		newBundle.put(Constants.SOURCE_MAT_GRAY, gray);
		newBundle.put(Constants.PUPIL_ROI_RECT, pupilRoiRect);
		newBundle.put(Constants.DETECTED_CIRCLES, mDetectedCirles);
		Log.i(RGB2GRAYFilter.TAG, this.getFilterName());
	
		return newBundle;
	}

	
	public static native long blobDetection(long frame);
}
