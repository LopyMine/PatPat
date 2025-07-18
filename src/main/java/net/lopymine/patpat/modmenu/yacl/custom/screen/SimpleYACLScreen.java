package net.lopymine.patpat.modmenu.yacl.custom.screen;

//? if >=1.20.1 {

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.YetAnotherConfigLib.Builder;
import dev.isxander.yacl3.gui.YACLScreen;
import net.lopymine.patpat.utils.ModMenuUtils;
import net.minecraft.client.gui.screens.Screen;
import java.util.function.Consumer;

@SuppressWarnings("unused")
public class SimpleYACLScreen {

	private final Builder builder;
	private final Screen parent;

	public SimpleYACLScreen(Screen parent, Runnable onSave, Consumer<YACLScreen> onInit) {
		this.builder = YetAnotherConfigLib.createBuilder()
				.title(ModMenuUtils.getModTitle())
				.save(onSave)
				.screenInit(onInit);
		this.parent  = parent;
	}

	public static SimpleYACLScreen startBuilder(Screen parent, Runnable onSave) {
		return new SimpleYACLScreen(parent, onSave, (yaclScreen) -> {});
	}

	public static SimpleYACLScreen startBuilder(Screen parent, Runnable onSave, Consumer<YACLScreen> onInit) {
		return new SimpleYACLScreen(parent, onSave, onInit);
	}

	public SimpleYACLScreen categories(ConfigCategory... categories) {
		for (ConfigCategory category : categories) {
			if (category == null) {
				continue;
			}
			this.builder.category(category);
		}
		return this;
	}

	public Screen build() {
		return this.builder.build().generateScreen(this.parent);
	}
}
//?}
