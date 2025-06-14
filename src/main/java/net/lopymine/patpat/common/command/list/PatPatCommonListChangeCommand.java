package net.lopymine.patpat.common.command.list;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.*;
import net.minecraft.world.entity.EntityType;

import net.lopymine.patpat.client.config.resourcepack.ListMode;
import net.lopymine.patpat.utils.*;

import java.util.*;
import java.util.function.Consumer;

public class PatPatCommonListChangeCommand {

	public static final Style EMPTY_TEXT_STYLE = Style.EMPTY.withColor(ChatFormatting.GRAY).withItalic(true);

	public static void changeList(boolean add, Map<UUID, String> map, UUID uuid, String nickname, Consumer<Component> sendFeedback) {
		boolean success = add ? !map.containsKey(uuid) && map.put(uuid, nickname) == null : map.containsKey(uuid) && map.remove(uuid) != null;

		String action = add ? "add" : "remove";
		String result = success ? "success" : "already";
		String key = String.format("list.%s.%s", action, result);

		MutableComponent text = CommandText.goldenArgs(key, nickname)
				.withShowEntity(EntityType.PLAYER, uuid, nickname)
				.withCopyToClipboard(uuid)
				.finish();

		sendFeedback.accept(success ? text.withStyle(add ? ChatFormatting.GREEN : ChatFormatting.RED) : text);
	}

	public static void sendInfo(Map<UUID, String> map, ListMode listMode, Consumer<Component> sendFeedback) {
		Collection<String> values = map.values();
		Iterator<String> iterator = values.iterator();

		MutableComponent playersText = TextUtils.literal("");

		if (!iterator.hasNext()) {
			playersText = CommandText.text("list.empty")
					.finish()
					.withStyle(EMPTY_TEXT_STYLE);
		}

		while (iterator.hasNext()) {
			String nickname = iterator.next();
			playersText.append(nickname + (iterator.hasNext() ? ", " : ""));
		}

		Component modeText = CommandText.goldenArgs("list.info.mode", listMode.getText()).finish();
		Component contentText = CommandText.goldenArgs("list.info.content", values.size()).finish();

		sendFeedback.accept(modeText);
		sendFeedback.accept(contentText);
		sendFeedback.accept(playersText);
	}
}
