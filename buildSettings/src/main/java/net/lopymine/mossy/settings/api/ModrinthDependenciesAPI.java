package net.lopymine.mossy.settings.api;

import com.google.gson.*;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.jetbrains.annotations.NotNull;

public class ModrinthDependenciesAPI {

	@NotNull
	public static String getVersion(String modId, String minecraftVersion, String loader) {
		String encodedLoader = URLEncoder.encode("[\"%s\"]".formatted(loader), StandardCharsets.UTF_8);
		String encodedMinecraftVersion = URLEncoder.encode("[\"%s\"]".formatted(minecraftVersion), StandardCharsets.UTF_8);
		String url = "https://api.modrinth.com/v2/project/%s/version?loaders=%s&game_versions=%s".formatted(modId, encodedLoader, encodedMinecraftVersion);
		JsonElement element;
		try {
			element = JsonHelper.get(url);
		} catch (FileNotFoundException e) {
			System.out.printf("\nFailed to find Modrinth project with id \"%s\"%n", modId);
			e.printStackTrace(System.out);
			return "unknown";
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return "unknown";
		}
		JsonArray array = element.getAsJsonArray();
		if (array.isEmpty()) {
			return "unknown";
		}
		JsonElement jsonElement = array.get(0);
		JsonObject jsonObject = jsonElement.getAsJsonObject();

		return jsonObject.get("version_number").getAsString();
	}

}
