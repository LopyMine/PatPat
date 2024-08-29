package net.lopymine.patpat.manager.client;

import net.minecraft.resource.*;
import net.minecraft.util.Identifier;

import net.fabricmc.fabric.api.resource.*;

import net.lopymine.patpat.client.PatPatClient;
import net.lopymine.patpat.config.client.PatPatClientConfig;
import net.lopymine.patpat.utils.IdentifierUtils;

import java.util.*;

public class PatPatClientReloadListener implements SimpleSynchronousResourceReloadListener {

	public static void register() {
		ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new PatPatClientReloadListener());
	}

	@Override
	public Identifier getFabricId() {
		return IdentifierUtils.id("patpat_packs_listener");
	}

	@Override
	public void reload(ResourceManager manager) {
		List<ResourcePack> list = manager.streamResourcePacks().toList();
		if (list.isEmpty()) {
			return;
		}
		PatPatClientConfig config = PatPatClient.getConfig();
		if (!config.isModEnabled()) {
			return;
		}
		PatPatClientResourcePackManager.INSTANCE.reload(new ArrayList<>(list), manager);
	}
}
