package net.lopymine.patpat.client.resourcepack;

import java.util.concurrent.Executor;
import net.lopymine.patpat.PatPat;
import net.lopymine.patpat.entrypoint.MultiLoader;
import net.lopymine.patpat.utils.RLUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.resources.*;

import net.lopymine.patpat.client.config.PatPatClientConfig;

import java.util.*;
import org.jetbrains.annotations.NotNull;

public class PatPatClientReloadListener extends AbstractResourceReloadListener {

	public static void register() {
		MultiLoader.getInstance().registerResourceReloadListener(new PatPatClientReloadListener());
	}

	@Override
	public String getModId() {
		return PatPat.MOD_ID;
	}

	@Override
	protected void reloadStuff(PreparationBarrier synchronizer, ResourceManager manager, Executor prepareExecutor, Executor applyExecutor) {
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
