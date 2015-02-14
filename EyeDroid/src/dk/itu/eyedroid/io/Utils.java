package dk.itu.eyedroid.io;

import java.nio.ByteBuffer;

/**
 * General purpose functionality.
 */
public class Utils {
	/**
	 * Create byte[] output containing the gaze position coordinates and a
	 * message.
	 * 
	 * @param Message
	 *            to client
	 * @param x
	 *            X coordinate
	 * @param y
	 *            Y coordinate
	 * @return Byte array.
	 */
	public static byte[] generateOutput(int message, int x, int y) {
		ByteBuffer b = ByteBuffer.allocate(GlassConfig.MSG_SIZE);
		b.putInt(0, message);
		b.putInt(4, x);
		b.putInt(8, y);
		return b.array();
	}

	/**
	 * Convert Byte Array to 32-bit integer
	 * 
	 * @param bytes
	 *            Byte array
	 * @param offset
	 *            Array offset
	 * @return Integer number
	 */
	public static int toInt(byte[] bytes, int offset) {
		int ret = 0;
		for (int i = offset; i < offset + 4; i++) {
			ret <<= 8;
			ret |= (int) bytes[i] & 0xFF;
		}
		return ret;
	}
}
