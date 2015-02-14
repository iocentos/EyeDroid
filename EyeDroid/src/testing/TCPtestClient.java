package testing;

import java.io.InputStream;
import java.net.Socket;

public class TCPtestClient {

	static final String HOST = "130.226.142.243";
	static final int PORT = 5000;

	public final static int MSG_SIZE = 12;

	public static void main(String[] args) {
		try {
			Socket socket = new Socket(HOST, PORT);
			if (socket.isConnected())
				System.out.println("Connected!");

			InputStream input = socket.getInputStream();
			byte[] bytes = new byte[MSG_SIZE];

			int counter = 0;
			while (counter < 50) {
				input.read(bytes, 0, MSG_SIZE);
				System.out.println(toInt(bytes, 0) + " " + toInt(bytes, 4)
						+ " " + toInt(bytes, 8));
			}

			socket.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public static int toInt(byte[] bytes, int offset) {
		int ret = 0;
		for (int i = offset; i < offset + 4; i++) {
			ret <<= 8;
			ret |= (int) bytes[i] & 0xFF;
		}
		return ret;
	}
}
