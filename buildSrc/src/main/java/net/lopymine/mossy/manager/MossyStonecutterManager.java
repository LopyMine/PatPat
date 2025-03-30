package net.lopymine.mossy.manager;

import dev.kikugie.stonecutter.*;
import lombok.experimental.ExtensionMethod;
import org.gradle.api.Project;

import net.lopymine.mossy.MossyPlugin;

import java.util.Map;
import org.jetbrains.annotations.NotNull;

@ExtensionMethod(MossyPlugin.class)
public class MossyStonecutterManager {

	public static void apply(@NotNull Project project, MossyPlugin plugin) {
		StonecutterBuild stonecutter = project.getStonecutter();

		String mcVersion = plugin.getProjectMultiVersion().projectVersion();
		Map<String, String> properties = project.getMossyProperties("data");
		properties.putAll(project.getMossyProperties("build"));
		properties.put("java", String.valueOf(plugin.getJavaVersionIndex()));
		properties.put("minecraft", mcVersion);
		properties.put("fabric_api_id", project.getStonecutter().compare("1.19.1", mcVersion) >= 0 ? "fabric" : "fabric-api");
		properties.put("mod_version", project.getVersion().toString());

		properties.forEach((key, value) -> {
			addSwap(stonecutter, value, key);
		});
	}

	private static void addSwap(StonecutterBuild stonecutter, String value, String propertyId) {
		stonecutter.swap(propertyId, getFormatted(value));
	}

	private static @NotNull String getFormatted(String modVersion) {
		return "\"%s\";".formatted(modVersion);
	}
}
