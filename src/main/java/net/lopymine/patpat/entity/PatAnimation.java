package net.lopymine.patpat.entity;

import lombok.Getter;
import lombok.Setter;
import net.lopymine.patpat.config.PatPatHandConfig;
import net.lopymine.patpat.manager.PatPatResourcePackManager;
import net.lopymine.patpat.utils.IdentifierUtils;
import net.lopymine.patpat.utils.SoundUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

@Getter
public class PatAnimation {
	public static final Identifier PATPAT_TEXTURE = IdentifierUtils.textureId("default/patpat.png");

	private static final PatAnimation DEFAULT_PATPAT_ANIMATION = new PatAnimation(
		PATPAT_TEXTURE,
		560,
		112,
		240,
		SoundUtils.getSoundEvent("patpat")
	);

	private static final PatAnimation DEFAULT_LOPI_ANIMATION = new PatAnimation(
		PATPAT_TEXTURE,
		560,
		112,
		350,
		SoundUtils.getSoundEvent("lopi")
	);

	private final int duration;
	private final Identifier texture;
	private final int textureWidth;
	private final int frameSize;
	private final int totalFrames;
	private final SoundEvent sound;
	private long timeOfStart;

	@Setter
	private int frame;

	public PatAnimation(Identifier texture, int textureWidth, int frameSize, int duration, SoundEvent sound) {
		this.timeOfStart = System.currentTimeMillis();
		this.textureWidth = textureWidth;
		this.totalFrames = MathHelper.floor((float) textureWidth / frameSize);
		this.frameSize = frameSize;
		this.duration = duration;
		this.texture = texture;
		this.sound = sound;
		this.frame = 0;
	}

	public static PatAnimation of(Entity entity, boolean isAuthor) {
		PatPatHandConfig config = PatPatResourcePackManager.INSTANCE.getOverrideHandConfig();
		if (config == null) {
			config = PatPatResourcePackManager.INSTANCE.getHandConfig(entity);
		}
		if (config != null) {
			return config.getAnimation().copy();
		}
		if (entity.getType().equals(EntityType.GOAT) && entity.getName().getString().equals("Снежа") && isAuthor) {
			return DEFAULT_LOPI_ANIMATION.copy();
		}
		return DEFAULT_PATPAT_ANIMATION.copy();
	}

	public void resetAnimation() {
		this.timeOfStart = System.currentTimeMillis();
		this.frame = 0;
	}

	public PatAnimation copy() {
		return new PatAnimation(
			this.texture,
			this.textureWidth,
			this.frameSize,
			this.duration,
			this.sound
		);
	}

}
