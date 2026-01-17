package net.lopymine.patpat.entrypoint.loader;

import java.io.InputStream;
import java.nio.file.Path;
import net.lopymine.patpat.entrypoint.loader.IModLoader.ModEnvironment;
import org.jetbrains.annotations.Nullable;

public interface IEarlyModLoader {

	Path getConfigDir();

	boolean isDevelopmentEnvironment();

	boolean isModLoaded(String modId);

	@Nullable
	InputStream loadModFile(String modId, String path);

	ModEnvironment getEnvironment();


}
