package dk.itu.eyedroid;

import java.io.IOException;

import org.opencv.android.CameraBridgeViewBase;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.hardware.Camera.CameraInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import dk.itu.eyedroid.filters.AfterErodeDilateFilter;
import dk.itu.eyedroid.filters.BeforeErodeDilateFilter;
import dk.itu.eyedroid.filters.BlobDetectionFilter;
import dk.itu.eyedroid.filters.DetectAndDrawPupilFilter;
import dk.itu.eyedroid.filters.EyeDetectionFilter;
import dk.itu.eyedroid.filters.RGB2GRAYFilter;
import dk.itu.eyedroid.filters.ThresholdFilter;
import dk.itu.eyedroid.io.IOAndroidController;
import dk.itu.eyedroid.io.protocols.InputNetStreamingProtocol;
import dk.itu.eyedroid.io.protocols.InputStreamCamera;
import dk.itu.eyedroid.io.protocols.InputStreamUSBCamera;
import dk.itu.eyedroid.io.protocols.OutputNetTCPProtocol;
import dk.itu.spcl.jlpf.core.Filter;
import dk.itu.spcl.jlpf.core.FilterComposite;
import dk.itu.spcl.jlpf.core.ProcessingCore;
import dk.itu.spcl.jlpf.io.IOController;
import dk.itu.spcl.jlpf.io.IOProtocolReader;
import dk.itu.spcl.jlpf.io.IOProtocolWriter;
import dk.itu.spcl.jlpf.io.IORWDefaultImpl;
import dk.itu.spcl.jlpf.io.InputReader;

public class TestFragment extends Fragment {

	final String URL = "http://217.197.157.7:7070/axis-cgi/mjpg/video.cgi?resolution=320x240";

	public static final String TAG = "TestFragment";

	public static final String CAMERA_OPTION = "camera_option";
	public static final int FRONT_CAMERA = 0;
	public static final int BACK_CAMERA = 1;
	public static final int USB_CAMERA = 2;

	private View mRootView;
	private ImageView mImageView;

	private ProcessingCore core;
	private IOController ioController;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		mRootView = inflater.inflate(R.layout.streaming_layout, container,
				false);
		mImageView = (ImageView) mRootView.findViewById(R.id.mjpeg_view);
		return mRootView;
	}

	public void setUpAlgorithm() {
		RGB2GRAYFilter rgb2gray = new RGB2GRAYFilter();
		rgb2gray.setFilterName("RGB2Gray");
		FilterComposite compo1 = new FilterComposite();
		FilterComposite compo2 = new FilterComposite();
		FilterComposite compo3 = new FilterComposite();

		BeforeErodeDilateFilter beforeErode = new BeforeErodeDilateFilter();
		beforeErode.setFilterName("Before dilation");

		ThresholdFilter thresholdFilter = new ThresholdFilter();
		thresholdFilter.setFilterName("Threshold");

		AfterErodeDilateFilter afterErode = new AfterErodeDilateFilter();
		afterErode.setFilterName("After dilation");

		BlobDetectionFilter blobDetectionFilter = new BlobDetectionFilter();
		blobDetectionFilter.setFilterName("Blob detection");

		DetectAndDrawPupilFilter detectAndDrawPupilFilter = new DetectAndDrawPupilFilter();
		detectAndDrawPupilFilter.setFilterName("Detect and draw");

		compo1.addFilter(rgb2gray);
		compo1.addFilter(beforeErode);
		compo1.addFilter(thresholdFilter);
		compo2.addFilter(afterErode);

		compo3.addFilter(blobDetectionFilter);
		compo3.addFilter(detectAndDrawPupilFilter);

		compo1.setFilterName("Composite 1");
		compo2.setFilterName("Composite 2");
		compo3.setFilterName("Composite 3");

		core.addFilter(compo1);
		core.addFilter(compo2);
		core.addFilter(compo3);

	}

	public void createProtocols() {

		int whichCamera = this.getArguments().getInt(CAMERA_OPTION);
		CameraBridgeViewBase camera = (CameraBridgeViewBase) mRootView
				.findViewById(R.id.opencv_camera_view);

		IOProtocolReader inProtocol = null;

		switch (whichCamera) {
		case FRONT_CAMERA:
			inProtocol = new InputStreamCamera(getActivity(), camera,
					CameraInfo.CAMERA_FACING_FRONT);
			break;
		case BACK_CAMERA:
			inProtocol = new InputStreamCamera(getActivity(), camera,
					CameraInfo.CAMERA_FACING_BACK);
			break;
		case USB_CAMERA:
			inProtocol = new InputStreamUSBCamera(getActivity(), 3);
			break;
		default:
			break;
		}

		TestWriter outProtocol = new TestWriter();
		IORWDefaultImpl io_rw = new IORWDefaultImpl(inProtocol, outProtocol);


		core = new ProcessingCore(10);
		ioController = new IOAndroidController(core, io_rw, io_rw);

	}

	@Override
	public void onResume() {
		super.onResume();
		Log.i(TAG, "OnResume");
		
		createProtocols();
		
		setUpAlgorithm();

		core.start(3);
		core.enableStatistics(new StatistcsLogger(), 5000);
		ioController.start();
	}

	@Override
	public void onPause() {
		super.onPause();
		Log.i(TAG, "OnPause");
		core.stop();
		ioController.stop();
	}

	public class TestWriter implements IOProtocolWriter {

		@Override
		public void cleanup() {

		}

		@Override
		public void init() {

		}

		@Override
		public void write(dk.itu.spcl.jlpf.common.Bundle arg0)
				throws IOException {
			Log.i("---------------", "Writer is active");

			final Bitmap bitmap = (Bitmap) arg0.get(Constants.SINK_BITMAP);
			Log.i(RGB2GRAYFilter.TAG,
					"----------------------------------------");
			arg0 = null;
			TestFragment.this.getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					mImageView.setImageBitmap(bitmap);
				}
			});
			arg0 = null;
		}
	}

}