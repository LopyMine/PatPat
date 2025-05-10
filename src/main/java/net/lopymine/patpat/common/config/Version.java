package net.lopymine.patpat.common.config;

import com.mojang.serialization.Codec;

import net.lopymine.patpat.PatPat;
import net.lopymine.patpat.client.PatPatClient;

import org.jetbrains.annotations.NotNull;

public record Version(int major, int minor, int patch) {

	public static final Version INVALID = new Version(-1, -1, -1);

	public static final Codec<Version> CODEC = Codec.STRING.xmap(Version::of, Version::toString);

	public static final Version MOD_VERSION = Version.of(PatPat.MOD_VERSION.substring(0, PatPat.MOD_VERSION.indexOf('+')));

	public static final Version RESOURCE_PACKS_MIN_SUPPORT_VERSION = Version.of("1.0.0");

	public static final Version PACKET_V1_VERSION = Version.of("1.0.0");
	public static final Version PACKET_V2_VERSION = Version.of("1.2.0");

	public static final Version SERVER_CONFIG_VERSION = Version.of(PatPat.SERVER_CONFIG_VERSION);
	public static final Version CLIENT_CONFIG_VERSION = Version.of(PatPatClient.CLIENT_CONFIG_VERSION);

	public static Version of(int major, int minor, int patch) {
		if (major < 0) {
			throw new IllegalArgumentException("Version major cannot be less than zero");
		}
		if (minor < 0) {
			throw new IllegalArgumentException("Version minor cannot be less than zero");
		}
		if (patch < 0) {
			throw new IllegalArgumentException("Version patch cannot be less than zero");
		}
		return new Version(major, minor, patch);
	}

	public static Version of(@NotNull String version) {
		String[] numbers = version.split("\\.");
		int major;
		int minor;
		int patch;
		try {
			major = Integer.parseInt(numbers[0]);
			minor = Integer.parseInt(numbers[1]);
			patch = Integer.parseInt(numbers[2]);
		} catch (Exception ignored) {
			return INVALID;
		}
		return new Version(major, minor, patch);
	}

	public boolean isLessThan(Version version) {
		if (this.major != version.major) {
			return this.major < version.major;
		}
		if (this.minor != version.minor) {
			return this.minor < version.minor;
		}
		if (this.patch != version.patch) {
			return this.patch < version.patch;
		}
		return false;
	}

	public boolean isMoreThan(Version version) {
		if (this.major != version.major) {
			return this.major > version.major;
		}
		if (this.minor != version.minor) {
			return this.minor > version.minor;
		}
		if (this.patch != version.patch) {
			return this.patch > version.patch;
		}
		return false;
	}

	public boolean isGreaterOrEqualThan(Version version) {
		return !this.isLessThan(version);
	}

	public boolean isLesserOrEqualThan(Version version) {
		return !this.isMoreThan(version);
	}

	public boolean is(Version version) {
		return this.major == version.major &&
				this.minor == version.minor &&
				this.patch == version.patch;
	}

	@Override
	public String toString() {
		return "%d.%d.%d".formatted(this.major, this.minor, this.patch);
	}

	public boolean isInvalid() {
		return this == INVALID;
	}
}