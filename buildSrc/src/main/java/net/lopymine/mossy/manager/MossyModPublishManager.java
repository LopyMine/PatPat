package net.lopymine.mossy.manager;

import lombok.experimental.ExtensionMethod;
import me.modmuss50.mpp.*;
import org.codehaus.groovy.runtime.ResourceGroovyMethods;
import org.gradle.api.*;
import org.gradle.api.file.RegularFile;
import org.gradle.api.provider.Provider;

import net.fabricmc.loom.task.RemapJarTask;

import net.lopymine.mossy.MossyPlugin;
import net.lopymine.mossy.multi.MultiVersion;

import java.io.*;
import java.util.Arrays;
import org.jetbrains.annotations.NotNull;

@ExtensionMethod(MossyPlugin.class)
public class MossyModPublishManager {

	public static void apply(@NotNull Project project, MossyPlugin mossyPlugin, ModPublishExtension mpe) {
		MultiVersion projectMultiVersion = mossyPlugin.getProjectMultiVersion();
		String name = "[%s] %s v%s".formatted(projectMultiVersion.toVersionRange(), project.getProperty("data.mod_name"), project.getProperty("data.mod_version"));

		String[] loaders = project.getProperty("loaders").split(" ");
		String modrinthId = project.getProperty("modrinth_id");
		String curseForgeId = project.getProperty("curseforge_id");
		String[] dependsEmbeds = project.getProperty("depends_embeds").split(" ");
		String[] dependsRequires = project.getProperty("depends_requires").split(" ");
		String[] dependsOptional = project.getProperty("depends_optional").split(" ");
		String[] dependsIncompatible = project.getProperty("depends_incompatible").split(" ");
		String versionType = project.getProperty("version_type");
		int maxJavaVersion = Integer.parseInt(project.getProperty("max_java_version"));
		Boolean isForClient = Boolean.parseBoolean(project.getProperty("is_for_client"));
		Boolean isForServer = Boolean.parseBoolean(project.getProperty("is_for_server"));
		boolean testPublish = Boolean.parseBoolean(project.getProperty("test_publish"));

		String curseForgeApiKey = project.getProviders().environmentVariable("CURSEFORGE_API_KEY").getOrNull();
		String modrinthApiKey = project.getProviders().environmentVariable("MODRINTH_API_KEY").getOrNull();

		boolean cannotUpload = testPublish || curseForgeApiKey == null || modrinthApiKey == null;

		mpe.getDisplayName().set(name);
		mpe.getFile().set(getModFile(project));
		mpe.getChangelog().set(getChangeLog(project));
		mpe.getType().set(getType(versionType));
		mpe.getModLoaders().set(Arrays.asList(loaders));
		mpe.getDryRun().set(cannotUpload);

		mpe.curseforge((curseforge) -> {
			curseforge.getProjectId().set(curseForgeId);
			curseforge.getAccessToken().set(curseForgeApiKey);

			for (int i = 17; i < maxJavaVersion + 1; i++) {
				curseforge.getJavaVersions().add(JavaVersion.values()[i]);
			}

			curseforge.getClientRequired().set(isForClient);
			curseforge.getServerRequired().set(isForServer);

			if (projectMultiVersion.minIsMax()) {
				curseforge.getMinecraftVersions().add(projectMultiVersion.maxVersion());
			} else {
				curseforge.minecraftVersionRange((options) -> {
					options.getStart().set(projectMultiVersion.minVersion());
					options.getEnd().set(projectMultiVersion.maxVersion());
				});
			}

			if (!dependsEmbeds[0].equals("none")) {
				curseforge.embeds(dependsEmbeds);
			}
			if (!dependsRequires[0].equals("none")) {
				curseforge.requires(dependsRequires);
			}
			if (!dependsOptional[0].equals("none")) {
				curseforge.optional(dependsOptional);
			}
			if (!dependsIncompatible[0].equals("none")) {
				curseforge.incompatible(dependsIncompatible);
			}
		});

		mpe.modrinth((modrinth) -> {
			modrinth.getProjectId().set(modrinthId);
			modrinth.getAccessToken().set(modrinthApiKey);

			if (projectMultiVersion.minIsMax()) {
				modrinth.getMinecraftVersions().add(projectMultiVersion.maxVersion());
			} else {
				modrinth.minecraftVersionRange((options) -> {
					options.getStart().set(projectMultiVersion.minVersion());
					options.getEnd().set(projectMultiVersion.maxVersion());
				});
			}

			if (!dependsEmbeds[0].equals("none")) {
				modrinth.embeds(dependsEmbeds);
			}
			if (!dependsRequires[0].equals("none")) {
				modrinth.requires(dependsRequires);
			}
			if (!dependsOptional[0].equals("none")) {
				modrinth.optional(dependsOptional);
			}
			if (!dependsIncompatible[0].equals("none")) {
				modrinth.incompatible(dependsIncompatible);
			}
		});

		MossyPlugin.LOGGER.logModule("MPP","Configuring \"%s\"", mpe.getDisplayName().get());
		MossyPlugin.LOGGER.logModule("MPP","Dry Run: %s", mpe.getDryRun().get());
	}

	private static Provider<RegularFile> getModFile(@NotNull Project project) {
		return ((RemapJarTask) project.getTasks().getByName("remapJar")).getArchiveFile();
	}

	private static ReleaseType getType(String versionType) {
		return switch (versionType) {
			case "RELEASE" -> ReleaseType.STABLE;
			case "BETA" -> ReleaseType.BETA;
			case "ALPHA" -> ReleaseType.ALPHA;
			default -> throw new IllegalArgumentException("Unknown version type!");
		};
	}

	private static String getChangeLog(@NotNull Project project) {
		try {
			File file = project.getRootFile("CHANGELOG.md");
			if (file.exists()) {
				String text = ResourceGroovyMethods.getText(file);
				if (!text.isBlank()) {
					return text;
				}
			}
		} catch (IOException e) {
			throw new RuntimeException("Failed to read changelog:", e);
		}
		return "No changelog specified.";
	}

}
