package net.lopymine.patpat.config;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.Getter;
import lombok.experimental.ExtensionMethod;
import net.lopymine.patpat.extension.EntityExtension;
import net.minecraft.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

@Getter
@ExtensionMethod(EntityExtension.class)
public class EntityConfig {

	public static final Codec<EntityConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		Codec.STRING.fieldOf("id").forGetter(EntityConfig::getEntityId),
		Codec.STRING.optionalFieldOf("name", null).forGetter(EntityConfig::getEntityName),
		Codec.STRING.comapFlatMap(EntityConfig::parseUuid, UUID::toString).optionalFieldOf("uuid", null).orElse(null).forGetter(EntityConfig::getEntityUuid)
	).apply(instance, EntityConfig::new));
	public static final Codec<EntityConfig> ENTITY_FIELD = Codec.either(Codec.STRING, EntityConfig.CODEC).xmap(either -> {
		if (either.left().isPresent()) {
			return EntityConfig.of(either.left().get());
		}
		if (either.right().isPresent()) {
			return either.right().get();
		}
		return null;
	}, entityConfig -> {
		if (entityConfig == null) {
			return null;
		}
		String id = entityConfig.getEntityId();
		String name = entityConfig.getEntityName();
		UUID uuid = entityConfig.getEntityUuid();
		if (name == null && uuid == null) {
			return Either.left(id);
		}
		return Either.right(entityConfig);
	});
	private final String entityId;
	private final String entityName;
	private final UUID entityUuid;

	public EntityConfig(@NotNull String entityId, @Nullable String entityName, @Nullable UUID entityUuid) {
		this.entityId = entityId;
		this.entityName = entityName;
		this.entityUuid = entityUuid;
	}

	public static EntityConfig of(String entityId) {
		return new EntityConfig(entityId, null, null);
	}

	public static DataResult<UUID> parseUuid(String value) {
		try {
			return DataResult.success(UUID.fromString(value));
		} catch (IllegalArgumentException e) {
			return DataResult.error(() -> "String '%s' cannot be converted to a UUID".formatted(value), (UUID) null);
		}
	}

	public boolean is(@NotNull Entity entity) {
		return entity.getTypeId().equalsIgnoreCase(entityId)
			&& (entityName == null || entityName.equals(entity.getName().toString()))
			&& (entityUuid == null || entityUuid.equals(entity.getUuid()));
	}

	public boolean is(@NotNull String entityTypeId, @Nullable String entityName, @Nullable UUID entityUuid) {
		return entityTypeId.equalsIgnoreCase(entityId)
			&& (this.entityName == null || this.entityName.equals(entityName))
			&& (this.entityUuid == null || this.entityUuid.equals(entityUuid));
	}
}
