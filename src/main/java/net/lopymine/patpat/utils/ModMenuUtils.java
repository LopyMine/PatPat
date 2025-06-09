package net.lopymine.patpat.utils;

import net.lopymine.patpat.modmenu.yacl.custom.utils.SimpleContent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
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

	public static Component getName(String key) {
		return TextUtils.text(key + ".name");
	}

	public static Component getDescription(String key) {
		return TextUtils.text(key + ".description");
	}

	public static ResourceLocation getContentId(SimpleContent content, String contentId) {
		return IdentifierUtils.id(String.format("textures/config/%s.%s", contentId, content.getFileExtension()));
	}

	public static Component getModTitle() {
		return TextUtils.text("modmenu.title");
	}

	public static Function<Boolean, Component> getEnabledOrDisabledFormatter() {
		return state -> TextUtils.text("modmenu.formatter.enabled_or_disabled." + state);
	}
}
