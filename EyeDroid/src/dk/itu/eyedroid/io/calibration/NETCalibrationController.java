package dk.itu.eyedroid.io.calibration;

import java.io.IOException;

import dk.itu.eyedroid.io.Server;

public abstract class NETCalibrationController {
	protected  final Server mServer;				// UDP server		

	/**
	 * Default constructor
	 * @param server Network server
	 */
	public NETCalibrationController(Server server){
		mServer = server;
	}

	/**
	 * Main calibration method
	 * @param receivePacket Calibration message received by the server
	 * @throws IOException
	 */
	public abstract boolean calibrate() throws IOException;
}
