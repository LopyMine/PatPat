package net.lopymine.patpat.utils;

import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import net.lopymine.patpat.PatPat;
import net.lopymine.patpat.client.PatPatClient;
import net.lopymine.patpat.modmenu.screen.yacl.custom.utils.SimpleContent;

import java.util.function.Function;

public class ModMenuUtils {

	public static String getOptionKey(String optionId) {
		return String.format("modmenu.option.%s", optionId);
	}

	public static String getCategoryKey(String categoryId) {
		return String.format("modmenu.category.%s", categoryId);
	}

	public static String getGroupKey(String groupId) {
		return String.format("modmenu.group.%s", groupId);
	}

	public static Text getName(String key) {
		return TextUtils.text(key + ".name");
	}

	public static Text getDescription(String key) {
		return TextUtils.text(key + ".description");
	}

	public static Identifier getContentId(SimpleContent content, String contentId) {
		return IdentifierUtils.id(String.format("textures/config/%s.%s", contentId, content.getFileExtension()));
	}

	public static Text getModTitle() {
		return TextUtils.text("modmenu.title");
	}

	public static Function<Boolean, Text> getEnabledOrDisabledFormatter() {
		return state -> TextUtils.text("modmenu.formatter.enabled_or_disabled." + state);
	}
}
