package net.lopymine.patpat.common.migrate;

import com.google.gson.Gson;
import lombok.*;

import net.lopymine.patpat.*;
import net.lopymine.patpat.common.config.PatPatConfigManager;

import java.io.*;
import java.nio.file.*;

@Getter
@Setter
public abstract class AbstractConfigMigrateHandler implements MigrateHandler {

	protected static final Gson GSON = new Gson();

	private final PatLogger logger;
	private String migrateFileName;
	private String migrateVersion;
	private Path configFolder;

	private boolean shouldSave = true;

	protected AbstractConfigMigrateHandler(String migrateFileName, String migrateVersion, PatLogger logger) {
		this.migrateFileName = migrateFileName;
		this.migrateVersion = migrateVersion;
		this.logger = logger;
	}

	protected abstract boolean needToMigrateFile();

	protected abstract boolean migrateFile();

	protected String getBackupFileName() {
		return this.getMigrateFileName() + ".bkp";
	}

	public Path getConfigFolder() {
		return this.configFolder == null ? PatPatConfigManager.CONFIG_PATH : this.configFolder;
	}

	@Override
	public boolean needMigrate() {
		File file = this.getMigrateFile().toFile();
		if (!file.exists()) {
			return false;
		}
		return this.needToMigrateFile();
	}

	@Override
	public boolean migrate() {
		File file = this.getMigrateFile().toFile();
		if (!file.exists()) {
			return false;
		}
		if (!this.createBackup()) {
			return false;
		}
		return this.migrateFile();
	}

	protected boolean createBackup() {
		try {
			Files.copy(this.getMigrateFile(), this.getBackupFile(), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			this.getLogger().error("Failed to create old config backup!", e);
			return false;
		}
		return true;
	}

	protected Path getMigrateFile() {
		return this.getConfigFolder().resolve(this.getMigrateFileName());
	}

	protected Path getBackupFile() {
		return this.getConfigFolder().resolve(this.getBackupFileName());
	}

}
