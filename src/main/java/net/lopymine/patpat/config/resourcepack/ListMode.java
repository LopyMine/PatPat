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

	public static final Codec<ListMode> CODEC = Codec.STRING.xmap((string) -> {
		ListMode listMode = ListMode.getById(string);
		if (listMode == null) {
			listMode = DISABLED;
		}
		return listMode;
	}, Enum::name);

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

	public Text getText() {
		return TextUtils.literal(String.format("&%s%s&f", this.formatting./*? >=1.17 {*/getCode()/*?} else {*//*code*//*?}*/, this.name()));
	}
}
