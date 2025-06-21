package net.lopymine.patpat.client.resourcepack;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.fabricmc.fabric.api.resource.*;

import net.lopymine.patpat.client.config.PatPatClientConfig;
import net.lopymine.patpat.utils.IdentifierUtils;

import java.util.*;

public class PatPatClientReloadListener implements SimpleSynchronousResourceReloadListener {

	public static void register() {
		ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(new PatPatClientReloadListener());
	}

	@Override
	public ResourceLocation getFabricId() {
		return IdentifierUtils.modId("patpat_packs_listener");
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
