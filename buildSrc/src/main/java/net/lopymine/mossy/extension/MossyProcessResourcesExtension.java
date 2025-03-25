package net.lopymine.mossy.extension;

import lombok.Getter;
import org.gradle.api.tasks.Input;

import java.util.List;
import org.jetbrains.annotations.Nullable;

@Getter
public class MossyProcessResourcesExtension {

	@Input
	@Nullable
	List<String> expandFiles;

}
