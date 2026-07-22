package net.lopymine.patpat.entrypoint.impl.neoforge.loader;

//? if neoforge {


/*import java.io.*;
import java.nio.file.*;
import net.lopymine.patpat.PatPat;
import net.lopymine.patpat.entrypoint.loader.IEarlyCommonModLoader;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.*;
import net.neoforged.neoforgespi.language.IModFileInfo;

public class NeoForgeEarlyModLoader implements IEarlyCommonModLoader {

	@Override
	public String getPlatform() {
		return "NeoForge";
	}

	@Override
	public boolean isDevelopmentEnvironment() {
		//? if >=1.21.9 {
		/^return !FMLEnvironment.isProduction();
		^///?}

		//? if <=1.21.8 {
		return !FMLEnvironment.production;
		//?}
	}

	@Override
	public Path getConfigDir() {
		return FMLPaths.CONFIGDIR.get().resolve("%s/".formatted(PatPat.MOD_ID));
	}

	@Override
	public boolean isModLoaded(String modId) {
		ModList list = ModList.get();
		if (list == null) {
			//? if >=1.21.9 {
			/^return FMLLoader.getCurrent().getLoadingModList().getModFileById(modId) != null;
			^///?}

			//? if <=1.21.8 {
			return FMLLoader.getLoadingModList().getModFileById(modId) != null;
			//?}
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
			//? if >=1.21.9 {
			/^return file.getFile().getContents().openFile(path);
			^///?}

			//? if <=1.21.8 {
			return Files.newInputStream(file.getFile().findResource(path));
			//?}
		} catch (IOException e) {
			PatPat.LOGGER.error("Failed to open file at \"{}\", reason:", path, e);
			return null;
		}
	}

	@Override
	public ModEnvironment getEnvironment() {
		//? if >=1.21.9 {
		/^return switch (FMLEnvironment.getDist()) {
			case CLIENT -> ModEnvironment.CLIENT;
			case DEDICATED_SERVER -> ModEnvironment.SERVER;
		};
		^///?}

		//? if <=1.21.8 {
		return switch (FMLEnvironment.dist) {
			case CLIENT -> ModEnvironment.CLIENT;
			case DEDICATED_SERVER -> ModEnvironment.SERVER;
		};
		//?}
	}
}

*///?}
