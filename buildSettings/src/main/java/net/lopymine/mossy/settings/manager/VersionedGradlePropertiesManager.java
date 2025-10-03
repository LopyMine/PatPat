package net.lopymine.mossy.settings.manager;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.function.*;
import net.lopymine.mossy.settings.api.*;
import org.gradle.api.initialization.Settings;
import org.jetbrains.annotations.NotNull;

public class VersionedGradlePropertiesManager {

	public static void apply(@NotNull Settings settings, Properties gradleProperties, List<String> multiVersions, List<String> additionalDependencies) {
		Path path = settings.getRootDir().toPath();

		for (String version : multiVersions) {
			try {
				createGradleProperties(
						path,
						version,
						additionalDependencies,
						gradleProperties,
						(modId, ver) -> ModrinthDependenciesAPI.getVersion(modId, version, "fabric"),
						FabricDependenciesAPI::getYarnVersion
				);
			} catch (Exception e) {
				throw new RuntimeException("Failed to create versioned gradle properties for " + version + ", reason: " + e.getMessage(), e);
			}
		}
	}

	public static void createGradleProperties(Path rootPath, String version, List<String> additionalDependencies, Properties rootGradleProperties, BiFunction<String, String, String> dependResolver, Function<String, String> yarnResolver) throws IOException {
		File file = getOrCreateGradlePropertiesFile(rootPath, version);
		if (file == null) {
			return;
		}

		Properties gradleProperties = new Properties();
		try (InputStream in = new FileInputStream(file)) {
			gradleProperties.load(in);
		}

		String fileText = Files.readString(file.toPath(), StandardCharsets.UTF_8);
		boolean isEmpty = fileText.isBlank();

		List<String> missingDependencies = new ArrayList<>();
		for (String id : additionalDependencies) {
			String key = "dep." + id;
			String markerVal = rootGradleProperties.getProperty(key);
			if (!"[VERSIONED]".equals(markerVal)) {
				continue;
			}
			if (!gradleProperties.containsKey(key)) {
				missingDependencies.add(id);
			}
		}

		List<String> oldDependencies = new ArrayList<>();
		for (String key : gradleProperties.stringPropertyNames()) {
			if (!key.startsWith("dep.")) {
				continue;
			}
			String markerVal = rootGradleProperties.getProperty(key);
			if (markerVal != null && !"[VERSIONED]".equals(markerVal)) {
				oldDependencies.add(key);
				continue;
			}
			String shortId = substringSince(key, ".");
			if (!additionalDependencies.contains(shortId)) {
				oldDependencies.add(key);
			}
		}

		boolean update = fileText.replace(" ", "").contains("=[UPDATE]");

		if (!isEmpty && !update && missingDependencies.isEmpty() && oldDependencies.isEmpty()) {
			return;
		}

		if (isEmpty) {
			StringBuilder builder = new StringBuilder();
			builder.append("# Versioned Properties\n");
			builder.append("# Tip: You can set any dependency value to \"[UPDATE]\"\n");
			builder.append("# and reload Gradle to update only it's value.\n\n");
			builder.append("# Fabric Properties, check https://fabricmc.net/develop/\n");
			builder.append("build.yarn=").append(yarnResolver.apply(version)).append("\n");
			builder.append("build.fabric_api=").append(dependResolver.apply("fabric-api", version));
			if (!additionalDependencies.isEmpty()) {
				builder.append("\n\n");
				builder.append("# Additional Dependencies Properties\n");
				for (String depend : additionalDependencies) {
					builder.append("# ").append(depend).append(", check https://modrinth.com/mod/").append(depend).append("/versions?g=").append(version).append("&l=fabric\n");
					builder.append("dep.").append(depend).append("=").append(dependResolver.apply(depend, version)).append("\n");
				}
			}
			Files.writeString(file.toPath(), builder.toString(), StandardCharsets.UTF_8);
			System.out.println("Successfully created gradle.properties for " + version);
			return;
		}

		if (update) {
			String text = fileText.replace(" ", "ㅤ").trim();
			for (String key : gradleProperties.stringPropertyNames()) {
				String value = gradleProperties.getProperty(key);
				if (!"[UPDATE]".equals(value)) {
					continue;
				}
				String oldLine = key + "=[UPDATE]";
				String updatedLine = null;
				if (key.startsWith("dep.")) {
					String depId = substringSince(key, ".");
					updatedLine = key + "=" + dependResolver.apply(depId, version);
				} else if (key.startsWith("build.")) {
					String buildProp = substringSince(key, ".");
					if ("yarn".equals(buildProp)) {
						updatedLine = key + "=" + yarnResolver.apply(version);
					} else if ("fabric_api".equals(buildProp)) {
						updatedLine = key + "=" + dependResolver.apply("fabric-api", version);
					}
				}
				if (updatedLine == null) {
					continue;
				}
				text = text.replace(oldLine, updatedLine);
			}
			String finalText = text.replace("ㅤ", " ");
			Files.writeString(file.toPath(), finalText, StandardCharsets.UTF_8);
			System.out.println("Successfully updated gradle.properties for " + version);
		}

		if (!missingDependencies.isEmpty()) {
			String text = Files.readString(file.toPath(), StandardCharsets.UTF_8);
			if (!text.endsWith("\n")) text = text + "\n";
			StringBuilder builder = new StringBuilder(text);
			for (String depend : missingDependencies) {
				builder.append("# ").append(depend).append(", check https://modrinth.com/mod/").append(depend).append("/versions?g=").append(version).append("&l=fabric\n");
				builder.append("dep.").append(depend).append("=").append(dependResolver.apply(depend, version)).append("\n");
			}
			Files.writeString(file.toPath(), builder.toString(), StandardCharsets.UTF_8);
			System.out.println("Successfully added new depends " + missingDependencies + " to gradle.properties for " + version);
		}

		if (!oldDependencies.isEmpty()) {
			String text = Files.readString(file.toPath(), StandardCharsets.UTF_8);
			List<String> removed = new ArrayList<>();
			for (String fullKey : oldDependencies) {
				String dep = substringSince(fullKey, ".");
				String propValue = gradleProperties.getProperty(fullKey);
				String block = "# " + dep + ", check https://modrinth.com/mod/" + dep + "/versions?g=" + version + "&l=fabric\n" + "dep." + dep + "=" + propValue + "\n";
				text = text.replace(block, "");
				removed.add(dep);
			}
			Files.writeString(file.toPath(), text, StandardCharsets.UTF_8);
			System.out.println("Successfully removed old depends " + removed + " from gradle.properties for " + version);
		}
	}

	private static File getOrCreateGradlePropertiesFile(Path path, String version) {
		try {
			Path folder = path.resolve("versions/" + version);
			File folderFile = folder.toFile();
			if (!folderFile.exists() && !folderFile.mkdirs()) {
				System.out.println("Failed to get or create folder for " + version);
				return null;
			}
			File gradlePropertiesFile = folder.resolve("gradle.properties").toFile();
			if (!gradlePropertiesFile.exists() && !gradlePropertiesFile.createNewFile()) {
				System.out.println("Failed to get or create gradle.properties for " + version);
				return null;
			}
			return gradlePropertiesFile;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("all")
	private static String substringSince(String text, String since) {
		int index = text.indexOf(since);
		if (index == -1) {
			return text;
		}
		return text.substring(index + 1);
	}
}
