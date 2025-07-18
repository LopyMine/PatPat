package net.lopymine.patpat.client.config.resourcepack;

import com.mojang.serialization.Codec;

import net.lopymine.patpat.utils.TextUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.*;

import org.jetbrains.annotations.Nullable;

public enum ListMode {
	WHITELIST(ChatFormatting.GREEN),
	BLACKLIST(ChatFormatting.GREEN),
	DISABLED(ChatFormatting.RED);

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
		ListMode value = getById(modeId);
		return value == null ? listMode : value;
	}

	public MutableComponent getText() {
		return TextUtils.literal(this.name()).withStyle(this.formatting);
	}
}
