package net.lopymine.patpat.server.packet;

import net.minecraft.entity.*;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.ChunkPos;

import net.fabricmc.fabric.api.networking.v1.*;

import net.lopymine.patpat.PatPat;
import net.lopymine.patpat.packet.*;
import net.lopymine.patpat.server.ratelimit.*;

import java.util.*;
import java.util.function.Predicate;

//? <=1.19.3 {
/*import net.minecraft.network.PacketByteBuf;
 *///?}

public class PatPatServerPacketManager {

	private PatPatServerPacketManager() {
		throw new IllegalStateException("Manager class");
	}

	private static final List<Predicate<ServerPlayerEntity>> HANDLERS = new ArrayList<>();

	public static void register() {
		HANDLERS.clear();
		HANDLERS.add(PatPatServerListManager::canPat);
		HANDLERS.add(PatPatServerRateLimitManager::canPat);

		// Register all packets
		//? >=1.20.5 {
		net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry.playC2S().register(HelloPatPatServerC2SPacket.TYPE, HelloPatPatServerC2SPacket.CODEC);
		net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry.playS2C().register(HelloPatPatPlayerS2CPacket.TYPE, HelloPatPatPlayerS2CPacket.CODEC);

		net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry.playC2S().register(PatEntityC2SPacket.TYPE, PatEntityC2SPacket.CODEC);
		net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry.playS2C().register(PatEntityS2CPacket.TYPE, PatEntityS2CPacket.CODEC);
		net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry.playS2C().register(PatEntityForReplayModS2CPacket.TYPE, PatEntityForReplayModS2CPacket.CODEC);
		//?}

		// Register hello PatPat server packet
		ServerPlayNetworking.registerGlobalReceiver(
				HelloPatPatServerC2SPacket./*? >=1.19.4 {*/TYPE/*?} else {*//*PACKET_ID*//*?}*/,
				/*? >=1.20.5 {*/(packet, context) -> { ServerPlayerEntity sender = context.player(); /*?} elif <=1.20.4 && >=1.19.4 {*//*(packet, sender, responseSender) -> {*//*?} else {*//*(server, sender, networkHandler, buf, responseSender) -> { PatEntityC2SPacket packet = new PatEntityC2SPacket(buf);*//*?}*/
					ServerPlayNetworking.send(sender, new HelloPatPatPlayerS2CPacket());
				}
		);

		// Register patting packet
		ServerPlayNetworking.registerGlobalReceiver(PatEntityC2SPacket./*? >=1.19.4 {*/TYPE/*?} else {*//*PACKET_ID*//*?}*/,
				/*? >=1.20.5 {*/(packet, context) -> { ServerPlayerEntity sender = context.player(); /*?} elif <=1.20.4 && >=1.19.4 {*//*(packet, sender, responseSender) -> {*//*?} else {*//*(server, sender, networkHandler, buf, responseSender) -> { PatEntityC2SPacket packet = new PatEntityC2SPacket(buf);*//*?}*/
					for (Predicate<ServerPlayerEntity> handler : HANDLERS) {
						if (!handler.test(sender)) {
							return;
						}
					}

					ServerWorld serverWorld = (ServerWorld) sender./*? >=1.18 {*/getWorld()/*?} else {*//*world*//*?}*/;
					Entity entity = serverWorld.getEntity(packet.getPattedEntityUuid());
					if (!(entity instanceof LivingEntity)) {
						return;
					}
					if (entity.isInvisible()) {
						PatPat.LOGGER.warn("Received packet from client, {} patted {}, but patted entity is invisible! This shouldn't happens because it should checks at client-side!", sender.getName(), entity.getName());
						return;
					}

					ChunkPos chunkPos = /*? >=1.17 {*/entity.getChunkPos()/*?} else {*//*serverWorld.getChunk(entity.getBlockPos()).getPos()*//*?}*/;
					for (ServerPlayerEntity player : PlayerLookup.tracking(serverWorld, chunkPos)) {
						if (player.equals(sender)) {
							continue;
						}

						//? >=1.19.4 {
						ServerPlayNetworking.send(player, new PatEntityS2CPacket(packet.getPattedEntityUuid(), sender.getUuid()));
						//?} else {
						/*PatEntityS2CPacket response = new PatEntityS2CPacket(packet.getPattedEntityUuid(), sender.getUuid());
						PacketByteBuf responseBuf = PacketByteBufs.create();
						response.write(responseBuf);
						ServerPlayNetworking.send(player, PatEntityS2CPacket.PACKET_ID, responseBuf);
						*///?}
					}
				});
	}
}