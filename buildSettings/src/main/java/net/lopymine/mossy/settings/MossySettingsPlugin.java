package net.lopymine.mossy.settings;

import java.io.*;
import java.util.*;
import lombok.Getter;
import net.lopymine.mossy.settings.manager.*;
import org.gradle.api.*;

import org.gradle.api.initialization.Settings;
import org.jetbrains.annotations.NotNull;

@Getter
public class MossySettingsPlugin implements Plugin<Settings> {

	@Override
	public void apply(@NotNull Settings settings) {
		Properties gradleProperties = getGradleProperties(settings.getRootDir());

		settings.getRootProject().setName(getProperty(gradleProperties, "data.mod_name"));

		List<String> additionalDependencies = getAdditionalDependencies(gradleProperties);
		if (additionalDependencies.isEmpty()) {
			System.out.println("No additional dependencies!");
		} else {
			System.out.println("Found additional dependencies: [%s]".formatted(String.join(", ", additionalDependencies)));
		}

		List<String> multiVersions = getMultiVersions(gradleProperties);

		System.out.println("Found MC versions: [%s]".formatted(String.join(", ", multiVersions)));

		StonecutterManager.apply(settings, multiVersions);
		AccessWidenerManager.apply(settings, multiVersions);
		VersionedGradlePropertiesManager.apply(settings, gradleProperties, multiVersions, additionalDependencies);
	}

	public List<String> getAdditionalDependencies(Properties properties) {
		List<String> additionalDepends = new ArrayList<>();

		for (Map.Entry<Object, Object> entry : properties.entrySet()) {
			String key = entry.getKey().toString();
			if (key.startsWith("dep.")) {
				int i = key.indexOf(".") + 1;
				String modId = key.substring(i);
				additionalDepends.add(modId);
			}
		}

		return additionalDepends;
	}

	public static List<String> getMultiVersions(Properties gradleProperties) {
		String multiVersions = getProperty(gradleProperties, "multi_versions");

		List<String> versions = new ArrayList<>();

		for (String s : multiVersions.split(" ")) {
			int index = s.indexOf("[");
			if (index == -1) {
				versions.add(s);
			} else {
				versions.add(s.substring(0, index));
			}
		}

		return versions;
	}

	public static String getProperty(Properties gradleProperties, String id) {
		if (!gradleProperties.containsKey(id)) {
			throw new IllegalArgumentException("Missing important property with id \"%s\" !".formatted(id));
		}
		return gradleProperties.get(id).toString();
	}

	public static @NotNull Properties getGradleProperties(File project) {
		Properties properties = new Properties();
		try (FileReader reader = new FileReader(project.toPath().resolve("gradle.properties").toFile())) {
			properties.load(reader);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return properties;
	}

}
