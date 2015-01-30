package dk.itu.eyedroid.io;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.atomic.AtomicBoolean;

import android.util.Log;
import dk.itu.eyedroid.io.protocols.OutputNetProtocolController;

public class ServerTCP extends Server implements Runnable {
	
	
	public interface NetworkCallbacks{
		public void onClientConnected(InetAddress clientIP);
	}

	public AtomicBoolean isRunning;
	private ServerSocket serverSocket;
	private Socket clientSocket;
	public AtomicBoolean isConnected;

	public AtomicBoolean shouldKeepRunning;
	
	private NetworkCallbacks mCallbacks;
	
	private OutputNetProtocolController protocolController;

	public ServerTCP(int port , OutputNetProtocolController controller) {
		super(port);
		protocolController = controller;
		
		isRunning = new AtomicBoolean();
		isConnected = new AtomicBoolean();
		shouldKeepRunning = new AtomicBoolean();

		isRunning.set(false);
		isConnected.set(false);
	}

	@Override
	public void start() throws SocketException {
		try {
			serverSocket = new ServerSocket(super.mServerPort);
		} catch (IOException e) {
			e.printStackTrace();
		}

		shouldKeepRunning.set(true);
	}

	@Override
	public int[] read() throws IOException {
		if( isConnected.get() && shouldKeepRunning.get() ){
			byte[] receiveData = new byte[GlassConfig.MSG_SIZE];
			
			clientSocket.getInputStream().read(receiveData, 0, GlassConfig.MSG_SIZE);

			return new int[]{Utils.toInt(receiveData,0),Utils.toInt(receiveData,4),Utils.toInt(receiveData,8)};
		}
		return new int[]{-1,-1,-1};
	}

	@Override
	public void send(int message, int x, int y) throws IOException {
		
		byte[] data = Utils.generateOutput(message, x, y);
		
		clientSocket.getOutputStream().write(data);
		
		clientSocket.getOutputStream().flush();
	}

	@Override
	public void stop() {	
		shouldKeepRunning.set(false);
		cleanUp();
	}
	
	private void cleanUp(){
		isRunning.set(false);
		if (serverSocket != null)
			try {
				serverSocket.close();
			} catch (IOException e) {}

		if (clientSocket != null)
			try {
				clientSocket.close();
			} catch (IOException e) {}
	}

	@Override
	public void run() {
		isRunning.set(true);
		
		try {
			start();
		} catch (SocketException e) {
			Log.i(GlassConfig.TAG, "TCP Server port is taken");
		}
		
		Log.i(GlassConfig.TAG, "TCP Server thread is now running");
		
		while(shouldKeepRunning.get()){
			
			while( !isConnected.get())
				acceptClient();
			
			try {
				protocolController.processMessage(read());
			} catch (IOException e) {
				//Notify coordinate sender to stop
				try {
					protocolController.processMessage(new int[]{GlassConfig.TO_EYEDROID_STREAM_GAZE_HMGT_STOP,-1,-1});
					clientSocket.close();
				} catch (IOException e1) {}
				finally{
					isConnected.set(false);
				}
			}
		}
	}

	public void acceptClient() {
		try {
			clientSocket = serverSocket.accept();
			isConnected.set(true);
			Log.i(GlassConfig.TAG, "TCP Server accepted a client");
			
			if( mCallbacks != null ){
				mCallbacks.onClientConnected(((InetSocketAddress)clientSocket.getRemoteSocketAddress()).getAddress());
				
			}

		} catch (IOException e) {
			isConnected.set(false);
			if (clientSocket != null) {
				try {
					clientSocket.close();
				} catch (IOException e1) {
				}
			}
		}

	}

	public NetworkCallbacks getCallbacks() {
		return mCallbacks;
	}

	public void setCallbacks(NetworkCallbacks mCallbacks) {
		this.mCallbacks = mCallbacks;
	}
	
	
	
	
}
