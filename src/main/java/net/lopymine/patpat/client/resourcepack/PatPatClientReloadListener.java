package net.lopymine.patpat.client.resourcepack;

import net.lopymine.patpat.entrypoint.MultiLoader;
import net.lopymine.patpat.utils.RLUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.resources.*;

import net.lopymine.patpat.client.config.PatPatClientConfig;

import java.util.*;
import org.jetbrains.annotations.NotNull;

public class PatPatClientReloadListener implements ResourceManagerReloadListener {

	public static void register() {
		MultiLoader.getInstance().registerResourceReloadListener(RLUtils.modId("patpat_packs_listener"), new PatPatClientReloadListener());
	}

	@Override
	public void onResourceManagerReload(@NotNull ResourceManager manager) {
		List<PackResources> list = Minecraft.getInstance().getResourceManager().listPacks().toList();
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
