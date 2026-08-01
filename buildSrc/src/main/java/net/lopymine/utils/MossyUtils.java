package net.lopymine.utils;

import java.util.*;
import org.gradle.api.Project;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class MossyUtils {

	public static String getProperty(@NotNull Properties properties, String id) {
		if (!properties.containsKey(id)) {
			throw new IllegalArgumentException("Missing important property with id \"%s\" !".formatted(id));
		}
		return properties.get(id).toString();
	}

	public static String getProperty(@NotNull Project project, String id) {
		Map<String, ?> properties = project.getProperties();
		if (!properties.containsKey(id)) {
			throw new IllegalArgumentException("Missing important property with id \"%s\" !".formatted(id));
		}
		return properties.get(id).toString();
	}

	public static String substringBeforeLast(String value, String since) {
		int i = value.lastIndexOf(since);
		if (i == -1) {
			return value;
		}
		return value.substring(0, i);
	}

	public static String substringSinceLast(String value, String since) {
		int i = value.lastIndexOf(since);
		if (i == -1) {
			return value;
		}
		return value.substring(i + 1);
	}

	public static String substringBefore(String value, String since) {
		int i = value.indexOf(since);
		if (i == -1) {
			return value;
		}
		return value.substring(0, i);
	}

	public static String substringSince(String value, String since) {
		int i = value.indexOf(since);
		if (i == -1) {
			return value;
		}
		return value.substring(i + 1);
	}

}
