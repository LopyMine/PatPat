package net.lopymine.patpat.config.resourcepack;

import lombok.Getter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.entity.LivingEntity;
import net.minecraft.resource.Resource;
import net.minecraft.util.Identifier;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.lopymine.patpat.manager.client.*;
import net.lopymine.patpat.utils.IdentifierUtils;

import java.io.*;
import java.util.*;

@Getter
public final class CustomAnimationSettingsConfig {
	public static final Codec<CustomAnimationSettingsConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.STRING.fieldOf("texture").xmap(IdentifierUtils::textureId, Identifier::toString).forGetter(CustomAnimationSettingsConfig::getTexture),
			Codec.INT.fieldOf("duration").forGetter(CustomAnimationSettingsConfig::getDuration),
			FrameConfig.CODEC.fieldOf("frame").forGetter(CustomAnimationSettingsConfig::getFrameConfig),
			SoundConfig.STRINGED_CODEC.fieldOf("sound").forGetter(CustomAnimationSettingsConfig::getSoundConfig)
	).apply(instance, CustomAnimationSettingsConfig::new));

	public static final CustomAnimationSettingsConfig DEFAULT_PATPAT_ANIMATION = new CustomAnimationSettingsConfig(
			IdentifierUtils.textureId("default/patpat.png"),
			240,
			FrameConfig.DEFAULT_FRAME,
			SoundConfig.PATPAT_SOUND
	);

	public static final CustomAnimationSettingsConfig DONOR_ANIMATION = new CustomAnimationSettingsConfig(
			IdentifierUtils.textureId("donors/golden_patpat.png"),
			240,
			FrameConfig.DEFAULT_FRAME,
			SoundConfig.PATPAT_SOUND
	);

	private final Identifier texture;
	private final int duration;
	private final FrameConfig frameConfig;
	private final SoundConfig soundConfig;
	private int textureWidth;
	private int textureHeight;

	public CustomAnimationSettingsConfig(Identifier texture, int duration, FrameConfig frameConfig, SoundConfig soundConfig) {
		this.texture = texture;
		this.duration = duration;
		this.frameConfig = frameConfig;
		this.soundConfig = soundConfig;
		this.getSize();
	}

	public static CustomAnimationSettingsConfig of(LivingEntity entity, PlayerConfig whoPatted, boolean donor) {
		if (donor && PatPatClientDonorManager.getInstance().getDonors().contains(whoPatted.getUuid())) {
			return DONOR_ANIMATION;
		}
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
				"texture=" + texture + ", " +
				"duration=" + duration + ", " +
				"frameConfig=" + frameConfig + ", " +
				"soundConfig=" + soundConfig + ']';
	}

	private void getSize() {
		Optional<Resource> resource = MinecraftClient.getInstance().getResourceManager().getResource(this.texture);
		if (resource.isEmpty()) {
			System.out.printf("Texture '%s' is not found from resource%n", this.texture);
			return;
		}
		try (InputStream inputStream = resource.get().getInputStream()) {
			NativeImage nativeImage = NativeImage.read(inputStream);
			this.textureWidth = nativeImage.getWidth();
			this.textureHeight = nativeImage.getHeight();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
