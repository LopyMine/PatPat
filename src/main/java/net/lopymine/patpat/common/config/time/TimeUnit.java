package net.lopymine.patpat.common.config.time;

import lombok.*;

@Getter
@RequiredArgsConstructor
public enum TimeUnit {
	SECONDS("sec", 1),
	MINUTES("min", 60 * SECONDS.multiplier),
	HOURS("hour", 60 * MINUTES.multiplier),
	DAYS("day", 24 * HOURS.multiplier);

	private final String name;
	private final int multiplier;
}