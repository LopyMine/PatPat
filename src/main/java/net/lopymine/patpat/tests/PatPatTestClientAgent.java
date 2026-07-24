package net.lopymine.patpat.tests;

import java.io.File;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.BooleanSupplier;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.platform.InputConstants.Key;
import org.lwjgl.glfw.GLFW;

import net.lopymine.patpat.client.config.PatPatClientConfig;
import net.lopymine.patpat.client.keybinding.*;
import net.lopymine.patpat.client.manager.PatPatClientManager;
import net.lopymine.patpat.mixin.tests.*;
import net.lopymine.patpat.tests.PatPatTestAgent.PatPatTestRequest;
import net.minecraft.client.*;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.components.events.*;
import net.minecraft.client.gui.screens.ConnectScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens./*? if >=1.21 {*/options./*?}*/controls.KeyBindsScreen;
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
	private static final int SCROLL_TIMEOUT_TICKS = 400;
	private static final int LIST_TOP_PADDING = 40;
	private static final int LIST_BOTTOM_PADDING = 32;
	private static final int ROW_HEIGHT = 20;

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
			case "OPEN_KEY_BINDS" -> openKeyBinds(minecraft);
			case "SCROLL_TO_KEYBINDING" -> scrollToKeybinding(minecraft);
			case "CLICK_KEYBINDING" -> clickKeybinding(minecraft);
			case "PRESS_KEYS" -> pressKeys(minecraft, request.argument().trim());
			case "KEYBINDING" -> keybinding();
			case "ENABLE_SHADERS" -> enableShaders();
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
		boolean[] created = {false};

		return () -> {
			if (ticks > WORLD_LOAD_TIMEOUT_TICKS) {
				throw new IllegalStateException("The test world was not loaded in %d ticks".formatted(WORLD_LOAD_TIMEOUT_TICKS));
			}
			if (!created[0]) {
				if (minecraft.getOverlay() != null) {
					return false;
				}
				created[0] = true;
				createFreshLevel(minecraft, name);
			}
			return isWorldReady(minecraft);
		};
	}

	private static void createFreshLevel(Minecraft minecraft, String name) {
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

	private static boolean isWorldReady(Minecraft minecraft) {
		MinecraftServer server = minecraft.getSingleplayerServer();
		return minecraft.level != null && minecraft.player != null && minecraft.screen == null && server != null && server.isReady();
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

	private static BooleanSupplier openKeyBinds(Minecraft minecraft) {
		return () -> {
			if (minecraft.getOverlay() != null) {
				return false;
			}
			if (!(minecraft.screen instanceof KeyBindsScreen)) {
				minecraft.setScreen(new KeyBindsScreen(minecraft.screen, minecraft.options));
				return false;
			}
			return findKeybindingButton(minecraft) != null;
		};
	}

	private static BooleanSupplier scrollToKeybinding(Minecraft minecraft) {
		return () -> {
			AbstractSelectionList<?> list = findKeyBindsList(minecraft);
			int index = list == null ? -1 : findKeybindingIndex(list);
			if (index == -1) {
				if (ticks > SCROLL_TIMEOUT_TICKS) {
					throw new IllegalStateException("The PatPat keybinding entry was not found in the key binds list");
				}
				return false;
			}

			int top = LIST_TOP_PADDING;
			int bottom = minecraft.getWindow().getGuiScaledHeight() - LIST_BOTTOM_PADDING;
			int rowTop = ((AbstractSelectionListInvoker) list).invokeGetRowTop(index);

			if (rowTop >= top && (rowTop + ROW_HEIGHT) <= bottom) {
				payload = "%d".formatted(rowTop);
				return true;
			}
			if (ticks > SCROLL_TIMEOUT_TICKS) {
				throw new IllegalStateException("Failed to scroll the key binds list to the PatPat keybinding, the row is at %d".formatted(rowTop));
			}

			moveMouse(minecraft, minecraft.getWindow().getGuiScaledWidth() / 2.0D, minecraft.getWindow().getGuiScaledHeight() / 2.0D);
			scroll(minecraft, rowTop < top ? 5.0D : -5.0D);
			return false;
		};
	}

	private static AbstractSelectionList<?> findKeyBindsList(Minecraft minecraft) {
		Screen screen = minecraft.screen;
		if (screen == null) {
			return null;
		}
		for (GuiEventListener child : screen.children()) {
			if (child instanceof AbstractSelectionList<?> list) {
				return list;
			}
		}
		return null;
	}

	private static int findKeybindingIndex(AbstractSelectionList<?> list) {
		List<?> entries = list.children();
		for (int index = 0; index < entries.size(); index++) {
			if (entries.get(index) instanceof KeyEntryAccessor entry && entry.getKeyMapping() instanceof PatPatKeybinding) {
				return index;
			}
		}
		return -1;
	}

	private static BooleanSupplier clickKeybinding(Minecraft minecraft) {
		int[] index = {0};
		return () -> {
			if (index[0] == 0) {
				Button button = awaitKeybindingButton(minecraft);
				if (button == null) {
					return false;
				}
				moveMouse(minecraft, getButtonX(button) + (button.getWidth() / 2.0D), getButtonY(button) + (button.getHeight() / 2.0D));
				index[0]++;
				return false;
			}
			if (index[0] == 1) {
				click(minecraft, GLFW.GLFW_PRESS);
				index[0]++;
				return false;
			}
			if (index[0] == 2) {
				click(minecraft, GLFW.GLFW_RELEASE);
				index[0]++;
				return false;
			}
			PatPatKeybinding keybinding = PatPatClientKeybindingManager.getPatKeybinding();
			if (!(minecraft.screen instanceof KeyBindsScreen screen) || screen.selectedKey != keybinding) {
				throw new IllegalStateException("The PatPat keybinding was not selected by the click");
			}
			return ticks >= 5;
		};
	}

	private static BooleanSupplier pressKeys(Minecraft minecraft, String argument) {
		List<Key> keys = new ArrayList<>();
		for (String name : argument.split("\\+")) {
			keys.add(InputConstants.getKey("key.keyboard." + name.trim().toLowerCase(Locale.ROOT).replace('_', '.')));
		}

		List<Runnable> steps = new ArrayList<>();
		for (Key key : keys) {
			steps.add(() -> sendKey(minecraft, key, GLFW.GLFW_PRESS));
		}
		for (int i = keys.size() - 1; i >= 0; i--) {
			Key key = keys.get(i);
			steps.add(() -> sendKey(minecraft, key, GLFW.GLFW_RELEASE));
		}

		int[] index = {0};
		return () -> {
			if (index[0] >= steps.size()) {
				return true;
			}
			steps.get(index[0]++).run();
			return false;
		};
	}

	private static BooleanSupplier enableShaders() {
		payload = net.lopymine.patpat.compat.iris.IrisShaderController.enableFirstShaderPack();
		return () -> ticks >= 20;
	}

	private static BooleanSupplier keybinding() {
		KeybindingCombination combination = PatPatClientKeybindingManager.getPatKeybinding().getCombination();
		Key attributeKey = combination.getAttributeKey();
		Key key = combination.getKey();

		payload = "%s+%s".formatted(attributeKey == null ? "none" : attributeKey.getName(), key == null ? "none" : key.getName());
		return () -> true;
	}

	private static Button awaitKeybindingButton(Minecraft minecraft) {
		Button button = findKeybindingButton(minecraft);
		if (button == null && ticks > SCROLL_TIMEOUT_TICKS) {
			throw new IllegalStateException("The PatPat keybinding entry was not found in the key binds list");
		}
		return button;
	}

	private static Button findKeybindingButton(Minecraft minecraft) {
		Screen screen = minecraft.screen;
		return screen == null ? null : findKeybindingButton(screen.children());
	}

	private static Button findKeybindingButton(List<? extends GuiEventListener> children) {
		for (GuiEventListener child : children) {
			if (child instanceof KeyEntryAccessor entry && entry.getKeyMapping() instanceof PatPatKeybinding) {
				return entry.getChangeButton();
			}
			if (child instanceof ContainerEventHandler container) {
				Button button = findKeybindingButton(container.children());
				if (button != null) {
					return button;
				}
			}
		}
		return null;
	}

	private static int getButtonX(Button button) {
		//? if >=1.19.3 {
		return button.getX();
		//?} else {
		/*return button.x;
		*///?}
	}

	private static int getButtonY(Button button) {
		//? if >=1.19.3 {
		return button.getY();
		//?} else {
		/*return button.y;
		*///?}
	}

	private static void moveMouse(Minecraft minecraft, double guiX, double guiY) {
		double x = (guiX * minecraft.getWindow().getScreenWidth()) / minecraft.getWindow().getGuiScaledWidth();
		double y = (guiY * minecraft.getWindow().getScreenHeight()) / minecraft.getWindow().getGuiScaledHeight();

		PatPatTestMode.runSyntheticInput(() -> ((MouseHandlerInvoker) minecraft.mouseHandler).invokeOnMove(getWindowHandle(minecraft), x, y));
	}

	private static void scroll(Minecraft minecraft, double amount) {
		PatPatTestMode.runSyntheticInput(() -> ((MouseHandlerInvoker) minecraft.mouseHandler).invokeOnScroll(getWindowHandle(minecraft), 0.0D, amount));
	}

	private static void click(Minecraft minecraft, int action) {
		PatPatTestMode.runSyntheticInput(() -> {
			//? if >=1.21.9 {
			((MouseHandlerInvoker) minecraft.mouseHandler).invokeOnButton(getWindowHandle(minecraft), new net.minecraft.client.input.MouseButtonInfo(GLFW.GLFW_MOUSE_BUTTON_LEFT, 0), action);
			//?} else {
			/*((MouseHandlerInvoker) minecraft.mouseHandler).invokeOnPress(getWindowHandle(minecraft), GLFW.GLFW_MOUSE_BUTTON_LEFT, action, 0);
			*///?}
		});
	}

	private static void sendKey(Minecraft minecraft, Key key, int action) {
		PatPatTestMode.runSyntheticInput(() -> {
			//? if >=1.21.9 {
			((KeyboardHandlerInvoker) minecraft.keyboardHandler).invokeKeyPress(getWindowHandle(minecraft), action, new net.minecraft.client.input.KeyEvent(key.getValue(), 0, 0));
			//?} else {
			/*((KeyboardHandlerInvoker) minecraft.keyboardHandler).invokeKeyPress(getWindowHandle(minecraft), key.getValue(), 0, action, 0);
			*///?}
		});
	}

	private static long getWindowHandle(Minecraft minecraft) {
		return minecraft.getWindow()/*? if <=1.21.8 {*//*.getWindow()*//*?} else {*/.handle()/*?}*/;
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
