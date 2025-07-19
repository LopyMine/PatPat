package net.lopymine.patpat.modmenu;

import lombok.Getter;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.CustomValue;

import net.lopymine.patpat.PatLogger;
import net.lopymine.patpat.PatPat;

import java.util.*;

@Getter
public class ModMenuTranslators {

	private static final PatLogger logger = PatPat.LOGGER.extend("ModMenuTranslators");
	private static final List<String> TRANSLATOR_KEY = List.of("Translator");

	private Map<String, Collection<String>> translators = new HashMap<>();

	private static ModMenuTranslators instance;


	private ModMenuTranslators() {
		Optional<ModContainer> modContainer = FabricLoader.getInstance().getModContainer(PatPat.MOD_ID);
		if (modContainer.isEmpty()) {
			logger.warn("Failure to get mod container!");
			return;
		}
		CustomValue translatorsSection = modContainer.get().getMetadata().getCustomValue("translators");
		if (translatorsSection == null) {
			logger.warn("Failure to get translators section from fabric.mod.json!");
			return;
		}
		Map<String, Collection<String>> translatorsMap = new HashMap<>();
		translatorsSection.getAsObject().forEach(entry ->
				translatorsMap.put(entry.getKey() + " - " + entry.getValue().getAsString(), TRANSLATOR_KEY)
		);
		this.translators = Collections.unmodifiableMap(translatorsMap);
	}

	public static ModMenuTranslators getInstance() {
		if (instance == null) {
			instance = new ModMenuTranslators();
		}
		return instance;
	}

}
