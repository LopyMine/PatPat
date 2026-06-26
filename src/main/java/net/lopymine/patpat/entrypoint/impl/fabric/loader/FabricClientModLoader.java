//~ client_fabric_commands

package net.lopymine.patpat.entrypoint.impl.fabric.loader;

//? if fabric {

import com.mojang.brigadier.CommandDispatcher;
import java.util.function.Consumer;
import net.fabricmc.fabric.api.client.command.v2.*;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.*;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.networking.v1.*;
import net.fabricmc.fabric.api.networking.v1.*;
import net.lopymine.patpat.client.resourcepack.*;
import net.lopymine.patpat.entrypoint.loader.client.IClientModLoader;
import net.lopymine.patpat.packet.*;
import net.lopymine.patpat.utils.*;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Registry;
import net.minecraft.server.packs.PackType;
import net.minecraft.sounds.SoundEvent;

import net.fabricmc.fabric.api.resource.v1.ResourceLoader;

public class FabricClientModLoader implements IClientModLoader {

	@Override
	public void registerClientCommands(Consumer<CommandDispatcher<FabricClientCommandSource>> consumer) {
		ClientCommandRegistrationCallback.EVENT.register(
				(dispatcher, environment) -> consumer.accept(dispatcher)
		);
	}

	@Override
	public void registerAfterEntitiesRenderer(CustomRenderer renderer) {
	}

	@Override
	public void registerAfterWorldTickListener(Consumer<ClientLevel> consumer) {
		ClientTickEvents.END_LEVEL_TICK.register(consumer::accept);
	}

	@Override
	public void registerResourceReloadListener(AbstractResourceReloadListener listener) {
		ResourceLoader.get(PackType.CLIENT_RESOURCES).registerReloadListener(listener.getId(), listener);
	}

	@Override
	public void registerKeybinding(KeyMapping keybinding) {
		KeyMappingHelper.registerKeyMapping(keybinding);
	}

	@Override
	public void registerSounds(Consumer<SoundRegister> consumer) {
		consumer.accept(new FabricSoundRegister());
	}

	private static class FabricSoundRegister implements SoundRegister {

		@Override
		public SoundEvent registerSound(String id) {
			return Registry.register(
					VersionedThings.SOUND_EVENT,
					RLUtils.modId(id),
					SoundUtils.getSoundEvent(id)
			);
		}

		@Override
		public void finish() {
			// NO-OP
		}
	}

	@Override
	public void registerClientPlayerLogListener(ClientPlayerLogListener consumer) {
		ClientPlayConnectionEvents.INIT.register((handler, client) -> {
			consumer.onLog(true);
		});
		ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
			consumer.onLog(false);
		});
	}

	@Override
	public void registerOnClientStop(Runnable runnable) {
		ClientLifecycleEvents.CLIENT_STOPPING.register((client) -> runnable.run());
	}

	@Override
	public void registerClientPackets(Consumer<ClientPacketRegister> consumer) {
		consumer.accept(new FabricClientPacketRegister());
	}

	public static class FabricClientPacketRegister implements ClientPacketRegister {

		@Override
		public <P extends BasePatPatPacket<P>> void register(PatPatPacketType<P> type, PatPatClientPacketHandler<P> handler) {
			ClientPlayNetworking.registerGlobalReceiver( type.getPacketId(),
				(packet, context) -> { PacketSender responseSender = context.responseSender();
				if (packet instanceof PingPatPacket<?, ?> pingPacket) {
					pingPacket.setPacketReply(responseSender::sendPacket);
				}
				handler.handle(packet);
			});
		}
	}

	@Override
	public void sendPacketToServer(BasePatPatPacket<?> packet) {
		if (packet instanceof PongPatPacket<?> pingPong && pingPong.canPong()) {
			pingPong.pong(packet);
		} else {
			ClientPlayNetworking.send(packet);
		}
	}

}

//?}
