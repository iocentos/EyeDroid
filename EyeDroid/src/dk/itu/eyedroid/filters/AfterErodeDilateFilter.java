package dk.itu.eyedroid.filters;

import org.opencv.core.Mat;

import android.util.Log;
import dk.itu.eyedroid.Constants;
import dk.itu.spcl.jlpf.common.Bundle;
import dk.itu.spcl.jlpf.core.Filter;
/**
 *  Algorithm step: 4
 *  Edore (3 times) and dilation (2 times) is done in case the thresholding
 *  step detected other dark blobs in the image except the pupil. By using 
 *  erode in the output image any small dark blobs are shrinked until they 
 *  disappeared. Dilation was used to bring the pupil blob back to its original 
 *  size. This step was necessary in order to remove blob outliers.
 */
public class AfterErodeDilateFilter extends Filter{

	@Override
	protected Bundle execute(Bundle arg0) {

		Mat rgba = (Mat) arg0.get(Constants.SOURCE_MAT_RGB);
		Mat gray = (Mat) arg0.get(Constants.SOURCE_MAT_GRAY);
		long pupilRoiRect = (Long) arg0.get(Constants.PUPIL_ROI_RECT);
		arg0 = null;
		
		afterErodeDilate(gray.getNativeObjAddr());
		
		Bundle newBundle = new Bundle();
		newBundle.put(Constants.SOURCE_MAT_RGB, rgba);
		newBundle.put(Constants.SOURCE_MAT_GRAY, gray);
		newBundle.put(Constants.PUPIL_ROI_RECT, pupilRoiRect);
		Log.i(RGB2GRAYFilter.TAG, this.getFilterName());
	
		return newBundle;
	}
	
	public static native void afterErodeDilate(long frame);
}
