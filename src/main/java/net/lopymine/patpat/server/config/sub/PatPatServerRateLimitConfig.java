package net.lopymine.patpat.server.config.sub;

import lombok.*;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.lopymine.patpat.common.config.time.Time;

@Getter
@Setter
@AllArgsConstructor
public class PatPatServerRateLimitConfig {

	public static final Codec<PatPatServerRateLimitConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.BOOL.fieldOf("enable").forGetter(PatPatServerRateLimitConfig::isEnabled),
			Codec.INT.fieldOf("tokenLimit").forGetter(PatPatServerRateLimitConfig::getTokenLimit),
			Codec.INT.fieldOf("tokenIncrement").forGetter(PatPatServerRateLimitConfig::getTokenIncrement),
			Time.CODEC.fieldOf("tokenIncrementInterval").forGetter(PatPatServerRateLimitConfig::getTokenIncrementInterval),
			Codec.STRING.fieldOf("permissionBypass").forGetter(PatPatServerRateLimitConfig::getPermissionBypass)
	).apply(instance, PatPatServerRateLimitConfig::new));

	private boolean enabled;
	private int tokenLimit;
	private int tokenIncrement;
	private Time tokenIncrementInterval;
	private String permissionBypass;

	public PatPatServerRateLimitConfig() {
		this.enabled                = false;
		this.tokenLimit             = 20;
		this.tokenIncrement         = 1;
		this.tokenIncrementInterval = Time.of("1sec");
		this.permissionBypass       = "patpat.ratelimit.bypass";
	}

}
