//? >=1.19.4 {
package net.lopymine.patpat.config.migrate;

import net.lopymine.patpat.server.config.migrate.PatPatServerConfigMigrateVersion0;
import net.lopymine.patpat.util.FileUtils;
import org.junit.jupiter.api.*;

import net.lopymine.patpat.client.config.resourcepack.ListMode;
import net.lopymine.patpat.server.config.sub.PatPatServerPlayerListConfig;
import net.lopymine.patpat.server.config.migrate.PatPatServerConfigMigrateVersion0.Configs;

import java.io.File;
import java.util.UUID;

class MigrateTests {

	@Nested
	class V0 {

		@Test
		void test1() {
			File file = FileUtils.getResource("migrate/v0/test1.json5");
			Assertions.assertTrue(file.exists());

			Configs configs = new PatPatServerConfigMigrateVersion0().transform(file);
			Assertions.assertNotNull(configs);
			Assertions.assertEquals(ListMode.BLACKLIST, configs.config().getListMode());
			PatPatServerPlayerListConfig playerListConfig = configs.playerListConfig();
			Assertions.assertTrue(playerListConfig.contains(UUID.fromString("65adfaa1-0196-053e-07ea-1260729a2e34")));
			Assertions.assertTrue(playerListConfig.contains(UUID.fromString("a4369612-0487-0038-0339-1f1073297c22")));
		}

		@Test
		void test2() {
			File file = FileUtils.getResource("migrate/v0/test2.json5");
			Assertions.assertTrue(file.exists());

			Configs configs = new PatPatServerConfigMigrateVersion0().transform(file);
			Assertions.assertNotNull(configs);
			Assertions.assertEquals(ListMode.WHITELIST, configs.config().getListMode());
			Assertions.assertTrue(configs.playerListConfig().getUuids().isEmpty());
		}
	}
}
//?}
