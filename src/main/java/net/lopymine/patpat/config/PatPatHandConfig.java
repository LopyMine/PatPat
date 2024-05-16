package net.lopymine.patpat.config;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.ExtensionMethod;
import net.lopymine.patpat.extension.EntityExtension;
import net.minecraft.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

import static net.lopymine.patpat.config.EntityConfig.ENTITY_FIELD;

@Getter
@Setter
@ExtensionMethod(EntityExtension.class)
public final class PatPatHandConfig {

	public static final Codec<PatPatHandConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		Version.CODEC.optionalFieldOf("version", Version.DEFAULT).forGetter(PatPatHandConfig::getVersion),
		AnimationConfig.CODEC.fieldOf("animation").forGetter(PatPatHandConfig::getAnimation),
		Codec.BOOL.optionalFieldOf("blacklist", false).forGetter(PatPatHandConfig::isBlacklist),
		Codec.BOOL.optionalFieldOf("override", false).forGetter(PatPatHandConfig::isOverride),
		Codec.either(ENTITY_FIELD.listOf(), ENTITY_FIELD).fieldOf("entities").forGetter(PatPatHandConfig::getEither)
	).apply(instance, PatPatHandConfig::new));


	private final Version version;
	private final AnimationConfig animation;
	private final boolean blacklist;
	private final boolean override;
	private final Either<List<EntityConfig>, EntityConfig> either;
	private List<EntityConfig> entities;
	private boolean all;

	public PatPatHandConfig(Version version, AnimationConfig animation, boolean blacklist, boolean override, Either<List<EntityConfig>, EntityConfig> either) {
		this.version = version;
		this.animation = animation;
		this.blacklist = blacklist;
		this.override = override;
		this.either = either;
		either.left().ifPresentOrElse(
			left -> this.entities = left,
			() -> this.entities = List.of(either.right().orElseThrow())
		);
		for (EntityConfig config : this.entities) {
			if (config.getEntityId().equals("all")) {
				this.all = true;
				return;
			}
		}
	}

	public boolean isApply(@NotNull Entity entity) {
		return isApply(entity.getTypeId(), entity.getName().toString(), entity.getUuid());
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
