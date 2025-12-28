package net.lopymine.patpat.translation;

import net.lopymine.patpat.PatPat;
import net.lopymine.patpat.dedicated.PatPatDedicatedServerTranslationManager;
import net.lopymine.patpat.entrypoint.IModLoader.ModEnvironment;
import net.lopymine.patpat.entrypoint.MultiLoader;
import net.lopymine.patpat.utils.TextUtils;
import net.minecraft.network.chat.MutableComponent;

public class PatTranslation {

	public static MutableComponent text(String path, Object... args) {
		String key = String.format("%s.%s", PatPat.MOD_ID, path);
		
		return MultiLoader.getInstance().getEnvironment() == ModEnvironment.SERVER ?
				TextUtils.processWithArgs(PatPatDedicatedServerTranslationManager.getOrDefault(key), args)
				:
				TextUtils.translatable(key, args);
	}

}
