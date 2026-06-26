package net.lopymine.patpat.client.config.resourcepack;

import com.mojang.authlib.GameProfile;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.*;
import lombok.experimental.ExtensionMethod;
import net.lopymine.patpat.client.PatPatClient;
import net.lopymine.patpat.extension.*;
import net.lopymine.patpat.utils.VersionedThings;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.Nullable;

@ExtensionMethod(value = {EntityExtension.class, GameProfileExtension.class})
public record PlayerConfig(@Nullable String name, @Nullable UUID uuid) {

	public static final Codec<PlayerConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.STRING.optionalFieldOf("name").forGetter(PlayerConfig::getOptionalName),
			VersionedThings.UUID_CODEC.optionalFieldOf("uuid").forGetter(PlayerConfig::getOptionalUuid)
	).apply(instance, PlayerConfig::new));

	public PlayerConfig(Optional<String> name, Optional<UUID> uuid) {
		this(name.orElse(null), uuid.orElse(null));
	}

	public static PlayerConfig currentSession() {
		Minecraft minecraft = Minecraft.getInstance();
		GameProfile profile = minecraft.getGameProfile();
		return PlayerConfig.of(profile.getName(), profile.getUUID());
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
		boolean b = that.name == null || Objects.equals(this.name, that.name);
		boolean b1 = that.uuid == null || Objects.equals(this.uuid, that.uuid);
		PatPatClient.LOGGER.debug("Comparing PlayerConfigs: [{} and {}] [{} and {}] - {}", this.name, that.name, this.uuid, that.uuid, b && b1);
		return b && b1;
	}

	@Override
	public String toString() {
		return "PlayerConfig{" +
				"name='" + this.name + '\'' +
				", uuid=" + this.uuid +
				'}';
	}

}
