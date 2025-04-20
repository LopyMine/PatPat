package net.lopymine.patpat.client.config.resourcepack;

import lombok.Getter;
import lombok.experimental.ExtensionMethod;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.lopymine.patpat.extension.EntityExtension;
import net.lopymine.patpat.utils.VersionedThings;

import java.util.*;
import org.jetbrains.annotations.*;

@Getter
@ExtensionMethod(EntityExtension.class)
public class EntityConfig {

	public static final Codec<EntityConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.STRING.fieldOf("id").forGetter(EntityConfig::getEntityId),
			Codec.STRING.optionalFieldOf("name").forGetter(EntityConfig::getOptionalEntityName),
			VersionedThings.UUID_CODEC.optionalFieldOf("uuid").forGetter(EntityConfig::getOptionalEntityUuid),
			PlayerConfig.CODEC.listOf().optionalFieldOf("from").forGetter(EntityConfig::getOptionalFrom)
	).apply(instance, EntityConfig::new));

	public static final Codec<EntityConfig> STRINGED_CODEC = Codec.either(Codec.STRING, EntityConfig.CODEC).xmap(either -> {
		if (either.left().isPresent()) {
			return EntityConfig.of(either.left().get());
		}
		if (either.right().isPresent()) {
			return either.right().get();
		}
		return null;
	}, entityData -> {
		String id = entityData.getEntityId();
		String name = entityData.getEntityName();
		UUID uuid = entityData.getEntityUuid();
		List<PlayerConfig> from = entityData.getEntitiesFrom();
		if (name == null && uuid == null && from == null) {
			return Either.left(id);
		}
		return Either.right(entityData);
	});

	public static final Codec<List<EntityConfig>> LISTED_CODEC = Codec.either(STRINGED_CODEC.listOf(), STRINGED_CODEC).xmap(either -> {
		if (either.left().isPresent()) {
			return either.left().get();
		}
		if (either.right().isPresent()) {
			return new ArrayList<>(List.of(either.right().get()));
		}
		return new ArrayList<>();
	}, list -> {
		if (list.size() == 1) {
			return Either.right(list.get(0));
		}
		return Either.left(list);
	});

	@NotNull
	private final String entityId;
	@Nullable
	private final String entityName;
	@Nullable
	private final UUID entityUuid;
	@Nullable
	private final List<PlayerConfig> entitiesFrom;

	public EntityConfig(@NotNull String entityId, Optional<String> entityName, Optional<UUID> entityUuid, Optional<List<PlayerConfig>> entitiesFrom) {
		this.entityId     = entityId;
		this.entityName   = entityName.orElse(null);
		this.entityUuid   = entityUuid.orElse(null);
		this.entitiesFrom = entitiesFrom.orElse(null);
	}

	public static EntityConfig of(@NotNull String entityId) {
		return new EntityConfig(entityId, Optional.empty(), Optional.empty(), Optional.empty());
	}

	public boolean is(@NotNull String entityTypeId, @Nullable String entityName, @Nullable UUID entityUuid) {
		return entityTypeId.equalsIgnoreCase(this.entityId)
				&& (this.entityName == null || Objects.equals(this.entityName, entityName))
				&& (this.entityUuid == null || Objects.equals(this.entityUuid, entityUuid));
	}

	private Optional<String> getOptionalEntityName() {
		return Optional.ofNullable(this.entityName);
	}

	private Optional<UUID> getOptionalEntityUuid() {
		return Optional.ofNullable(this.entityUuid);
	}

	private Optional<List<PlayerConfig>> getOptionalFrom() {
		return Optional.ofNullable(this.entitiesFrom);
	}

	@Override
	public String toString() {
		return "EntityConfig{" +
				"entityId='" + entityId + '\'' +
				", entityName='" + entityName + '\'' +
				", entityUuid=" + entityUuid +
				", from=" + entitiesFrom +
				'}';
	}
}
