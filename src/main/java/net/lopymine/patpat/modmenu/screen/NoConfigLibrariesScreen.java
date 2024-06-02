package net.lopymine.patpat.modmenu.screen;

import com.google.common.collect.Sets;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.*;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Util;

import net.lopymine.patpat.client.PatPatClient;

import java.net.*;
import java.util.*;

public class NoConfigLibrariesScreen {

	private NoConfigLibrariesScreen() {
		throw new IllegalStateException("Screen class");
	}

	private static final Text TITLE = Text.translatable("patpat.modmenu.title");
	private static final Text MESSAGE = Text.translatable("patpat.modmenu.no_yacl_screen.message");
	private static final Set<String> ALLOWED_PROTOCOLS = Sets.newHashSet("http", "https");
	private static final String YACL_MODRINTH_LINK = "https://modrinth.com/mod/yacl/versions";

	public static Screen createScreen(Screen parent) {
		return new ConfirmScreen(open -> NoConfigLibrariesScreen.onConfirm(open, parent), NoConfigLibrariesScreen.TITLE, NoConfigLibrariesScreen.MESSAGE, ScreenTexts.CONTINUE, ScreenTexts.BACK);
	}

	private static void onConfirm(boolean open, Screen parent) {
		if (!open) {
			MinecraftClient.getInstance().setScreen(parent);
			return;
		}

		try {
			String url = NoConfigLibrariesScreen.YACL_MODRINTH_LINK;
			URI link = new URI(url);
			String string = link.getScheme();
			if (string == null) {
				throw new URISyntaxException(url, "Missing protocol");
			}
			if (!NoConfigLibrariesScreen.ALLOWED_PROTOCOLS.contains(string.toLowerCase(Locale.ROOT))) {
				throw new URISyntaxException(url, "Unsupported protocol: " + string.toLowerCase(Locale.ROOT));
			}
			Util.getOperatingSystem().open(link);
		} catch (URISyntaxException e) {
			PatPatClient.LOGGER.error("Can't open YACL Modrinth page:", e);
		}
	}
}
