package net.lopymine.patpat.config.resourcepack;

import lombok.*;
import lombok.experimental.ExtensionMethod;
import net.minecraft.entity.Entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.lopymine.patpat.client.PatPatClient;
import net.lopymine.patpat.extension.EntityExtension;

import java.util.*;
import org.jetbrains.annotations.*;

@Getter
@Setter
@ExtensionMethod(EntityExtension.class)
public final class CustomAnimationConfig implements Comparable<CustomAnimationConfig> {

	public static final Codec<CustomAnimationConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Version.CODEC.fieldOf("version").forGetter(CustomAnimationConfig::getVersion),
			Codec.INT.optionalFieldOf("priority", 0).forGetter(CustomAnimationConfig::getPriority),
			CustomAnimationSettingsConfig.CODEC.fieldOf("animation").forGetter(CustomAnimationConfig::getAnimation),
			Codec.BOOL.optionalFieldOf("blacklist", false).forGetter(CustomAnimationConfig::isBlacklist),
			EntityConfig.LISTED_CODEC.fieldOf("entities").forGetter(CustomAnimationConfig::getEntities)
	).apply(instance, CustomAnimationConfig::new));

	private final Version version;
	private final int priority;
	private final CustomAnimationSettingsConfig animation;
	private final boolean blacklist;
	private final boolean useForAll;
	private List<EntityConfig> entities;
	private String configPath;

	public CustomAnimationConfig(Version version, int priority, CustomAnimationSettingsConfig animation, boolean blacklist, List<EntityConfig> entities) {
		this.version   = version;
		this.priority  = priority;
		this.animation = animation;
		this.blacklist = blacklist;
		this.entities  = entities;
		this.useForAll = entities.stream().anyMatch(config -> config.getEntityId().equals("all"));
	}

	public boolean canUseFor(@NotNull Entity entity, @NotNull PlayerConfig whoPatted) {
		return this.canUseFor(entity.getTypeId(), entity.getName().getString(), entity.getUuid(), whoPatted);
	}

	public boolean canUseFor(@NotNull String entityTypeId, @Nullable String entityName, @Nullable UUID entityUuid, @NotNull PlayerConfig whoPatted) {
		if (this.useForAll) {
			return !this.blacklist;
		}
		for (EntityConfig entityConfig : this.entities) {
			if (PatPatClient.getConfig().isDebugLogEnabled()) {
				PatPatClient.LOGGER.info("Comparing Entity Config: " + entityConfig.toString());
				PatPatClient.LOGGER.info("with {}, {}, {}, {}", entityTypeId, entityName, entityUuid, whoPatted.toString());
			}
			if (entityConfig.is(entityTypeId, entityName, entityUuid) && (entityConfig.getEntitiesFrom() == null || entityConfig.getEntitiesFrom().contains(whoPatted))) {
				return !this.blacklist;
			}
		}
		return this.blacklist;
	}

	@Override
	public int compareTo(@NotNull CustomAnimationConfig o) {
		int compare = Integer.compare(this.priority, o.getPriority());
		if (compare == 0) {
			compare = this.configPath.compareTo(o.getConfigPath());
		}
		return compare;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		CustomAnimationConfig that = (CustomAnimationConfig) o;
		return priority == that.priority
				&& blacklist == that.blacklist
				&& useForAll == that.useForAll
				&& Objects.equals(version, that.version)
				&& Objects.equals(animation, that.animation)
				&& Objects.equals(entities, that.entities);
	}

	@Override
	public int hashCode() {
		return Objects.hash(version, priority, animation, blacklist, useForAll, entities);
	}

	@Override
	public String toString() {
		return "CustomAnimationConfig{version='%s',priority='%s', animation='%s', blacklist='%s', useForAll='%s', entities=%s}"
				.formatted(this.version, this.priority, this.animation, this.blacklist, this.useForAll, this.entities);
	}
}
