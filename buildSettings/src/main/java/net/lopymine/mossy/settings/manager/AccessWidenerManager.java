package net.lopymine.mossy.settings.manager;

import java.io.*;
import java.nio.file.Path;
import java.util.List;
import org.gradle.api.initialization.Settings;
import org.jetbrains.annotations.*;

public class AccessWidenerManager {

	public static void apply(@NotNull Settings settings, List<String> multiVersions) {
		Path path = settings.getRootDir().toPath();
		for (String version : multiVersions) {
			createExampleAccessWidener(path, version);
		}
	}

	public static void createExampleAccessWidener(Path rootProject, String version) {
		File awFile = createAWFile(rootProject, version);
		if (awFile == null) {
			return;
		}

		try (FileWriter writer = new FileWriter(awFile)) {
			writer.write("accessWidener v2 named\n");
			writer.write("# " + version + " AW\n");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		System.out.println("Successfully created AW for " + version);
	}

	@Nullable
	private static File createAWFile(Path rootProject, String version) {
		Path awsFolder = rootProject.resolve("src/main/resources/aws/");
		File awsFolderFile = awsFolder.toFile();
		if (!awsFolderFile.exists() && !awsFolderFile.mkdirs()) {
			System.out.println("Failed to get or create AW folder for " + version);
			return null;
		}

		File versionedAWFile = awsFolder.resolve(version + ".accesswidener").toFile();
		if (versionedAWFile.exists()) {
			return null;
		}

		try {
			if (!versionedAWFile.createNewFile()) {
				System.out.println("Failed to create AW file for " + version);
				return null;
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return versionedAWFile;
	}
}
