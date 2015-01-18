package dk.itu.eyedroid.io.protocols;

import java.io.IOException;

import android.util.Log;
import dk.itu.eyedroid.Constants;
import dk.itu.eyedroid.io.NetClientConfig;
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
	 * Initialize server and wait for incoming connections. When a client
	 * connects, close server socket. If non-intentional error ocurrs, throw it
	 * to a higher level.
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
	 * Send result to client. In case of error throw an exception in order to
	 * restart the protocol.
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
//				Log.i(NetClientConfig.TAG ,"Reading message from client");
				super.mController.processMessage(super.mServer.read(false));
			}

			//Send coordinates if system is calibrated
			//Check for pupil detection
			if (x != -1 && y != -1 && super.mController.isStarted.get()){
				Log.i(NetClientConfig.TAG ,"Sending coordinates to client. Calibration is finished here");
//				 int[] xy = super.mController.mCalibrationController.getCalibrationMapper().map(x, y);

//				 Log.i(NetClientConfig.TAG, "Coords orig : " + x  + "," + y + "  client : " + xy[0] + "," + xy[1]);
				super.sendCoordinates(x,y);
			}
		}
		bundle = null;
	}
	
	/**
	 * Close server and connected client socket in case they are open.
	 */
	@Override
	public void cleanup() {
		super.mServer.stop();
	}

}