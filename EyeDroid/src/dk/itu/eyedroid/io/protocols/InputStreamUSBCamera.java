package dk.itu.eyedroid.io.protocols;

import java.io.IOException;

import org.opencv.core.Rect;

import dk.itu.eyedroid.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import dk.itu.spcl.jlpf.common.Bundle;
import dk.itu.spcl.jlpf.io.IOProtocolReader;

public class InputStreamUSBCamera implements IOProtocolReader {

	private static final boolean DEBUG = true;
	private static final String TAG = "WebCam";
	protected Context context;
	Thread mainLoop = null;
	private Bitmap bmp = null;

	private boolean cameraExists = false;
	private boolean shouldStop = false;

	// /dev/videox (x=cameraId+cameraBase) is used.
	// In some omap devices, system uses /dev/video[0-3],
	// so users must use /dev/video[4-].
	// In such a case, try cameraId=0 and cameraBase=4
	private int cameraId = 3;
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


	
	public InputStreamUSBCamera( Context context , int cameraId) {
		this.context = context;
		this.cameraId = cameraId;
	}

	@Override
	public void cleanup() {
		if (DEBUG)
			Log.d(TAG, "surfaceDestroyed");
		if (cameraExists) {
			shouldStop = true;
			while (shouldStop) {
				try {
					Thread.sleep(100); // wait for thread stopping
				} catch (Exception e) {
				}
			}
		}
		stopCamera();

	}

	@Override
	public void init() throws IOException {
		if (DEBUG)
			Log.d(TAG, "surfaceCreated");
		if (bmp == null) {
			bmp = Bitmap.createBitmap(IMG_WIDTH, IMG_HEIGHT,
					Bitmap.Config.ARGB_8888);
		}
		// /dev/videox (x=cameraId + cameraBase) is used
		int ret = prepareCameraWithBase(cameraId, cameraBase);

		if (ret != -1)
			cameraExists = true;
	}

	@Override
	public Bundle read() throws IOException {
//		 obtaining a camera image (pixel data are stored in an array in JNI).
		if (cameraExists) {
			processCamera();
			// camera image to bmp
			pixeltobmp(bmp);

			Bundle bundle = new Bundle();
			bundle.put(InputNetStreamingProtocol.INPUT_BITMAP, bmp);
			return bundle;
		}
		
		Bundle b = new Bundle();
		b.put(InputNetStreamingProtocol.INPUT_BITMAP, BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher));

		return b;
	}

}
