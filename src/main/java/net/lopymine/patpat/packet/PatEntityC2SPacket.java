package net.lopymine.patpat.packet;

import lombok.Getter;
import net.minecraft.entity.Entity;
import net.minecraft.network.*;
import net.minecraft.util.Identifier;

//? <=1.20.4 && >=1.19.4
/*import net.fabricmc.fabric.api.networking.v1.*;*/
//? >=1.20.5
import net.minecraft.network.packet.CustomPayload;
import net.lopymine.patpat.utils.IdentifierUtils;

import java.util.UUID;

@Getter
public class PatEntityC2SPacket /*? >=1.20.5 {*/ implements CustomPayload /*?} elif >=1.19.4 {*/ /*implements FabricPacket *//*?}*/ {

	public static final Identifier PACKET_ID = IdentifierUtils.id("pat_entity_c2s_packet");

	//? >=1.20.5 {
	public static final Id<PatEntityC2SPacket> TYPE = new Id<>(PACKET_ID);
	public static final net.minecraft.network.codec.PacketCodec<RegistryByteBuf, PatEntityC2SPacket> CODEC = net.minecraft.network.packet.CustomPayload.codecOf(PatEntityC2SPacket::write, PatEntityC2SPacket::new);
	//?} elif >=1.19.4 {
	/*public static final PacketType<PatEntityC2SPacket> TYPE = PacketType.create(PACKET_ID, PatEntityC2SPacket::new);
	*///?}

	private final UUID pattedEntityUuid;

	public PatEntityC2SPacket(Entity pattedEntity) {
		this.pattedEntityUuid = pattedEntity.getUuid();
	}

	public PatEntityC2SPacket(/*? if >=1.20.5 {*/RegistryByteBuf/*} else {*//*PacketByteBuf*//*}*/ buf) {
		this.pattedEntityUuid = buf.readUuid();
	}

	//? <=1.20.4 && >=1.19.4
	/*@Override*/
	public void write(/*? if >=1.20.5 {*/RegistryByteBuf/*} else {*//*PacketByteBuf*//*}*/ buf) {
		buf.writeUuid(this.pattedEntityUuid);
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
