package net.lopymine.patpat.entrypoint.neoforge.loader;

//? if neoforge {
/*import com.mojang.brigadier.CommandDispatcher;

import java.util.function.Consumer;
import net.lopymine.patpat.PatPat;
import net.lopymine.patpat.client.resourcepack.AbstractResourceReloadListener;
import net.lopymine.patpat.entrypoint.loader.client.IClientModLoader;
import net.lopymine.patpat.entrypoint.neoforge.*;
import net.lopymine.patpat.packet.*;
import net.lopymine.patpat.utils.*;
import net.minecraft.client.*;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.event.lifecycle.PatPatForgeClientStoppingEvent;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import net.neoforged.neoforge.client.network.event.RegisterClientPayloadHandlersEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.server.*;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.registries.DeferredRegister;

public class NeoForgeModLoader implements IClientModLoader {

	@Override
	public void registerOnClientStop(Runnable runnable) {
		NeoForge.EVENT_BUS.addListener(PatPatForgeClientStoppingEvent.class, event -> runnable.run());
	}

	@Override
	public void registerClientCommands(Consumer<CommandDispatcher<CommandSourceStack>> consumer) {
		NeoForge.EVENT_BUS.addListener(RegisterClientCommandsEvent.class, (event) -> consumer.accept(event.getDispatcher()));
	}

	@Override
	public void registerServerCommands(Consumer<CommandDispatcher<CommandSourceStack>> consumer) {
		NeoForge.EVENT_BUS.addListener(RegisterCommandsEvent.class, (event) -> consumer.accept(event.getDispatcher()));
	}

	@Override
	public void registerAfterEntitiesRenderer(CustomRenderer renderer) {
		//? if <=1.21.8 {
		NeoForge.EVENT_BUS.addListener(Post.class, (p) -> renderer.render(p.getMultiBufferSource(), p.getPoseStack()));
		 //?}
	}

	public void registerAfterWorldTickListener(Consumer<ClientLevel> runnable) {
		NeoForge.EVENT_BUS.addListener(ClientTickEvent.Post.class, (p) -> {
			ClientLevel level = Minecraft.getInstance().level;
			if (level == null) {
				return;
			}
			runnable.accept(level);
		});
	}

	@Override
	public void registerResourceReloadListener(AbstractResourceReloadListener listener) {
		NeoForgeClientEntrypoint.getEventBus().addListener(AddClientReloadListenersEvent.class, (e) -> e.addListener(listener.getId(), listener));
	}

	@Override
	public void registerKeybinding(KeyMapping keybinding) {
		NeoForgeClientEntrypoint.getEventBus().addListener(RegisterKeyMappingsEvent.class, (event) -> event.register(keybinding));
	}

	@Override
	public void registerSounds(Consumer<SoundRegister> consumer) {
		NeoForgeSoundRegister register = new NeoForgeSoundRegister();
		consumer.accept(register);
		register.finish();
	}

	public static class NeoForgeSoundRegister implements SoundRegister {

		private final DeferredRegister<SoundEvent> register;

		public NeoForgeSoundRegister() {
			this.register = DeferredRegister.create(VersionedThings.SOUND_EVENT.key(), PatPat.MOD_ID);
		}

		public SoundEvent registerSound(String id) {
			SoundEvent event = SoundUtils.getSoundEvent(id);
			this.register.register(id, () -> event);
			return event;
		}

		public void finish() {
			this.register.register(NeoForgeClientEntrypoint.getEventBus());
		}

	}

	@Override
	public void registerServerPlayerLogListener(ServerPlayerLogListener consumer) {
		NeoForge.EVENT_BUS.addListener(PlayerEvent.PlayerLoggedOutEvent.class, event -> consumer.onLog(false, event.getEntity()));
		NeoForge.EVENT_BUS.addListener(PlayerEvent.PlayerLoggedInEvent.class, event -> consumer.onLog(true, event.getEntity()));
	}

	@Override
	public void registerClientPlayerLogListener(ClientPlayerLogListener consumer) {
		NeoForge.EVENT_BUS.addListener(ClientPlayerNetworkEvent.LoggingOut.class, (e) -> consumer.onLog(false));
		NeoForge.EVENT_BUS.addListener(ClientPlayerNetworkEvent.LoggingIn.class, (e) -> consumer.onLog(true));
	}

	@Override
	public void registerOnServerStart(Runnable runnable) {
		NeoForge.EVENT_BUS.addListener(ServerStartedEvent.class, (s) -> runnable.run());
	}

	@Override
	public void registerOnServerStop(Runnable runnable) {
		NeoForge.EVENT_BUS.addListener(ServerStoppingEvent.class, (s) -> runnable.run());
	}

	@Override
	public void registerClientPackets(Consumer<ClientPacketRegister> consumer) {
		NeoForgeClientEntrypoint.getEventBus().addListener(RegisterClientPayloadHandlersEvent.class, (event) -> {
			ClientPacketRegister register = new ClientPacketRegister() {
				@Override
				public <P extends BasePatPatPacket<P>> void register(PatPatPacketType<P> type, PatPatClientPacketHandler<P> handler) {
					IPayloadHandler<P> payloadHandler = (packet, context) -> {
						if (packet instanceof PingPatPacket<?, ?> pingPacket) {
							pingPacket.setPacketReply(context::reply);
						}
						handler.handle(packet);
					};
					event.register(type.getPacketId(), payloadHandler);
				}
			};
			consumer.accept(register);
		});
	}

	@Override
	public void registerServerPackets(Consumer<ServerPacketRegister> consumer) {
		NeoForgeCommonEntrypoint.getEventBus().addListener(RegisterPayloadHandlersEvent.class, (e) -> {
			PayloadRegistrar registrar = e.registrar("1").optional();

			ServerPacketRegister register = new ServerPacketRegister() {
				@Override
				public <P extends BasePatPatPacket<P>> void register(PatPatPacketType<P> type, PacketRegistrationSide registrationSide, PatPatServerPacketHandler<P> handler) {
					IPayloadHandler<P> payloadHandler = (packet, context) -> {
						Player player = context.player();
						if (!(player instanceof ServerPlayer serverPlayer)) {
							return;
						}
						handler.handle(serverPlayer, packet);
					};

					switch (registrationSide) {
						case C2S -> registrar.playToServer(type.getPacketId(), type.getCodec(), payloadHandler);
						case S2C -> registrar.commonToClient(type.getPacketId(), type.getCodec());
						case BOTH -> {
							registrar.playToClient(type.getPacketId(), type.getCodec(), payloadHandler);
							registrar.playToServer(type.getPacketId(), type.getCodec(), payloadHandler);
						}
					}
				}
			};
			consumer.accept(register);
		});
	}

	@Override
	public void sendPacketToPlayer(ServerPlayer player, BasePatPatPacket<?> packet) {
		if (packet instanceof PongPatPacket<?> pingPong && pingPong.canPong()) {
			pingPong.pong(packet);
		} else {
			PacketDistributor.sendToPlayer(player, packet);
		}
	}

	@Override
	public void sendPacketToServer(BasePatPatPacket<?> packet) {
		if (packet instanceof PongPatPacket<?> pingPong && pingPong.canPong()) {
			pingPong.pong(packet);
		} else {
			ClientPacketDistributor.sendToServer(packet);
		}
	}

}
*///?}
