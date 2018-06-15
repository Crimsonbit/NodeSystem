package at.crimsonbit.nodesystem.application;

public class ApplicationContext {

	private static ApplicationContext singleton;

	private String version = "0.0.1";
	private String authors = "Alexander Daum, Florian Wagner";
	private String git = "https://github.com/Crimsonbit/NodeSystem";

	public ApplicationContext() {
	}

	public static ApplicationContext getContext() {
		if (singleton == null) {
			singleton = new ApplicationContext();
		}
		return singleton;
	}

	public String getVersion() {
		return version;
	}

	public String getAuthors() {
		return authors;
	}

	public String getGit() {
		return git;
	}

}
