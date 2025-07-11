package net.lopymine.patpat.utils;

import lombok.experimental.UtilityClass;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;

import net.lopymine.patpat.PatLogger;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class IgnoreMobListConfigUtils {

	public static void create(File config, PatLogger logger) {
		try {
			if (!config.createNewFile()) {
				logger.error("Invoked player list config creation, but config already exists!");
			}
		} catch (Exception e) {
			logger.error("Failed to create %s config!".formatted(config.getName()), e);
		}
	}


	public static void read(File config, PatLogger logger, Set<EntityType<?>> set) {
		if (!config.exists()) {
			PlayerListConfigUtils.create(config, logger);
			return;
		}

		int lineNumber = 0;
		String line;
		try (BufferedReader reader = new BufferedReader(new FileReader(config))) {
			line = reader.readLine();
			while (line != null) {
				lineNumber++;
				try {
					EntityType<?> entityType = VersionedThings.ENTITY_TYPE.getOptional(ResourceLocationUtils.parse(line)).orElse(null);
					if (entityType == null) {
						logger.error("EntityType on line {} is not exist:", lineNumber, line, config.getName());
					} else {
						set.add(entityType);
					}
				} catch (Exception e) {
					logger.error("Failed to parse line %d while parsing IgnoreMobListConfig: %s".formatted(lineNumber, line), e);
				}
				line = reader.readLine();
			}
		} catch (Exception e) {
			logger.error("Failed to reload IgnoreMobListConfig:", e);
		}
	}

	public static void save(File config, PatLogger logger, Set<EntityType<?>> set) {
		try (FileWriter writer = new FileWriter(config, StandardCharsets.UTF_8)) {
			String collect = set.stream()
					.map(entityType -> {
						ResourceLocation entityTypeResource = VersionedThings.ENTITY_TYPE.getKey(entityType);
						if (entityTypeResource == null) {
							return null;
						}
						return entityTypeResource.toString();
					})
					.collect(Collectors.joining("\n"));
			logger.debug("Saving list with entity types:");
			logger.debug(collect);
			writer.write(collect);
		} catch (Exception e) {
			logger.error("IgnoreMobListConfig with name " + config.getName(), e);
		}
	}
}
