package net.lopymine.patpat.client.config.list;

import lombok.Getter;
import net.lopymine.patpat.common.config.list.AbstractListConfig;
import net.lopymine.patpat.utils.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;

import net.lopymine.patpat.PatLogger;
import net.lopymine.patpat.client.PatPatClient;
import net.lopymine.patpat.common.config.PatPatConfigManager;

import java.io.File;
import java.util.*;
import org.jetbrains.annotations.NotNull;

@Getter
public class PatPatClientIgnoreMobListConfig extends AbstractListConfig<EntityType<?>> {

	private static final PatLogger LOGGER = PatPatClient.LOGGER.extend(PatPatClientIgnoreMobListConfig.class.getSimpleName());
	private static final File CONFIG_FILE = PatPatConfigManager.CONFIG_PATH.resolve("ignore_mob_list.txt").toFile();
	private static final PatPatClientIgnoreMobListConfig INSTANCE = new PatPatClientIgnoreMobListConfig();

	public static PatPatClientIgnoreMobListConfig getInstance() {
		return getInitialized(INSTANCE);
	}

	private final Set<EntityType<?>> values = new HashSet<>();

	private PatPatClientIgnoreMobListConfig() {
		super(new HashSet<>(), LOGGER, CONFIG_FILE);
	}

	public boolean addMob(@NotNull EntityType<?> type) {
		return this.values.add(type);
	}

	public boolean removeMob(@NotNull EntityType<?> type) {
		return this.values.remove(type);
	}

	public boolean isIgnored(@NotNull EntityType<?> type) {
		return this.values.contains(type);
	}

	@Override
	protected String encode(EntityType<?> element) {
		ResourceLocation entityTypeResource = VersionedThings.ENTITY_TYPE.getKey(element);
		if (entityTypeResource == null) {
			return null;
		}
		return entityTypeResource.toString();
	}

	@Override
	protected EntityType<?> decode(String line) {
		EntityType<?> entityType = VersionedThings.ENTITY_TYPE.getOptional(ResourceLocationUtils.parse(line)).orElse(null);
		if (entityType == null) {
			this.getLogger().error("Failed to find entity type from line: \"{}\"", line);
			return null;
		}
		return entityType;
	}
}
