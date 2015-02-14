package dk.itu.eyedroid.io.protocols;

import java.io.IOException;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.Rect;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import dk.itu.eyedroid.Constants;
import dk.itu.spcl.jlpf.common.Bundle;
import dk.itu.spcl.jlpf.io.IOProtocolReader;

/**
 * Read video streaming from USB camera.
 */
public class InputStreamUSBCamera implements IOProtocolReader {

	private static final boolean DEBUG = true; // Debug mode
	private static final String TAG = "UsbWebCam"; // Log TAG
	protected Context context; // Application context
	Thread mainLoop = null;
	private Bitmap bmp = null;

	private boolean cameraExists = false; // Camera exists?
	private boolean shouldStop = false; // Stop flag

	// /dev/videox (x=cameraId+cameraBase) is used.
	// In some omap devices, system uses /dev/video[0-3],
	// so users must use /dev/video[4-].
	// In such a case, try cameraId=0 and cameraBase=4
	private int cameraId = 0;
	private int cameraBase = 0;

	// This definition also exists in ImageProc.h.
	// Webcam must support the resolution 640x480 with YUYV format.
	static final int IMG_WIDTH = 640;
	static final int IMG_HEIGHT = 480;

	// The following variables are used to draw camera images.
	private int winWidth = 0;
	private int winHeight = 0;
	private Rect rect;
	private int dw, dh;
	private float rate;

	// JNI functions
	public native int prepareCamera(int videoid);

	public native int prepareCameraWithBase(int videoid, int camerabase);

	public native void processCamera();

	public native void stopCamera();

	public native void pixeltobmp(Bitmap bitmap);

	/**
	 * Default constructor
	 * 
	 * @param context
	 *            Application context
	 * @param cameraId
	 *            Camera device Id
	 */
	public InputStreamUSBCamera(Context context, int cameraId) {
		this.context = context;
		this.cameraId = cameraId;
	}

	/**
	 * Set permissions to read from USB cam and initiliaze the reader
	 */
	@Override
	public void init() throws IOException {

		Runtime.getRuntime().exec(
				new String[] {
						"su",
						"-c",
						"/system/bin/sh -c \"chmod 666 /dev/video" + cameraId
								+ "\"" });
		Log.i(TAG, "Initiating usb camera with device id " + cameraId);
		if (DEBUG)
			Log.d(TAG, "surfaceCreated");
		if (bmp == null) {
			bmp = Bitmap.createBitmap(IMG_WIDTH, IMG_HEIGHT,
					Bitmap.Config.ARGB_8888);
		}

		// /dev/videox (x=cameraId + cameraBase) is used
		int ret = prepareCameraWithBase(cameraId, cameraBase);

		if (ret != -1) {
			cameraExists = true;
			Log.i(TAG, "Usb camera with device id " + cameraId + " is open");
		} else {
			Log.i(TAG, "Usb camera with device id " + cameraId
					+ "Could not open");
			throw new IOException("Could not open usb camera with device id "
					+ cameraId);
		}
	}

	/**
	 * Read frame and create a bundle
	 * 
	 * @return Frame Bundle
	 */
	@Override
	public Bundle read() throws IOException {
		// obtaining a camera image (pixel data are stored in an array in JNI).
		if (cameraExists) {
			processCamera();
			// camera image to bmp
			pixeltobmp(bmp);

			Mat mat = new Mat();
			Utils.bitmapToMat(bmp, mat);

			Bundle bundle = new Bundle();
			Log.i(TAG, "Usb camera got new frame " + cameraId
					+ "Could not open");
			bundle.put(Constants.SOURCE_MAT_RGB, mat);
			bundle.put(Constants.SOURCE_BITMAP, bmp);
			return bundle;
		}
		throw new IOException("Usb camera is not open. Could not read frame");
	}

	/**
	 * Cleanup reader
	 */
	@Override
	public void cleanup() {
		Log.i(TAG, "Cleaning up usb camera with device id " + cameraId);
		if (DEBUG)
			Log.d(TAG, "surfaceDestroyed");
		stopCamera();
	}
}
