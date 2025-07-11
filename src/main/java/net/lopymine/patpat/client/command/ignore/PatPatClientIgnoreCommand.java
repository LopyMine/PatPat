package net.lopymine.patpat.client.command.ignore;

import lombok.experimental.ExtensionMethod;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command./*? if >=1.19 {*/ v2 /*?} else {*/ /*v1 *//*?}*/.FabricClientCommandSource;

import net.lopymine.patpat.client.command.argument.EntityTypeArgumentType;
import net.lopymine.patpat.client.config.IgnoreMobListConfig;
import net.lopymine.patpat.extension.ClientCommandExtension;
import net.lopymine.patpat.utils.CommandText;
import net.lopymine.patpat.utils.VersionedThings;

import java.util.Objects;

import static net.fabricmc.fabric.api.client.command./*? if >=1.19 {*/ v2 /*?} else {*/ /*v1 *//*?}*/.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command./*? if >=1.19 {*/ v2 /*?} else {*/ /*v1 *//*?}*/.ClientCommandManager.literal;

@ExtensionMethod(ClientCommandExtension.class)
public class PatPatClientIgnoreCommand {

	private static final String ENTITY_TYPE_ARGUMENT_NAME = "entity";

	private PatPatClientIgnoreCommand() {
		throw new IllegalStateException("Command class");
	}

	public static LiteralArgumentBuilder<FabricClientCommandSource> get() {
		return literal("ignore")
				.then(getAdd())
				.then(getRemove());
	}

	public static LiteralArgumentBuilder<FabricClientCommandSource> getAdd() {
		return literal("add")
				.then(argument(ENTITY_TYPE_ARGUMENT_NAME, EntityTypeArgumentType.entityType())
						.suggests((context, builder) -> SharedSuggestionProvider
								.suggestResource(VersionedThings.ENTITY_TYPE.stream()
												.filter(
														/*? >1.19.2 {*/entityType -> entityType.isEnabled((context.getSource()).enabledFeatures()) && entityType.canSummon()
														/*?} else {*/
														/*EntityType::canSummon
														*//*?}*/
												),
										builder,
										EntityType::getKey,
										EntityType::getDescription)
						)
						.executes(context -> onIgnoreChange(context, true)));
	}

	public static LiteralArgumentBuilder<FabricClientCommandSource> getRemove() {
		return literal("remove")
				.then(argument(ENTITY_TYPE_ARGUMENT_NAME, EntityTypeArgumentType.entityType())
						.suggests((context, builder) -> SharedSuggestionProvider
								.suggestResource(IgnoreMobListConfig.getInstance().getIgnoredMobs().stream().toList(),
										builder,
										EntityType::getKey,
										EntityType::getDescription
								)
						)
						.executes(context -> onIgnoreChange(context, false)));
	}

	private static int onIgnoreChange(CommandContext<FabricClientCommandSource> context, boolean add) {
		EntityType<?> entityType = EntityTypeArgumentType.getEntityType(ENTITY_TYPE_ARGUMENT_NAME, context);
		IgnoreMobListConfig config = IgnoreMobListConfig.getInstance();
		boolean success = add ? config.addMob(entityType) : config.removeMob(entityType);
		String entityTypeStringed = Objects.requireNonNull(VersionedThings.ENTITY_TYPE.getKey(entityType)).toString();
		Component text;
		if (success) {
			config.saveAsync();
			if (add) {
				text = CommandText.text("ignore.add.success", entityTypeStringed).finish();
			} else {
				text = CommandText.text("ignore.remove.success", entityTypeStringed).finish();
			}
		} else {
			if (add) {
				text = CommandText.text("ignore.add.already", entityTypeStringed).finish();
			} else {
				text = CommandText.text("ignore.remove.already", entityTypeStringed).finish();
			}
		}
		context.sendMsg(text);
		return Command.SINGLE_SUCCESS;
	}

}
