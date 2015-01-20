package dk.itu.eyedroid.io.protocols;

import java.io.IOException;

import android.util.Log;
import dk.itu.eyedroid.io.NetClientConfig;
import dk.itu.eyedroid.io.calibration.NETCalibrationController;
/**
 * Handle network communication messages from Glass client
 * TCPtestClient client: https://github.com/dmardanbeigi/GlassGaze
 */
public class OutputNetProtocolControllerGlass extends OutputNetProtocolController {

	/*
	 * Default constructor
	 * @param server Server
	 * @param calibration Calibration controller
	 */
	public OutputNetProtocolControllerGlass(NETCalibrationController calibration) {
		super(calibration);
	}

	/**
	 * Process messages from client application
	 * @param message Message code
	 * @throws IOException
	 */
	public void processMessage(int[] message) throws IOException {
		switch (message[0]) {
		
		// Start calibration
		case NetClientConfig.TO_EYEDROID_CALIBRATE_DISPLAY_4:
			Log.i(NetClientConfig.TAG, "Server received TO_EYEDROID_CALIBRATE_DISPLAY_4");
			super.mCalibrationController.calibrate();
			break;
			
		// Start sending coordinates to client (HMGT mapping)
		case NetClientConfig.TO_EYEDROID_STREAM_GAZE_HMGT_START:
			Log.i(NetClientConfig.TAG,"Server received TO_EYEDROID_STREAM_GAZE_HMGT_START");
			if (super.mCalibrationController.getCalibrationMapper().isCalibrated()) {
				super.mUseHMGT = true;
				super.isStarted.set(true);
			}
			break;
		
		// Start sending coordinates to client (RGT mapping)
		case NetClientConfig.TO_EYEDROID_STREAM_GAZE_RGT_START:
			Log.i(NetClientConfig.TAG, "Server received TO_EYEDROID_STREAM_GAZE_RGT_START");
			if (super.mCalibrationController.getCalibrationMapper().isCalibrated()) {
				super.mUseHMGT = false;
				super.isStarted.set(true);
			}
			break;
			
		// Stop sending coordinates to client
		case NetClientConfig.TO_EYEDROID_STREAM_GAZE_HMGT_STOP:
		case NetClientConfig.TO_EYEDROID_STREAM_GAZE_RGT_STOP:
			Log.i(NetClientConfig.TAG, "Server received TO_EYEDROID_STREAM_GAZE_STOP");
			super.isStarted.set(false);
			break;
			
		default:
			break;
		}
	}

	/**
	 * Calibration started callback.
	 */
	@Override
	public void onCalibrationStarted() {
		Log.i(NetClientConfig.TAG, "Calibration callbacks. On start");
		super.isStarted.set(false);
		super.isCalibrating.set(true);
	}

	/**
	 * Calibration finished callback.
	 */
	@Override
	public void onCalibrationFinished() {
		Log.i(NetClientConfig.TAG, "Calibration callbacks. On stop");
		super.isCalibrating.set(false);

	}

	/**
	 * Calibration error callback.
	 */
	@Override
	public void onCalibrationError() {
		Log.i(NetClientConfig.TAG, "Calibration callbacks. On error");
		super.isStarted.set(false);
		super.isCalibrating.set(false);
	}
}
