package net.lopymine.patpat.client.config.resourcepack;

import com.mojang.serialization.Codec;

import net.lopymine.patpat.utils.TextUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

public enum ListMode {
	WHITELIST(ChatFormatting.DARK_GREEN),
	BLACKLIST(ChatFormatting.DARK_GREEN),
	DISABLED(ChatFormatting.DARK_RED);

	public static final Codec<ListMode> CODEC = Codec.STRING
			.xmap(string -> ListMode.getByIdOrDefault(string, DISABLED), Enum::name);

	private final ChatFormatting formatting;

	ListMode(ChatFormatting formatting) {
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

	public Component getText() {
		return TextUtils.literal(String.format("&%s%s&f", this.formatting./*? >=1.17 {*/getChar()/*?} else {*//*code*//*?}*/, this.name()));
	}
}
