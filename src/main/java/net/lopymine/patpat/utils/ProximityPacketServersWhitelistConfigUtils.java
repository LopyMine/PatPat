package net.lopymine.patpat.utils;

import lombok.experimental.UtilityClass;
import net.lopymine.patpat.PatLogger;
import net.lopymine.patpat.client.config.ProximityPacketServersWhitelistConfig;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;


// TODO: Merge all utils in one universal
@UtilityClass
public class ProximityPacketServersWhitelistConfigUtils {

	public static void create(File config, PatLogger logger) {
		try {
			if (config.createNewFile()) {
				try (FileWriter writer = new FileWriter(config, StandardCharsets.UTF_8)) {
					writer.write(String.join("\n", ProximityPacketServersWhitelistConfig.DEFAULT_VALUES));
				} catch (IOException e) {
					logger.error("Failed to write default values in %s config!".formatted(config.getName()), e);
				}
			} else {
				logger.error("Invoked server whitelist config creation, but config already exists!");
			}
		} catch (Exception e) {
			logger.error("Failed to create %s config!".formatted(config.getName()), e);
		}
	}


	public static void read(File config, PatLogger logger, List<String> list) {
		if (!config.exists()) {
			ProximityPacketServersWhitelistConfigUtils.create(config, logger);
		}

		int lineNumber = 0;
		String line;
		try (BufferedReader reader = new BufferedReader(new FileReader(config))) {
			line = reader.readLine();
			while (line != null) {
				lineNumber++;
				list.add(line);
				line = reader.readLine();
			}
		} catch (Exception e) {
			logger.error("Failed to reload IgnoreMobListConfig:", e);
		}
	}

	public static void save(File config, PatLogger logger, List<String> list) {
		try (FileWriter writer = new FileWriter(config, StandardCharsets.UTF_8)) {
			String collect = String.join("\n", list);
			logger.debug("Saving proximity packet server whitelist config:");
			logger.debug(collect);
			writer.write(collect);
		} catch (Exception e) {
			logger.error("ProximityPacketServerWhitelistConfig with name " + config.getName(), e);
		}
	}
}
