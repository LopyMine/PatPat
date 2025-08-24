package net.lopymine.patpat.common.config.list;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import lombok.*;
import net.lopymine.patpat.PatLogger;
import org.jetbrains.annotations.Nullable;

@Getter
@Setter
public abstract class AbstractMapConfig<K, V> {

	private final Map<K, V> standardValues;
	private final PatLogger logger;
	private final File configFile;

	private boolean initialized = false;

	public AbstractMapConfig(Map<K, V> standardValues, PatLogger logger, File configFile) {
		this.standardValues = standardValues;
		this.logger         = logger;
		this.configFile     = configFile;
	}

	public static <D, G, B extends AbstractMapConfig<D, G>> B getInitialized(B config) {
		if (config.isInitialized()) {
			return config;
		}
		config.read();
		config.setInitialized(true);;
		return config;
	}

	@Nullable
	protected abstract Map.Entry<K, V> decode(String line);

	@Nullable
	protected abstract String encode(Map.Entry<K, V> element);

	protected abstract Map<K, V> getValues();

	public void reload() {
		this.getValues().clear();
		this.read();
	}

	public void create() {
		this.create(this.getConfigFile());
	}

	public void create(File config) {
		try {
			if (config.createNewFile()) {
				try (FileWriter writer = new FileWriter(config, StandardCharsets.UTF_8)) {
					writer.write(String.join("\n", this.standardValues.entrySet().stream().map(this::encode).filter(Objects::nonNull).toList()));
				} catch (IOException e) {
					this.getLogger().error("Failed to write standard values in config!", e);
				}
			} else {
				this.getLogger().error("Invoked config creation, but config already exists!");
			}
		} catch (Exception e) {
			this.getLogger().error("Failed to create config!", e);
		}
	}

	public void read() {
		this.read(this.getConfigFile(), this.getValues());
	}

	public void read(File config, Map<K, V> map) {
		if (!config.exists()) {
			this.create(config);
		}

		String line;
		try (BufferedReader reader = new BufferedReader(new FileReader(config))) {
			line = reader.readLine();
			while (line != null) {
				Entry<K, V> decoded = this.decode(line);
				if (decoded != null) {
					map.put(decoded.getKey(), decoded.getValue());
				}
				line = reader.readLine();
			}
		} catch (Exception e) {
			this.getLogger().error("Failed to reload config:", e);
		}
	}

	public void saveAsync() {
		CompletableFuture.runAsync(this::save);
	}

	public void save() {
		this.save(this.getConfigFile(), this.getValues().entrySet().stream().map(this::encode).filter(Objects::nonNull).toList());
	}

	public void save(File config, Collection<String> list) {
		try (FileWriter writer = new FileWriter(config, StandardCharsets.UTF_8)) {
			String collect = String.join("\n", list);
			this.getLogger().debug("Saving config with values:");
			this.getLogger().debug(collect);
			writer.write(collect);
		} catch (Exception e) {
			this.getLogger().error("Failed to save config:", e);
		}
	}
}
