package net.lopymine.mossy.extension;

import lombok.Getter;
import org.gradle.api.Action;
import org.gradle.api.tasks.*;

@Getter
public class MossyDependenciesExtension {

	@Input
	String minecraft;

	@Input
	String mappings;

	@Input
	String fabricApi;

	@Input
	String fabricLoader;

	@Input
	String lombok;

	@Nested
	MossyAdditionalDependencies additional = new MossyAdditionalDependencies();

	public void additional(Action<MossyAdditionalDependencies> action) {
		action.execute(this.additional);
	}
}
