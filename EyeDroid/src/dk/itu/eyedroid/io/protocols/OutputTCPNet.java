package dk.itu.eyedroid.io.protocols;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicBoolean;

import dk.itu.eyedroid.Constants;
import dk.itu.eyedroid.io.NetClientConfig;
import dk.itu.spcl.jlpf.common.Bundle;
import dk.itu.spcl.jlpf.io.IOProtocolWriter;

/**
 * Generic TCP/IP output protocol implementation. Used to send processed bundle
 * results to a connected client. Sends X and Y gaze position coordinates as
 * result.
 */

public class OutputTCPNet implements IOProtocolWriter {

	private final int mPort; 				// Server port
	private ServerSocket serverSocket; 		// Server socket for new incomming connections
	private Socket mSocket; 				// Client socket
	private OutputStream mOutput; 			// Spcket output stream
	private AtomicBoolean isConnectionSet; 	// Client connection status
	private boolean isWaitingForConnection; // Server waiting for client status
	private boolean isSocketServerClosed; 	// Server socket was intentionally closed.
	private static final int SERVER_SOCKET_ACCEPT_TIME_OUT = 10;

	/**
	 * Deafult constructor
	 * 
	 * @param port
	 *            Server listener port
	 */
	public OutputTCPNet(int port) {
		mPort = port;
		isConnectionSet = new AtomicBoolean();
		isConnectionSet.set(false);
	}

	/**
	 * Initialize server and wait for incoming connections. When a client
	 * connects, close server socket. If non-intentional error ocurrs, throw it
	 * to a higher level.
	 */
	@Override
	public void init() throws IOException {
		try {
			if (!isWaitingForConnection && !isConnectionSet.get()) {
				isWaitingForConnection = true;
				serverSocket = new ServerSocket(mPort);
				serverSocket.setSoTimeout(SERVER_SOCKET_ACCEPT_TIME_OUT);
			}
		} catch (IOException e) {
			isWaitingForConnection = false;
			isConnectionSet.set(false);

			if (!isSocketServerClosed)
				throw e;
			else
				isSocketServerClosed = false;
		}
	}

	private void acceptClient() {
		try {
			mSocket = serverSocket.accept();
			mOutput = mSocket.getOutputStream();
			serverSocket.close();
			isWaitingForConnection = false;
			isConnectionSet.set(true);

		} catch (IOException e) {
			// do nothing will be handled from cleanup
		}

	}

	/**
	 * Send result to client. In case of error throw an exception in order to
	 * restart the protocol.
	 */
	@Override
	public void write(Bundle bundle) throws IOException {
		if (bundle != null && isConnectionSet.get()) {

			int x = (Integer) bundle.get(Constants.PUPIL_COORDINATES_X);
			int y = (Integer) bundle.get(Constants.PUPIL_COORDINATES_Y);

			if (x != -1 && y != -1) {

				byte[] output = generateOutput(x, y);

				synchronized (mSocket) {
					mOutput.write(output);
					mOutput.flush();
				}
			}
		} else {
			init();
			acceptClient();
		}

		bundle = null;
	}

	/**
	 * Close server and connected client socket in case they are open.
	 */
	@Override
	public void cleanup() {
		isConnectionSet.set(false);
		try {
			if (serverSocket != null) {
				if (!serverSocket.isClosed()) {
					isSocketServerClosed = true;
					serverSocket.close();
				}
			}
			if (mSocket != null) {
				synchronized (mSocket) {
					if (!mSocket.isClosed()) {
						mSocket.close();
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create byte[] output containing the gaze position coordinates.
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @return Byte array.
	 */
	private byte[] generateOutput(int x, int y) {
		ByteBuffer b = ByteBuffer.allocate(NetClientConfig.MSG_SIZE);

		if (NetClientConfig.USE_HMGT)
			b.putInt(0, NetClientConfig.TO_CLIENT_GAZE_HMGT);
		else
			b.putInt(0, NetClientConfig.TO_CLIENT_GAZE_RGT);

		b.putInt(4, x);
		b.putInt(8, y);
		return b.array();
	}
}