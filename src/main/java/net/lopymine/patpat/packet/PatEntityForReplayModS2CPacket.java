package net.lopymine.patpat.packet;

import lombok.Getter;
import net.minecraft.network.*;
import net.minecraft.util.Identifier;

//? <=1.20.4 && >=1.19.4
/*import net.fabricmc.fabric.api.networking.v1.*;*/
//? >=1.20.5
import net.minecraft.network.packet.CustomPayload;
import net.lopymine.patpat.utils.IdentifierUtils;

import java.util.UUID;

@Getter
public class PatEntityForReplayModS2CPacket /*? >=1.20.5 {*/implements CustomPayload /*?} elif >=1.19.4 {*/ /*implements FabricPacket *//*?}*/ {

	public static final Identifier PACKET_ID = IdentifierUtils.id("pat_entity_for_replay_s2c_packet");

	//? >=1.20.5 {
	public static final Id<PatEntityForReplayModS2CPacket> TYPE = new Id<>(PACKET_ID);
	public static final net.minecraft.network.codec.PacketCodec<RegistryByteBuf, PatEntityForReplayModS2CPacket> CODEC = net.minecraft.network.packet.CustomPayload.codecOf(PatEntityForReplayModS2CPacket::write, PatEntityForReplayModS2CPacket::new);
	//?} elif >=1.19.4 {
	/*public static final PacketType<PatEntityForReplayModS2CPacket> TYPE = PacketType.create(PACKET_ID, PatEntityForReplayModS2CPacket::new);
	 *///?}

	private final UUID pattedEntityUuid;
	private final UUID whoPattedUuid;

	public PatEntityForReplayModS2CPacket(UUID pattedEntityUuid, UUID whoPattedUuid) {
		this.pattedEntityUuid = pattedEntityUuid;
		this.whoPattedUuid    = whoPattedUuid;
	}

	public PatEntityForReplayModS2CPacket(/*? if >=1.20.5 {*/RegistryByteBuf/*?} else {*//*PacketByteBuf*//*?}*/ buf) {
		this.pattedEntityUuid = buf.readUuid();
		this.whoPattedUuid    = buf.readUuid();
	}

	//? <=1.20.4 && >=1.19.4
	/*@Override*/
	public void write(/*? if >=1.20.5 {*/RegistryByteBuf/*?} else {*//*PacketByteBuf*//*?}*/ buf) {
		buf.writeUuid(this.pattedEntityUuid);
		buf.writeUuid(this.whoPattedUuid);
	}

	//? >=1.20.5 {
	@Override
	public Id<? extends CustomPayload> getId() {
		return TYPE;
	}
	//?} elif >=1.19.4 {
	/*@Override
	public PacketType<?> getType() {
		return TYPE;
	}
	*///?}
}
