package dk.itu.eyedroid.filters;

import org.opencv.android.Utils;
import org.opencv.core.Mat;

import android.graphics.Bitmap;
import dk.itu.eyedroid.Constants;
import dk.itu.eyedroid.io.protocols.InputNetStreamingProtocol;
import dk.itu.spcl.jlpf.common.Bundle;
import dk.itu.spcl.jlpf.core.Filter;

public class EyeDetectionFilter extends Filter{

	private long mNativeObject = 0;
	
	public EyeDetectionFilter() {
		mNativeObject = createNativeObject();
	}
	
	public void destroyTracker(){
		destroyNativeObject(mNativeObject);
	}
	
	public void process(Mat inputFrame , Mat outputFrame){
		processNativeFrame(mNativeObject, inputFrame.getNativeObjAddr(), outputFrame.getNativeObjAddr());
	}
	
	@Override
	protected Bundle execute(Bundle arg0) {
		
		Mat rgba = (Mat) arg0.get(Constants.SOURCE_MAT_RGB);
		Mat gray = new Mat();
		
		process(rgba, gray);
		
		Bitmap bmp = Bitmap.createBitmap(rgba.width(), rgba.height(), Bitmap.Config.ARGB_8888);
		Utils.matToBitmap(rgba, bmp);
		
		Bundle bundle = new Bundle();
		bundle.put(Constants.SINK_BITMAP, bmp);
		
		return bundle;
	}
	
	public static  native long createNativeObject();
	public static native void destroyNativeObject( long thiz );
	public static native void processNativeFrame( long thiz , long intputFrame , long outputFrame);

}
