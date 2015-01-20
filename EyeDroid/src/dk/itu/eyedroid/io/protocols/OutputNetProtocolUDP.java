package dk.itu.eyedroid.io.protocols;

import java.io.IOException;

import dk.itu.eyedroid.Constants;
import dk.itu.eyedroid.io.Server;
import dk.itu.spcl.jlpf.common.Bundle;

/**
 * UDP/IP output protocol implementation. Used to send processed bundle results
 * to a connected client. Sends X and Y gaze position coordinates as result.
 */
public class OutputNetProtocolUDP extends OutputNetProtocol   {

	/**
	 * Deafult constructor
	 * 
	 * @param port Server listener port
	 * @param controller Network message controller
	 */
	public OutputNetProtocolUDP(Server server, OutputNetProtocolController controller) {
		super(server, controller);
	}

	/**
	 * Initialize server.
	 */
	@Override
	public void init() throws IOException {
		try {
			super.mServer.start();
		} catch (IOException e) {
			throw e;
		}
	}

	/**
	 * Read incoming messages and send result to client. 
	 * Sample coordinates during calibration
	 * In case of error throw an exception in order to restart the protocol.
	 */
	@Override
	public void write(Bundle bundle) throws IOException {
		if (bundle != null) {

			int x = (Integer) bundle.get(Constants.PUPIL_COORDINATES_X);
			int y = (Integer) bundle.get(Constants.PUPIL_COORDINATES_Y);

			if(super.mController.isCalibrating.get()){
				//Set sampled values for calibration
				super.setXY(x, y);
			}else{
				//Process incoming message
				super.mController.processMessage(super.mServer.read(false));
			}

			//Send coordinates if system is calibrated
			//Check for pupil detection
			if (x != -1 && y != -1 && super.mController.isStarted.get()){

				//TODO Add mapping
//				int[] xy = super.mController.mCalibrationController.getCalibrationMapper().map(x, y);
//				Log.i(NetClientConfig.TAG, "Coords orig : " + x  + "," + y + "  client : " + xy[0] + "," + xy[1]);
//				super.sendCoordinates(xy[0],xy[1]);
				
				super.sendCoordinates(x,y);
			}
		}
		bundle = null;
	}

	/**
	 * Close server.
	 */
	@Override
	public void cleanup() {
		super.mServer.stop();
	}

}