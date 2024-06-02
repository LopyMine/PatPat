package net.lopymine.patpat.config.resourcepack;

import net.minecraft.util.StringIdentifiable;

import com.mojang.serialization.Codec;

import org.jetbrains.annotations.Nullable;

public enum ListMode implements StringIdentifiable {
	WHITELIST("whitelist"),
	BLACKLIST("blacklist"),
	DISABLED("disabled");

	public static final Codec<ListMode> CODEC = StringIdentifiable.createCodec(ListMode::values);
	private final String id;

	ListMode(String id) {
		this.id = id;
	}

	@Nullable
	public static ListMode getById(String modeId) {
		try {
			return ListMode.valueOf(modeId);
		} catch (IllegalArgumentException ignored) {
			return null;
		}
	}

	@Override
	public String asString() {
		return this.id;
	}

	@Override
	public String toString() {
		return this.asString();
	}
}
