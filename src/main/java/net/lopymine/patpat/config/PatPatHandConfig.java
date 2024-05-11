package net.lopymine.patpat.config;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import net.lopymine.patpat.entity.PatAnimation;
import net.lopymine.patpat.utils.IdentifierUtils;
import net.lopymine.patpat.utils.SoundUtils;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Getter
@Setter
public final class PatPatHandConfig {

	// TODO
	//  На будущее реализовать настройки размера (scale), пропорции(можно через width и height или ratio), смещение вверх/вниз и влево/вправо

	public static final Codec<PatPatHandConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		Codec.STRING.fieldOf("texture").xmap(IdentifierUtils::textureId, Identifier::toString).forGetter(PatPatHandConfig::getTexture),
		Codec.STRING.fieldOf("sounds").xmap(SoundUtils::getSoundEvent, sound -> sound.getId().toString()).forGetter(PatPatHandConfig::getSoundEvent),
		Codec.INT.fieldOf("duration").forGetter(PatPatHandConfig::getDuration),
		Codec.INT.fieldOf("texture_width").forGetter(PatPatHandConfig::getTextureWidth),
		Codec.INT.fieldOf("frame_size").forGetter(PatPatHandConfig::getFrameSize),
		Codec.BOOL.fieldOf("override").orElse(false).forGetter(PatPatHandConfig::isOverride),
		Codec.either(Codec.STRING.listOf(), Codec.STRING).fieldOf("entities").forGetter(PatPatHandConfig::getEither)
	).apply(instance, PatPatHandConfig::new));


	private final Identifier texture;
	private final SoundEvent soundEvent;
	private final int duration;
	private final int textureWidth;
	private final int frameSize;
	private final boolean override;
	private final Either<List<String>, String> either;
	private List<String> entities;
	@Getter(AccessLevel.NONE)
	private PatAnimation animation;

	public PatPatHandConfig(Identifier texture, SoundEvent soundEvent, int duration, int textureWidth, int frameSize, boolean override, Either<List<String>, String> either) {
		this.texture = texture;
		this.soundEvent = soundEvent;
		this.duration = duration;
		this.textureWidth = textureWidth;
		this.frameSize = frameSize;
		this.override = override;
		this.either = either;
		either.left().ifPresentOrElse(
			left -> this.entities = left,
			() -> this.entities = List.of(either.right().orElseThrow())
		);
	}

	@NotNull
	public PatAnimation getAnimation() {
		if (this.animation == null) {
			this.animation = new PatAnimation(
				this.texture,
				this.textureWidth,
				this.frameSize,
				this.duration,
				this.soundEvent
			);
		}
		return this.animation;
	}

	@Override
	public String toString() {
		return "PatPatHandConfig{" +
			"texture='" + texture + '\'' +
			", sounds='" + soundEvent + '\'' +
			", duration=" + duration +
			", textureWidth=" + textureWidth +
			", frameSize=" + frameSize +
			", override=" + override +
			", entities=" + entities +
			'}';
	}
}
