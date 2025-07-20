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
	/*? >=1.20.4 {*/
	private Map<String, Collection<String>> translators = new HashMap<>();
	/*?} else {*/
	/*private List<String> translators = new ArrayList<>();
	*//*?}*/

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
		/*? >=1.20.4 {*/
		Map<String, Collection<String>> translatorsMap = new HashMap<>();
		translatorsSection.getAsObject().forEach(entry ->
				translatorsMap.put(entry.getKey() + " - " + entry.getValue().getAsString(), TRANSLATOR_KEY)
		);
		this.translators = Collections.unmodifiableMap(translatorsMap);
		/*?} else {*/
		/*List<String> translatorsList = new ArrayList<>();
		translatorsSection.getAsObject().forEach(entry ->
				translatorsList.add(entry.getKey() + " - " + entry.getValue().getAsString() + " (Translator)")
		);
		this.translators = Collections.unmodifiableList(translatorsList);
		*//*?}*/
	}

	public static ModMenuTranslators getInstance() {
		if (instance == null) {
			instance = new ModMenuTranslators();
		}
		return instance;
	}

}
