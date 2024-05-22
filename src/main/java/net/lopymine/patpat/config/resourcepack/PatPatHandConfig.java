package net.lopymine.patpat.config.resourcepack;

import lombok.*;
import lombok.experimental.ExtensionMethod;
import net.minecraft.entity.Entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.lopymine.patpat.extension.EntityExtension;

import java.util.*;
import org.jetbrains.annotations.*;

@Getter
@Setter
@ExtensionMethod(EntityExtension.class)
public final class PatPatHandConfig {
	public static final Codec<PatPatHandConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		Version.CODEC.optionalFieldOf("version", Version.DEFAULT).forGetter(PatPatHandConfig::getVersion),
		AnimationConfig.CODEC.fieldOf("animation").forGetter(PatPatHandConfig::getAnimation),
		Codec.BOOL.optionalFieldOf("blacklist", false).forGetter(PatPatHandConfig::isBlacklist),
		Codec.BOOL.optionalFieldOf("override", false).forGetter(PatPatHandConfig::isOverride),
			EntityConfig.LISTED_CODEC.fieldOf("entities").forGetter(PatPatHandConfig::getEntities)
	).apply(instance, PatPatHandConfig::new));

	private final Version version;
	private final AnimationConfig animation;
	private final boolean blacklist;
	private final boolean override;
	private List<EntityConfig> entities;
	private boolean all;

	public PatPatHandConfig(Version version, AnimationConfig animation, boolean blacklist, boolean override, List<EntityConfig> entities) {
		this.version = version;
		this.animation = animation;
		this.blacklist = blacklist;
		this.override = override;
		this.entities = entities;
		for (EntityConfig config : this.entities) {
			if (config.getEntityId().equals("all")) {
				this.all = true;
				return;
			}
		}
	}

	public boolean isApply(@NotNull Entity entity) {
		return this.isApply(entity.getTypeId(), entity.getName().getString(), entity.getUuid());
	}

	public boolean isApply(@NotNull String entityTypeId, @Nullable String entityName, @Nullable UUID entityUuid) {
		if (this.all) {
			return !this.blacklist;
		}
		for (EntityConfig entityConfig : this.entities) {
			if (entityConfig.is(entityTypeId, entityName, entityUuid)) {
				return !this.blacklist;
			}
		}
		return this.blacklist;
	}

	@Override
	public String toString() {
		return "PatPatHandConfig{version='%s', animation='%s', blacklist='%s', override='%s', all='%s', entities=%s}"
			.formatted(version, animation, blacklist, override, all, entities);
	}
}
