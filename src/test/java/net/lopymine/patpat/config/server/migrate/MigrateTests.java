//? >=1.21.1 {
package net.lopymine.patpat.config.server.migrate;

import org.junit.jupiter.api.*;

import net.lopymine.patpat.config.resourcepack.ListMode;
import net.lopymine.patpat.config.server.PlayerListConfig;
import net.lopymine.patpat.config.server.migrate.MigrateVersion0.Configs;

import java.io.File;
import java.net.URL;
import java.util.UUID;

class MigrateTests {

	@Nested
	class V0 {

		@Test
		void test1() {
			ClassLoader classLoader = getClass().getClassLoader();
			URL resource = classLoader.getResource("migrate/v0/test1.json5");
			Assertions.assertNotNull(resource);
			File file = new File(resource.getFile());
			Assertions.assertTrue(file.exists());

			Configs configs = new MigrateVersion0().transform(file);
			Assertions.assertNotNull(configs);
			Assertions.assertEquals(ListMode.BLACKLIST, configs.config().getListMode());
			PlayerListConfig playerListConfig = configs.playerListConfig();
			Assertions.assertTrue(playerListConfig.contains(UUID.fromString("65adfaa1-0196-053e-07ea-1260729a2e34")));
			Assertions.assertTrue(playerListConfig.contains(UUID.fromString("a4369612-0487-0038-0339-1f1073297c22")));
		}

		@Test
		void test2() {
			ClassLoader classLoader = getClass().getClassLoader();
			URL resource = classLoader.getResource("migrate/v0/test2.json5");
			Assertions.assertNotNull(resource);
			File file = new File(resource.getFile());
			Assertions.assertTrue(file.exists());

			Configs configs = new MigrateVersion0().transform(file);
			Assertions.assertNotNull(configs);
			Assertions.assertEquals(ListMode.WHITELIST, configs.config().getListMode());
			Assertions.assertTrue(configs.playerListConfig().getUuids().isEmpty());
		}
	}
}
//?}
