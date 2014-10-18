package dk.itu.eyedroid.io;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.NativeCameraView;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;

import dk.itu.eyedroid.R;
import android.content.Context;
import android.graphics.Camera;
import android.util.Log;
import dk.itu.eyedroid.MainActivity;
import dk.itu.spcl.jlpf.common.Bundle;
import dk.itu.spcl.jlpf.io.IOProtocolReader;

public class InputStreamCamera implements IOProtocolReader,
		CvCameraViewListener2 {
	
	private static final String TAG = "InputStreamCamera";

	private CameraBridgeViewBase mOpenCvCameraView;
	private Context mContext;
	
	private int mCameraId;

	private Mat rgba;
	private Mat grey;
	

	public InputStreamCamera(Context context , CameraBridgeViewBase camera , int camId ){
		mContext = context;
		mOpenCvCameraView = camera;
		mCameraId = camId;

	}

	@Override
	public void cleanup() {
		Log.i(TAG, "cleaning up opencv reader");
		mOpenCvCameraView.disableView();
	}

	@Override
	public void init() {
		
		
		mOpenCvCameraView.setCameraIndex(mCameraId);
		
		mOpenCvCameraView.setCvCameraViewListener(this);
		
		BaseLoaderCallback mLoaderCallbacks = new BaseLoaderCallback(mContext) {

			@Override
			public void onManagerConnected(int status) {
				switch (status) {
				case LoaderCallbackInterface.SUCCESS: {
					mOpenCvCameraView.enableView();
					break;
				}
				default: {
					super.onManagerConnected(status);
					break;
				}
				}

				mOpenCvCameraView.enableView();
			}
		};
		
		OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_9, mContext, mLoaderCallbacks);
	}

	@Override
	public Bundle read() throws IOException {
		//TODO implement with latch
		return null;
	}

	@Override
	public void onCameraViewStarted(int width, int height) {
		Log.i(TAG, "started");
		rgba = new Mat();
		grey = new Mat();
	}

	@Override
	public void onCameraViewStopped() {
		Log.i(TAG, "stopped");
		rgba.release();
		grey.release();

	}

	@Override
	public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
		Log.i(TAG, "got frame");
		Log.i(TAG , "frame " + inputFrame.rgba().width()  + "   " + 
				inputFrame.rgba().height());
		return null;
	}

}
