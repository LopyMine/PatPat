package net.lopymine.patpat.entrypoint.forge.loader;

//? if forge {

/*import java.io.*;
import java.nio.file.*;
import net.lopymine.patpat.PatPat;
import net.lopymine.patpat.entrypoint.loader.IEarlyCommonModLoader;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.*;
import net.minecraftforge.forgespi.language.IModFileInfo;
import org.jetbrains.annotations.Nullable;

public class ForgeEarlyCommonModLoader implements IEarlyCommonModLoader {

	@Override
	public String getPlatform() {
		return "Forge";
	}

	@Override
	public Path getConfigDir() {
		return FMLPaths.CONFIGDIR.get();
	}

	@Override
	public boolean isDevelopmentEnvironment() {
		return !FMLLoader.isProduction();
	}

	@Override
	public boolean isModLoaded(String modId) {
		ModList list = ModList.get();
		if (list == null) {
			return FMLLoader.getLoadingModList().getModFileById(modId) != null;
		} else {
			return list.isLoaded(modId);
		}
	}

	@Override
	public @Nullable InputStream loadModFile(String modId, String path) {
		IModFileInfo file = ModList.get().getModFileById(modId);
		if (file == null) {
			PatPat.LOGGER.error("Failed to load file at \"{}\", because \"{}\" mod container doesn't exits!", path, modId);
			return null;
		}
		try {
			return Files.newInputStream(file.getFile().findResource(path));
		} catch (IOException e) {
			PatPat.LOGGER.error("Failed to open file at \"{}\", reason:", path, e);
			return null;
		}
	}

	@Override
	public ModEnvironment getEnvironment() {
		return switch (FMLEnvironment.dist) {
			case CLIENT -> ModEnvironment.CLIENT;
			case DEDICATED_SERVER -> ModEnvironment.SERVER;
		};
	}
}
*///?}
