package net.lopymine.patpat.argument;

import net.minecraft.command.CommandSource;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.*;
import com.mojang.brigadier.suggestion.*;

import net.lopymine.patpat.client.PatPatClient;
import net.lopymine.patpat.config.resourcepack.ListMode;
import net.lopymine.patpat.utils.CommandTextBuilder;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public class ListModeArgumentType implements ArgumentType<ListMode> {

	public static final DynamicCommandExceptionType FAILED_PARSING = new DynamicCommandExceptionType(o -> CommandTextBuilder.startBuilder("argument.list_mode.exception.failed_parsing", o).build());

	private ListModeArgumentType() {
	}

	public static ListModeArgumentType listMode() {
		return new ListModeArgumentType();
	}

	public static <S> ListMode getListMode(CommandContext<S> context, String id) {
		return context.getArgument(id, ListMode.class);
	}

	@Override
	public ListMode parse(StringReader reader) throws CommandSyntaxException {
		String modeId = reader.readUnquotedString();
		if (PatPatClient.getConfig().isDebugLogEnabled()) {
			PatPatClient.info("Parted modeId from ListModeArgumentType: {}", modeId);
		}
		ListMode listMode = ListMode.getById(modeId);
		if (listMode == null) {
			throw FAILED_PARSING.createWithContext(reader, reader.getString());
		}
		return listMode;
	}

	@Override
	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
		return CommandSource.suggestMatching(Arrays.stream(ListMode.values()).flatMap((listMode) -> Stream.of(listMode.name())).toList(), builder);
	}

	@Override
	public Collection<String> getExamples() {
		return Arrays.stream(ListMode.values()).flatMap((listMode) -> Stream.of(listMode.name())).toList();
	}
}
