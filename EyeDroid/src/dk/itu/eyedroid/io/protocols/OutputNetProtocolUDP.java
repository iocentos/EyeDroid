package dk.itu.eyedroid.io.protocols;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import dk.itu.eyedroid.Constants;
import dk.itu.eyedroid.io.GlassConfig;
import dk.itu.eyedroid.io.ServerTCP.NetworkCallbacks;
import dk.itu.eyedroid.io.Utils;
import dk.itu.spcl.jlpf.common.Bundle;

/**
 * UDP/IP output protocol implementation. Used to send processed bundle results
 * to a connected client. Sends X and Y gaze position coordinates as result.
 */
public class OutputNetProtocolUDP extends OutputNetProtocol implements
		NetworkCallbacks {

	private DatagramSocket mServerSocket; // Server socket for streaming
	private int mServerPort; // UDP server port
	private InetAddress mClientIp; // Client IP address
	private int mClientPort; // Client UDP port

	/**
	 * Deafult constructor
	 * 
	 * @param controller
	 *            Network message controller
	 */
	public OutputNetProtocolUDP(OutputNetProtocolController controller) {
		super(controller);
	}

	@Override
	public void init() throws IOException {
		mServerPort = GlassConfig.GAZE_STREAMING_UDP_PORT;
		mClientPort = GlassConfig.GAZE_STREAMING_UDP_PORT;
		mServerSocket = new DatagramSocket(mServerPort);
	}

	/**
	 * Read incoming messages and send result to client. Sample coordinates
	 * during calibration In case of error throw an exception in order to
	 * restart the protocol.
	 * 
	 * @param bundle
	 *            Bundle object read from core
	 */
	@Override
	public void write(Bundle bundle) throws IOException {
		if (bundle != null) {

			int x = (Integer) bundle.get(Constants.PUPIL_COORDINATES_X);
			int y = (Integer) bundle.get(Constants.PUPIL_COORDINATES_Y);

			// Set sampled values for calibration or experiment
			if ((super.mController.isCalibrating.get() || super.mController.isExperimentRunning
					.get()) && x != -1 && y != -1) {
				super.setXY(x, y);
			}

			// Send coordinates if system is calibrated
			// Check for pupil detection
			if (x != -1 && y != -1 && super.mController.isStarted.get()) {

				int[] xy = super.mController.mCalibrationController
						.getCalibrationMapper().map(x, y);

				if (super.mController.mUseHMGT)
					sendCoordinates(GlassConfig.TO_CLIENT_GAZE_HMGT, xy[0],
							xy[1]);
				else
					sendCoordinates(GlassConfig.TO_CLIENT_GAZE_RGT, xy[0],
							xy[1]);
			}
		}
		bundle = null;
	}

	/**
	 * Close server.
	 */
	@Override
	public void cleanup() {
		mServerSocket.close();
	}

	/**
	 * Send message to client
	 * 
	 * @param Message
	 *            type
	 * @param x
	 *            X-coordinate
	 * @param y
	 *            Y-coordinate
	 */
	@Override
	protected void sendCoordinates(int message, int x, int y)
			throws IOException {
		byte[] output = Utils.generateOutput(message, x, y);
		DatagramPacket sendPacket = new DatagramPacket(output, output.length,
				mClientIp, mClientPort);
		synchronized (mServerSocket) {
			mServerSocket.send(sendPacket);
		}
	}

	@Override
	public void onClientConnected(InetAddress clientIP) {
		mClientIp = clientIP;
	}
}