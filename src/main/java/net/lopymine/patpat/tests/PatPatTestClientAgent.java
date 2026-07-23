package net.lopymine.patpat.tests;

import java.io.File;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.BooleanSupplier;

import com.mojang.blaze3d.platform.InputConstants;

import net.lopymine.patpat.client.config.PatPatClientConfig;
import net.lopymine.patpat.client.keybinding.*;
import net.lopymine.patpat.client.manager.PatPatClientManager;
import net.lopymine.patpat.tests.PatPatTestAgent.PatPatTestRequest;
import net.minecraft.client.*;
import net.minecraft.client.gui.screens.ConnectScreen;
import net.minecraft.client.multiplayer.ServerData;
//? if >=1.17 {
import net.minecraft.client.multiplayer.resolver.ServerAddress;
//?} else {
/*import net.minecraft.client.multiplayer.ServerAddress;
*///?}
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.Entity;

public class PatPatTestClientAgent {

	private static final int PAT_TIMEOUT_TICKS = 40;
	private static final int JOIN_ATTEMPT_TICKS = 200;
	private static final int JOIN_ATTEMPTS = 5;
	private static final int WORLD_LOAD_TIMEOUT_TICKS = 12000;
	private static final long WORLD_SEED = 0L;

	//? if >=26.1 {
	private static final String[] WORLD_SETUP_COMMANDS = {
			"gamerule advance_time false",
			"gamerule advance_weather false",
			"gamerule spawn_mobs false",
			"gamerule spawn_monsters false",
			"gamerule random_tick_speed 0",
			"gamerule send_command_feedback false",
			"time set noon",
			"weather clear"
	};
	//?} else {
	/*private static final String[] WORLD_SETUP_COMMANDS = {
			"gamerule doDaylightCycle false",
			"gamerule doWeatherCycle false",
			"gamerule doMobSpawning false",
			"gamerule randomTickSpeed 0",
			"gamerule doFireTick false",
			"gamerule sendCommandFeedback false",
			"time set noon",
			"weather clear"
	};
	*///?}

	private static PatPatTestAgent agent;
	private static PatPatTestRequest current;
	private static BooleanSupplier task;
	private static String payload;
	private static int ticks;
	private static volatile boolean abandoned;

	private PatPatTestClientAgent() {
		throw new IllegalStateException("Agent class");
	}

	public static void register() {
		if (!PatPatTestAgent.isEnabled()) {
			return;
		}
		agent = new PatPatTestAgent("CLIENT");
		agent.setDisconnectHandler(() -> abandoned = true);
	}

	public static void onClientTick() {
		if (agent == null) {
			return;
		}

		Minecraft minecraft = Minecraft.getInstance();
		if (abandoned) {
			abandoned = false;
			minecraft.stop();
			return;
		}

		if (!agent.isConnected()) {
			if (minecraft.getUser() == null) {
				return;
			}
			agent.connect(minecraft.getUser().getName());
			return;
		}

		if (task != null) {
			ticks++;
			boolean done;
			try {
				done = task.getAsBoolean();
			} catch (Exception e) {
				PatPatTestAgent.LOGGER.error("Test command {} failed", current.command(), e);
				agent.error(current, e.toString());
				task    = null;
				current = null;
				return;
			}
			if (done) {
				PatPatTestAgent.LOGGER.info("Finished command {} in {} ticks{}", current.command(), ticks, payload.isBlank() ? "" : " -> " + payload);
				agent.ok(current, payload);
				task    = null;
				current = null;
			}
			return;
		}

		PatPatTestRequest request = agent.poll();
		if (request == null) {
			return;
		}

		current = request;
		payload = "";
		ticks   = 0;
		PatPatTestAgent.LOGGER.info("Starting command {} {}", request.command(), request.argument());
		try {
			task = createTask(minecraft, request);
		} catch (Exception e) {
			PatPatTestAgent.LOGGER.error("Failed to start test command {}", request.command(), e);
			agent.error(request, e.toString());
			current = null;
		}
	}

	private static BooleanSupplier createTask(Minecraft minecraft, PatPatTestRequest request) {
		return switch (request.command()) {
			case "PREPARE" -> prepare();
			case "JOIN" -> join(minecraft, request.argument());
			case "CREATE_WORLD" -> createWorld(minecraft, request.argument().trim());
			case "SETUP_WORLD" -> runCommands(minecraft, WORLD_SETUP_COMMANDS);
			case "RUN_COMMAND" -> runCommands(minecraft, request.argument());
			case "WAIT_TICKS" -> waitTicks(Integer.parseInt(request.argument().trim()));
			case "LOOK_AT" -> lookAt(minecraft, request.argument().trim());
			case "PAT" -> pat(minecraft, request.argument().trim());
			case "WAIT_PAT" -> waitPat(minecraft, request.argument().trim());
			case "SCREENSHOT" -> screenshot(minecraft, request.argument().trim());
			case "CAMERA" -> camera(minecraft, request.argument().trim());
			case "QUIT" -> quit(minecraft);
			default -> throw new IllegalArgumentException("Unknown command: " + request.command());
		};
	}

	private static BooleanSupplier prepare() {
		PatPatClientConfig config = PatPatClientConfig.getInstance();
		config.getMainConfig().setModEnabled(true);
		config.getMultiPlayerConfig().setPatMeEnabled(true);
		config.getVisualConfig().setHidingNicknameEnabled(false);
		PatPatClientManager.clearPatEntities();
		return () -> true;
	}

	private static BooleanSupplier join(Minecraft minecraft, String address) {
		ServerAddress serverAddress = ServerAddress.parseString(address.trim());
		//? if >=1.20.2 {
		ServerData data = new ServerData("patpat-test", address.trim(), ServerData.Type.OTHER);
		//?} else {
		/*ServerData data = new ServerData("patpat-test", address.trim(), false);
		*///?}

		int[] attempt = {1};
		int[] attemptStartedAt = {0};
		startConnecting(minecraft, serverAddress, data);

		return () -> {
			if (minecraft.level != null && minecraft.player != null && minecraft.getConnection() != null) {
				return true;
			}
			if ((ticks - attemptStartedAt[0]) < JOIN_ATTEMPT_TICKS) {
				return false;
			}
			if (attempt[0] >= JOIN_ATTEMPTS) {
				throw new IllegalStateException("Failed to join %s after %d attempts".formatted(address, attempt[0]));
			}

			attempt[0]++;
			attemptStartedAt[0] = ticks;
			PatPatTestAgent.LOGGER.warn("Join attempt {} to {} timed out, retrying", attempt[0] - 1, address);
			startConnecting(minecraft, serverAddress, data);
			return false;
		};
	}

	private static void startConnecting(Minecraft minecraft, ServerAddress serverAddress, ServerData data) {
		//? if >=1.20.5 {
		ConnectScreen.startConnecting(new TitleScreenHolder().get(), minecraft, serverAddress, data, false, null);
		//?} elif >=1.20 {
		/*ConnectScreen.startConnecting(new TitleScreenHolder().get(), minecraft, serverAddress, data, false);
		*///?} elif >=1.17 {
		/*ConnectScreen.startConnecting(new TitleScreenHolder().get(), minecraft, serverAddress, data);
		*///?} else {
		/*minecraft.setScreen(new ConnectScreen(new TitleScreenHolder().get(), minecraft, data));
		*///?}
	}

	private static BooleanSupplier createWorld(Minecraft minecraft, String name) {
		//? if >=1.20.3 {
		minecraft.createWorldOpenFlows().createFreshLevel(name, createLevelSettings(name), createWorldOptions(), PatPatTestClientAgent::createFlatWorldDimensions, new TitleScreenHolder().get());
		//?} elif >=1.19.3 {
		/*minecraft.createWorldOpenFlows().createFreshLevel(name, createLevelSettings(name), createWorldOptions(), PatPatTestClientAgent::createFlatWorldDimensions);
		*///?} elif >=1.19 {
		/*net.minecraft.core.RegistryAccess.Frozen registries = net.minecraft.core.RegistryAccess.BUILTIN.get();
		minecraft.createWorldOpenFlows().createFreshLevel(name, createLevelSettings(name), registries, createFlatWorldGenSettings(registries));
		*///?} elif >=1.18.2 {
		/*net.minecraft.core.RegistryAccess.Frozen registries = net.minecraft.core.RegistryAccess.BUILTIN.get();
		minecraft.createLevel(name, createLevelSettings(name), registries, createFlatWorldGenSettings(registries));
		*///?} else {
		/*net.minecraft.core.RegistryAccess.RegistryHolder registries = net.minecraft.core.RegistryAccess.builtin();
		minecraft.createLevel(name, createLevelSettings(name), registries, createFlatWorldGenSettings(registries));
		*///?}

		return awaitWorld(minecraft);
	}

	private static net.minecraft.world.level.LevelSettings createLevelSettings(String name) {
		//? if >=26.1 {
		return new net.minecraft.world.level.LevelSettings(
				name,
				net.minecraft.world.level.GameType.CREATIVE,
				new net.minecraft.world.level.LevelSettings.DifficultySettings(net.minecraft.world.Difficulty.PEACEFUL, false, true),
				true,
				net.minecraft.world.level.WorldDataConfiguration.DEFAULT
		);
		//?} elif >=1.21.11 {
		/*return new net.minecraft.world.level.LevelSettings(
				name,
				net.minecraft.world.level.GameType.CREATIVE,
				false,
				net.minecraft.world.Difficulty.PEACEFUL,
				true,
				new net.minecraft.world.level.gamerules.GameRules(net.minecraft.world.level.WorldDataConfiguration.DEFAULT.enabledFeatures()),
				net.minecraft.world.level.WorldDataConfiguration.DEFAULT
		);
		*///?} elif >=1.21.4 {
		/*return new net.minecraft.world.level.LevelSettings(
				name,
				net.minecraft.world.level.GameType.CREATIVE,
				false,
				net.minecraft.world.Difficulty.PEACEFUL,
				true,
				new net.minecraft.world.level.GameRules(net.minecraft.world.level.WorldDataConfiguration.DEFAULT.enabledFeatures()),
				net.minecraft.world.level.WorldDataConfiguration.DEFAULT
		);
		*///?} elif >=1.19.3 {
		/*return new net.minecraft.world.level.LevelSettings(
				name,
				net.minecraft.world.level.GameType.CREATIVE,
				false,
				net.minecraft.world.Difficulty.PEACEFUL,
				true,
				new net.minecraft.world.level.GameRules(),
				net.minecraft.world.level.WorldDataConfiguration.DEFAULT
		);
		*///?} else {
		/*return new net.minecraft.world.level.LevelSettings(
				name,
				net.minecraft.world.level.GameType.CREATIVE,
				false,
				net.minecraft.world.Difficulty.PEACEFUL,
				true,
				new net.minecraft.world.level.GameRules(),
				net.minecraft.world.level.DataPackConfig.DEFAULT
		);
		*///?}
	}

	//? if >=1.19.3 {
	private static net.minecraft.world.level.levelgen.WorldOptions createWorldOptions() {
		return new net.minecraft.world.level.levelgen.WorldOptions(WORLD_SEED, false, false);
	}
	//?}

	//? if >=1.21.4 {
	private static net.minecraft.world.level.levelgen.WorldDimensions createFlatWorldDimensions(net.minecraft.core.HolderLookup.Provider registries) {
		return net.minecraft.world.level.levelgen.presets.WorldPresets.createFlatWorldDimensions(registries);
	}
	//?} elif >=1.19.3 {
	/*private static net.minecraft.world.level.levelgen.WorldDimensions createFlatWorldDimensions(net.minecraft.core.RegistryAccess registries) {
		return registries.registryOrThrow(net.minecraft.core.registries.Registries.WORLD_PRESET)
				.getOrThrow(net.minecraft.world.level.levelgen.presets.WorldPresets.FLAT)
				.createWorldDimensions();
	}
	*///?} elif >=1.19 {
	/*private static net.minecraft.world.level.levelgen.WorldGenSettings createFlatWorldGenSettings(net.minecraft.core.RegistryAccess registries) {
		return registries.registryOrThrow(net.minecraft.core.Registry.WORLD_PRESET_REGISTRY)
				.getOrThrow(net.minecraft.world.level.levelgen.presets.WorldPresets.FLAT)
				.createWorldGenSettings(WORLD_SEED, false, false);
	}
	*///?} elif >=1.18.2 {
	/*private static net.minecraft.world.level.levelgen.WorldGenSettings createFlatWorldGenSettings(net.minecraft.core.RegistryAccess registries) {
		net.minecraft.core.Registry<net.minecraft.world.level.biome.Biome> biomes = registries.registryOrThrow(net.minecraft.core.Registry.BIOME_REGISTRY);
		net.minecraft.core.Registry<net.minecraft.world.level.levelgen.structure.StructureSet> structureSets = registries.registryOrThrow(net.minecraft.core.Registry.STRUCTURE_SET_REGISTRY);
		net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorSettings flat = net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorSettings.getDefault(biomes, structureSets);

		return new net.minecraft.world.level.levelgen.WorldGenSettings(WORLD_SEED, false, false, net.minecraft.world.level.levelgen.WorldGenSettings.withOverworld(
				registries.registryOrThrow(net.minecraft.core.Registry.DIMENSION_TYPE_REGISTRY),
				net.minecraft.world.level.dimension.DimensionType.defaultDimensions(registries, WORLD_SEED),
				new net.minecraft.world.level.levelgen.FlatLevelSource(structureSets, flat)
		));
	}
	*///?} elif >=1.18 {
	/*private static net.minecraft.world.level.levelgen.WorldGenSettings createFlatWorldGenSettings(net.minecraft.core.RegistryAccess.RegistryHolder registries) {
		net.minecraft.core.Registry<net.minecraft.world.level.biome.Biome> biomes = registries.registryOrThrow(net.minecraft.core.Registry.BIOME_REGISTRY);
		net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorSettings flat = net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorSettings.getDefault(biomes);

		return new net.minecraft.world.level.levelgen.WorldGenSettings(WORLD_SEED, false, false, net.minecraft.world.level.levelgen.WorldGenSettings.withOverworld(
				registries.registryOrThrow(net.minecraft.core.Registry.DIMENSION_TYPE_REGISTRY),
				net.minecraft.world.level.dimension.DimensionType.defaultDimensions(registries, WORLD_SEED),
				new net.minecraft.world.level.levelgen.FlatLevelSource(flat)
		));
	}
	*///?} else {
	/*private static net.minecraft.world.level.levelgen.WorldGenSettings createFlatWorldGenSettings(net.minecraft.core.RegistryAccess.RegistryHolder registries) {
		net.minecraft.core.Registry<net.minecraft.world.level.dimension.DimensionType> dimensionTypes = registries.registryOrThrow(net.minecraft.core.Registry.DIMENSION_TYPE_REGISTRY);
		net.minecraft.core.Registry<net.minecraft.world.level.biome.Biome> biomes = registries.registryOrThrow(net.minecraft.core.Registry.BIOME_REGISTRY);
		net.minecraft.core.Registry<net.minecraft.world.level.levelgen.NoiseGeneratorSettings> noiseSettings = registries.registryOrThrow(net.minecraft.core.Registry.NOISE_GENERATOR_SETTINGS_REGISTRY);
		net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorSettings flat = net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorSettings.getDefault(biomes);

		return new net.minecraft.world.level.levelgen.WorldGenSettings(WORLD_SEED, false, false, net.minecraft.world.level.levelgen.WorldGenSettings.withOverworld(
				dimensionTypes,
				net.minecraft.world.level.dimension.DimensionType.defaultDimensions(dimensionTypes, biomes, noiseSettings, WORLD_SEED),
				new net.minecraft.world.level.levelgen.FlatLevelSource(flat)
		));
	}
	*///?}

	private static BooleanSupplier awaitWorld(Minecraft minecraft) {
		return () -> {
			if (ticks > WORLD_LOAD_TIMEOUT_TICKS) {
				throw new IllegalStateException("The test world was not loaded in %d ticks".formatted(WORLD_LOAD_TIMEOUT_TICKS));
			}
			MinecraftServer server = minecraft.getSingleplayerServer();
			return minecraft.level != null && minecraft.player != null && minecraft.screen == null && server != null && server.isReady();
		};
	}

	private static BooleanSupplier runCommands(Minecraft minecraft, String... commands) {
		MinecraftServer server = minecraft.getSingleplayerServer();
		if (server == null) {
			throw new IllegalStateException("Client is not in a singleplayer world");
		}

		CompletableFuture<Void> future = new CompletableFuture<>();
		server.execute(() -> {
			try {
				for (String command : commands) {
					//? if >=1.19 {
					server.getCommands().performPrefixedCommand(server.createCommandSourceStack(), command);
					//?} else {
					/*server.getCommands().performCommand(server.createCommandSourceStack(), command);
					*///?}
				}
				future.complete(null);
			} catch (Exception e) {
				future.completeExceptionally(e);
			}
		});

		return () -> {
			if (!future.isDone()) {
				return false;
			}
			future.join();
			return true;
		};
	}

	private static BooleanSupplier waitTicks(int amount) {
		return () -> ticks >= amount;
	}

	private static BooleanSupplier lookAt(Minecraft minecraft, String name) {
		LocalPlayer player = minecraft.player;
		if (player == null || minecraft.level == null) {
			throw new IllegalStateException("Client is not in a level");
		}

		Entity target = findEntity(minecraft, name);
		if (target == null) {
			throw new IllegalStateException("Entity not found: " + name);
		}

		double dx = target.getX() - player.getX();
		double dy = target.getEyeY() - player.getEyeY();
		double dz = target.getZ() - player.getZ();
		double horizontal = Math.sqrt((dx * dx) + (dz * dz));

		float yRot = (float) (Mth.atan2(dz, dx) * (180F / Math.PI)) - 90.0F;
		float xRot = (float) (-(Mth.atan2(dy, horizontal) * (180F / Math.PI)));

		//? if >=1.17 {
		player.setYRot(yRot);
		player.setXRot(xRot);
		//?} else {
		/*player.yRot = yRot;
		player.xRot = xRot;
		*///?}
		player.yHeadRot = yRot;
		player.yBodyRot = yRot;
		player.yHeadRotO = yRot;
		player.yRotO     = yRot;
		player.xRotO     = xRot;

		return () -> ticks >= 5;
	}

	private static BooleanSupplier pat(Minecraft minecraft, String target) {
		PatPatKeybinding keybinding = PatPatClientKeybindingManager.getPatKeybinding();
		keybinding.setKey(keybinding.getDefaultKey());

		KeybindingCombination combination = PatPatKeybinding.DEFAULT_COMBINATION;
		InputConstants.Key attributeKey = combination.getAttributeKey();
		InputConstants.Key key = combination.getKey();

		if (attributeKey != null) {
			keybinding.onKeyAction(attributeKey, true);
		}
		keybinding.onKeyAction(key, true);

		return () -> {
			UUID uuid = findEntityUuid(minecraft, target);
			boolean patted = uuid != null && PatPatClientManager.PAT_ENTITIES.containsKey(uuid);

			if (!patted && ticks < PAT_TIMEOUT_TICKS) {
				return false;
			}

			if (attributeKey != null) {
				keybinding.onKeyAction(attributeKey, false);
			}
			keybinding.onKeyAction(key, false);

			if (!patted) {
				throw new IllegalStateException("Pat was not registered on the sender for " + target);
			}
			return true;
		};
	}

	private static BooleanSupplier waitPat(Minecraft minecraft, String target) {
		return () -> {
			LocalPlayer player = minecraft.player;
			if (player == null) {
				return false;
			}
			UUID uuid = "SELF".equals(target) ? player.getUUID() : findEntityUuid(minecraft, target);
			return uuid != null && PatPatClientManager.PAT_ENTITIES.containsKey(uuid);
		};
	}

	private static UUID findEntityUuid(Minecraft minecraft, String name) {
		Entity entity = findEntity(minecraft, name);
		return entity == null ? null : entity.getUUID();
	}

	private static Entity findEntity(Minecraft minecraft, String name) {
		LocalPlayer player = minecraft.player;
		if (minecraft.level == null || player == null) {
			return null;
		}

		Entity nearest = null;
		double nearestDistance = Double.MAX_VALUE;

		for (Entity entity : minecraft.level.entitiesForRendering()) {
			if (entity == player || !entity.getName().getString().equals(name)) {
				continue;
			}
			double distance = entity.distanceToSqr(player);
			if (distance < nearestDistance) {
				nearest         = entity;
				nearestDistance = distance;
			}
		}
		return nearest;
	}

	private static BooleanSupplier screenshot(Minecraft minecraft, String name) {
		File file = new File(new File(minecraft.gameDirectory, "screenshots"), name + ".png");
		if (file.exists() && !file.delete()) {
			throw new IllegalStateException("Failed to delete previous screenshot: " + file);
		}

		//? if >=1.21.6 {
		Screenshot.grab(minecraft.gameDirectory, name + ".png", minecraft.getMainRenderTarget(), 1, component -> {});
		//?} elif >=1.17.1 {
		/*Screenshot.grab(minecraft.gameDirectory, name + ".png", minecraft.getMainRenderTarget(), component -> {});
		*///?} else {
		/*Screenshot.grab(minecraft.gameDirectory, name + ".png", minecraft.getWindow().getWidth(), minecraft.getWindow().getHeight(), minecraft.getMainRenderTarget(), component -> {});
		*///?}

		long[] previousSize = {-1L};
		return () -> {
			long size = file.length();
			boolean stable = size > 0L && size == previousSize[0];
			previousSize[0] = size;
			if (stable) {
				payload = file.getAbsolutePath();
			}
			return stable;
		};
	}

	private static BooleanSupplier camera(Minecraft minecraft, String type) {
		CameraType requested = CameraType.valueOf(type);
		minecraft.options.setCameraType(requested);

		return () -> {
			if (ticks < 5) {
				return false;
			}
			CameraType actual = minecraft.options.getCameraType();
			if (actual != requested) {
				throw new IllegalStateException("Camera type reverted to " + actual);
			}
			payload = actual.name();
			return true;
		};
	}

	private static BooleanSupplier quit(Minecraft minecraft) {
		minecraft.stop();
		return () -> true;
	}

	private static class TitleScreenHolder {

		public net.minecraft.client.gui.screens.Screen get() {
			return new net.minecraft.client.gui.screens.TitleScreen();
		}
	}
}
