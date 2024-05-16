package net.lopymine.patpat.config;

import com.mojang.serialization.Codec;
import org.jetbrains.annotations.NotNull;

// TODO Переместить в более подходящее место

public record Version(int major, int minor, int patch) {

	public static final Codec<Version> CODEC = Codec.STRING.xmap(Version::of, Version::toString);
	public static final Version DEFAULT = new Version(0, 0, 1);

	public static Version of(@NotNull String version) {
		String[] numbers = version.split("\\.");
		int major = 0;
		int minor = 0;
		int patch = 0;
		try {
			major = Integer.parseInt(numbers[0]);
		} catch (NumberFormatException | ArrayIndexOutOfBoundsException ignored) {
			// An error occurred while parsing major value
		}
		try {
			minor = Integer.parseInt(numbers[1]);
		} catch (NumberFormatException | ArrayIndexOutOfBoundsException ignored) {
			// An error occurred while parsing minor value
		}
		try {
			patch = Integer.parseInt(numbers[2]);
		} catch (NumberFormatException | ArrayIndexOutOfBoundsException ignored) {
			// An error occurred while parsing patch value
		}
		return new Version(major, minor, patch);
	}

	public boolean isLessThan(Version v) {
		if (this.major != v.major) {
			return this.major < v.major;
		}
		if (this.minor != v.minor) {
			return this.minor < v.minor;
		}
		if (this.patch != v.patch) {
			return this.patch < v.patch;
		}
		return false;
	}

	public boolean isMoreThan(Version v) {
		if (this.major != v.major) {
			return this.major > v.major;
		}
		if (this.minor != v.minor) {
			return this.minor > v.minor;
		}
		if (this.patch != v.patch) {
			return this.patch > v.patch;
		}
		return false;
	}

	public boolean isGreaterOrEqualThan(Version version) {
		return !this.isLessThan(version);
	}

	public boolean isLesserOrEqualThan(Version version) {
		return !this.isMoreThan(version);
	}

	@Override
	public String toString() {
		return "%d.%d.%d".formatted(major, minor, patch);
	}
}