package net.lopymine.patpat.entity;

import net.minecraft.entity.*;
import net.minecraft.util.*;

import net.lopymine.patpat.PatPat;
import net.lopymine.patpat.config.PatPatHandConfig;
import net.lopymine.patpat.manager.PatPatResourcePackManager;

public class PatAnimation {
    public static final Identifier PATPAT_TEXTURE = PatPat.i("textures/default/patpat.png");

    private final long timeOfStart;
    private final int duration;
    private final Identifier texture;
    private final int textureWidth;
    private final int frameSize;
    private final int totalFrames;
    private final String sounds;
    private int frame;

    public PatAnimation(Identifier texture, int textureWidth, int totalFrames, int frameSize, int duration, String sounds) {
        this.timeOfStart = Util.getMeasuringTimeMs();
        this.textureWidth = textureWidth;
        this.totalFrames = totalFrames;
        this.frameSize = frameSize;
        this.duration = duration;
        this.texture = texture;
        this.sounds = sounds;
        this.frame = 0;
    }

    public static PatAnimation of(Entity entity, boolean bl) {
        PatPatHandConfig config = PatPatResourcePackManager.INSTANCE.getOverrideHandConfig();
        if (config == null) {
            config = PatPatResourcePackManager.INSTANCE.getFor(entity);
        }
        if (config != null) {
            return new PatAnimation(PatPat.i("textures/" + config.texture()), config.textureWidth(), config.totalFrames(), config.frameSize(), config.duration(), config.sounds());
        }
        if (entity.getType().equals(EntityType.GOAT) && entity.getName().getString().equals("Снежа") && bl) {
            return new PatAnimation(PATPAT_TEXTURE, 560, 5, 112, 350, "lopi");
        }
        return new PatAnimation(PATPAT_TEXTURE, 560, 5, 112, 240, "patpat");
    }

    public long getTimeOfStart() {
        return this.timeOfStart;
    }

    public int getDuration() {
        return this.duration;
    }

    public Identifier getTexture() {
        return this.texture;
    }

    public int getTextureWidth() {
        return this.textureWidth;
    }

    public int getFrameSize() {
        return this.frameSize;
    }

    public String getSounds() {
        return this.sounds;
    }

    public int getFrame() {
        return this.frame;
    }

    public int getTotalFrames() {
        return this.totalFrames;
    }

    public void setFrame(int frame) {
        this.frame = frame;
    }
}
