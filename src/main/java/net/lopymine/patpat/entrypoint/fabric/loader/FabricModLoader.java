package net.lopymine.patpat.entrypoint.fabric.loader;

//? if fabric {

/*import com.mojang.brigadier.CommandDispatcher;
import java.util.function.Consumer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.*;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.*;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.*;
import net.lopymine.patpat.client.resourcepack.*;
import net.lopymine.patpat.entrypoint.loader.client.IClientModLoader;
import net.lopymine.patpat.packet.*;
import net.lopymine.patpat.utils.*;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.Registry;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.PackType;
import net.minecraft.sounds.SoundEvent;

//? if >=1.21.9 {
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
//?}

//? if <=1.21.8 {
/^import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents;
^///?}

public class FabricModLoader extends FabricSillyModLoader implements IClientModLoader {

	@Override
	public void registerServerCommands(Consumer<CommandDispatcher<CommandSourceStack>> consumer) {
		//? if >=1.19 {
		CommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess, environment) -> {
			consumer.accept(dispatcher);
		}));
		//?} else {
		/^CommandRegistrationCallback.EVENT.register(((dispatcher, dedicated) -> {
			consumer.accept(dispatcher);
		}));
		^///?}
	}


	@Override
	public void registerAfterEntitiesRenderer(CustomRenderer renderer) {
		//? if <=1.21.8 {
		/^WorldRenderEvents.AFTER_ENTITIES.register((context) -> renderer.render(context.consumers(), context.matrices()));
		^///?}
	}

	@Override
	public void registerAfterWorldTickListener(Consumer<ClientLevel> consumer) {
		ClientTickEvents.END_WORLD_TICK.register(consumer::accept);
	}

	@Override
	public void registerResourceReloadListener(AbstractResourceReloadListener listener) {
		//? if >=1.21.9 {
		ResourceLoader.get(PackType.CLIENT_RESOURCES).registerReloader(listener.getId(), listener);
		//?} else {
		/^ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(listener);
		 ^///?}
	}

	@Override
	public void registerKeybinding(KeyMapping keybinding) {
		KeyBindingHelper.registerKeyBinding(keybinding);
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
	public void registerServerPlayerLogListener(ServerPlayerLogListener consumer) {
		ServerPlayConnectionEvents.INIT.register((handler, server) -> { ServerPlayer player = /^? if >=1.21 {^/ handler.getPlayer() /^?} else {^/ /^handler.player ^//^?}^/;
			consumer.onLog(true, player);
		});
		ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> { ServerPlayer player = /^? if >=1.21 {^/ handler.getPlayer() /^?} else {^/ /^handler.player ^//^?}^/;
			consumer.onLog(false, player);
		});
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
	public void registerOnServerStart(Runnable runnable) {
		ServerLifecycleEvents.SERVER_STARTED.register((server) -> runnable.run());
	}

	@Override
	public void registerOnServerStop(Runnable runnable) {
		ServerLifecycleEvents.SERVER_STOPPING.register((server) -> runnable.run());
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
			ClientPlayNetworking.registerGlobalReceiver(/^? if >=1.19.4 {^/ type.getPacketId(), /^?} else {^//^type.getId(),^//^?}^/
				//? if >=1.20.5 {
				(packet, context) -> { PacketSender responseSender = context.responseSender();
				//?} elif <=1.20.4 && >=1.19.4 {
				/^(packet, player, responseSender) -> {
				 ^///?} else {
				/^(client, handler, buf, responseSender) -> { P packet = id.getFactory().apply(buf);
				 ^///?}
				if (packet instanceof PingPatPacket<?, ?> pingPacket) {
					pingPacket.setPacketReply(responseSender::sendPacket);
				}
				handler.handle(packet);
			});
		}
	}

	@Override
	public void registerServerPackets(Consumer<ServerPacketRegister> consumer) {
		consumer.accept(new FabricServerPacketRegister());
	}

	public static class FabricServerPacketRegister implements ServerPacketRegister {

		@Override
		public <P extends BasePatPatPacket<P>> void register(PatPatPacketType<P> type, PacketRegistrationSide registrationSide, PatPatServerPacketHandler<P> handler) {
			//? if >=1.20.5 {
			if (registrationSide == PacketRegistrationSide.C2S || registrationSide == PacketRegistrationSide.BOTH) {
				PayloadTypeRegistry.playC2S().register(type.getPacketId(), type.getCodec());
			}
			if (registrationSide == PacketRegistrationSide.S2C || registrationSide == PacketRegistrationSide.BOTH) {
				PayloadTypeRegistry.playS2C().register(type.getPacketId(), type.getCodec());
			}
			//?}
			
			if (registrationSide != PacketRegistrationSide.C2S && registrationSide != PacketRegistrationSide.BOTH) {
				return;
			}

			ServerPlayNetworking.registerGlobalReceiver(/^? if >=1.19.4 {^/ type.getPacketId(), /^?} else {^/ /^type.getId(), ^//^?}^/
				//? if >=1.20.5 {
				(packet, context) -> { ServerPlayer sender = context.player(); PacketSender responseSender = context.responseSender();
				//?} elif >=1.19.4 && <=1.20.4 {
				/^(packet, sender, responseSender) -> {
				 ^///?} else {
				/^(server, sender, networkHandler, buf, responseSender) -> { T packet = id.getFactory().apply(buf);
				 ^///?}
					if (packet instanceof PingPatPacket<?, ?> pingPacket) {
						pingPacket.setPacketReply(responseSender::sendPacket);
					}
					handler.handle(sender, packet);
				});
		}
	}

	@Override
	public void sendPacketToPlayer(ServerPlayer player, BasePatPatPacket<?> packet) {
		//? if <1.19.4 {
		/^ResourceLocation id = packet.getPatPatType().getId();
		FriendlyByteBuf buf = PacketByteBufs.create();
		packet.write(buf);
		^///?}
		if (packet instanceof PongPatPacket<?> pingPong && pingPong.canPong()) {
			//? if >=1.19.4 {
			pingPong.pong(packet);
			//?} else {
			/^pingPong.pong(id, buf);
			 ^///?}
		} else {
			//? if >=1.19.4 {
			ServerPlayNetworking.send(player, packet);
			//?} else {
			/^ServerPlayNetworking.send(player, id, buf);
			 ^///?}
		}
	}

	@Override
	public void sendPacketToServer(BasePatPatPacket<?> packet) {
		//? if <1.19.4 {
		/^ResourceLocation id = packet.getPatPatType().getId();
		FriendlyByteBuf buf = PacketByteBufs.create();
		packet.write(buf);
		^///?}
		if (packet instanceof PongPatPacket<?> pingPong && pingPong.canPong()) {
			//? if >=1.19.4 {
			pingPong.pong(packet);
			//?} else {
			/^pingPong.pong(id, buf);
			^///?}
		} else {
			//? if >=1.19.4 {
			ClientPlayNetworking.send(packet);
			//?} else {
			/^ClientPlayNetworking.send(id, buf);
			^///?}
		}
	}

}

*///?}
