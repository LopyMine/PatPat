package net.lopymine.patpat.client.resourcepack;

import net.lopymine.patpat.PatPat;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.*;
import net.fabricmc.fabric.api.resource.*;

import net.lopymine.patpat.client.config.PatPatClientConfig;
import net.lopymine.patpat.utils.IdentifierUtils;

import java.util.*;

//? if >=1.21.9 {
/*import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
*///?}

public class PatPatClientReloadListener implements /*? if <=1.21.8 {*/ SimpleSynchronousResourceReloadListener /*?} else {*/ /*ResourceManagerReloadListener *//*?}*/ {

	public static void register() {
		//? if >=1.21.9 {
		/*ResourceLoader.get(PackType.CLIENT_RESOURCES).registerReloader(getFabricId(), new PatPatClientReloadListener());
		*///?} else {
		ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(new PatPatClientReloadListener());
		//?}
	}

	/*? if <=1.21.8 {*/@Override/*?}*/
	public /*? if >=1.21.9 {*/ /*static *//*?}*/ ResourceLocation getFabricId() {
		return IdentifierUtils.modId("%s-reload-listener".formatted(PatPat.MOD_ID));
	}

	@Override
	public void onResourceManagerReload(ResourceManager manager) {
		List<PackResources> list = manager.listPacks().toList();
		if (list.isEmpty()) {
			return;
		}
		PatPatClientConfig config = PatPatClientConfig.getInstance();
		if (!config.getMainConfig().isModEnabled()) {
			return;
		}
		PatPatClientResourcePackManager.INSTANCE.reload(new ArrayList<>(list), manager);
	}
}
