package net.lopymine.mossy;

import dev.kikugie.stonecutter.controller.StonecutterControllerExtension;
import dev.kikugie.stonecutter.data.StonecutterProject;
import java.io.IOException;
import lombok.experimental.ExtensionMethod;
import org.gradle.*;
import org.gradle.api.*;
import org.gradle.api.initialization.Settings;
import org.gradle.api.invocation.Gradle;
import org.gradle.api.tasks.*;

import java.util.List;
import org.jetbrains.annotations.NotNull;

@ExtensionMethod(MossyPlugin.class)
public class MossyPluginStonecutter implements Plugin<Project> {

	@Override
	public void apply(@NotNull Project project) {
		TaskContainer tasks = project.getTasks();
		StonecutterControllerExtension controller = project.getExtensions().getByType(StonecutterControllerExtension.class);

		tasks.register("submodulesUpdate", (task) -> {
			task.doFirst((a) -> {
				try {
					Runtime.getRuntime().exec(new String[]{"git", "submodule", "update", "--init", "--force", "--remote"});
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			});
		});

		for (StonecutterProject version : controller.getVersions()) {
			tasks.register("buildAndCollect+%s".formatted(version.getProject()), (task) -> {
				task.dependsOn("submodulesUpdate");
				task.dependsOn(":%s:buildAndCollect".formatted(version.getProject()));
				task.setGroup("mossy-build");
			});
		}

		for (StonecutterProject version : controller.getVersions()) {
			tasks.register("publish+%s".formatted(version.getProject()), (task) -> {
				task.dependsOn("submodulesUpdate");
				task.dependsOn(":%s:publishMods".formatted(version.getProject()));
				task.setGroup("mossy-publish");
			});
		}

		tasks.register("buildAndCollect+All", (task) -> {
			task.dependsOn("submodulesUpdate");
			controller.getVersions().forEach((version) -> {
				task.dependsOn(":%s:buildAndCollect".formatted(version.getProject()));
			});
			task.setGroup("mossy-build");
		});

		tasks.register("buildAndCollect+Specified", (task) -> {
			task.dependsOn("submodulesUpdate");
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
			task.dependsOn("submodulesUpdate");
			List<StonecutterProject> versions = controller.getVersions()
					.stream()
					.sorted((a, b) -> controller.compare(a.getProject(), b.getProject()))
					.toList();

			for (String publishTask : List.of("publishModrinth", "publishCurseforge")) {
				for (int i = 1; i < versions.size(); i++) {
					StonecutterProject first = versions.get(i - 1);
					StonecutterProject second = versions.get(i);

					TaskProvider<Task> firstTask = project.getChildProjects().get(first.getProject()).getTasks().named(publishTask);
					TaskProvider<Task> secondTask = project.getChildProjects().get(second.getProject()).getTasks().named(publishTask);
					task.dependsOn(firstTask, secondTask);

					secondTask.configure((t) -> t.setMustRunAfter(List.of(firstTask)));
				}
			}

			task.setGroup("mossy-publish");
		});

		tasks.register("publish+Specified", (task) -> {
			task.dependsOn("submodulesUpdate");
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
