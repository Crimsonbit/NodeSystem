package at.crimsonbit.nodesystem.terrain;

public class SingletonCLMorph {

	private static OpenCLMorph singleton;

	public static OpenCLMorph getInstance() {
		if (singleton == null) {
			singleton = new OpenCLMorph();
		}
		return singleton;
	}
	
}
