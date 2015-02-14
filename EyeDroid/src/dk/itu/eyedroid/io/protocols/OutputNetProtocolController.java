package dk.itu.eyedroid.io.protocols;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import dk.itu.eyedroid.io.calibration.NETCalibrationController;
import dk.itu.eyedroid.io.calibration.NETCalibrationController.CalibrationCallbacks;
import dk.itu.eyedroid.io.experiment.NETExperimentController;
import dk.itu.eyedroid.io.experiment.NETExperimentController.ExperimentCallbacks;

/**
 * Abstract network client communication message handling
 */
public abstract class OutputNetProtocolController implements
		CalibrationCallbacks, ExperimentCallbacks {

	protected NETCalibrationController mCalibrationController; // Calibration
																// controller
	protected NETExperimentController mExperimentController; // Experiment
																// controller
	protected Boolean mUseHMGT; // HMGT or RGT mode
	public AtomicBoolean isCalibrating; // True when calibrating is in process
	public AtomicBoolean isStarted; // True when client requested coordinates
	public AtomicBoolean isExperimentRunning; // True when client requested to
												// experiment

	/**
	 * Default constructor
	 * 
	 * @param calibration
	 *            Calibration controller
	 */
	public OutputNetProtocolController(NETCalibrationController calibration,
			NETExperimentController experiment) {
		mCalibrationController = calibration;
		mExperimentController = experiment;
		isCalibrating = new AtomicBoolean(false);
		isStarted = new AtomicBoolean(false);
		isExperimentRunning = new AtomicBoolean(false);
	}

	/**
	 * Process messages from client application
	 * 
	 * @param message
	 *            Message [message_code, X, Y]
	 * @thows IOException
	 */
	public abstract void processMessage(int[] message) throws IOException;
}
