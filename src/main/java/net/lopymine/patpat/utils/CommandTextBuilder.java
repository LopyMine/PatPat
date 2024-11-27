package net.lopymine.patpat.utils;

import net.minecraft.entity.EntityType;
import net.minecraft.text.*;
import net.minecraft.text.HoverEvent.*;

import java.util.UUID;

public class CommandTextBuilder {

	private final String key;
	private final MutableText text;

	private CommandTextBuilder(String key, Object... args) {
		this.key  = key;
		this.text = CommandTextBuilder.translatable(key, args);
	}

	private static MutableText translatable(String key, Object... args) {
		for (int i = 0; i < args.length; ++i) {
			Object object = args[i];
			if (!isPrimitive(object) && !(object instanceof Text)) {
				args[i] = String.valueOf(object);
			}
		}

		return TextUtils.literal(TextUtils.text(key, args).getString().replace("&", "§"));
	}

	private static boolean isPrimitive(Object object) {
		return object instanceof Number || object instanceof Boolean || object instanceof String;
	}

	public static CommandTextBuilder startBuilder(String key, Object... args) {
		return new CommandTextBuilder("command." + key, args);
	}

	public CommandTextBuilder withShowEntity(EntityType<?> type, UUID uuid, String name) {
		return this.withShowEntity(type, uuid, TextUtils.literal(name));
	}

	public CommandTextBuilder withShowEntity(EntityType<?> type, UUID uuid, Text name) {
		return this.withHoverEvent(Action.SHOW_ENTITY, new EntityContent(type, uuid, name));
	}

	public CommandTextBuilder withHoverText(Object... args) {
		MutableText hoverText = CommandTextBuilder.translatable(this.key + ".hover_text", args);
		return this.withHoverEvent(Action.SHOW_TEXT, hoverText);
	}

	public <T> CommandTextBuilder withHoverEvent(HoverEvent.Action<T> action, T value) {
		Style style = this.text.getStyle().withHoverEvent(new HoverEvent(action, value));
		this.text.setStyle(style);
		return this;
	}

	public CommandTextBuilder withClickEvent(ClickEvent.Action action, Object value) {
		Style style = this.text.getStyle().withClickEvent(new ClickEvent(action, String.valueOf(value)));
		this.text.setStyle(style);
		return this;
	}

	public Text build() {
		return this.text;
	}
}
