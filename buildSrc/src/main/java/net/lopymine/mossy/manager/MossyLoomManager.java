package net.lopymine.mossy.manager;

import lombok.experimental.ExtensionMethod;
import org.gradle.api.*;

import net.fabricmc.loom.api.LoomGradleExtensionAPI;
import net.fabricmc.loom.configuration.ide.RunConfigSettings;

import net.lopymine.mossy.MossyPlugin;

import java.io.File;
import java.util.*;
import java.util.regex.Pattern;
import org.jetbrains.annotations.*;

@ExtensionMethod(MossyPlugin.class)
public class MossyLoomManager {

	private static final Pattern PLAYER_NICKNAME_PATTERN = Pattern.compile("[a-zA-Z0-9_]{2,16}$");

	@SuppressWarnings("UnstableApiUsage")
	public static void apply(@NotNull Project project, MossyPlugin plugin, LoomGradleExtensionAPI loom) {
		String modId = project.getProperty("data.mod_id");
		String currentVersion = plugin.getProjectMultiVersion().projectVersion();
		File file = project.getRootFile("src/main/resources/aws/%s.accesswidener".formatted(currentVersion));

		// Mixins and AWs

		loom.getMixin().getDefaultRefmapName().set("%s.refmap.json".formatted(modId));
		loom.getAccessWidenerPath().set(file);

		// Run Configs

		Properties personalProperties = project.getPersonalProperties();

		String playerNickname = getPlayerNickname(personalProperties);
		UUID playerUuid = getPlayerUuid(personalProperties);
		Object quickPlayWorld = personalProperties.get("quick_play_world");
		Object pathToSpongeMixin = personalProperties.get("absolute_path_to_sponge_mixin");

		for (RunConfigSettings runConfig : loom.getRunConfigs()) {
			runConfig.setIdeConfigGenerated(true);
			runConfig.setRunDir("../../runs/" + runConfig.getEnvironment());

			if (runConfig.getEnvironment().equals("client")) {
				addProgramArg(runConfig, "--username", playerNickname);
				addProgramArg(runConfig, "--uuid", playerUuid);
				addProgramArg(runConfig, "--quickPlaySingleplayer", quickPlayWorld);
				addVMArg(runConfig, "-javaagent", pathToSpongeMixin);
			}
 		}
	}

	private static String getPlayerNickname(Properties personalProperties) {
		Object o = personalProperties.get("player_nickname");
		if (o == null) {
			return "Player";
		}
		String playerNickname = o.toString();
		if (!PLAYER_NICKNAME_PATTERN.matcher(playerNickname).matches()) {
			return "Player";
		}
		return playerNickname;
	}

	private static @Nullable UUID getPlayerUuid(Properties personalProperties) {
		try {
			Object o = personalProperties.get("player_uuid");
			if (o == null) {
				return null;
			}
			return UUID.fromString(o.toString());
		} catch (Exception e) {
			return null;
		}
	}

	@SuppressWarnings("all")
	private static void addVMArg(RunConfigSettings settings, String propertyKey, @Nullable Object propertyValue) {
		if (propertyValue == null || propertyValue.toString().equals("none")) {
			return;
		}
		settings.getVmArgs().add("%s:%s".formatted(propertyKey, propertyValue.toString()));
	}

	private static void addProgramArg(RunConfigSettings settings, String propertyKey, @Nullable Object propertyValue) {
		if (propertyValue == null || propertyValue.toString().equals("none")) {
			return;
		}
		List<String> programArgs = settings.getProgramArgs();
		programArgs.add(propertyKey);
		programArgs.add(propertyValue.toString());
	}
}
