package dk.itu.eyedroid.demo;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.opencv.android.CameraBridgeViewBase;

import android.app.Activity;
import android.app.Fragment;
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
import dk.itu.eyedroid.EyeDroid;
import dk.itu.eyedroid.R;
import dk.itu.eyedroid.filters.PreviewFilter;
import dk.itu.eyedroid.io.calibration.CalibrationMapper;
import dk.itu.eyedroid.io.calibration.GlassCalibrationMapper;
import dk.itu.eyedroid.io.calibration.NETCalibrationController;
import dk.itu.eyedroid.io.calibration.NETCalibrationControllerGlass;
import dk.itu.eyedroid.io.protocols.InputStreamCamera;
import dk.itu.eyedroid.io.protocols.InputStreamUSBCamera;
import dk.itu.eyedroid.io.protocols.OutputNetProtocol;
import dk.itu.eyedroid.io.protocols.OutputNetProtocolController;
import dk.itu.eyedroid.io.protocols.OutputNetProtocolControllerGlass;
import dk.itu.eyedroid.io.protocols.OutputNetProtocolUDP;
import dk.itu.eyedroid.io.protocols.ServerTCP;
import dk.itu.spcl.jlpf.io.IOProtocolReader;
import dk.itu.spcl.jlpf.io.IORWDefaultImpl;

public class MainFragment extends Fragment {

	final String URL = "http://217.197.157.7:7070/axis-cgi/mjpg/video.cgi?resolution=320x240";

	public static final String TAG = "TestFragment";

	public static final String CAMERA_OPTION = "camera_option";
	public static final int FRONT_CAMERA = 0;
	public static final int BACK_CAMERA = 1;
	public static final int USB_CAMERA = 2;

	private final static int GLASS_SCREEN_WIDTH = 640;
	private final static int GLASS_SCREEN_HEIGHT = 360;

	private View mRootView;
	private ImageView mImageView;

	private EyeDroid EYEDROID ; 

	private PreviewFilter mPreviewFilter;
	
	private ServerTCP server;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.streaming_layout, container,false);
		mImageView = (ImageView) mRootView.findViewById(R.id.mjpeg_view);
		EYEDROID = new EyeDroid(getActivity());
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
		if (item.getItemId() == R.id.preview) {
			if (mPreviewFilter.isEnabled()) {
				mPreviewFilter.disablePreview();
				item.setIcon(getResources().getDrawable(R.drawable.start_btn));
			} else {
				mPreviewFilter.enablePreview();
				item.setIcon(getResources().getDrawable(R.drawable.stop_btn));
			}
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public IORWDefaultImpl createProtocols() {

		int whichCamera = this.getArguments().getInt(CAMERA_OPTION);
		CameraBridgeViewBase camera = (CameraBridgeViewBase) mRootView.findViewById(R.id.opencv_camera_view);

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
		
		CalibrationMapper mapper = new GlassCalibrationMapper(2, 2, GLASS_SCREEN_WIDTH, GLASS_SCREEN_HEIGHT);

		NETCalibrationController calibrationController = new NETCalibrationControllerGlass(mapper);

		OutputNetProtocolController controller = new OutputNetProtocolControllerGlass(calibrationController);

		this.server = new ServerTCP(5000, controller);
		
		ExecutorService executor = Executors.newSingleThreadExecutor();
		executor.execute(server);

		calibrationController.setServer(server);
		
		calibrationController.setCalibrationCallbacks(controller);

		OutputNetProtocol outProtocol;
		try {
			outProtocol = new OutputNetProtocolUDP(controller, 6000, InetAddress.getByName(""), 6000);
			
			calibrationController.setOutputProtocol(outProtocol);

			//OutputNetProtocolTCP outProtocol = new OutputNetProtocolTCP(5000);
			IORWDefaultImpl io_rw = new IORWDefaultImpl(inProtocol, outProtocol);
			return io_rw;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.i(TAG, "OnResume");
		IORWDefaultImpl io = createProtocols();
		EYEDROID.setIOProtocols(io , io);
		mPreviewFilter = EYEDROID.addAndGetPreview(mImageView);
		EYEDROID.start();
	}

	@Override
	public void onPause() {
		super.onPause();
		Log.i(TAG, "OnPause");
		EYEDROID.stop();
	}
}