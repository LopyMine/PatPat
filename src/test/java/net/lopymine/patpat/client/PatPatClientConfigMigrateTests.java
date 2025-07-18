//? >=1.19.4 {
package net.lopymine.patpat.client;

import net.minecraft.server.Bootstrap;
import net.minecraft.SharedConstants;
import org.junit.jupiter.api.*;

import net.lopymine.patpat.client.config.*;
import net.lopymine.patpat.client.config.migrate.*;
import net.lopymine.patpat.client.config.resourcepack.ListMode;
import net.lopymine.patpat.client.config.sub.*;
import net.lopymine.patpat.common.config.vector.Vec3f;
import net.lopymine.patpat.util.PathUtils;

import java.io.*;
import java.nio.file.Path;
import java.util.*;

public class PatPatClientConfigMigrateTests {

	@BeforeAll
	static void beforeAll() {
		SharedConstants.tryDetectVersion();
		Bootstrap.bootStrap();
	}

	@Nested
	class V0 {

		private Path getConfigFolder() {
			return PathUtils.getResource("client/");
		}

		@Test
		void testMigrationV0() {
			// PREPARE DATA
			File file = this.getConfigFolder().resolve("test_1.json5").toFile();
			Assertions.assertTrue(file.exists());

			PatPatClientConfig config = PatPatClientConfig.getNewInstance();
			PatPatClientMainConfig mainConfig = config.getMainConfig();
			mainConfig.setModEnabled(false);
			mainConfig.setDebugLogEnabled(false);

			PatPatClientMultiplayerConfig serverConfig = config.getMultiPlayerConfig();
			serverConfig.setListMode(ListMode.DISABLED);
			serverConfig.setBypassServerResourcePackEnabled(false);
			serverConfig.setPatMeEnabled(false);

			PatPatClientPlayerListConfig playerListConfig = new PatPatClientPlayerListConfig();

			PatPatClientVisualConfig visualConfig = config.getVisualConfig();
			visualConfig.setPatWeight(0);
			visualConfig.setAnimationOffsets(new Vec3f());
			visualConfig.setCameraShackingEnabled(false);
			visualConfig.setClientSwingHandEnabled(false);
			visualConfig.setServerSwingHandEnabled(false);
			visualConfig.setHidingNicknameEnabled(false);

			PatPatClientResourcePacksConfig resourcePacksConfig = config.getResourcePacksConfig();
			resourcePacksConfig.setSkipOldAnimationsEnabled(false);

			PatPatClientSoundsConfig soundsConfig = config.getSoundsConfig();
			soundsConfig.setSoundsVolume(0);
			soundsConfig.setSoundsEnabled(false);

			// PREPARE MIGRATION ENVIRONMENT
			PatPatClientConfigMigrateVersion0 migrator = new PatPatClientConfigMigrateVersion0();
			migrator.setConfigFolder(this.getConfigFolder());
			migrator.setMigrateFileName(file.getName());
			migrator.setShouldSave(false);
			migrator.setConfig(config);
			migrator.setPlayerListConfig(playerListConfig);

			// MIGRATE
			PatPatClientConfigMigrateManager migrateManager = new PatPatClientConfigMigrateManager(config, migrator);
			migrateManager.migrate();

			// ASSERT VALUES

			// Main config
			Assertions.assertTrue(mainConfig.isModEnabled());
			Assertions.assertTrue(mainConfig.isDebugLogEnabled());

			// Server config
			Assertions.assertTrue(serverConfig.isPatMeEnabled());
			Assertions.assertTrue(serverConfig.isBypassServerResourcePackPriorityEnabled());
			Assertions.assertEquals(ListMode.WHITELIST, serverConfig.getListMode());
			Assertions.assertEquals(Map.of(UUID.fromString("192e3748-12d5-4573-a8a5-479cd394a1dc"), "LopyMine"), playerListConfig.getMap());

			// Visual config
			Assertions.assertTrue(visualConfig.isCameraShackingEnabled());
			Assertions.assertTrue(visualConfig.isClientSwingHandEnabled());
			Assertions.assertTrue(visualConfig.isServerSwingHandEnabled());
			Assertions.assertTrue(visualConfig.isHidingNicknameEnabled());
			Assertions.assertEquals(1.234F, visualConfig.getPatWeight());
			Assertions.assertEquals(new Vec3f(1.0F, 1.2F, 1.23F), config.getVisualConfig().getAnimationOffsets());

			// Resource packs config
			Assertions.assertTrue(resourcePacksConfig.isSkipOldAnimationsEnabled());

			// Sounds Config
			Assertions.assertEquals(1.2346F, soundsConfig.getSoundsVolume());
			Assertions.assertTrue(soundsConfig.isSoundsEnabled());
		}
	}
}
//?}
