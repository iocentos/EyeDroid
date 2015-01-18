package dk.itu.eyedroid.io.protocols;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import dk.itu.eyedroid.io.calibration.NETCalibrationController;

public abstract class OutputNetProtocolController {

	protected NETCalibrationController mCalibration;// Calibration controller
	protected Boolean mUseHMGT;						// HMGT or RGT mode
	public AtomicBoolean isCalibrating;				// True when calibrating is in process
	public AtomicBoolean isStarted;					// True when client requested coordinates

	/**
	 * Default constructor
	 * @param server Server
	 * @param calibration Calibration controller
	 */
	public OutputNetProtocolController(NETCalibrationController calibration){
		mCalibration = calibration;
		isCalibrating = new AtomicBoolean();
		isStarted = new AtomicBoolean();
	}
	
	/**
	 * Process messages from client application
	 *  @param message Message code
	 */
	public abstract void processMessage(int[] message) throws IOException;
	
}
