package dk.itu.eyedroid.filters;

import org.opencv.android.Utils;
import org.opencv.core.Mat;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.util.Log;
import dk.itu.eyedroid.Constants;
import dk.itu.spcl.jlpf.common.Bundle;
import dk.itu.spcl.jlpf.core.Filter;

public class DetectAndDrawPupilFilter extends Filter {

	@Override
	protected Bundle execute(Bundle arg0) {

		Mat rgba = (Mat) arg0.get(Constants.SOURCE_MAT_RGB);
		Mat gray = (Mat) arg0.get(Constants.SOURCE_MAT_GRAY);
		long pupilRoiRect = (Long) arg0.get(Constants.PUPIL_ROI_RECT);
		long detectedCircles = (Long) arg0.get(Constants.DETECTED_CIRCLES);
		arg0 = null;

		detectPupilAndDraw(rgba.getNativeObjAddr(), gray.getNativeObjAddr(),
				pupilRoiRect, detectedCircles);

		Log.i(RGB2GRAYFilter.TAG, this.getFilterName() + "start");
		Bitmap bitmap = Bitmap.createBitmap(rgba.cols(), rgba.rows(),
				Config.ARGB_8888);
		Utils.matToBitmap(rgba, bitmap);

		Bundle newBundle = new Bundle();
		newBundle.put(Constants.SINK_BITMAP, bitmap);
		Log.i(RGB2GRAYFilter.TAG, this.getFilterName());

		return newBundle;
	}

	public static native void detectPupilAndDraw(long originalFrame,
			long processedFrame, long pupilRoi, long detectedCircles);

}
