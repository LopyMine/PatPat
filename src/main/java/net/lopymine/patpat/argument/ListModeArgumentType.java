package net.lopymine.patpat.argument;

import net.minecraft.command.argument.EnumArgumentType;

import com.mojang.brigadier.context.CommandContext;

import net.lopymine.patpat.config.resourcepack.ListMode;

public class ListModeArgumentType extends EnumArgumentType<ListMode> {
	private ListModeArgumentType() {
		super(ListMode.CODEC, ListMode::values);
	}

	public static EnumArgumentType<ListMode> listMode() {
		return new ListModeArgumentType();
	}

	public static <S> ListMode getListMode(CommandContext<S> context, String id) {
		return context.getArgument(id, ListMode.class);
	}
}
