package net.lopymine.patpat.utils;

import com.google.gson.*;

import net.lopymine.patpat.client.PatPatClient;

import java.io.*;
import java.net.*;
import org.jetbrains.annotations.*;

public class GithubSourcesUtils {
	private GithubSourcesUtils() {
		throw new IllegalStateException("Utility class");
	}

	@Nullable
	public static JsonElement getSources(String url) {
		try {
			return GithubSourcesUtils.getSources(new URL(url));
		} catch (MalformedURLException e) {
			PatPatClient.LOGGER.error("Failed to get github sources: ", e);
		}
		return null;
	}

	@Nullable
	public static JsonElement getSources(@NotNull URL url) {
		try {
			URLConnection connection = url.openConnection();
			connection.connect();
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
				return JsonParser.parseReader(reader);
			}
		} catch (Exception e) {
			PatPatClient.LOGGER.error("Failed to get github sources: ", e);
		}
		return null;
	}
}
