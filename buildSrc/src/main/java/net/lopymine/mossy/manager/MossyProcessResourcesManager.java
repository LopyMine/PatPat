package net.lopymine.mossy.manager;

import lombok.experimental.ExtensionMethod;
import org.gradle.api.*;
import org.gradle.api.internal.TaskInputsInternal;
import org.gradle.language.jvm.tasks.ProcessResources;

import net.lopymine.mossy.MossyPlugin;
import net.lopymine.mossy.extension.*;

import java.util.*;
import org.jetbrains.annotations.NotNull;

@ExtensionMethod(MossyPlugin.class)
public class MossyProcessResourcesManager {

	public static void apply(@NotNull Project project, MossyPlugin plugin) {
		project.getExtensions().create("mossyResources", MossyProcessResourcesExtension.class);

		project.getGradle().addProjectEvaluationListener(new ProjectEvaluationListener() {
			@Override
			public void beforeEvaluate(@NotNull Project project) {
			}

			@Override
			public void afterEvaluate(@NotNull Project project, @NotNull ProjectState state) {
				MossyProcessResourcesExtension extension = project.getExtensions().getByType(MossyProcessResourcesExtension.class);
				MossyProcessResourcesManager.processResources(project, plugin, extension);
				project.getGradle().removeProjectEvaluationListener(this);
			}
		});
	}

	private static void processResources(Project project, MossyPlugin plugin, MossyProcessResourcesExtension extension) {
		ProcessResources processResources = (ProcessResources) project.getTasks().getByName("processResources");
		TaskInputsInternal inputs = processResources.getInputs();

		String mcVersion = plugin.getProjectMultiVersion().projectVersion();
		String modId = project.getProperty("data.mod_id");

		Map<String, String> properties = project.getMossyProperties("data");
		properties.putAll(project.getMossyProperties("build"));
		properties.put("java", String.valueOf(plugin.getJavaVersionIndex()));
		properties.put("minecraft", mcVersion);
		properties.put("fabric_api_id", project.getStonecutter().compare("1.19.1", mcVersion) >= 0 ? "fabric" : "fabric-api");

		properties.forEach(inputs::property);

		List<String> patterns = new ArrayList<>(List.of("*.json5", "*.json", "assets/%s/lang/*.json".formatted(modId)));
		List<String> expandFiles = extension.getExpandFiles();
		if (expandFiles != null) {
			patterns.addAll(expandFiles);
		}

		processResources.filesMatching(patterns, (details) -> {
			details.expand(properties);
		});

		processResources.filesMatching("aws/*.accesswidener", (details) -> {
			if (!details.getName().startsWith(mcVersion)) {
				details.exclude();
			}
		});
	}
}
