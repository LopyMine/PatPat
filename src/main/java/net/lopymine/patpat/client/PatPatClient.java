package net.lopymine.patpat.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.lopymine.patpat.PatPat;
import net.lopymine.patpat.entity.PatEntity;
import net.lopymine.patpat.manager.PatPatSoundManager;
import net.lopymine.patpat.packet.PatEntityS2CPacket;
import net.lopymine.patpat.utils.WorldUtils;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class PatPatClient implements ClientModInitializer {
	private static final Set<UUID> AUTHORS = Set.of(
		UUID.fromString("192e3748-12d5-4573-a8a5-479cd394a1dc"), // LopyMine
		UUID.fromString("7b829ed5-9b74-428f-9b4d-ede06975fbc1") // nikita51
	);

	private static final Map<UUID, PatEntity> PAT_ENTITIES = new HashMap<>();
	private static final Set<UUID> BANNED_PLAYERS = new HashSet<>(); // TODO

	@Nullable
	public static PatEntity getPatEntity(@NotNull LivingEntity entity) {
		return PAT_ENTITIES.get(entity.getUuid());
	}

	public static void removePatEntity(@NotNull LivingEntity entity) {
		PAT_ENTITIES.remove(entity.getUuid());
	}

	public static void removePatEntity(@NotNull PatEntity patEntity) {
		LivingEntity entity = patEntity.getEntity();
		if (entity != null) {
			removePatEntity(entity);
		}
	}

	public static PatEntity pat(@NotNull LivingEntity entity, @Nullable UUID pattingPlayerUuid) {
		UUID uuid = entity.getUuid();
		PatEntity patEntity = PAT_ENTITIES.get(uuid);
		if (patEntity == null) {
			patEntity = new PatEntity(entity, pattingPlayerUuid != null && AUTHORS.contains(pattingPlayerUuid));
			PAT_ENTITIES.put(uuid, patEntity);
		} else {
			patEntity.getAnimation().resetAnimation();
		}
		return patEntity;
	}

	@Override
	public void onInitializeClient() {
		PatPat.LOGGER.info("PatPat Client Initialized");
		PatPatSoundManager.onInitialize();

		ClientPlayNetworking.registerGlobalReceiver(PatEntityS2CPacket.TYPE, ((packet, player, responseSender) -> {
			ClientWorld clientWorld = player.clientWorld;
			if (clientWorld == null) {
				return;
			}
			if (BANNED_PLAYERS.contains(packet.getPattingPlayerUuid())) {
				return;
			}
			Entity entity = WorldUtils.getEntity(clientWorld, packet.getPatEntityUuid());
			if (!(entity instanceof LivingEntity livingEntity)) {
				return;
			}
			PatEntity patEntity = PatPatClient.pat(livingEntity, packet.getPattingPlayerUuid());
			PatPatSoundManager.playSound(patEntity, player);
		}));
	}
}
