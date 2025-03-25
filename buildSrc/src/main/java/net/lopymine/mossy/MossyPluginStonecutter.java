package net.lopymine.mossy;

import dev.kikugie.stonecutter.*;
import lombok.experimental.ExtensionMethod;
import org.gradle.*;
import org.gradle.api.*;
import org.gradle.api.initialization.Settings;
import org.gradle.api.invocation.Gradle;
import org.gradle.api.tasks.TaskContainer;

import java.util.*;
import java.util.function.*;
import org.jetbrains.annotations.NotNull;

@ExtensionMethod(MossyPlugin.class)
public class MossyPluginStonecutter implements Plugin<Project> {

	@Override
	public void apply(@NotNull Project project) {
		StonecutterController controller = project.getExtensions().getByType(StonecutterController.class);
		BiConsumer<String, Consumer<ChiseledTask>> registerConsumer = MossyPluginStonecutter.getRegisterConsumer(project, controller);

		registerConsumer.accept("chiseledBuildAndCollectAll", (chiseledTask) -> {
			chiseledTask.setGroup("mossy-build");
			chiseledTask.ofTask("buildAndCollect");
		});

		registerConsumer.accept("chiseledPublishAll", (chiseledTask) -> {
			chiseledTask.setGroup("mossy-publish");
			chiseledTask.ofTask("publishMods");
		});

		registerConsumer.accept("chiseledPublishSpecified", (chiseledTask) -> {
			chiseledTask.setGroup("mossy-publish");
			List<String> publicationVersions = project.getPublicationVersions();
			List<StonecutterProject> list = chiseledTask.getVersions()
					.get()
					.stream()
					.filter(stonecutterProject -> publicationVersions.contains(stonecutterProject.getProject()))
					.toList();
			chiseledTask.getVersions().set(list);
			chiseledTask.ofTask("publishMods");
		});

		for (StonecutterProject version : controller.getVersions()) {
			registerConsumer.accept("chiseledBuildAndCollect+%s".formatted(version.getProject()), (chiseledTask) -> {
				chiseledTask.setGroup("mossy-build");
				chiseledTask.getVersions().value(List.of(version));
				chiseledTask.ofTask("buildAndCollect");
			});
			registerConsumer.accept("chiseledPublish+%s".formatted(version.getProject()), (chiseledTask) -> {
				chiseledTask.setGroup("mossy-publish");
				chiseledTask.getVersions().value(List.of(version));
				chiseledTask.ofTask("publishMods");
			});
		}

		project.getGradle().addBuildListener(new BuildListener() {
			@Override
			public void settingsEvaluated(@NotNull Settings settings) {

			}

			@Override
			public void projectsLoaded(@NotNull Gradle gradle) {

			}

			@Override
			public void projectsEvaluated(@NotNull Gradle gradle) {
				for (Task task : project.getTasks()) {
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

	private static BiConsumer<String, Consumer<ChiseledTask>> getRegisterConsumer(@NotNull Project project, StonecutterController controller) {
		return (name, consumer) -> {
			TaskContainer tasks = project.getTasks();
			controller.registerChiseled(tasks.register(name, controller.getChiseled(), (consumer::accept)));
		};
	}
}
