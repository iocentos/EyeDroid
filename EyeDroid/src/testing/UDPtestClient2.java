package testing;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import dk.itu.eyedroid.io.NetClientConfig;
import dk.itu.eyedroid.io.Utils;

public class UDPtestClient2 {

	public static void main(String args[]) {

		try {
			DatagramSocket socket = new DatagramSocket(5000);
			// socket.connect(new InetSocketAddress("192.168.150.5", 5000));

//			if (socket.isConnected())
//				System.out.println("Connected to server");
//			else
//				System.exit(1);

			byte[] sendData = new byte[12];
			byte[] receiveData = new byte[12];

			sendData = Utils.generateOutput(
					NetClientConfig.TO_EYEDROID_CALIBRATE_DISPLAY_4, 0, 0);
			System.out.println("sending message to server");
			
			InetAddress address = InetAddress.getByName("192.168.150.5");

			socket.send(new DatagramPacket(sendData, sendData.length,
					address , 5000));

			System.out.println("sent message to server");

			DatagramPacket packet = new DatagramPacket(receiveData,
					receiveData.length);
			socket.receive(packet);

			System.out.println("Client received 1st TO_CLIENT_CALIBRATE_DISPLAY with Point(-1,-1)");

			sendReady(socket);

			sendData = Utils.generateOutput(
					NetClientConfig.TO_EYEDROID_STREAM_GAZE_HMGT_START, 0, 0);
			
			socket.send(new DatagramPacket(sendData, sendData.length,
					address , 5000));

			int count = 0;
			while(count++ < 200){
				socket.receive(packet);
				int[] bla = new int[]{Utils.toInt(packet.getData(),0) ,
				       	Utils.toInt(packet.getData(),4) ,
				       	Utils.toInt(packet.getData(),8)};  

				System.out.println("Received : " + bla[0] +" " + bla[1] + " " + bla[2] );
			}

			sendData = Utils.generateOutput(
					NetClientConfig.TO_EYEDROID_STREAM_GAZE_HMGT_STOP, 0, 0);
			
			socket.send(new DatagramPacket(sendData, sendData.length,
					address , 5000));


			socket.close();

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void sendReady(DatagramSocket socket){
		try{
			byte[] sendData = new byte[12];
			byte[] receiveData = new byte[12];
			InetAddress address = InetAddress.getByName("192.168.150.5");
			DatagramPacket packet = new DatagramPacket(receiveData, receiveData.length);

			for( int i = 0 ; i < 4 ; i++ ){
				sendData = Utils.generateOutput(
					NetClientConfig.TO_EYEDROID_READY, 0, 0);

				System.out.println("Sending " + i + " TO_EYEDROID_READY");

				socket.send(new DatagramPacket(sendData, sendData.length,
						address , 5000));


				socket.receive(packet);
				System.out.println("Received " + i + " TO_CLIENT_CALIBRATE_DISPLAY");
			}
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}



}
