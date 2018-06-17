package at.crimsonbit.nodesystem.nodebackend.util;

public class Util {
	public static byte[] intToByteArray(int i) {
		byte[] b = new byte[4];
		b[0] = (byte) (i >> 24);
		b[1] = (byte) (i >> 16);
		b[2] = (byte) (i >> 8);
		b[3] = (byte) i;
		return b;
	}

	public static int byteArrayToInt(byte[] b) {
		if (b.length < 4) {
			throw new IllegalArgumentException("Expected at least 4 bytes for an int");
		}
		int i = 0;
		i |= b[3];
		i |= b[2] << 8;
		i |= b[1] << 16;
		i |= b[0] << 24;
		return i;

	}
}
