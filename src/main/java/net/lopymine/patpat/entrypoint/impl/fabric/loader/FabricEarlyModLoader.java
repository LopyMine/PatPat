package net.lopymine.patpat.entrypoint.impl.fabric.loader;

//? if fabric {

/*import java.io.InputStream;
import java.nio.file.*;
import java.util.Optional;
import net.fabricmc.loader.api.*;
import net.lopymine.patpat.PatPat;
import net.lopymine.patpat.entrypoint.loader.IEarlyCommonModLoader;
import org.jetbrains.annotations.Nullable;

public class FabricEarlyModLoader implements IEarlyCommonModLoader {

	@Override
	public String getPlatform() {
		return "Fabric";
	}

	@Override
	public Path getConfigDir() {
		return FabricLoader.getInstance().getConfigDir();
	}

	@Override
	public boolean isDevelopmentEnvironment() {
		return FabricLoader.getInstance().isDevelopmentEnvironment();
	}

	@Override
	public boolean isModLoaded(String modId) {
		return FabricLoader.getInstance().isModLoaded(modId);
	}

	@Override
	public @Nullable InputStream loadModFile(String modId, String path) {
		ModContainer modContainer = FabricLoader.getInstance().getModContainer(modId).orElse(null);
		if (modContainer == null) {
			PatPat.LOGGER.error("Failed to load file at \"{}\", because \"{}\" mod container doesn't exits!", path, modId);
			return null;
		}
		Optional<Path> optional = modContainer.findPath(path);
		return optional.map((p) -> {
			try {
				return Files.newInputStream(p);
			} catch (Exception e) {
				PatPat.LOGGER.error("Failed to open file at \"{}\", reason:", path, e);
			}
			return null;
		}).orElse(null);
	}

	@Override
	public ModEnvironment getEnvironment() {
		return switch (FabricLoader.getInstance().getEnvironmentType()) {
			case CLIENT -> ModEnvironment.CLIENT;
			case SERVER -> ModEnvironment.SERVER;
		};
	}

}

*///?}
