package net.lopymine.patpat.utils;

import net.lopymine.patpat.PatTranslation;
import net.lopymine.patpat.modmenu.yacl.custom.utils.SimpleContent;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.*;
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

	public static MutableComponent getName(String key) {
		return PatTranslation.text(key + ".name");
	}

	public static MutableComponent getDescription(String key) {
		return PatTranslation.text(key + ".description");
	}

	public static MutableComponent getOptionName(String optionId) {
		return getName(getOptionKey(optionId));
	}

	public static MutableComponent getOptionDescription(String optionId) {
		return getDescription(getOptionKey(optionId));
	}

	public static MutableComponent getModTitle() {
		return PatTranslation.text("modmenu.title");
	}

	public static ResourceLocation getContentId(SimpleContent content, String contentId) {
		return IdentifierUtils.modId(String.format("textures/config/%s.%s", contentId, content.getFileExtension()));
	}

	public static Function<Boolean, Component> getEnabledOrDisabledFormatter() {
		return state -> PatTranslation.text("formatter.enabled_or_disabled." + Boolean.TRUE.equals(state));
	}

	public static Function<Boolean, Component> getEnabledOrDisabledFormatterColored() {
		return state -> PatTranslation.text("formatter.enabled_or_disabled." + Boolean.TRUE.equals(state)).withStyle(Boolean.TRUE.equals(state) ? ChatFormatting.GREEN : ChatFormatting.RED);
	}
}
