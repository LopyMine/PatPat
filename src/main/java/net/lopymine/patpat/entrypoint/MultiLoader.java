package net.lopymine.patpat.entrypoint;

public class MultiLoader {

	private static IModLoader LOADER = null;

	public static IModLoader getInstance() {
		if (LOADER == null) {
			throw new IllegalArgumentException("MultiLoader hasn't been initialized yet");
		}
		return LOADER;
	}

	public static void initialize(IModLoader loader) {
		LOADER = loader;
	}
}
