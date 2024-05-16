package net.lopymine.patpat.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.Getter;
import net.lopymine.patpat.utils.IdentifierUtils;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import static net.lopymine.patpat.config.SoundConfig.SOUND_FIELD;

@Getter
public class AnimationConfig {

	public static final Codec<AnimationConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		Codec.STRING.fieldOf("texture").xmap(IdentifierUtils::textureId, Identifier::toString).forGetter(AnimationConfig::getTexture),
		Codec.INT.fieldOf("texture_width").forGetter(AnimationConfig::getTextureWidth),
		Codec.INT.fieldOf("frame_size").forGetter(AnimationConfig::getFrameSize),
		Codec.INT.fieldOf("duration").forGetter(AnimationConfig::getDuration),
		FrameConfig.CODEC.optionalFieldOf("frame_setting", FrameConfig.DEFAULT_FRAME).forGetter(AnimationConfig::getFrameConfig),
		SOUND_FIELD.fieldOf("sound").forGetter(AnimationConfig::getSoundConfig)
	).apply(instance, AnimationConfig::new));

	private final Identifier texture;
	private final int textureWidth;
	private final int frameSize;
	private final int totalFrames;
	private final int duration;

	private final FrameConfig frameConfig;
	private final SoundConfig soundConfig;


	public AnimationConfig(Identifier texture, int textureWidth, int frameSize, int duration, FrameConfig frame, SoundConfig soundConfig) {
		this.texture = texture;
		this.textureWidth = textureWidth;
		this.frameSize = frameSize;
		this.totalFrames = textureWidth / frameSize;
		this.duration = duration;
		this.frameConfig = frame;
		this.soundConfig = soundConfig;
	}

	public AnimationConfig(Identifier texture, int textureWidth, int frameSize, int duration, SoundEvent sound) {
		this.texture = texture;
		this.textureWidth = textureWidth;
		this.frameSize = frameSize;
		this.totalFrames = textureWidth / frameSize;
		this.duration = duration;
		this.frameConfig = FrameConfig.DEFAULT_FRAME;
		this.soundConfig = new SoundConfig(sound);
	}


}
