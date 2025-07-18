package net.lopymine.patpat.utils;

import net.minecraft.locale.Language;
import net.minecraft.network.chat.*;

import net.lopymine.patpat.PatPat;

import java.util.*;
import java.util.regex.*;

public class TextUtils {

	public static final Pattern ARGUMENT_PATTERN = Pattern.compile("\\{\\d+}");

	private TextUtils() {
		throw new IllegalStateException("Utility class");
	}

	public static MutableComponent translatable(String key, Object... args) {
		return processWithArgs(Language.getInstance().getOrDefault(key), args);
	}

	public static MutableComponent literal(Object text) {
		//? >=1.19 {
		return Component.literal(String.valueOf(text));
		//?} else {
		/*return new TextComponent(String.valueOf(text));
		*///?}
	}

	public static Component of(String key) {
		return Component.nullToEmpty(key);
	}

	public static MutableComponent processWithArgs(String text, Object... args) {
		LinkedList<String> parts = new LinkedList<>();

		Matcher matcher = ARGUMENT_PATTERN.matcher(text);

		int lastEnd = 0;
		while (matcher.find()) {
			parts.add(text.substring(lastEnd, matcher.start()));
			parts.add(matcher.group());
			lastEnd = matcher.end();
		}
		parts.add(text.substring(lastEnd));

		String[] array = parts.toArray(new String[0]);

		MutableComponent result = literal("");

		for (String part : array) {
			boolean argument = false;
			for (int i = 0; i < args.length; i++) {
				Object arg = args[i];
				if (part.equals("{" + i + "}")) {
					result.append(getArgument(arg));
					argument = true;
					break;
				}
			}
			if (!argument) {
				result.append(part);
			}
		}

		return result;
	}

	private static Component getArgument(Object arg) {
		if (arg instanceof Component component) {
			return component;
		}
		return TextUtils.literal(String.valueOf(arg));
	}

}
