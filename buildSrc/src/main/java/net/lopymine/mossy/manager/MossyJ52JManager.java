package net.lopymine.mossy.manager;

import dev.kikugie.j52j.*;
import lombok.experimental.ExtensionMethod;
import org.gradle.api.Project;

import net.lopymine.mossy.MossyPlugin;

@ExtensionMethod(MossyPlugin.class)
public class MossyJ52JManager {

	public static void apply(Project project) {
		project.getExtensions().configure(J52JExtension.class, (extension) -> {
			extension.params((properties) -> {
				properties.setPrettyPrinting(true);
			});
		});
	}

}
