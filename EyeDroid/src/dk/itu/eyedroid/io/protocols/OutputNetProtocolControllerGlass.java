package dk.itu.eyedroid.io.protocols;

import java.io.IOException;

import android.util.Log;
import dk.itu.eyedroid.io.GlassConfig;
import dk.itu.eyedroid.io.calibration.NETCalibrationController;
import dk.itu.eyedroid.io.experiment.NETExperimentController;
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
	public OutputNetProtocolControllerGlass(NETCalibrationController calibration, NETExperimentController experiment) {
		super(calibration, experiment);
	}

	/**
	 * Process messages from client application
	 * @param message Message code
	 * @throws IOException
	 */
	public void processMessage(int[] message) throws IOException {
		switch (message[0]) {
		
		// Start calibration
		case GlassConfig.TO_EYEDROID_CALIBRATE_DISPLAY_4:
			Log.i(GlassConfig.TAG, "Server received TO_EYEDROID_CALIBRATE_DISPLAY_4");
			super.mCalibrationController.calibrate();
			break;
			
		// Start sending coordinates to client (HMGT mapping)
		case GlassConfig.TO_EYEDROID_STREAM_GAZE_HMGT_START:
			Log.i(GlassConfig.TAG,"Server received TO_EYEDROID_STREAM_GAZE_HMGT_START");
			if (super.mCalibrationController.getCalibrationMapper().isCalibrated()) {
				super.mUseHMGT = true;
				super.isStarted.set(true);
			}
			break;
		
		// Start sending coordinates to client (RGT mapping)
		case GlassConfig.TO_EYEDROID_STREAM_GAZE_RGT_START:
			Log.i(GlassConfig.TAG, "Server received TO_EYEDROID_STREAM_GAZE_RGT_START");
			if (super.mCalibrationController.getCalibrationMapper().isCalibrated()) {
				super.mUseHMGT = false;
				super.isStarted.set(true);
			}
			break;
			
		// Stop sending coordinates to client
		case GlassConfig.TO_EYEDROID_STREAM_GAZE_HMGT_STOP:
		case GlassConfig.TO_EYEDROID_STREAM_GAZE_RGT_STOP:
			Log.i(GlassConfig.TAG, "Server received TO_EYEDROID_STREAM_GAZE_STOP");
			super.isStarted.set(false);
			break;
			
		// Client requests to start experiment
		case GlassConfig.TO_EYEDROID_EXPERIMENT_START:
			Log.i(GlassConfig.TAG, "Server received TO_EYEDROID_EXPERIMENT_START");
			super.mExperimentController.experiment();
			break;
			
		default:
			break;
		}
	}
	
	/************Calibration callbacks********/
	
	/**
	 * Calibration started callback.
	 */
	@Override
	public void onCalibrationStarted() {
		Log.i(GlassConfig.TAG, "Calibration callbacks. On start");
		super.isStarted.set(false);
		super.isCalibrating.set(true);
	}

	/**
	 * Calibration finished callback.
	 */
	@Override
	public void onCalibrationFinished() {
		Log.i(GlassConfig.TAG, "Calibration callbacks. On stop");
		super.isCalibrating.set(false);

	}

	/**
	 * Calibration error callback.
	 */
	@Override
	public void onCalibrationError() {
		Log.i(GlassConfig.TAG, "Calibration callbacks. On error");
		super.isStarted.set(false);
		super.isCalibrating.set(false);
	}
	
	/************Experiment callbacks********/

	/**
	 * Experiment started callback.
	 */
	@Override
	public void onExperimentStarted() {
		Log.i(GlassConfig.TAG, "Experiment callbacks. On start");
		super.isStarted.set(false);
		super.isExperimentRunning.set(true);
	}

	/**
	 * Experiment finished callback.
	 */
	@Override
	public void onExperimentFinished() {
		Log.i(GlassConfig.TAG, "Experiment callbacks. On stop");
		super.isExperimentRunning.set(false);
	}

	/**
	 * Experiment error callback.
	 */
	@Override
	public void onExperimentError() {
		Log.i(GlassConfig.TAG, "Experiment callbacks. On error");
		super.isExperimentRunning.set(false);
		super.isExperimentRunning.set(false);
	}
}
