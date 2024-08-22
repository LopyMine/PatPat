package net.lopymine.patpat.config;

import lombok.*;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.lopymine.patpat.utils.VersionedThings;

import java.util.*;

@Getter
public class PlayerInfoConfig {

	public static final Codec<PlayerInfoConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.STRING.fieldOf("nickname").forGetter(PlayerInfoConfig::getNickname),
			VersionedThings.UUID_CODEC.fieldOf("uuid").forGetter(PlayerInfoConfig::getUuid)
	).apply(instance, PlayerInfoConfig::new));

	@Getter(AccessLevel.PRIVATE)
	private final Pair<String, UUID> pair;

	public PlayerInfoConfig(String nickname, UUID uuid) {
		this.pair = new Pair<>(nickname, uuid);
	}

	public String getNickname() {
		return this.pair.getFirst();
	}

	public UUID getUuid() {
		return this.pair.getSecond();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof PlayerInfoConfig that)) return false;
		return Objects.equals(this.getUuid(), that.getUuid());
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(this.getUuid());
	}
}
