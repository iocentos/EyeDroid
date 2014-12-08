package dk.itu.eyedroid;

import java.io.IOException;

import org.opencv.android.CameraBridgeViewBase;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.hardware.Camera.CameraInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import dk.itu.eyedroid.filters.AfterErodeDilateFilter;
import dk.itu.eyedroid.filters.BeforeErodeDilateFilter;
import dk.itu.eyedroid.filters.BlobDetectionFilter;
import dk.itu.eyedroid.filters.CoordinatesFilter;
import dk.itu.eyedroid.filters.DetectAndDrawPupilFilter;
import dk.itu.eyedroid.filters.PreviewFilter;
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

public class MainFragment extends Fragment {

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
	
	private PreviewFilter mPreviewFilter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		mRootView = inflater.inflate(R.layout.streaming_layout, container,
				false);
		mImageView = (ImageView) mRootView.findViewById(R.id.mjpeg_view);
		return mRootView;
	}
	
	

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.setHasOptionsMenu(true);
	}



	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.main_framgnet, menu);
	}



	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if( item.getItemId() == R.id.preview ){
			if( mPreviewFilter.isEnabled()){
				mPreviewFilter.disablePreview();
				item.setIcon(getResources().getDrawable(R.drawable.start_btn));
			}
			else{
				mPreviewFilter.enablePreview();
				item.setIcon(getResources().getDrawable(R.drawable.stop_btn));
			}
			
			return true;
		}
		return super.onOptionsItemSelected(item);
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
		
		CoordinatesFilter coordinatesFilter = new CoordinatesFilter();
		coordinatesFilter.setFilterName("Coordinates");
		
		mPreviewFilter = new PreviewFilter(getActivity(), mImageView);
		mPreviewFilter.setFilterName("Preview filter");

		compo1.addFilter(rgb2gray);
		compo1.addFilter(beforeErode);
		compo1.addFilter(thresholdFilter);
		compo2.addFilter(afterErode);

		compo3.addFilter(blobDetectionFilter);
		compo3.addFilter(detectAndDrawPupilFilter);
		compo3.addFilter(coordinatesFilter);
		compo3.addFilter(mPreviewFilter);

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

//		PreviewProtocol outProtocol = new PreviewProtocol(getActivity(), mImageView);
		OutputNetTCPProtocol outProtocol = new OutputNetTCPProtocol(5000);
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
//		core.enableStatistics(new StatistcsLogger(), 5000);
		ioController.start();
	}

	@Override
	public void onPause() {
		super.onPause();
		Log.i(TAG, "OnPause");
		core.stop();
		ioController.stop();
	}
}