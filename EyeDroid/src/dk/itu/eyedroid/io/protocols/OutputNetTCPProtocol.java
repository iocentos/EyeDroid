package dk.itu.eyedroid.io.protocols;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicBoolean;

import dk.itu.spcl.jlpf.common.Bundle;
import dk.itu.spcl.jlpf.io.IOProtocolWriter;

/**
 * TCP/IP output protocol implementation. Used to send processed bundle results to a connected client.
 * Sends X and Y gaze position coordinates as result.
 */

public class OutputNetTCPProtocol implements IOProtocolWriter{

	public final int INDICATOR = 0;			//Message descriptor
	public final int MSG_SIZE = 12;			//byteArray[12]
	
	public final String X_COORDINATE = "x";	//Gaze position X coordinate bundle identifier
	public final String Y_COORDINATE = "y"; //Gaze position Y coordinate bundle identifier

	private final int mPort;				//Server port
	private ServerSocket serverSocket;		//Server socket for new incomming connections
	private Socket mSocket;					//Client socket
	private OutputStream mOutput;			//Spcket output stream
	private AtomicBoolean isConnectionSet;	//Client connection status
	private boolean isWaitingForConnection;	//Server waiting for client status
	private boolean isSocketServerClosed;	//Server socket was intentionally closed.

	/**
	 * Deafult constructor
	 * @param port Server listener port
	 */
	public OutputNetTCPProtocol(int port) {
		mPort = port;
		isConnectionSet = new AtomicBoolean();
		isConnectionSet.set(false);
	}

	/**
	 * Initialize server and wait for incoming connections. When a client connects, close server socket.
	 * If non-intentional error ocurrs, throw it to a higher level. 
	 */
	@Override
	public void init() throws IOException{
		try {
			if(!isWaitingForConnection && !isConnectionSet.get()){
				isWaitingForConnection = true;
				serverSocket = new ServerSocket(mPort);
				mSocket = serverSocket.accept();		
				mOutput = mSocket.getOutputStream();			
				serverSocket.close();
				isWaitingForConnection = false;
				isConnectionSet.set(true);
			}
		} catch (IOException e) {
			isWaitingForConnection = false;
			isConnectionSet.set(false);

			if(!isSocketServerClosed)
				throw e;
			else
				isSocketServerClosed = false;
		}
	}

	/**
	 * Send result to client. In case of error throw an exception in order to restart the protocol.
	 */
	@Override
	public void write(Bundle bundle) throws IOException{		
		if(bundle != null && isConnectionSet.get()){
			//TODO Uncomment and remove output test.
			//byte[] output = generateOutput((int)bundle.get(X_COORDINATE),(int)bundle.get(Y_COORDINATE));
			byte[] output = generateOutput(1,2);

			synchronized(mSocket){			
				mOutput.write(output);
				mOutput.flush();
			}
		}
	}

	/**
	 * Close server and connected client socket in case they are open.
	 */
	@Override
	public void cleanup() {
		isConnectionSet.set(false);
		try {
			if(serverSocket != null){
				if (!serverSocket.isClosed()){
					isSocketServerClosed = true;
					serverSocket.close();
				}
			}
			if(mSocket != null){
				synchronized(mSocket){
					if (!mSocket.isClosed()){
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
	private byte[] generateOutput (int x, int y){		
		ByteBuffer b =  ByteBuffer.allocate(MSG_SIZE);
		b.putInt(0,this.INDICATOR);
		b.putInt(4,x);
		b.putInt(8,y);
		return b.array();
	}
}