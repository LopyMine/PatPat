package net.lopymine.patpat.config.server;

import lombok.*;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

@Getter
@Setter
@AllArgsConstructor
public class RateLimitConfig {

	public static final Codec<RateLimitConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.BOOL.fieldOf("enable").forGetter(RateLimitConfig::isEnabled),
			Codec.INT.fieldOf("tokenLimit").forGetter(RateLimitConfig::getTokenLimit),
			Codec.INT.fieldOf("tokenIncrement").forGetter(RateLimitConfig::getTokenIncrement),
			Time.CODEC.fieldOf("tokenIncrementInterval").forGetter(RateLimitConfig::getTokenIncrementInterval),
			Codec.STRING.fieldOf("permissionBypass").forGetter(RateLimitConfig::getPermissionBypass)
	).apply(instance, RateLimitConfig::new));

	private boolean enabled;
	private int tokenLimit;
	private int tokenIncrement;
	private Time tokenIncrementInterval;
	private String permissionBypass;

	public RateLimitConfig() {
		this.enabled                = false;
		this.tokenLimit             = 20;
		this.tokenIncrement         = 1;
		this.tokenIncrementInterval = Time.of("1sec");
		this.permissionBypass       = "patpat.ratelimit.bypass";
	}




}
