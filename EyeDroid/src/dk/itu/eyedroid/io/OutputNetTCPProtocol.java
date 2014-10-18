package dk.itu.eyedroid.io;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;
import java.io.PrintWriter;
import java.io.IOException;

import dk.itu.spcl.jlpf.common.Bundle;
import dk.itu.spcl.jlpf.io.IOProtocolWriter;

public class OutputNetTCPProtocol implements IOProtocolWriter{

	public final String X_COORDINATE = "x";
	public final String Y_COORDINATE = "y";

	private final int mPort;
	private Socket mSocket;
	private PrintWriter mOutput;
	private boolean isWaitingForConnection;
	private AtomicBoolean isConnectionSet;

	public OutputNetTCPProtocol(int port) {
		mPort = port;
		isConnectionSet = new AtomicBoolean();
		isConnectionSet.set(false);
	}

	@Override
	public void init() {
		try {
			if(!isWaitingForConnection && !isConnectionSet.get()){
				isWaitingForConnection = true;
				ServerSocket serverSocket = new ServerSocket(mPort);
				mSocket = serverSocket.accept();			
				mOutput = new PrintWriter(mSocket.getOutputStream(), true);			
				serverSocket.close();
				isWaitingForConnection = false;
				isConnectionSet.set(true);
			}
		} catch (IOException e) {
			isWaitingForConnection = false;
			isConnectionSet.set(false);
			e.printStackTrace();
		}
	}

	@Override
	public void write(Bundle bundle) throws IOException{
		if(bundle != null && isConnectionSet.get()){
			//TODO String output = generateOutput((double)bundle.get(X_COORDINATE),(double)bundle.get(Y_COORDINATE));
			String output = generateOutput(1,2);

			synchronized(mSocket){			
				mOutput.println(output);
				mOutput.flush();
			}
		}
	}

	@Override
	public void cleanup() {
		isConnectionSet.set(false);
		try {
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