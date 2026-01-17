package net.lopymine.patpat.entrypoint.neoforge.loader;

import java.io.*;
import java.nio.file.Path;
import net.lopymine.patpat.PatPat;
import net.lopymine.patpat.entrypoint.loader.IEarlyModLoader;
import net.lopymine.patpat.entrypoint.loader.IModLoader.ModEnvironment;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.*;
import net.neoforged.neoforgespi.language.IModFileInfo;

public class NeoForgeEarlyModLoader implements IEarlyModLoader {

	@Override
	public boolean isDevelopmentEnvironment() {
		return !FMLEnvironment.isProduction();
	}

	@Override
	public Path getConfigDir() {
		return FMLPaths.CONFIGDIR.get().resolve("%s/".formatted(PatPat.MOD_ID));
	}

	@Override
	public boolean isModLoaded(String modId) {
		ModList list = ModList.get();
		if (list == null) {
			return FMLLoader.getCurrent().getLoadingModList().getModFileById(modId) != null;
		}
		return list.isLoaded(modId);
	}

	@Override
	public InputStream loadModFile(String modId, String path) {
		IModFileInfo file = ModList.get().getModFileById(modId);
		if (file == null) {
			PatPat.LOGGER.error("Failed to load file at \"{}\", because \"{}\" mod container doesn't exits!", path, modId);
			return null;
		}
		try {
			return file.getFile().getContents().openFile(path);
		} catch (IOException e) {
			PatPat.LOGGER.error("Failed to open file at \"{}\", reason:", path, e);
			return null;
		}
	}

	@Override
	public ModEnvironment getEnvironment() {
		return switch (FMLEnvironment.getDist()) {
			case CLIENT -> ModEnvironment.CLIENT;
			case DEDICATED_SERVER -> ModEnvironment.SERVER;
		};
	}
}
