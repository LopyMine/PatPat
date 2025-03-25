package net.lopymine.mossy.extension;

import lombok.Getter;

import java.util.*;

@Getter
public class MossyAdditionalDependencies {

	private final Map<String, AdditionalDependencyOverride> overrides = new HashMap<>();
	private final Set<String> disabled = new HashSet<>();

	public void override(String configurationName, String modId) {
		this.overrides.put(modId, new AdditionalDependencyOverride(modId, configurationName));
	}

	public void disable(String modId) {
		this.disabled.add(modId);
	}

	public record AdditionalDependencyOverride(String modId, String configurationName) {

	}
}
