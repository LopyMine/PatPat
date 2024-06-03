package net.lopymine.patpat.utils;

import net.minecraft.util.Identifier;

import net.lopymine.patpat.modmenu.screen.yacl.simple.SimpleContent;

public class ModMenuUtils {

	private ModMenuUtils() {
		throw new IllegalStateException("Utility class");
	}

	public static String getGroupTitleKey(String groupId) {
		return "patpat.modmenu." + groupId;
	}

	public static String getOptionKey(String groupId, String optionId) {
		return "patpat.modmenu." + groupId + ".option." + optionId;
	}

	public static String getDescriptionKey(String key) {
		return key + ".description";
	}

	public static Identifier getContentId(SimpleContent content, String optionId) {
		return IdentifierUtils.textureId(String.format("config/%s/%s.%s", content.getFolder(), optionId, content.getFileExtension()));
	}
}
