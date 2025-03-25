package net.lopymine.patpat.config.resourcepack;

import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import com.mojang.serialization.Codec;

import net.lopymine.patpat.utils.TextUtils;

import org.jetbrains.annotations.Nullable;

public enum ListMode {
	WHITELIST(Formatting.DARK_GREEN),
	BLACKLIST(Formatting.DARK_GREEN),
	DISABLED(Formatting.DARK_RED);

	public static final Codec<ListMode> CODEC = Codec.STRING
			.xmap(string -> ListMode.getByIdOrDefault(string, DISABLED), Enum::name);

	private final Formatting formatting;

	ListMode(Formatting formatting) {
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

	@Nullable
	public static ListMode getByIdOrDefault(String modeId, ListMode listMode) {
		@Nullable ListMode value = getById(modeId);
		return value == null ? listMode : value;
	}

	public Text getText() {
		return TextUtils.literal(String.format("&%s%s&f", this.formatting./*? >=1.17 {*/getCode()/*?} else {*//*code*//*?}*/, this.name()));
	}
}
