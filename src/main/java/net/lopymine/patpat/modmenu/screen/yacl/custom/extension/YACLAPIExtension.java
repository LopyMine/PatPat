package net.lopymine.patpat.modmenu.screen.yacl.custom.extension;

import dev.isxander.yacl3.api.*;

import net.fabricmc.loader.api.*;
import net.fabricmc.loader.impl.util.version.StringVersion;

import net.lopymine.patpat.PatPat;

import java.util.*;

@SuppressWarnings("unused")
public class YACLAPIExtension {

	private static final String STATE_MANAGER_VERSION = "3.6.0";

	public static <A> ListOption.Builder<A> bindingE(ListOption.Builder<A> builder, Binding<List<A>> binding, boolean instant) {
		Version currentYACLVersion = getCurrentYACLVersion();

		if (currentYACLVersion.compareTo(getVersion(STATE_MANAGER_VERSION)) >= 0) {
			builder.state(instant ? StateManager.createInstant(binding) : StateManager.createSimple(binding));
		} else {
			builder.binding(binding);
			//builder.instant(instant);
		}

		return builder;
	}

	public static <A> Option.Builder<A> bindingE(Option.Builder<A> builder, Binding<A> binding, boolean instant) {
		Version currentYACLVersion = getCurrentYACLVersion();

		if (currentYACLVersion.compareTo(getVersion(STATE_MANAGER_VERSION)) >= 0) {
			builder.stateManager(instant ? StateManager.createInstant(binding) : StateManager.createSimple(binding));
		} else {
			builder.binding(binding);
			builder.instant(instant);
		}

		return builder;
	}

	private static Version getCurrentYACLVersion() {
		return FabricLoader.getInstance().getModContainer("yet_another_config_lib_v3").orElseThrow(
				() -> new NoSuchElementException(
						"Failed to find Yet Another Config Lib [YACL], this shouldn't happen! Please report this crash to discord server of %s mod!".formatted(PatPat.MOD_NAME)
				)
		).getMetadata().getVersion();
	}

	private static Version getVersion(String version) {
		try {
			return Version.parse(version);
		} catch (Exception ignored) {
			return new StringVersion("1.0.0");
		}
	}
}
