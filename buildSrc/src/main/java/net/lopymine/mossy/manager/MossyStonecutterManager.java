package net.lopymine.mossy.manager;

import dev.kikugie.stonecutter.*;
import lombok.experimental.ExtensionMethod;
import org.gradle.api.Project;

import net.lopymine.mossy.MossyPlugin;

import org.jetbrains.annotations.NotNull;

@ExtensionMethod(MossyPlugin.class)
public class MossyStonecutterManager {

	public static void apply(@NotNull Project project) {
		StonecutterBuild stonecutter = project.getStonecutter();

		addSwap(stonecutter, project, "data.mod_version");
		addSwap(stonecutter, project, "data.mod_id");
		addSwap(stonecutter, project, "data.mod_name");
		addSwap(stonecutter, project, "dep.yacl");
	}

	private static void addSwap(StonecutterBuild stonecutter, @NotNull Project project, String propertyId) {
		String property = project.getProperty(propertyId);
		stonecutter.swap(propertyId.substringSince("."), getFormatted(property));
	}

	private static @NotNull String getFormatted(String modVersion) {
		return "\"%s\";".formatted(modVersion);
	}
}
