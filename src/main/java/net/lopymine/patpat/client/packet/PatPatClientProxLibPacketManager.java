package net.lopymine.patpat.client.packet;

import net.minecraft.client.MinecraftClient;
import net.lopymine.patpat.client.PatPatClient;
import net.lopymine.patpat.compat.LoadedMods;
import net.lopymine.patpat.packet.s2c.PatEntityS2CPacketV2;

import java.io.*;

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
			PatPatClient.LOGGER.debug("Received ProxLib packet, ProxLib enabled: {}", PatPatClientProxLibManager.isEnabled());
			if (!PatPatClientProxLibManager.isEnabled()) {
				return;
			}
			try {
				int pattedEntityId = decodeProxyPatPacket(data);
				int whoPattedId = entity.getId();

				PatPatClientPacketManager.handlePatting(new PatEntityS2CPacketV2(pattedEntityId, whoPattedId), false);
			} catch (Exception e) {
				PatPatClient.LOGGER.debug("Failed to handle ProxLib packet from player: {}, packet id: {}, data: {}", entity.getName().getString(), id, data, e);
			}
		});
		//?}
	}

	public static void onPat(int pattedEntityId) {
		if (!LoadedMods.PROX_LIB_MOD_LOADED) {
			return;
		}
		//? if proxlib {
		try {
			int packetsCount = me.enderkill98.proxlib.client.ProxLib.sendPacket(MinecraftClient.getInstance(), PAT_PACKET_IDENTIFIER, encodeProxyPatPacket(pattedEntityId));
			PatPatClient.LOGGER.debug("Sent proxy packets ({}) to pat entity with id {}", packetsCount, pattedEntityId);
		} catch (Exception e) {
			PatPatClient.LOGGER.debug("Failed to send proxy packet, patted entity: {}, packet id: {}, data: {}", pattedEntityId, PAT_PAT_PACKETS_ID, e);
		}
		//?}
	}

	private static int decodeProxyPatPacket(byte[] data) throws IOException {
		ByteArrayInputStream array = new ByteArrayInputStream(data);
		DataInput input = new DataInputStream(array);
		int pattedEntityId = readVarInt(input);
		array.close();
		return pattedEntityId;
	}

	private static byte[] encodeProxyPatPacket(int pattedEntityId) throws IOException {
		ByteArrayOutputStream array = new ByteArrayOutputStream();
		DataOutputStream data = new DataOutputStream(array);
		writeVarInt(data, pattedEntityId);
		data.close();
		return array.toByteArray();
	}

	private static int readVarInt(DataInput input) throws IOException {
		int i = 0;
		int j = 0;

		byte b;
		do {
			b = input.readByte();
			i |= (b & 127) << j++ * 7;
			if (j > 5) {
				throw new RuntimeException("VarInt too big");
			}
		} while((b & 128) == 128);

		return i;
	}

	private static void writeVarInt(DataOutputStream data, int value) throws IOException {
		while((value & -128) != 0) {
			data.writeByte(value & 127 | 128);
			value >>>= 7;
		}

		data.writeByte(value);
	}
}
