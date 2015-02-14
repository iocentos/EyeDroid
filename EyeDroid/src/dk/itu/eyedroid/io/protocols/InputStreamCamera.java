package dk.itu.eyedroid.io.protocols;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.Utils;
import org.opencv.core.Mat;

import android.graphics.Bitmap;
import android.util.Log;
import dk.itu.eyedroid.Constants;
import dk.itu.eyedroid.filters.RGB2GRAYFilter;
import dk.itu.spcl.jlpf.common.Bundle;
import dk.itu.spcl.jlpf.io.IOProtocolReader;

public class InputStreamCamera implements IOProtocolReader,CvCameraViewListener2 {

	private static final String TAG = "InputStreamCamera";
	private CameraBridgeViewBase mOpenCvCameraView;
	private int mCameraId;
	private Mat rgba;
	private Mat gray;
	private CountDownLatch startGate;
	private CountDownLatch endGate;

	private Bitmap mBitmap;

	public InputStreamCamera(CameraBridgeViewBase camera, int camId) {
		mOpenCvCameraView = camera;
		mCameraId = camId;
		startGate = new CountDownLatch(1);
		endGate = new CountDownLatch(1);
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
		mOpenCvCameraView.enableView();
	}

	@Override
	public Bundle read() throws IOException {

		Bundle bundle = new Bundle();
		try {
			startGate.await();
			Log.i(TAG, "reader entered read method");
			bundle.put(Constants.SOURCE_BITMAP, mBitmap);
			bundle.put(Constants.SOURCE_MAT_RGB, rgba);
			bundle.put(Constants.SOURCE_MAT_GRAY, gray);
			startGate = new CountDownLatch(1);
			endGate.countDown();
		} catch (InterruptedException e) {
			e.printStackTrace();
			throw new IOException();
		}

		return bundle;
	}

	@Override
	public void onCameraViewStarted(int width, int height) {
		Log.i(TAG, "started");
		rgba = new Mat();
		gray = new Mat();
	}

	@Override
	public void onCameraViewStopped() {
		Log.i(TAG, "stopped");
		rgba.release();
		gray.release();

	}

	@Override
	public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
		Log.i(TAG, "got frame");

		rgba = inputFrame.rgba();
		gray = inputFrame.gray();

		try {
			mBitmap = Bitmap.createBitmap(rgba.cols(), rgba.rows(), Bitmap.Config.ARGB_8888);

			Bitmap temp = Bitmap.createScaledBitmap(mBitmap, 640, 480, false);
			Log.i(RGB2GRAYFilter.TAG, "W :" + temp.getWidth() + " H : " + temp.getHeight());

			mBitmap = temp;

			Utils.matToBitmap(rgba, mBitmap);
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}

		startGate.countDown();
		try {
			endGate.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		endGate = new CountDownLatch(1);

		return null;
	}

}
