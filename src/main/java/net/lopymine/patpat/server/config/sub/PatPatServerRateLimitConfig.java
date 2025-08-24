package net.lopymine.patpat.server.config.sub;

import java.util.function.Supplier;
import lombok.*;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.lopymine.patpat.common.config.time.Time;
import net.lopymine.patpat.server.config.PatPatServerConfig;
import net.lopymine.patpat.utils.CodecUtils;
import static net.lopymine.patpat.utils.CodecUtils.option;

@Getter
@Setter
@AllArgsConstructor
public class PatPatServerRateLimitConfig {

	public static final String STANDARD_PERMISSION = "patpat.ratelimit.bypass";

	public static final Codec<PatPatServerRateLimitConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			option("enable", false, Codec.BOOL, PatPatServerRateLimitConfig::isEnabled),
			option("tokenLimit", 20, Codec.INT, PatPatServerRateLimitConfig::getTokenLimit),
			option("tokenIncrement", 1, Codec.INT, PatPatServerRateLimitConfig::getTokenIncrement),
			option("tokenIncrementInterval", Time.of("1sec"), Time.CODEC, PatPatServerRateLimitConfig::getTokenIncrementInterval),
			option("permissionBypass", STANDARD_PERMISSION, Codec.STRING, PatPatServerRateLimitConfig::getPermissionBypass)
	).apply(instance, PatPatServerRateLimitConfig::new));

	private boolean enabled;
	private int tokenLimit;
	private int tokenIncrement;
	private Time tokenIncrementInterval;
	private String permissionBypass;

	public static Supplier<PatPatServerRateLimitConfig> getNewInstance() {
		return () -> CodecUtils.parseNewInstanceHacky(CODEC);
	}

}
