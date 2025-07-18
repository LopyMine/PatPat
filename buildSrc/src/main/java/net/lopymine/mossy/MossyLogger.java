package net.lopymine.mossy;

import org.gradle.api.Project;

public class MossyLogger {

	private Project project;

	public void setup(Project project) {
		this.project = project;
	}

	@SuppressWarnings("all")
	public void log(String text, Object... objects) {
		System.out.println("[Mossy/%s] %s".formatted(this.project.getName(), text.formatted(objects)));
	}

	@SuppressWarnings("all")
	public void logModule(String module, String text, Object... objects) {
		System.out.println("[Mossy/%s/%s] %s".formatted(this.project.getName(), module, text.formatted(objects)));
	}

}
