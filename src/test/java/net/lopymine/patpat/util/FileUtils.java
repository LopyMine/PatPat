package net.lopymine.patpat.util;

import java.io.File;

public class FileUtils {

	private static final String RESOURCE_PATH = "build/resources/test";

	private FileUtils() {
		throw new IllegalStateException("Utility class");
	}

	public static File getResource(String test) {
		return new File(RESOURCE_PATH, test);
	}
}
