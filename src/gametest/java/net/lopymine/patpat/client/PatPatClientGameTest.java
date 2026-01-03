package net.lopymine.patpat.client;

import net.minecraft.client.gui.screens.worldselection.WorldCreationUiState.SelectedGameMode;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.wolf.Wolf;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.phys.Vec3;
import org.lwjgl.glfw.GLFW;

import net.fabricmc.fabric.api.client.gametest.v1.FabricClientGameTest;
import net.fabricmc.fabric.api.client.gametest.v1.context.ClientGameTestContext;
import net.fabricmc.fabric.api.client.gametest.v1.context.TestSingleplayerContext;

import net.lopymine.patpat.client.manager.PatPatClientManager;

@SuppressWarnings("UnstableApiUsage")
public class PatPatClientGameTest implements FabricClientGameTest {

	@Override
	public void runTest(ClientGameTestContext context) {

		try(TestSingleplayerContext singleplayer = context.worldBuilder().adjustSettings(worldCreationUiState -> {
			GameRules gamerules = worldCreationUiState.getGameRules();
			gamerules.set(GameRules.ADVANCE_TIME, false, null);
			gamerules.set(GameRules.ADVANCE_WEATHER, false, null);
			gamerules.set(GameRules.SPAWN_MOBS, false, null);
			gamerules.set(GameRules.SPAWN_MONSTERS, false, null);
			gamerules.set(GameRules.SPAWN_WANDERING_TRADERS, false, null);
			gamerules.set(GameRules.SPAWN_PATROLS, false, null);
			gamerules.set(GameRules.RESPAWN_RADIUS, 0, null);
			worldCreationUiState.setGameMode(SelectedGameMode.CREATIVE);
			worldCreationUiState.setGameRules(gamerules);
		}).create()) {

			String nickname = context.computeOnClient(minecraft -> {
				assert minecraft.player != null;
				return minecraft.player.nameAndId().name();
			});
			singleplayer.getServer().runOnServer(minecraftServer -> {
				minecraftServer.levelKeys().stream().filter(level -> level.identifier().toString().equals("minecraft:overworld")).findFirst().ifPresent(level -> {
					ServerPlayer player = minecraftServer.getPlayerList().getPlayer(nickname);
					assert player != null;
					ServerLevel serverLevel = player.level();
					serverLevel.noSave = true;
					Wolf entity = EntityType.WOLF.create(serverLevel, EntitySpawnReason.COMMAND);
					assert entity != null;
					entity.rotate(Rotation.CLOCKWISE_180);
					entity.tame(player);
					entity.setOrderedToSit(true);
					entity.tick();

					Vec3 pos = player.getPosition(0).add(0, 0, 2);
					entity.teleportTo(pos.x, pos.y, pos.z);
					serverLevel.addFreshEntity(entity);
				});
			});

			singleplayer.getClientWorld().waitForChunksDownload();
			context.takeScreenshot("initializing_world");
			context.getInput().holdKey(GLFW.GLFW_KEY_LEFT_SHIFT);
			context.runOnClient(minecraft -> {
				assert minecraft.player != null;
				minecraft.player.setXRot(27);
			});

			context.waitTicks(5);
			if (!detectPat(context)) {
				throw new RuntimeException("Client is not pat entity (or entity not in crosshair)");
			}
		}
		context.waitTicks(10);
		System.out.println("Ended");


	}

	public boolean detectPat(ClientGameTestContext context) {
		context.getInput().holdMouse(1);
		for (int i = 0; i < 5; i++) {
			context.waitTicks(5);
			if (!PatPatClientManager.PAT_ENTITIES.isEmpty()) {
				context.takeScreenshot("pat_entity");
				context.getInput().releaseMouse(1);
				return true;
			}
		}
		context.getInput().releaseMouse(1);
		return false;
	}
}
