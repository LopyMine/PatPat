package net.lopymine.patpat.utils;

import com.google.gson.*;

import net.lopymine.patpat.client.PatPatClient;

import java.io.*;
import org.jetbrains.annotations.*;

public class ModSourcesUtils {
	private ModSourcesUtils() {
		throw new IllegalStateException("Utility class");
	}

	@Nullable
	public static JsonElement getSources(String path) {
		InputStream stream = ModSourcesUtils.class.getClassLoader().getResourceAsStream(path);
		if (stream == null) {
			PatPatClient.LOGGER.error("Failed to find mod sources");
			return null;
		}
		return ModSourcesUtils.getSources(stream);
	}

	@Nullable
	public static JsonElement getSources(@NotNull InputStream stream) {
		try {
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
				return JsonParser.parseReader(reader);
			}
		} catch (Exception e) {
			PatPatClient.LOGGER.error("Failed to get github sources: ", e);
		}
		return null;
	}
}
