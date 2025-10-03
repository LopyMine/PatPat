package net.lopymine.mossy.settings.manager;

import dev.kikugie.stonecutter.settings.StonecutterSettingsExtension;
import java.util.List;
import org.gradle.api.initialization.Settings;
import org.jetbrains.annotations.NotNull;

public class StonecutterManager {

	public static void apply(@NotNull Settings settings, List<String> versions) {
		StonecutterSettingsExtension stonecutter = settings.getExtensions().getByType(StonecutterSettingsExtension.class);
		stonecutter.create(settings.getRootProject(), (builder) -> {
			builder.versions(versions);
			builder.getVcsVersion().set(versions.get(versions.size() - 1));
		});
	}

}
