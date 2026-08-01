//~ client_fabric_commands

package net.lopymine.patpat.entrypoint.impl.fabric.loader;

//? if fabric {

import com.mojang.brigadier.CommandDispatcher;
import java.util.function.Consumer;
//? if <1.19 {
/*import net.fabricmc.fabric.api.client.command.v1.*;
*///?} else {
import net.fabricmc.fabric.api.client.command.v2.*;
 //?}
import net.fabricmc.fabric.api.client.event.lifecycle.v1.*;
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

//? if >=1.21.9 {
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
//?}

//? if <=1.21.8 {
/*import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
*///?}

//? if <=1.19.3 {
/*import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.Identifier;
*///?}

//? if >=26.1 {
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
//?} else {
/*import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
 *///?}

public class FabricClientModLoader implements IClientModLoader {

	@Override
	public void registerClientCommands(Consumer<CommandDispatcher<FabricClientCommandSource>> consumer) {
		//? if >=1.19 {
		ClientCommandRegistrationCallback.EVENT.register(
				(dispatcher, environment) -> consumer.accept(dispatcher)
		);
		//?} else {
		/*consumer.accept(ClientCommandManager.DISPATCHER);
		 *///?}
	}

	@Override
	public void registerAfterEntitiesRenderer(CustomRenderer renderer) {
		//? if <=1.21.8 {
		/*WorldRenderEvents.AFTER_ENTITIES.register((context) -> renderer.render(context.consumers(), context.matrixStack()));
		*///?}
	}

	@Override
	public void registerAfterWorldTickListener(Consumer<ClientLevel> consumer) {
		//? if >=26.1 {
		ClientTickEvents.END_LEVEL_TICK.register(consumer::accept);
		//?} else {
		/*ClientTickEvents.END_WORLD_TICK.register(consumer::accept);
		 *///?}
	}

	@Override
	public void registerResourceReloadListener(AbstractResourceReloadListener listener) {
		//? if >=26.1 {
		ResourceLoader.get(PackType.CLIENT_RESOURCES).registerReloadListener(listener.getId(), listener);
		//?} elif >=1.21.9 {
		/*ResourceLoader.get(PackType.CLIENT_RESOURCES).registerReloader(listener.getId(), listener);
		 *///?} else {
		/*ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(listener);
		 *///?}
	}

	@Override
	public void registerKeybinding(KeyMapping keybinding) {
		//? if >=26.1 {
		KeyMappingHelper.registerKeyMapping(keybinding);
		//?} else {
		/*KeyBindingHelper.registerKeyBinding(keybinding);
		 *///?}
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
			ClientPlayNetworking.registerGlobalReceiver(/*? if >=1.19.4 {*/ type.getPacketId(), /*?} else {*//*type.getId(),*//*?}*/
				//? if >=1.20.5 {
				(packet, context) -> { PacketSender responseSender = context.responseSender();
				//?} elif <=1.20.4 && >=1.19.4 {
				/*(packet, player, responseSender) -> {
				 *///?} else {
				/*(client, listener, buf, responseSender) -> { P packet = type.getFactory().apply(buf);
				 *///?}
				if (packet instanceof PingPatPacket<?, ?> pingPacket) {
					pingPacket.setPacketReply(responseSender::sendPacket);
				}
				handler.handle(packet);
			});
		}
	}

	@Override
	public void sendPacketToServer(BasePatPatPacket<?> packet) {
		//? if <1.19.4 {
		/*Identifier id = packet.getPatPatType().getId();
		FriendlyByteBuf buf = PacketByteBufs.create();
		packet.write(buf);
		*///?}
		if (packet instanceof PongPatPacket<?> pingPong && pingPong.canPong()) {
			//? if >=1.19.4 {
			pingPong.pong(packet);
			//?} else {
			/*pingPong.pong(id, buf);
			*///?}
		} else {
			//? if >=1.19.4 {
			ClientPlayNetworking.send(packet);
			//?} else {
			/*ClientPlayNetworking.send(id, buf);
			*///?}
		}
	}

}

//?}
