package net.lopymine.patpat.common;

import net.minecraft.network.FriendlyByteBuf;

import com.mojang.serialization.Codec;

import net.lopymine.patpat.PatPat;
import net.lopymine.patpat.client.PatPatClient;

import org.jetbrains.annotations.NotNull;

public record Version(int major, int minor, int patch) {

	public static final Version INVALID = new Version(-1, -1, -1);

	public static final Codec<Version> CODEC = Codec.STRING.xmap(Version::of, Version::toString);

	public static final Version SERVER_CONFIG_VERSION = Version.of(PatPat.SERVER_CONFIG_VERSION);
	public static final Version CLIENT_CONFIG_VERSION = Version.of(PatPatClient.CLIENT_CONFIG_VERSION);
	public static final Version RESOURCE_PACKS_MIN_SUPPORT_VERSION = new Version(1, 0, 0);

	public static final Version CURRENT_MOD_VERSION = Version.of(PatPat.MOD_VERSION.substring(0, PatPat.MOD_VERSION.indexOf('+')));
	public static final Version PACKET_V1_VERSION = new Version(1, 0, 0);
	public static final Version PACKET_V2_VERSION = new Version(1, 2, 0);

	public static Version readVersion(FriendlyByteBuf buf) {
		try {
			int major = buf.readUnsignedByte();
			int minor = buf.readUnsignedByte();
			int patch = buf.readUnsignedByte();
			return new Version(major, minor, patch);
		} catch (Exception e) {
			throw new IllegalArgumentException("Failed to parse version:", e);
		}
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