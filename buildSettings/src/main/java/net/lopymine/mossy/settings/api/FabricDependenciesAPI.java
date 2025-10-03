package net.lopymine.mossy.settings.api;

public class FabricDependenciesAPI {

	public static String getYarnVersion(String minecraftVersion) {
		try {
			return JsonHelper.get("https://meta.fabricmc.net/v2/versions/yarn/%s?limit=1".formatted(minecraftVersion))
					.getAsJsonArray()
					.get(0)
					.getAsJsonObject()
					.get("version")
					.getAsString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
