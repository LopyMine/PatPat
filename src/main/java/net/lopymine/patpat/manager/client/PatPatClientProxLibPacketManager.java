package net.lopymine.patpat.manager.client;

import net.minecraft.client.MinecraftClient;

import net.lopymine.patpat.client.PatPatClient;
import net.lopymine.patpat.compat.LoadedMods;

import java.io.*;
import java.util.UUID;

public class PatPatClientProxLibPacketManager {

	public static final int PAT_PAT_PACKETS_ID = 2;

	//? if proxlib {
	public static final me.enderkill98.proxlib.ProxPacketIdentifier PAT_PACKET_IDENTIFIER = me.enderkill98.proxlib.ProxPacketIdentifier.of(PAT_PAT_PACKETS_ID, 0);
	//?}

	public static void register() {
		if (!LoadedMods.PROX_LIB_MOD_LOADED) {
			return;
		}
		//? if proxlib {
		me.enderkill98.proxlib.client.ProxLib.addHandlerFor(PAT_PACKET_IDENTIFIER, (entity, id, data) -> {
			PatPatClient.LOGGER.debug("Received proxy packet, proxy enabled: {}", PatPatClientProxLibManager.isEnabled());
			if (!PatPatClientProxLibManager.isEnabled()) {
				return;
			}
			try {
				UUID pattedEntityUuid = decodeProxyPatPacket(data);
				UUID whoPattedUuid = entity.getUuid();

				PatPatClientPacketManager.handlePatting(whoPattedUuid, pattedEntityUuid, false);
			} catch (Exception e) {
				PatPatClient.LOGGER.debug("Failed to handle proxy packet from player: {}, packet id: {}, data: {}", entity.getName().getString(), id, data, e);
			}
		});
		//?}
	}

	private static UUID decodeProxyPatPacket(byte[] data) throws IOException {
		ByteArrayInputStream array = new ByteArrayInputStream(data);
		DataInput input = new DataInputStream(array);
		UUID pattedEntityUuid = new UUID(input.readLong(), input.readLong());
		array.close();
		return pattedEntityUuid;
	}

	private static byte[] encodeProxyPatPacket(UUID pattedEntityUuid) throws IOException {
		ByteArrayOutputStream array = new ByteArrayOutputStream();
		DataOutputStream data = new DataOutputStream(array);
		data.writeLong(pattedEntityUuid.getMostSignificantBits());
		data.writeLong(pattedEntityUuid.getLeastSignificantBits());
		data.close();
		return array.toByteArray();
	}

	public static void onPat(UUID pattedEntityUuid) {
		if (!LoadedMods.PROX_LIB_MOD_LOADED) {
			return;
		}
		//? if proxlib {
		try {
			me.enderkill98.proxlib.client.ProxLib.sendPacket(MinecraftClient.getInstance(), PAT_PACKET_IDENTIFIER, encodeProxyPatPacket(pattedEntityUuid));
		} catch (Exception e) {
			PatPatClient.LOGGER.debug("Failed to send proxy packet, patted entity: {}, packet id: {}, data: {}", pattedEntityUuid, PAT_PAT_PACKETS_ID, e);
		}
		//?}
	}
}
