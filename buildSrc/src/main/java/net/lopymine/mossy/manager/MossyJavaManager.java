package net.lopymine.mossy.manager;

import lombok.experimental.ExtensionMethod;
import org.gradle.api.*;
import org.gradle.api.plugins.*;
import org.gradle.api.tasks.TaskCollection;
import org.gradle.api.tasks.compile.JavaCompile;

import net.lopymine.mossy.MossyPlugin;

import org.jetbrains.annotations.NotNull;

@ExtensionMethod(MossyPlugin.class)
public class MossyJavaManager {

	public static void apply(@NotNull Project project, MossyPlugin mossyPlugin) {
		int javaVersionIndex = mossyPlugin.getJavaVersionIndex();
		JavaVersion javaVersion = mossyPlugin.getJavaVersion();

		TaskCollection<JavaCompile> collection = project.getTasks().withType(JavaCompile.class);
		for (JavaCompile javaCompile : collection) {
			javaCompile.getOptions().getRelease().set(javaVersionIndex);
		}

		JavaPluginExtension javaExtension = project.getExtensions().getByType(JavaPluginExtension.class);
		javaExtension.setSourceCompatibility(javaVersion);
		javaExtension.setTargetCompatibility(javaVersion);
	}
}
