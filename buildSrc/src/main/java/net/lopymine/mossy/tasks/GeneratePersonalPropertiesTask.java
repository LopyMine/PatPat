package net.lopymine.mossy.tasks;

import lombok.experimental.ExtensionMethod;
import org.gradle.api.*;
import org.gradle.api.tasks.TaskAction;

import net.lopymine.mossy.MossyPlugin;

import java.io.*;
import java.nio.file.Files;

@ExtensionMethod(MossyPlugin.class)
public class GeneratePersonalPropertiesTask extends DefaultTask {

	@TaskAction
	public void generate() {
		Project project = this.getProject();
		File file = project.getRootFile("personal/");
		if (!file.exists() && !file.mkdirs()) {
			return;
		}
		try {
			File personalPropertiesFile = file.toPath().resolve("personal.properties").toFile();
			if (personalPropertiesFile.exists()) {
				return;
			}
			if (!personalPropertiesFile.createNewFile()) {
				return;
			}

			String strip = """
					# # # # # # # # # # # # # #
					#   Personal Properties   #
					# # # # # # # # # # # # # #
					
					# If you don't want to set up any property, just set it to "none".
					# Remember to run "regenerateRunConfigurations" task after changing this file!
					
					# Player properties
					player_nickname=Steve
					player_uuid=8667ba71-b85a-4004-af54-457a9734eed7
					
					# Hot Swapping
					absolute_path_to_sponge_mixin=none
					
					# Quick Play
					quick_play_world=none
					""".stripIndent().strip();

			Files.write(personalPropertiesFile.toPath(), strip.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
