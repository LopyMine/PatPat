package net.lopymine.patpat.utils;

import lombok.experimental.UtilityClass;
import net.lopymine.patpat.PatLogger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.util.Set;


// TODO: Merge all utils in one universal
@UtilityClass
public class ProximityPacketServerWhitelistConfigUtils {

	public static void create(File config, PatLogger logger) {
		try {
			if (!config.createNewFile()) {
				logger.error("Invoked server whitelist config creation, but config already exists!");
			}
		} catch (Exception e) {
			logger.error("Failed to create %s config!".formatted(config.getName()), e);
		}
	}


	public static void read(File config, PatLogger logger, Set<String> set) {
		if (!config.exists()) {
			ProximityPacketServerWhitelistConfigUtils.create(config, logger);
			return;
		}

		int lineNumber = 0;
		String line;
		try (BufferedReader reader = new BufferedReader(new FileReader(config))) {
			line = reader.readLine();
			while (line != null) {
				lineNumber++;
				set.add(line);
				line = reader.readLine();
			}
		} catch (Exception e) {
			logger.error("Failed to reload IgnoreMobListConfig:", e);
		}
	}

	public static void save(File config, PatLogger logger, Set<String> set) {
		try (FileWriter writer = new FileWriter(config, StandardCharsets.UTF_8)) {
			String collect = String.join("\n", set);
			logger.debug("Saving proximity packet server whitelist config:");
			logger.debug(collect);
			writer.write(collect);
		} catch (Exception e) {
			logger.error("ProximityPacketServerWhitelistConfig with name " + config.getName(), e);
		}
	}
}
