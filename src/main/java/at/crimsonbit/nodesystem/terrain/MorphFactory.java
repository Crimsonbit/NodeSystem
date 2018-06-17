package at.crimsonbit.nodesystem.terrain;

import at.crimsonbit.nodesystem.terrain.exceptions.NoDeviceAvailableException;
import at.crimsonbit.nodesystem.util.ThreadPool;

public class MorphFactory {

	public static IMorph getMorph() {
		try {
			return new OpenCLMorph();
		} catch (NoDeviceAvailableException e) {
			if (Runtime.getRuntime().availableProcessors() > 2)
				return new MultiThreadMorph(ThreadPool.getThreadPool().getPool(),
						ThreadPool.getThreadPool().getThreads());
			else
				return new Morph();
		}
	}

}
