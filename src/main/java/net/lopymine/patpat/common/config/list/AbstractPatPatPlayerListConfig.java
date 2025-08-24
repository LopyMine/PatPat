package net.lopymine.patpat.common.config.list;

import java.io.File;
import java.util.*;
import java.util.Map.Entry;
import lombok.Getter;
import net.lopymine.patpat.PatLogger;
import net.lopymine.patpat.client.PatPatClient;
import net.lopymine.patpat.common.config.PatPatConfigManager;
import org.jetbrains.annotations.Nullable;

@Getter
public abstract class AbstractPatPatPlayerListConfig extends AbstractMapConfig<UUID, String> {

	private final Map<UUID, String> values = new HashMap<>();

	public AbstractPatPatPlayerListConfig(Map<UUID, String> standardValues, PatLogger logger, File configFile) {
		super(standardValues, logger, configFile);
	}

	public boolean contains(UUID uuid){
		return this.values.containsKey(uuid);
	}

	@Override
	protected @Nullable Entry<UUID, String> decode(String line) {
		try {
			String[] uuidNicknamePair = line.split(" ");
			return Map.entry(UUID.fromString(uuidNicknamePair[0]), uuidNicknamePair[1]);
		} catch (Exception e) {
			this.getLogger().error("Failed to parse line \"{}\":", line);
		}
		return null;
	}

	@Override
	protected @Nullable String encode(Entry<UUID, String> element) {
		return "%s %s".formatted(element.getKey(), element.getValue());
	}
}
