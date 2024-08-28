package net.lopymine.patpat.manager.server;

import net.minecraft.entity.*;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.ChunkPos;

import com.mojang.authlib.GameProfile;
import net.fabricmc.fabric.api.networking.v1.*;

import net.lopymine.patpat.PatPat;
import net.lopymine.patpat.client.PatPatClient;
import net.lopymine.patpat.config.resourcepack.ListMode;
import net.lopymine.patpat.config.server.PatPatServerConfig;
import net.lopymine.patpat.packet.*;

//? <=1.19.3 {
/*import net.minecraft.network.PacketByteBuf;*/
//?}

public class PatPatServerPacketManager {

	private PatPatServerPacketManager() {
		throw new IllegalStateException("Manager class");
	}

	public static void register() {
		//? >=1.20.5 {
		net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry.playC2S().register(PatEntityC2SPacket.TYPE, PatEntityC2SPacket.CODEC);
		net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry.playS2C().register(PatEntityS2CPacket.TYPE, PatEntityS2CPacket.CODEC);
		net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry.playS2C().register(PatEntityForReplayModS2CPacket.TYPE, PatEntityForReplayModS2CPacket.CODEC);
		//?}

		ServerPlayNetworking.registerGlobalReceiver(PatEntityC2SPacket./*? >=1.19.4 {*/TYPE/*?} else {*//*PACKET_ID*//*?}*/,
				/*? >=1.20.5 {*/(packet, context) -> {
					ServerPlayerEntity sender = context.player();/*?} elif <=1.20.4 && >=1.19.4 {*//*(packet, sender, responseSender) -> {*//*?} else {*//*(server, sender, handler, buf, responseSender) -> { PatEntityC2SPacket packet = new PatEntityC2SPacket(buf);*//*?}*/
					PatPatServerConfig config = PatPat.getConfig();
					GameProfile senderProfile = sender.getGameProfile();
					if ((config.getListMode() == ListMode.WHITELIST && !config.getList().containsKey(senderProfile.getId())) || (config.getListMode() == ListMode.BLACKLIST && config.getList().containsKey(senderProfile.getId()))) {
						return;
					}
					ServerWorld serverWorld = (ServerWorld) sender./*? >=1.18 {*/getWorld()/*?} else {*//*world*//*?}*/;
					Entity entity = serverWorld.getEntity(packet.getPattedEntityUuid());
					if (!(entity instanceof LivingEntity)) {
						return;
					}
					if (entity.isInvisible()) {
						PatPat.warn("Received packet from client, {} patted {}, but patted entity is invisible! This shouldn't happens because it should checks at client-side!", sender.getName(), entity.getName());
						return;
					}

					ChunkPos chunkPos = /*? >=1.17 {*/entity.getChunkPos()/*?} else {*//*serverWorld.getChunk(entity.getBlockPos()).getPos()*//*?}*/;
					for (ServerPlayerEntity player : PlayerLookup.tracking(serverWorld, chunkPos)) {
						if (player.equals(sender)) {
							continue;
						}

						//? >=1.19.4 {
						ServerPlayNetworking.send(player, new PatEntityS2CPacket(packet.getPattedEntityUuid(), senderProfile.getId()));
						//?} else {
						/*PatEntityS2CPacket response = new PatEntityS2CPacket(packet.getPattedEntityUuid(), senderProfile.getId());
						PacketByteBuf responseBuf = PacketByteBufs.create();
						response.write(responseBuf);
						ServerPlayNetworking.send(player, PatEntityS2CPacket.PACKET_ID, responseBuf);
						*///?}
					}
				});
	}
}