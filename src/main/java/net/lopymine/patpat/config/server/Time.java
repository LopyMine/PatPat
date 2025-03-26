package net.lopymine.patpat.config.server;

import lombok.*;

import com.mojang.serialization.Codec;

import net.lopymine.patpat.utils.TimeUtils;

import java.util.regex.*;

@Getter
@Setter
@AllArgsConstructor
public class Time {

	public static final Codec<Time> CODEC = Codec.STRING.xmap(Time::of, Time::toString);


	private static final String PATTERN_STRING = "(\\d+)([a-z]*)";
	private static final Pattern PATTERN = Pattern.compile(PATTERN_STRING, Pattern.CASE_INSENSITIVE);

	private RateLimitUnit unit;
	private int value;

	public static Time of(String value) {
		Matcher matcher = PATTERN.matcher(value);
		if (!matcher.find()) {
			throwExampleException(value);
		}
		int time = Integer.parseInt(matcher.group(1));
		RateLimitUnit unit = TimeUtils.getUnit(matcher.group(2).toLowerCase());
		return new Time(unit, time);
	}

	@Override
	public String toString() {
		return "%d%s".formatted(this.value, this.unit.getName());
	}


	private static void throwExampleException(String argument) {
		throw new IllegalArgumentException("String '%s' cannot parsed. Use a string like from the examples: 15, 30s, 20m, 1h, 2d".formatted(argument));
	}
}