package net.lopymine.patpat.entrypoint.loader;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.Locale;
import org.jetbrains.annotations.Nullable;

public interface IEarlyCommonModLoader {

	default String getFullPlatform() {
		ModEnvironment env = this.getEnvironment();
		String environment = env.name().substring(0, 1).toUpperCase(Locale.ROOT) + env.name().substring(1).toLowerCase(Locale.ROOT);
		return "%s/%s".formatted(this.getPlatform(), environment);
	}

	String getPlatform();

	Path getConfigDir();

	boolean isDevelopmentEnvironment();

	boolean isModLoaded(String modId);

	@Nullable
	InputStream loadModFile(String modId, String path);

	ModEnvironment getEnvironment();

	enum ModEnvironment {

		SERVER,
		CLIENT

	}

}
