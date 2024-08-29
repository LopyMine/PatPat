package net.lopymine.patpat;

import lombok.Getter;
import net.minecraft.text.MutableText;

import net.fabricmc.api.ModInitializer;

import net.lopymine.patpat.config.server.PatPatServerConfig;
import net.lopymine.patpat.manager.PatPatConfigManager;
import net.lopymine.patpat.manager.server.*;
import net.lopymine.patpat.utils.TextUtils;

//? >=1.17 {
import org.slf4j.*;
//?}

public class PatPat implements ModInitializer {

	public static final String MOD_VERSION = /*$ mod_version*/ "1.0.0";
	public static final String MOD_NAME = /*$ mod_name*/ "PatPat";
	public static final String MOD_ID = /*$ mod_id*/ "patpat";
	//? >=1.17
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);
	@Getter
	private static PatPatServerConfig config = PatPatServerConfig.getInstance();

	public static void info(String text, Object... args) {
		//? >=1.17 {
		LOGGER.info(text, args);
		//?} else {
		/*System.out.println("[PatPat/INFO] " + text.replace("{}", "%s").formatted(args));
		 *///?}
	}

	public static void warn(String text, Object... args) {
		//? >=1.17 {
		LOGGER.warn(text, args);
		//?} else {
		/*System.out.println("[PatPat/WARN] " + text.replace("{}", "%s").formatted(args));
		 *///?}
	}

	public static void error(String text, Object... args) {
		//? >=1.17 {
		LOGGER.error(text, args);
		//?} else {
		/*System.out.println("[PatPat/ERROR] " + text.replace("{}", "%s").formatted(args));
		 *///?}
	}

	public static MutableText text(String path, Object... args) {
		return TextUtils.translatable(String.format("%s.%s", MOD_ID, path), args);
	}

	@Override
	public void onInitialize() {
		PatPat.config = PatPatServerConfig.getInstance();
		PatPatConfigManager.onInitialize();
		PatPatServerCommandManager.register();
		PatPatServerPacketManager.register();
		info("PatPat Initialized");
	}
}