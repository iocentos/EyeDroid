package dk.itu.eyedroid.io;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class ServerUDP extends Server{

	/**
	 * Default constructor
	 * @param port Server port
	 */
	public ServerUDP(int port) {
		super(port);
	}

	private DatagramSocket mServerSocket; 								// Server socket for new incomming
	private static final int SERVER_SOCKET_ACCEPT_TIME_OUT = 2000;		// Receieve timeout
	
	/**
	 * Start server
	 */
	@Override
	public void start() throws SocketException {
		mServerSocket = new DatagramSocket(super.mServerPort);
		mServerSocket.setSoTimeout(SERVER_SOCKET_ACCEPT_TIME_OUT);
	}
	
	/**
	 * Read from socket
	 * @param block Block until a message is received
	 * @throws IOException 
	 */
	@Override
	public int[] read(boolean block) throws IOException{
		if(mServerSocket.getReceiveBufferSize() >= NetClientConfig.MSG_SIZE || !block){
			byte[] receiveData = new byte[NetClientConfig.MSG_SIZE];
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			mServerSocket.receive(receivePacket);
			
			//Set last message client network data
			super.mClientIPAddress = receivePacket.getAddress();
			super.mClientPort = receivePacket.getPort();
			
			return new int[]{Utils.toInt(receivePacket.getData(),0),Utils.toInt(receivePacket.getData(),4),Utils.toInt(receivePacket.getData(),8)};		
		}else
			return new int[]{-1,-1,-1};
	}

	/**
	 * Send message to client
	 *  @param Message to client
	 *  @param x X-coordinate
	 *  @param y Y-coordinate
	 */
	@Override
	public void send(int message, int x, int y) throws IOException{
		byte[] output = Utils.generateOutput(message, x, y);	
		DatagramPacket sendPacket = new DatagramPacket(output, output.length, super.mClientIPAddress, super.mClientPort);
		synchronized (mServerSocket) {
			mServerSocket.send(sendPacket);
		}
	}

	/**
	 * Close server and connected client socket in case they are open.
	 */
	public void stop(){
		if (mServerSocket != null) {
			if (!mServerSocket.isClosed()) {
				mServerSocket.close();
			}
		}
	}

}
