package testing;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import dk.itu.eyedroid.io.GlassConfig;
import dk.itu.eyedroid.io.Utils;

public class UDPtestClient {

	public static void main(String args[]) {

		InetAddress address;
		try {
			address = InetAddress.getByName("192.168.150.5");
			DatagramSocket socket = new DatagramSocket(5000, address);

			byte[] sendData = new byte[12];
			byte[] receiveData = new byte[12];

			sendData = Utils.generateOutput(
					GlassConfig.TO_EYEDROID_CALIBRATE_DISPLAY_4, 0, 0);

			socket.send(new DatagramPacket(sendData, sendData.length));

			DatagramPacket packet = new DatagramPacket(receiveData,
					receiveData.length);
			socket.receive(packet);

			System.out.println(receiveData);

			socket.close();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
