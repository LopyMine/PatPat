package net.lopymine.patpat;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.lopymine.patpat.dedicated.PatPatDedicatedServerTranslationManager;
import net.lopymine.patpat.utils.TextUtils;
import net.minecraft.network.chat.MutableComponent;

public class PatTranslation {

	public static MutableComponent text(String path, Object... args) {
		String key = String.format("%s.%s", PatPat.MOD_ID, path);
		
		return FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER ?
				TextUtils.processWithArgs(PatPatDedicatedServerTranslationManager.getOrDefault(key), args)
				:
				TextUtils.translatable(key, args);
	}

}
