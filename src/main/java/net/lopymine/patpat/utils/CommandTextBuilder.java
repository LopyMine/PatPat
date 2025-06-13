package net.lopymine.patpat.utils;

import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.HoverEvent.Action;
import net.minecraft.network.chat.HoverEvent.EntityTooltipInfo;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.EntityType;
import java.io.File;
import java.net.URI;
import java.nio.file.Path;
import java.util.UUID;
import org.jetbrains.annotations.Nullable;

public class CommandTextBuilder {

	private final String key;
	private final MutableComponent text;

	private CommandTextBuilder(String key, Object... args) {
		this.key  = key;
		this.text = TextUtils.text(key, args);
	}

	public static CommandTextBuilder startBuilder(String key, Object... args) {
		return new CommandTextBuilder("command." + key, args);
	}

	public CommandTextBuilder withShowEntity(EntityType<?> type, UUID uuid, String name) {
		return this.withShowEntity(type, uuid, TextUtils.literal(name));
	}

	public CommandTextBuilder withShowEntity(EntityType<?> type, UUID uuid, Component name) {
		HoverEvent hoverEvent = getHoverEvent(Action.SHOW_ENTITY, new EntityTooltipInfo(type, uuid, name));
		return this.withHoverEvent(hoverEvent);
	}

	public CommandTextBuilder withHoverText(Object... args) {
		MutableComponent hoverText = TextUtils.text(this.key + ".hover_text", args);
		HoverEvent hoverEvent = getHoverEvent(Action.SHOW_TEXT, hoverText);
		return this.withHoverEvent(hoverEvent);
	}

	public CommandTextBuilder withHoverEvent(HoverEvent hoverEvent) {
		Style style = this.text.getStyle().withHoverEvent(hoverEvent);
		this.text.setStyle(style);
		return this;
	}

	public CommandTextBuilder withCopyToClipboard(Object value) {
		ClickEvent clickEvent = getClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, value);
		return this.withClickEvent(clickEvent);
	}

	public CommandTextBuilder withClickEvent(ClickEvent clickEvent) {
		Style style = this.text.getStyle().withClickEvent(clickEvent);
		this.text.setStyle(style);
		return this;
	}

	public static <T> HoverEvent getHoverEvent(Action/*? <=1.21.4 {*/<T>/*?}*/ action, T value) {
		//? <=1.21.4 {
		return new HoverEvent(action, value);
		 /*?} else {*/
		/*return switch (action) {
			case SHOW_TEXT -> new ShowText((Text) value);
			case SHOW_ITEM -> new ShowItem((ItemStack) value);
			case SHOW_ENTITY -> new ShowEntity((EntityContent) value);
		};
		*//*?}*/
	}

	public static ClickEvent getClickEvent(ClickEvent.Action action, Object value) {
		//? <=1.21.4 {
		return new ClickEvent(action, String.valueOf(value));
		 /*?} else {*/
		/*return switch (action) {
			case OPEN_URL -> new OpenUrl((URI) value);
			case RUN_COMMAND -> new RunCommand(String.valueOf(value));
			case SUGGEST_COMMAND -> new SuggestCommand(String.valueOf(value));
			case CHANGE_PAGE -> new ChangePage((int) value);
			case COPY_TO_CLIPBOARD -> new CopyToClipboard(String.valueOf(value));
			case OPEN_FILE -> {
				if (value instanceof File file) {
					yield new OpenFile(file);
				}
				if (value instanceof Path path) {
					yield new OpenFile(path);
				}
				yield new OpenFile((String) value);
			}
		};
		*//*?}*/
	}

	public MutableComponent build() {
		return this.text;
	}
}
