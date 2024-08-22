package net.lopymine.patpat.config.resourcepack;

import lombok.Getter;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.lopymine.patpat.utils.VersionedThings;

import java.util.*;
import org.jetbrains.annotations.Nullable;

@Getter
public class PlayerConfig {
	public static final Codec<PlayerConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.STRING.optionalFieldOf("name").forGetter(PlayerConfig::getOptionalName),
			VersionedThings.UUID_CODEC.optionalFieldOf("uuid").forGetter(PlayerConfig::getOptionalUuid)
	).apply(instance, PlayerConfig::new));

	@Nullable
	private final String name;
	@Nullable
	private final UUID uuid;

	public PlayerConfig(Optional<String> name, Optional<UUID> uuid) {
		this.name = name.orElse(null);
		this.uuid = uuid.orElse(null);
	}

	public static PlayerConfig of(@Nullable String playerName, @Nullable UUID playerUuid) {
		return new PlayerConfig(Optional.ofNullable(playerName), Optional.ofNullable(playerUuid));
	}

	private Optional<String> getOptionalName() {
		return Optional.ofNullable(this.name);
	}

	private Optional<UUID> getOptionalUuid() {
		return Optional.ofNullable(this.uuid);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof PlayerConfig that)) return false;
		return (that.name == null || Objects.equals(this.name, that.name)) && (that.uuid == null || Objects.equals(this.uuid, that.uuid));
	}

	@Override
	public String toString() {
		return "PlayerConfig{" +
				"name='" + name + '\'' +
				", uuid=" + uuid +
				'}';
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.name, this.uuid);
	}
}
