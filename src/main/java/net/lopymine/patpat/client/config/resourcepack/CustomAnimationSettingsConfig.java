package net.lopymine.patpat.client.config.resourcepack;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.io.*;
import java.util.*;
import lombok.Getter;
import net.lopymine.patpat.client.PatPatClient;
import net.lopymine.patpat.client.resourcepack.PatPatClientResourcePackManager;
import net.lopymine.patpat.utils.RLUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

@Getter
public final class CustomAnimationSettingsConfig {

	public static final Codec<CustomAnimationSettingsConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.STRING.fieldOf("texture").xmap(RLUtils::vanillaId, Identifier::toString).forGetter(CustomAnimationSettingsConfig::getTexture),
			Codec.INT.fieldOf("duration").forGetter(CustomAnimationSettingsConfig::getDuration),
			FrameConfig.CODEC.fieldOf("frame").forGetter(CustomAnimationSettingsConfig::getFrameConfig),
			SoundConfig.STRINGED_CODEC.optionalFieldOf("sound").forGetter(CustomAnimationSettingsConfig::getOptionalSoundConfig)
	).apply(instance, CustomAnimationSettingsConfig::new));

	public static final CustomAnimationSettingsConfig DEFAULT_PATPAT_ANIMATION = new CustomAnimationSettingsConfig(
			RLUtils.modId("textures/default/patpat.png"),
			240,
			FrameConfig.DEFAULT_FRAME,
			Optional.of(SoundConfig.PAT_PAT_SOUND)
	);

	private final Identifier texture;
	private final int duration;
	private final FrameConfig frameConfig;
	@Nullable
	private final SoundConfig soundConfig;
	private int textureWidth;
	private int textureHeight;

	public CustomAnimationSettingsConfig(Identifier texture, int duration, FrameConfig frameConfig, Optional<SoundConfig> soundConfig) {
		this.texture     = texture;
		this.duration    = duration;
		this.frameConfig = frameConfig;
		this.soundConfig = soundConfig.orElse(null);
		this.loadSize();
	}

	public static CustomAnimationSettingsConfig of(LivingEntity entity, PlayerConfig whoPatted) {
		PatPatClientResourcePackManager manager = PatPatClientResourcePackManager.INSTANCE;
		CustomAnimationConfig config = manager.getAnimationConfig(entity, whoPatted);
		if (config != null) {
			return config.getAnimation();
		}
		return DEFAULT_PATPAT_ANIMATION;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (obj == null || obj.getClass() != this.getClass()) return false;
		CustomAnimationSettingsConfig that = (CustomAnimationSettingsConfig) obj;
		return Objects.equals(this.texture, that.texture) &&
				this.duration == that.duration &&
				Objects.equals(this.frameConfig, that.frameConfig) &&
				Objects.equals(this.soundConfig, that.soundConfig);
	}

	@Override
	public int hashCode() {
		return Objects.hash(texture, duration, frameConfig, soundConfig);
	}

	@Override
	public String toString() {
		return "CustomAnimationSettingsConfig[" +
				"texture=" + this.texture + ", " +
				"duration=" + this.duration + ", " +
				"frameConfig=" + this.frameConfig + ", " +
				"soundConfig=" + this.soundConfig + ']';
	}

	private void loadSize() {
		try {
			Optional<Resource> resource = Minecraft.getInstance().getResourceManager().getResource(this.texture);
			if (resource.isEmpty()) {
				PatPatClient.LOGGER.error("Failed to find texture at '{}'", this.texture);
				return;
			}
			InputStream inputStream = resource.get().open();
			NativeImage nativeImage = NativeImage.read(inputStream);
			this.textureWidth  = nativeImage.getWidth();
			this.textureHeight = nativeImage.getHeight();
		} catch (IOException e) {
			PatPatClient.LOGGER.error("Failed to load texture size from custom animation: ", e);
		}
	}

	private Optional<SoundConfig> getOptionalSoundConfig() {
		return Optional.ofNullable(this.soundConfig);
	}
}
