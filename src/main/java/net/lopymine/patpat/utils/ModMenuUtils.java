package net.lopymine.patpat.utils;

public class ModMenuUtils {
	public static String getGroupTitleKey(String group) {
		return "patpat.modmenu." + group;
	}

	public static String getOptionKey(String group, String option) {
		return "patpat.modmenu." + group + ".option." + option;
	}

	public static String getDescriptionKey(String key) {
		return key + ".description";
	}
}