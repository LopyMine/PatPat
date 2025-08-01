package net.lopymine.patpat.client.config.migrate;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import lombok.Setter;
import net.lopymine.patpat.client.PatPatClient;
import net.lopymine.patpat.client.config.PatPatClientConfig;
import net.lopymine.patpat.common.Version;
import net.lopymine.patpat.common.migrate.AbstractConfigMigrateHandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

// TODO: Update method for future config updates
// TODO: Write test on this migration
@Setter
public class PatPatClientConfigMigrateVersion100 extends AbstractConfigMigrateHandler {

	private static final String MIGRATE_FILE_NAME = "patpat-client.json5";
	private static final String MIGRATE_VERSION = "1.0.0";

	private PatPatClientConfig config = null;

	public PatPatClientConfigMigrateVersion100() {
		super(MIGRATE_FILE_NAME, MIGRATE_VERSION, PatPatClient.LOGGER);
	}

	@Override
	public boolean needToMigrateFile() {
		try {
			File migrateFile = this.getMigrateFile().toFile();
			JsonObject object = GSON.fromJson(new JsonReader(new FileReader(migrateFile)), JsonObject.class);
			JsonElement versionElement = object.get("version");
			if (versionElement == null) {
				return false;
			}
			Version version = Version.of(versionElement.getAsString());
			return version.is(Version.of(MIGRATE_VERSION));
		} catch (FileNotFoundException e) {
			getLogger().error("[MigrateVersion100] Not found file for migrate", e);
		}
		return false;
	}


	@Override
	public boolean migrateFile() {
		try {
			File migrateFile = this.getMigrateFile().toFile();
			JsonObject object = GSON.fromJson(new JsonReader(new FileReader(migrateFile)), JsonObject.class);
			PatPatClientConfig clientConfig = this.config == null ? PatPatClientConfig.getInstance() : this.config;
			this.migrateFields(object, clientConfig);
			return true;
		} catch (FileNotFoundException e) {
			getLogger().error("[MigrateVersion100] Not found file for migrate", e);
		}
		return false;
	}

	private void migrateFields(JsonObject object, PatPatClientConfig config) {
		config.setVersion(Version.CLIENT_CONFIG_VERSION);
		config.getProximityPacketsConfig().setProximityPacketsEnabled(false);
		if (this.isShouldSave()) {
			config.save();
		}
	}
}
