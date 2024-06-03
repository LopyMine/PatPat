package net.lopymine.patpat.utils;

import net.minecraft.entity.EntityType;
import net.minecraft.text.*;
import net.minecraft.text.HoverEvent.*;

import java.util.UUID;

public class CommandTextBuilder {
	private final String key;
	private final MutableText text;

	private CommandTextBuilder(String key, Object... args) {
		this.key = key;
		this.text = CommandTextBuilder.translatable(key, args);
	}

	private static MutableText translatable(String key, Object[] args) {
		return Text.literal(Text.stringifiedTranslatable(key, args).getString().replaceAll("&", "§"));
	}

	public static CommandTextBuilder startBuilder(String key, Object... args) {
		return new CommandTextBuilder(key, args);
	}

	public CommandTextBuilder withShowEntity(EntityType<?> type, UUID uuid, String name) {
		return this.withShowEntity(type, uuid, Text.literal(name));
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
