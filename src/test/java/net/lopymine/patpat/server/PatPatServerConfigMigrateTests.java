//? >=1.19.4 {
package net.lopymine.patpat.server;

import net.lopymine.patpat.server.config.PatPatServerConfig;
import net.lopymine.patpat.server.config.migrate.*;
import net.lopymine.patpat.util.PathUtils;
import org.junit.jupiter.api.*;

import net.lopymine.patpat.client.config.resourcepack.ListMode;
import net.lopymine.patpat.server.config.sub.PatPatServerPlayerListConfig;
import net.lopymine.patpat.server.config.migrate.PatPatServerConfigMigrateVersion0.Configs;

import java.awt.*;
import java.io.*;
import java.nio.file.Path;
import java.util.UUID;

class PatPatServerConfigMigrateTests {

	@Nested
	class V0 {

		private Path getConfigFolder() {
			return PathUtils.getResource("server/");
		}

		@Test
		void test1() throws IOException {
			File file = this.getConfigFolder().resolve("test_1.json5").toFile();
			Assertions.assertTrue(file.exists());

			PatPatServerConfig config = PatPatServerConfig.getNewInstance();
			config.setListMode(ListMode.DISABLED);
			PatPatServerPlayerListConfig listConfig = new PatPatServerPlayerListConfig();

			PatPatServerConfigMigrateVersion0 migrator = new PatPatServerConfigMigrateVersion0();
			migrator.setConfigFolder(this.getConfigFolder());
			migrator.setMigrateFileName(file.getName());
			migrator.setConfig(config);
			migrator.setListConfig(listConfig);
			migrator.setShouldSave(false);

			PatPatServerConfigMigrateManager migrateManager = new PatPatServerConfigMigrateManager(config, migrator);
			migrateManager.migrate();

			Assertions.assertEquals(ListMode.BLACKLIST, config.getListMode());
			Assertions.assertTrue(listConfig.contains(UUID.fromString("65adfaa1-0196-053e-07ea-1260729a2e34")));
			Assertions.assertTrue(listConfig.contains(UUID.fromString("a4369612-0487-0038-0339-1f1073297c22")));

			Path migratedFilesPath = this.getConfigFolder().resolve("test_1/");
			Assertions.assertTrue(migratedFilesPath.toFile().mkdirs());

			File migratedConfigFile = migratedFilesPath.resolve("migrated_test_1.json5").toFile();
			config.save(migratedConfigFile);

			File migratedConfigListFile = migratedFilesPath.resolve("migrated_list_test_1.txt").toFile();
			listConfig.save(migratedConfigListFile);

			Desktop.getDesktop().open(this.getConfigFolder().toFile());
		}

		@Test
		void test2() throws IOException {
			File file = this.getConfigFolder().resolve("test_2.json5").toFile();
			Assertions.assertTrue(file.exists());

			PatPatServerConfig config = PatPatServerConfig.getNewInstance();
			config.setListMode(ListMode.DISABLED);
			PatPatServerPlayerListConfig listConfig = new PatPatServerPlayerListConfig();
			listConfig.getUuids().add(UUID.randomUUID());

			PatPatServerConfigMigrateVersion0 migrator = new PatPatServerConfigMigrateVersion0();
			migrator.setConfigFolder(this.getConfigFolder());
			migrator.setMigrateFileName(file.getName());
			migrator.setConfig(config);
			migrator.setListConfig(listConfig);
			migrator.setShouldSave(false);

			PatPatServerConfigMigrateManager migrateManager = new PatPatServerConfigMigrateManager(config, migrator);
			migrateManager.migrate();

			Assertions.assertEquals(ListMode.WHITELIST, config.getListMode());
			Assertions.assertTrue(listConfig.getUuids().isEmpty());

			Path migratedFilesPath = this.getConfigFolder().resolve("test_2/");
			Assertions.assertTrue(migratedFilesPath.toFile().mkdirs());

			File migratedConfigFile = migratedFilesPath.resolve("migrated_test_2.json5").toFile();
			config.save(migratedConfigFile);

			File migratedConfigListFile = migratedFilesPath.resolve("migrated_list_test_2.txt").toFile();
			listConfig.save(migratedConfigListFile);

			Desktop.getDesktop().open(this.getConfigFolder().toFile());
		}
	}
}
//?}
