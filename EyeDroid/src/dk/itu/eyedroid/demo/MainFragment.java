package dk.itu.eyedroid.demo;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.opencv.android.CameraBridgeViewBase;

import android.app.Activity;
import android.app.Fragment;
import android.hardware.Camera.CameraInfo;
import android.os.Bundle;
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
import dk.itu.eyedroid.io.GlassConfig;
import dk.itu.eyedroid.io.ServerTCP;
import dk.itu.eyedroid.io.calibration.CalibrationMapper;
import dk.itu.eyedroid.io.calibration.CalibrationMapperGlass;
import dk.itu.eyedroid.io.calibration.NETCalibrationController;
import dk.itu.eyedroid.io.calibration.NETCalibrationControllerGlass;
import dk.itu.eyedroid.io.experiment.NETExperimentController;
import dk.itu.eyedroid.io.experiment.NETExperimentControllerGlass;
import dk.itu.eyedroid.io.protocols.InputStreamCamera;
import dk.itu.eyedroid.io.protocols.InputStreamUSBCamera;
import dk.itu.eyedroid.io.protocols.OutputNetProtocol;
import dk.itu.eyedroid.io.protocols.OutputNetProtocolController;
import dk.itu.eyedroid.io.protocols.OutputNetProtocolControllerGlass;
import dk.itu.eyedroid.io.protocols.OutputNetProtocolUDP;
import dk.itu.spcl.jlpf.io.IOProtocolReader;
import dk.itu.spcl.jlpf.io.IORWDefaultImpl;

/**
 * Demo created to be used with a Haytham Google Glass application demo. Client
 * application connects to EyeDroid, calibrates and conducts an experiment where
 * a set of points are shown randomly and sampled. This experiment was conducted
 * to evaluate the system accuracy. Additionaly, it can be used along the other
 * Haytham project glass applications, such as showing the gaze point on the
 * glass display.
 */
public class MainFragment extends Fragment {

	public static final String TAG = "MainFragment";
	public static final String CAMERA_OPTION = "camera_option";
	public static final int FRONT_CAMERA = 0; // Device front camera id.
	public static final int BACK_CAMERA = 1; // Device back camera id.
	public static final int USB_CAMERA = 2; // External usb plugged camera id.

	private EyeDroid EYEDROID; // Core component
	private View mRootView; // Fragment root view
	private ImageView mImageView; // View used to show the input video + the
									// resulting coordinates on screen.
	private PreviewFilter mPreviewFilter; // Last filter in the architecture.
											// Used to draw a circle in the
											// pupil.
	private ServerTCP server; // TCP server used to handle the connection
								// hand-shake protocol
	private ExecutorService executor; // Executor used to run the TCP server.

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.streaming_layout, container,
				false);
		mImageView = (ImageView) mRootView.findViewById(R.id.mjpeg_view);

		// Create EyeDroid.
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
			// Disable preview on screen by clicking start/stop button.
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

		// Setup EyeDroid input protocol.

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

		// Create a calibration mapper. Used to map the resultin coordinates
		// into the client display using and homography.
		CalibrationMapper mapper = new CalibrationMapperGlass(2, 2,
				GlassConfig.GLASS_SCREEN_WIDTH, GlassConfig.GLASS_SCREEN_HEIGHT);

		// Create a calibration controller. Used to coordinate the calibration
		// process
		NETCalibrationController calibrationController = new NETCalibrationControllerGlass(
				mapper);

		// Used to coordinate communication between EyeDroid and the Glass
		// client during the execution
		// of the experiment.
		NETExperimentController experimentController = new NETExperimentControllerGlass(
				mapper);

		// Setup output protocol. Used to coordinate messages from EyeDroid and
		// client
		OutputNetProtocolController controller = new OutputNetProtocolControllerGlass(
				calibrationController, experimentController);

		// Create TCP server and run it
		this.server = new ServerTCP(GlassConfig.TCP_SERVER_PORT, controller);
		this.executor = Executors.newSingleThreadExecutor();
		this.executor.execute(server);

		calibrationController.setServer(server);
		experimentController.setServer(server);
		calibrationController.setCalibrationCallbacks(controller);
		experimentController.setExperimentCallbacks(controller);

		// Create a UDP protocol. Used to sent coordinates
		OutputNetProtocol outProtocol = new OutputNetProtocolUDP(controller);
		this.server.setCallbacks((OutputNetProtocolUDP) outProtocol);
		experimentController.setOutputProtocol(outProtocol);
		calibrationController.setOutputProtocol(outProtocol);

		// Setup input and output to the core
		IORWDefaultImpl io_rw = new IORWDefaultImpl(inProtocol, outProtocol);
		return io_rw;
	}

	@Override
	public void onResume() {
		super.onResume();
		IORWDefaultImpl io = createProtocols();
		EYEDROID.setIOProtocols(io, io);
		mPreviewFilter = EYEDROID.addAndGetPreview(mImageView);
		EYEDROID.start();
	}

	@Override
	public void onPause() {
		super.onPause();
		EYEDROID.stop();
		server.stop();
		this.executor.shutdown();
	}
}