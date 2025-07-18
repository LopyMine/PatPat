package net.lopymine.patpat.utils;

import net.lopymine.patpat.common.config.time.TimeUnit;

import java.util.Set;
import java.util.stream.*;

public class TimeUtils {

	private TimeUtils(){
		throw new IllegalStateException("Utility class");
	}

	private static final Set<String> DAY_ENDING = Set.of("days", "day", "d");
	private static final Set<String> HOUR_ENDING = Set.of("hours", "hour", "h");
	private static final Set<String> MINUTE_ENDING = Set.of("minutes", "minute", "min", "m");
	private static final Set<String> SECOND_ENDING = Set.of("seconds", "second", "sec", "s");

	private static final String UNITS = Stream.of(DAY_ENDING, HOUR_ENDING, MINUTE_ENDING, SECOND_ENDING)
			.flatMap(Set::stream)
			.collect(Collectors.joining(", "));


	public static TimeUnit getUnit(String unit) {
		if (unit.isEmpty() || SECOND_ENDING.contains(unit)) {
			return TimeUnit.SECONDS;
		}
		if (MINUTE_ENDING.contains(unit)) {
			return TimeUnit.MINUTES;
		}
		if (HOUR_ENDING.contains(unit)) {
			return TimeUnit.HOURS;
		}
		if (DAY_ENDING.contains(unit)) {
			return TimeUnit.DAYS;
		}
		throw new IllegalArgumentException("'%s' is not unit, available units: %s".formatted(unit, UNITS));
	}
}