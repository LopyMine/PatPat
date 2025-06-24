package net.lopymine.patpat.modmenu;

import net.minecraft.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.*;

import net.lopymine.patpat.client.PatPatClient;
import net.lopymine.patpat.utils.*;

import java.net.*;
import java.util.*;

public class NoConfigLibrariesScreen {

	private NoConfigLibrariesScreen() {
		throw new IllegalStateException("Screen class, use NoConfigLibrariesScreen.createScreen(...) method!");
	}

	private static final Component TITLE;
	private static final Component MESSAGE;
	private static final Component OPEN_YACL_PAGE;
	private static final Component OPEN_CLOTH_CONFIG_PAGE;

	private static final Set<String> ALLOWED_PROTOCOLS = Set.of("http", "https");

	private static final String YACL_MODRINTH_LINK = "https://modrinth.com/mod/yacl?version=%s&loader=fabric#download";
	private static final String CLOTH_CONFIG_API_MODRINTH_LINK = "https://modrinth.com/mod/cloth-config?version=%s&loader=fabric#download";

	public static Screen createScreen(Screen parent) {
		return new ConfirmScreen(NoConfigLibrariesScreen::onConfirm, NoConfigLibrariesScreen.TITLE, NoConfigLibrariesScreen.MESSAGE, NoConfigLibrariesScreen.OPEN_YACL_PAGE, NoConfigLibrariesScreen.OPEN_CLOTH_CONFIG_PAGE) {
			@Override
			public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
				if (keyCode == 256 && this.shouldCloseOnEsc()) {
					Minecraft.getInstance().setScreen(parent);
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
			String url = (bl ? YACL_MODRINTH_LINK : CLOTH_CONFIG_API_MODRINTH_LINK).formatted(SharedConstants.getCurrentVersion()./*? if >=1.21.6 {*/name/*?} else {*//* getName *//*?}*/());

			URI link = new URI(url);
			String string = link.getScheme();
			if (string == null) {
				throw new URISyntaxException(url, "Missing protocol");
			}
			if (!ALLOWED_PROTOCOLS.contains(string.toLowerCase(Locale.ROOT))) {
				throw new URISyntaxException(url, "Unsupported protocol: " + string.toLowerCase(Locale.ROOT));
			}
			Util.getPlatform().openUri(link);
		} catch (URISyntaxException e) {
			PatPatClient.LOGGER.error("Can't open {} Modrinth page: ", (bl ? "YACL" : "Cloth Config API"), e);
		}
	}

	static {
		MutableComponent yaclShort = TextUtils.literal("YACL").withStyle(ChatFormatting.GOLD);
		Component yaclFull = TextUtils.literal("Yet Another Config Lib [YACL]").withStyle(ChatFormatting.GOLD);
		Component clothConfig = TextUtils.literal("Cloth Config API").withStyle(ChatFormatting.GOLD);
		Component closeText = TextUtils.text("modmenu.no_config_libraries_screen.close").withStyle(ChatFormatting.GRAY);

		TITLE = ModMenuUtils.getModTitle();
		MESSAGE        = TextUtils.text("modmenu.no_config_libraries_screen.message", yaclFull, clothConfig, closeText);
		OPEN_YACL_PAGE = TextUtils.text("modmenu.no_config_libraries_screen.open_page", yaclShort);
		OPEN_CLOTH_CONFIG_PAGE = TextUtils.text("modmenu.no_config_libraries_screen.open_page", clothConfig);
	}
}
