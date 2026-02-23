package net.lopymine.patpat.client.command.argument;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;

import net.lopymine.patpat.client.command.PatPatClientCommandManager;
import net.lopymine.patpat.utils.CommandText;
import net.lopymine.patpat.utils.VersionedThings;

import org.jetbrains.annotations.NotNull;

public class EntityTypeArgumentType implements ArgumentType<EntityType<?>> {

	public static final DynamicCommandExceptionType FAILED_PARSING = new DynamicCommandExceptionType(o -> CommandText.text("error.failed_when_parsing", o).finish());
	public static final DynamicCommandExceptionType UNKNOWN_ENTITY_TYPE = new DynamicCommandExceptionType(o -> CommandText.text("error.unknown_entity_type", o).finish());

	private EntityTypeArgumentType() {}

	public static @NotNull EntityTypeArgumentType entityType() {
		return new EntityTypeArgumentType();
	}

	public static <S> EntityType<?> getEntityType(String name, @NotNull CommandContext<S> context) {
		return context.getArgument(name, EntityType.class);
	}

	@Override
	public EntityType<?> parse(@NotNull StringReader reader) throws CommandSyntaxException {
		try {
			ResourceLocation s = ResourceLocation.read(reader);
			PatPatClientCommandManager.LOGGER.debug("Parsed EntityType from EntityTypeArgumentType: {}", s);
			EntityType<?> entityType = VersionedThings.ENTITY_TYPE.getOptional(s).orElse(null);
			if (entityType == null) {
				PatPatClientCommandManager.LOGGER.debug("entityType is null, cannot parse EntityType from EntityTypeArgumentType!");
				throw UNKNOWN_ENTITY_TYPE.createWithContext(reader, reader.getString());
			}
			return entityType;
		} catch (CommandSyntaxException e) {
			throw FAILED_PARSING.createWithContext(reader, reader.getString());
		}
	}
}