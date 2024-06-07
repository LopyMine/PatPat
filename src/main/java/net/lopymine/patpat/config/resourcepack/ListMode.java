package net.lopymine.patpat.config.resourcepack;

import net.minecraft.text.Text;
import net.minecraft.util.*;

import com.mojang.serialization.Codec;

import org.jetbrains.annotations.Nullable;

public enum ListMode implements StringIdentifiable {
	WHITELIST("WHITELIST", Formatting.DARK_GREEN),
	BLACKLIST("BLACKLIST", Formatting.DARK_GREEN),
	DISABLED("DISABLED", Formatting.DARK_RED);

	public static final Codec<ListMode> CODEC = StringIdentifiable.createCodec(ListMode::values);
	private final String id;
	private final Formatting formatting;

	ListMode(String id, Formatting formatting) {
		this.id = id;
		this.formatting = formatting;
	}

	@Nullable
	public static ListMode getById(String modeId) {
		try {
			return ListMode.valueOf(modeId);
		} catch (IllegalArgumentException ignored) {
			return null;
		}
	}

	public Text getText() {
		return Text.literal(String.format("&%s%s&f", this.formatting.getCode(), this.asString()));
	}

	@Override
	public String asString() {
		return this.id;
	}
}
