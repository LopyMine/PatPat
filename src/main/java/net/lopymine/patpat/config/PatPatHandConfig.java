package net.lopymine.patpat.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;

public record PatPatHandConfig(String texture, String sounds, int duration, int textureWidth, int totalFrames, int frameSize, boolean override, List<String> entities) {
    public static final Codec<PatPatHandConfig> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            Codec.STRING.fieldOf("texture").forGetter(PatPatHandConfig::texture),
            Codec.STRING.fieldOf("sounds").forGetter(PatPatHandConfig::sounds),
            Codec.INT.fieldOf("duration").forGetter(PatPatHandConfig::duration),
            Codec.INT.fieldOf("texture_width").forGetter(PatPatHandConfig::textureWidth),
            Codec.INT.fieldOf("total_frames").forGetter(PatPatHandConfig::totalFrames),
            Codec.INT.fieldOf("frame_size").forGetter(PatPatHandConfig::frameSize),
            Codec.BOOL.fieldOf("override").forGetter(PatPatHandConfig::override),
            Codec.STRING.listOf().fieldOf("entities").forGetter(PatPatHandConfig::entities)
    ).apply(instance, PatPatHandConfig::new));

    @Override
    public String toString() {
        return "PatPatHandConfig{" +
                "texture='" + texture + '\'' +
                ", sounds='" + sounds + '\'' +
                ", duration=" + duration +
                ", textureWidth=" + textureWidth +
                ", totalFrames=" + totalFrames +
                ", frameSize=" + frameSize +
                ", override=" + override +
                ", entities=" + entities +
                '}';
    }
}
