package net.lopymine.patpat.config.resourcepack;

import net.minecraft.entity.*;
import net.minecraft.util.Identifier;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.lopymine.patpat.manager.client.PatPatClientResourcePackManager;
import net.lopymine.patpat.utils.IdentifierUtils;

public record AnimationConfig(Identifier texture, int duration, FrameConfig frameConfig, SoundConfig soundConfig) {
	public static final Codec<AnimationConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.STRING.fieldOf("texture").xmap(IdentifierUtils::textureId, Identifier::toString).forGetter(AnimationConfig::texture),
			Codec.INT.fieldOf("duration").forGetter(AnimationConfig::duration),
			FrameConfig.CODEC.fieldOf("frame").forGetter(AnimationConfig::frameConfig),
			SoundConfig.STRINGED_CODEC.fieldOf("sound").forGetter(AnimationConfig::soundConfig)
	).apply(instance, AnimationConfig::new));

	public static final Identifier PATPAT_TEXTURE = IdentifierUtils.textureId("default/patpat.png");

	private static final AnimationConfig DEFAULT_PATPAT_ANIMATION = new AnimationConfig(
			PATPAT_TEXTURE,
			240,
			FrameConfig.DEFAULT_FRAME,
			SoundConfig.PATPAT_SOUND
	);

	private static final AnimationConfig DEFAULT_LOPI_ANIMATION = new AnimationConfig(
			PATPAT_TEXTURE,
			350,
			FrameConfig.DEFAULT_FRAME,
			SoundConfig.LOPI_SOUND
	);

	public static AnimationConfig of(Entity entity, boolean isAuthor) {
		PatPatHandConfig config = PatPatClientResourcePackManager.INSTANCE.getOverrideHandConfig();
		if (config == null) {
			config = PatPatClientResourcePackManager.INSTANCE.getHandConfig(entity);
		}
		if (config != null) {
			return config.getAnimation();
		}
		if (entity.getType().equals(EntityType.GOAT) && entity.getName().getString().equals("Снежа") && isAuthor) {
			return DEFAULT_LOPI_ANIMATION;
		}
		return DEFAULT_PATPAT_ANIMATION;
	}
}
