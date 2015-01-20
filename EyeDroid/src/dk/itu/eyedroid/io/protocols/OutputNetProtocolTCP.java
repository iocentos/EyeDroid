package dk.itu.eyedroid.io.protocols;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

import dk.itu.eyedroid.Constants;
import dk.itu.eyedroid.io.Utils;
import dk.itu.spcl.jlpf.common.Bundle;
import dk.itu.spcl.jlpf.io.IOProtocolWriter;

/**
 * Generic TCP/IP output protocol implementation. Used to send processed bundle results
 * to a connected client. Sends X and Y gaze position coordinates as result.
 * TODO. Implement using a protocoll controller and calibration
 */
public class OutputNetProtocolTCP implements IOProtocolWriter {

	private final int mPort; 					// Server port
	private ServerSocket serverSocket; 			// Server socket for new incomming
	private Socket mSocket; 					// Client socket
	private OutputStream mOutput; 				// Spcket output stream
	private AtomicBoolean isConnectionSet; 		// Client connection status
	private boolean isWaitingForConnection;		// Server waiting for client status
	private boolean isSocketServerClosed; 		// Server socket was intentionally closed.

	private static final int SERVER_SOCKET_ACCEPT_TIME_OUT = 10;	//Accept client timeout

	/**
	 * Deafult constructor
	 * @param port Server listener port
	 */
	public OutputNetProtocolTCP(int port) {
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
		}
		catch (IOException e) {}
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

				byte[] output;
				output = Utils.generateOutput(0,x, y);

				synchronized (mSocket) {
					mOutput.write(output);
					mOutput.flush();
				}
			}
		}else{
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
}