package net.lopymine.mossy.manager;

import dev.kikugie.fletching_table.extension.FletchingTableExtension;
import lombok.experimental.ExtensionMethod;
import net.lopymine.mossy.MossyPlugin;
import org.gradle.api.Project;

@ExtensionMethod(MossyPlugin.class)
public class MossyJ52JManager {

	public static void apply(Project project) {
		project.getExtensions().configure(FletchingTableExtension.class, (extension) -> {
			var main = extension.getJ52j().register("main");
			main.configure((container) -> container.extension("json", "**/*.json5"));
		});
	}

}
