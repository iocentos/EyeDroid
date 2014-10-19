package dk.itu.eyedroid.io.protocols;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

import dk.itu.spcl.jlpf.common.Bundle;
import dk.itu.spcl.jlpf.io.IOProtocolWriter;

public class OutputNetTCPProtocol implements IOProtocolWriter{

	public final String X_COORDINATE = "x";
	public final String Y_COORDINATE = "y";

	private final int mPort;
	private ServerSocket serverSocket;
	private Socket mSocket;
	private PrintWriter mOutput;
	private AtomicBoolean isConnectionSet;
	private boolean isWaitingForConnection;
	private boolean isSocketServerClosed;

	public OutputNetTCPProtocol(int port) {
		mPort = port;
		isConnectionSet = new AtomicBoolean();
		isConnectionSet.set(false);
	}

	@Override
	public void init() throws IOException{
		try {
			if(!isWaitingForConnection && !isConnectionSet.get()){
				isWaitingForConnection = true;
				serverSocket = new ServerSocket(mPort);
				mSocket = serverSocket.accept();		
				mOutput = new PrintWriter(mSocket.getOutputStream(), true);			
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

	@Override
	public void write(Bundle bundle) throws IOException{
		if(bundle != null && isConnectionSet.get()){

			//TODO Uncomment and remove output test.
			//String output = generateOutput(
			//		(double)bundle.get(X_COORDINATE),(double)bundle.get(Y_COORDINATE));
			String output = generateOutput(1,2);

			synchronized(mSocket){			
				mOutput.println(output);
				if(mOutput.checkError()){
					throw new IOException("Unable to write into output stream");
				}
			}
		}
	}

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

	private String generateOutput (double x, double y){
		return "{\"x\":\"" + x + "\",\"y\":\"" + y + "\"}";
	}
}