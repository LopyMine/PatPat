package net.lopymine.patpat.config.resourcepack;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.lopymine.patpat.manager.client.*;
import net.lopymine.patpat.utils.IdentifierUtils;

public record CustomAnimationSettingsConfig(Identifier texture, int duration, FrameConfig frameConfig, SoundConfig soundConfig) {
	public static final Codec<CustomAnimationSettingsConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.STRING.fieldOf("texture").xmap(IdentifierUtils::textureId, Identifier::toString).forGetter(CustomAnimationSettingsConfig::texture),
			Codec.INT.fieldOf("duration").forGetter(CustomAnimationSettingsConfig::duration),
			FrameConfig.CODEC.fieldOf("frame").forGetter(CustomAnimationSettingsConfig::frameConfig),
			SoundConfig.STRINGED_CODEC.fieldOf("sound").forGetter(CustomAnimationSettingsConfig::soundConfig)
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
}
