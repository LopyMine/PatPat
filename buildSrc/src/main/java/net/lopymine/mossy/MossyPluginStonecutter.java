package net.lopymine.mossy;

import dev.kikugie.stonecutter.controller.StonecutterControllerExtension;
import dev.kikugie.stonecutter.data.StonecutterProject;
import lombok.experimental.ExtensionMethod;
import org.gradle.*;
import org.gradle.api.*;
import org.gradle.api.initialization.Settings;
import org.gradle.api.invocation.Gradle;
import org.gradle.api.tasks.TaskContainer;

import java.util.List;
import org.jetbrains.annotations.NotNull;

@ExtensionMethod(MossyPlugin.class)
public class MossyPluginStonecutter implements Plugin<Project> {

	@Override
	public void apply(@NotNull Project project) {
		TaskContainer tasks = project.getTasks();
		StonecutterControllerExtension controller = project.getExtensions().getByType(StonecutterControllerExtension.class);

		for (StonecutterProject version : controller.getVersions()) {
			tasks.register("buildAndCollect+%s".formatted(version.getProject()), (task) -> {
				task.dependsOn(":%s:buildAndCollect".formatted(version.getProject()));
				task.setGroup("mossy-build");
			});
		}

		for (StonecutterProject version : controller.getVersions()) {
			tasks.register("publish+%s".formatted(version.getProject()), (task) -> {
				task.dependsOn(":%s:publishMods".formatted(version.getProject()));
				task.setGroup("mossy-publish");
			});
		}

		tasks.register("buildAndCollect+All", (task) -> {
			controller.getVersions().forEach((version) -> {
				task.dependsOn(":%s:buildAndCollect".formatted(version.getProject()));
			});
			task.setGroup("mossy-build");
		});

		tasks.register("buildAndCollect+Specified", (task) -> {
			List<String> versionsSpecifications = project.getVersionsSpecifications();
			controller.getVersions().forEach((version) -> {
				if (!versionsSpecifications.contains(version.getProject())) {
					return;
				}
				task.dependsOn(":%s:buildAndCollect".formatted(version.getProject()));
			});
			task.setGroup("mossy-build");
		});

		tasks.register("publish+All", (task) -> {
			controller.getVersions().forEach((version) -> {
				task.dependsOn(":%s:publishMods".formatted(version.getProject()));
			});
			task.setGroup("mossy-publish");
		});

		tasks.register("publish+Specified", (task) -> {
			List<String> versionsSpecifications = project.getVersionsSpecifications();
			controller.getVersions().forEach((version) -> {
				if (!versionsSpecifications.contains(version.getProject())) {
					return;
				}
				task.dependsOn(":%s:publishMods".formatted(version.getProject()));
			});
			task.setGroup("mossy-publish");
		});

		project.getGradle().addBuildListener(new BuildListener() {
			@Override
			public void settingsEvaluated(@NotNull Settings settings) {

			}

			@Override
			public void projectsLoaded(@NotNull Gradle gradle) {

			}

			@Override
			public void projectsEvaluated(@NotNull Gradle gradle) {
				for (Task task : tasks) {
					if (!"stonecutter".equals(task.getGroup())) {
						continue;
					}
					task.setGroup("mossy-stonecutter");
				}
			}

			@Override
			public void buildFinished(@NotNull BuildResult result) {

			}
		});
	}
}
