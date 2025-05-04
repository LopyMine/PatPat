package net.lopymine.patpat.utils;

import com.google.gson.*;

import com.mojang.serialization.*;

import net.lopymine.patpat.*;
import net.lopymine.patpat.client.PatPatClient;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;

public class ConfigUtils {

	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

	private static String generateUniqueBackupName(Path parentDir, String fileName) {
		String backupName = fileName + ".bkp";
		int index = 0;

		while (Files.exists(parentDir.resolve(backupName))) {
			backupName = String.format("%s%d.bkp", fileName, ++index);
		}

		return backupName;
	}

	private static void createBackup(File file) {
		try {
			Path fileFolder = file.toPath().getParent();
			String filename = file.getName();
			String backupFileName = generateUniqueBackupName(fileFolder, filename);

			Path backupPath = fileFolder.resolve(backupFileName);
			Files.copy(file.toPath(), backupPath);
		} catch (Exception e) {
			PatPat.LOGGER.error("Failed to backup config", e);
		}
	}

	private static <A> A create(Codec<A> codec, File location) {
		A config = CodecUtils.parseNewInstanceHacky(codec);
		if (location.exists()) {
			PatPatClient.LOGGER.error("Invoked create config method, but config already exists!");
			return config;
		}
		try (FileWriter writer = new FileWriter(location, StandardCharsets.UTF_8)) {
			String json = GSON.toJson(codec.encode(config, JsonOps.INSTANCE, JsonOps.INSTANCE.empty())/*? if >=1.20.5 {*/.getOrThrow());/*?} else*//*.getOrThrow(false, PatPatClient.LOGGER::error));*/
			writer.write(json);
		} catch (Exception e) {
			PatPatClient.LOGGER.error("Failed to create config", e);
		}
		return config;
	}

	public static <A> A readConfig(Codec<A> codec, File location, PatLogger logger) {
		if (!location.exists()) {
			return ConfigUtils.create(codec, location);
		}
		try (FileReader reader = new FileReader(location, StandardCharsets.UTF_8)) {
			return codec.decode(JsonOps.INSTANCE, /*? <=1.17.1 {*//*new JsonParser().parse(reader)*//*?} else {*/JsonParser.parseReader(reader)/*?}*/)/*? if >=1.20.5 {*/.getOrThrow()/*?} else {*//*.getOrThrow(false, PatPatClient.LOGGER::error)*//*?}*/.getFirst();
		} catch (Exception e) {
			logger.error("Failed to read config", e);
			createBackup(location);
		}
		return ConfigUtils.create(codec, location);
	}

	public static <A> void saveConfig(A config, Codec<A> codec, File location, PatLogger logger) {
		logger.debug("Saving config...");
		try (FileWriter writer = new FileWriter(location, StandardCharsets.UTF_8)) {
			String json = GSON.toJson(codec.encode(config, JsonOps.INSTANCE, JsonOps.INSTANCE.empty())/*? if >=1.20.5 {*/.getOrThrow());/*?} else*//*.getOrThrow(false, PatPatClient.LOGGER::error));*/
			writer.write(json);
		} catch (Exception e) {
			logger.error("Failed to save config:", e);
		}
		logger.debug("Config saved");
	}

}
