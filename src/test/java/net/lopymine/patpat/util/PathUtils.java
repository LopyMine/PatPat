package net.lopymine.patpat.util;

import java.nio.file.Path;

public class PathUtils {

	private static final Path RESOURCE_PATH = Path.of("build/resources/test");

	private PathUtils() {
		throw new IllegalStateException("Utility class");
	}

	public static Path getResource(String path) {
		return RESOURCE_PATH.resolve(path);
	}
}
