package net.lopymine.patpat.entrypoint.loader;

import java.io.InputStream;
import java.nio.file.Path;
import org.jetbrains.annotations.Nullable;

public interface IEarlyCommonModLoader {

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
