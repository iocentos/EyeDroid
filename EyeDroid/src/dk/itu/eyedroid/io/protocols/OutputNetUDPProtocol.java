package dk.itu.eyedroid.io.protocols;

import java.io.IOException;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicBoolean;

import dk.itu.eyedroid.Constants;
import dk.itu.eyedroid.NetClientConfig;
import dk.itu.spcl.jlpf.common.Bundle;
import dk.itu.spcl.jlpf.io.IOProtocolWriter;

/**
 * UDP/IP output protocol implementation. Used to send processed bundle results
 * to a connected client. Sends X and Y gaze position coordinates as result.
 */

public class OutputNetUDPProtocol implements IOProtocolWriter {

	private final int mPort; 					// Server port
	private DatagramSocket serverSocket; 		// Server socket for new incomming
	private InetAddress clientIPAddress;		// Client IP Address
	private int clientPort = 0;					// Client UDP port
	private int calibrationStep = 0;			// Calibration step. 0 = no calibration process started

	private static final int SERVER_SOCKET_ACCEPT_TIME_OUT = 10;

	/**
	 * Deafult constructor
	 * 
	 * @param port Server listener port
	 */
	public OutputNetUDPProtocol(int port) {
		mPort = port;
	}

	/**
	 * Initialize server and wait for incoming connections. When a client
	 * connects, close server socket. If non-intentional error ocurrs, throw it
	 * to a higher level.
	 */
	@Override
	public void init() throws IOException {
		try {
			serverSocket = new DatagramSocket(mPort);
			serverSocket.setSoTimeout(SERVER_SOCKET_ACCEPT_TIME_OUT);
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

			//Read
			if(serverSocket.getReceiveBufferSize() == NetClientConfig.MSG_SIZE){

				byte[] receiveData = new byte[NetClientConfig.MSG_SIZE];
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
				serverSocket.receive(receivePacket);

				int message = toInt(receivePacket.getData(),0);

				switch(message){
				case NetClientConfig.TO_EYEDROID_CALIBRATE_DISPLAY_4:	//Start calibration
				case NetClientConfig.TO_EYEDROID_READY:					//Client is ready for next step
					calibrate(receivePacket);
					break;
				case NetClientConfig.TO_EYEDROID_STREAM_GAZE_HMGT_START:

					break;
				case NetClientConfig.TO_EYEDROID_STREAM_GAZE_RGT_START:

					break;
				case NetClientConfig.TO_EYEDROID_STREAM_GAZE_HMGT_STOP:

					break;
				case NetClientConfig.TO_EYEDROID_STREAM_GAZE_RGT_STOP:

					break;
				default:
					break;
				}
			}

			//Send coordinates
			int x = (Integer) bundle.get(Constants.PUPIL_COORDINATES_X);
			int y = (Integer) bundle.get(Constants.PUPIL_COORDINATES_Y);

			if (x != -1 && y != -1 && clientIPAddress != null) {
				if(NetClientConfig.USE_HMGT)
					sendMessage(NetClientConfig.TO_CLIENT_GAZE_HMGT,x,y);
				else
					sendMessage(NetClientConfig.TO_CLIENT_GAZE_RGT,x,y);
			}
		}
		bundle = null;
	}

	/**
	 * Close server and connected client socket in case they are open.
	 */
	@Override
	public void cleanup() {
		/*isConnectionSet.set(false);
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
		}*/
	}


	/**
	 * Send message to client
	 * 
	 *  @param Message to client
	 *  @param x X-coordinate
	 *  @param y Y-coordinate
	 */
	private void sendMessage(int message, int x, int y) throws IOException{
		byte[] output = generateOutput(message, x, y);	
		DatagramPacket sendPacket = new DatagramPacket(output, output.length, clientIPAddress, clientPort);
		synchronized (serverSocket) {
			serverSocket.send(sendPacket);
		}
	}

	/**
	 * Calibration process
	 * 
	 * @param receivePacket Packet receieved from client.
	 * @throws IOException 
	 */
	private void calibrate(DatagramPacket receivePacket) throws IOException{

		switch(calibrationStep){
		case 0://Notify client process started
			sendMessage(NetClientConfig.TO_CLIENT_CALIBRATE_DISPLAY, -1, -1);
			break;
		case 1:
			sendMessage(NetClientConfig.TO_CLIENT_CALIBRATE_DISPLAY,0,0);
			break;
		
			
			
		}

		calibrationStep++;

		//TODO MOVE
		clientIPAddress = receivePacket.getAddress();
		clientPort = receivePacket.getPort();

	}

	/**
	 * Create byte[] output containing the gaze position coordinates.
	 * 
	 * @param Message to client
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @return Byte array.
	 */
	private byte[] generateOutput(int message, int x, int y) {
		ByteBuffer b = ByteBuffer.allocate(NetClientConfig.MSG_SIZE);
		b.putInt(0,message);
		b.putInt(4, x);
		b.putInt(8, y);
		return b.array();
	}

	/**
	 * Convert Byte Array to 32-bit integer
	 * 
	 * @param bytes Byte array
	 * @param offset Array offset 
	 * @return Integer number
	 */
	private int toInt(byte[] bytes, int offset) {
		int ret = 0;
		for (int i=offset; i<offset+4; i++) {
			ret <<= 8;
			ret |= (int)bytes[i] & 0xFF;
		}
		return ret;
	}
}