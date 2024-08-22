package net.lopymine.patpat.modmenu.screen;

import com.google.common.collect.Sets;
import net.minecraft.SharedConstants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.*;
import net.minecraft.text.Text;
import net.minecraft.util.Util;

import net.lopymine.patpat.client.PatPatClient;
import net.lopymine.patpat.utils.TextUtils;

import java.net.*;
import java.util.*;

public class NoConfigLibrariesScreen {

	private NoConfigLibrariesScreen() {
		throw new IllegalStateException("Screen class, use NoConfigLibrariesScreen.createScreen(...) method!");
	}

	private static final Text TITLE = TextUtils.translatable("patpat.modmenu.title");
	private static final Text MESSAGE = TextUtils.translatable("patpat.modmenu.no_config_libraries_screen.message");
	private static final Text OPEN_YACL_PAGE = TextUtils.translatable("patpat.modmenu.no_config_libraries_screen.open_yacl_page");
	private static final Text OPEN_CLOTH_CONFIG_PAGE = TextUtils.translatable("patpat.modmenu.no_config_libraries_screen.open_cloth_config_page");
	private static final Set<String> ALLOWED_PROTOCOLS = Sets.newHashSet("http", "https");
	private static final String YACL_MODRINTH_LINK = "https://modrinth.com/mod/yacl/versions?l=fabric&g=";
	private static final String CLOTH_CONFIG_API_MODRINTH_LINK = "https://modrinth.com/mod/cloth-config/versions?l=fabric&g=";

	public static Screen createScreen(Screen parent) {
		return new ConfirmScreen(NoConfigLibrariesScreen::onConfirm, NoConfigLibrariesScreen.TITLE, NoConfigLibrariesScreen.MESSAGE, NoConfigLibrariesScreen.OPEN_YACL_PAGE, NoConfigLibrariesScreen.OPEN_CLOTH_CONFIG_PAGE) {
			@Override
			public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
				if (keyCode == 256 && this.shouldCloseOnEsc()) {
					MinecraftClient.getInstance()./*? <=1.17 {*//*openScreen*//*?} else {*/setScreen/*?}*/(parent);
					return true;
				}
				return super.keyPressed(keyCode, scanCode, modifiers);
			}

			@Override
			public boolean shouldCloseOnEsc() {
				return true;
			}
		};
	}

	private static void onConfirm(boolean bl) {
		try {
			String url = (bl ? YACL_MODRINTH_LINK : CLOTH_CONFIG_API_MODRINTH_LINK) + SharedConstants.getGameVersion().getName();

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
			PatPatClient.error("Can't open {} Modrinth page: ", (bl ? "YACL" : "Cloth Config API"), e);
		}
	}
}
