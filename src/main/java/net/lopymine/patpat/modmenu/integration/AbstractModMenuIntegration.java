package net.lopymine.patpat.modmenu.integration;

//? if fabric {
/*
import net.lopymine.mossylib.MossyLib;
import net.lopymine.mossylib.client.MossyLibClient;
import net.minecraft.client.gui.screens.Screen;

public abstract class AbstractModMenuIntegration implements ModMenuApi {

	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		FabricLoader fabricLoader = FabricLoader.getInstance();
		if (fabricLoader.isModLoaded("yet_another_config_lib_v3")) {
			ModContainer modContainer = fabricLoader.getModContainer("yet_another_config_lib_v3").orElseThrow();
			Version version = modContainer.getMetadata().getVersion();
			try {
				Version requestsVersion = Version.parse(MossyLib.YACL_DEPEND_VERSION);
				if (version.compareTo(requestsVersion) >= 0) {
					return this::createConfigScreen;
				}
			} catch (VersionParsingException e) {
				MossyLibClient.LOGGER.error("Failed to compare YACL version, tell mod author about this error: ", e);
			}
			return parent -> NoConfigLibraryScreen.createScreenAboutOldVersion(parent, version.getFriendlyString(), this.getModId());
		}
		return (parent) -> NoConfigLibraryScreen.createScreen(parent, this.getModId());
	}

	protected abstract String getModId();

	protected abstract Screen createConfigScreen(Screen parent);
}

*/
//?} elif neoforge {

import net.neoforged.fml.*;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.minecraft.client.gui.screens.Screen;

public abstract class AbstractModMenuIntegration {

	public void register(ModContainer container) {
		container.registerExtensionPoint(IConfigScreenFactory.class, (modContainer, parent) -> createConfigScreen(parent));
	}

 	protected abstract String getModId();

	protected abstract Screen createConfigScreen(Screen parent);

}

//?} elif forge {

/*import net.lopymine.mossylib.MossyLib;
import net.lopymine.mossylib.client.MossyLibClient;
import net.lopymine.mossylib.loader.MossyLoader;
import net.minecraft.client.gui.screens.Screen;
import net.minecraftforge.client.ConfigScreenHandler.ConfigScreenFactory;
import net.minecraftforge.fml.*;
import org.apache.maven.artifact.versioning.*;

public abstract class AbstractModMenuIntegration {

	public void register(ModContainer container) {
		container.registerExtensionPoint(ConfigScreenFactory.class, () -> new ConfigScreenFactory((minecraft, parent) -> {
			if (MossyLoader.isModLoaded("yet_another_config_lib_v3", false)) {
				ModContainer yacl = ModList.get().getModContainerById("yet_another_config_lib_v3").orElseThrow();
				ArtifactVersion version = yacl.getModInfo().getVersion();
				try {
					ArtifactVersion requestsVersion = new DefaultArtifactVersion(MossyLib.YACL_DEPEND_VERSION);
					if (version.compareTo(requestsVersion) >= 0) {
						return this.createConfigScreen(parent);
					}
				} catch (Exception e) {
					MossyLibClient.LOGGER.error("Failed to compare YACL version, tell mod author about this error: ", e);
				}
				return NoConfigLibraryScreen.createScreenAboutOldVersion(parent, version.getQualifier(), this.getModId());
			}
			return NoConfigLibraryScreen.createScreen(parent, this.getModId());
		}));
	}

 	protected abstract String getModId();

	protected abstract Screen createConfigScreen(Screen parent);

}

*///?}
