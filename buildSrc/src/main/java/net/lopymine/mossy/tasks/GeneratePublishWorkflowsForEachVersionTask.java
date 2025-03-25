package net.lopymine.mossy.tasks;

import lombok.experimental.ExtensionMethod;
import org.gradle.api.*;
import org.gradle.api.tasks.TaskAction;

import net.lopymine.mossy.MossyPlugin;

import java.io.*;
import java.nio.file.Files;

@ExtensionMethod(MossyPlugin.class)
public class GeneratePublishWorkflowsForEachVersionTask extends DefaultTask {
	
	@TaskAction
	public void generate() {
		Project project = this.getProject();
		File file = project.getRootFile(".github/workflows/");
		if (!file.exists() && !file.mkdirs()) {
			return;
		}
		String[] multiVersions = project.getMultiVersions();
		for (String multiVersion : multiVersions) {
			try {
				File workflowFile = file.toPath().resolve("publish_%s.yml".formatted(multiVersion)).toFile();
				if (workflowFile.exists()) {
					continue;
				}
				if (!workflowFile.createNewFile()) {
					continue;
				}
				String strip = """
						# Generated workflow by task
						
						name: Publish MULTI_VERSION_ID Version
						on: [workflow_dispatch] # Manual trigger
						
						permissions:
						  contents: write
						
						jobs:
						  build:
						    runs-on: ubuntu-22.04
						    container:
						      image: mcr.microsoft.com/openjdk/jdk:21-ubuntu
						      options: --user root
						    steps:
						      - uses: actions/checkout@v4
						      - name: make gradle wrapper executable
						        run: chmod +x ./gradlew
						      - name: Publish MULTI_VERSION_ID Mod Version
						        run: ./gradlew chiseledBuildAndCollect+MULTI_VERSION_ID chiseledPublish+MULTI_VERSION_ID
						        env:
						          CURSEFORGE_API_KEY: ${{ secrets.CURSEFORGE_API_KEY }}
						          MODRINTH_API_KEY: ${{ secrets.MODRINTH_API_KEY }}
						""".replaceAll("MULTI_VERSION_ID", multiVersion).stripIndent().strip();
				Files.write(workflowFile.toPath(), strip.getBytes());
			} catch (Exception ignored) {
			}

		}
	}

}
