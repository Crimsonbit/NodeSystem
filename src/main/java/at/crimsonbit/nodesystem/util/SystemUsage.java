package at.crimsonbit.nodesystem.util;

import java.text.NumberFormat;

public class SystemUsage {

	private static Runtime runtime = Runtime.getRuntime();
	private static NumberFormat format = NumberFormat.getInstance();
	private static StringBuilder sb;
	
	public static String getRamInfo() {
		long maxMemory = runtime.maxMemory();
		long allocatedMemory = runtime.totalMemory();
		long freeMemory = runtime.freeMemory();
		sb = new StringBuilder();
		sb.append("free memory: " + format.format(freeMemory / 1024));
		sb.append(", allocated memory: " + format.format(allocatedMemory / 1024));
		sb.append(", max memory: " + format.format(maxMemory / 1024));
		sb.append(" total free memory: " + format.format((freeMemory + (maxMemory - allocatedMemory)) / 1024));
		return sb.toString();
	}
}
