package net.lopymine.patpat.entrypoint.impl.neoforge.loader;

//? if neoforge {
/*import com.mojang.brigadier.CommandDispatcher;

import java.util.function.Consumer;
import net.lopymine.patpat.PatPat;
import net.lopymine.patpat.client.resourcepack.AbstractResourceReloadListener;
import net.lopymine.patpat.entrypoint.ServerMultiLoader;
import net.lopymine.patpat.entrypoint.loader.client.IClientModLoader;
import net.lopymine.patpat.entrypoint.impl.neoforge.*;
import net.lopymine.patpat.entrypoint.impl.neoforge.event.*;
import net.lopymine.patpat.packet.*;
import net.lopymine.patpat.utils.*;
import net.minecraft.client.*;
import net.minecraft.client.multiplayer.*;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.PacketDistributor;
//? if =1.20.4 {
/^import net.neoforged.neoforge.network.registration.NetworkRegistry;
^///?}
import net.neoforged.neoforge.registries.DeferredRegister;

//? if >=1.21.7 {

import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import net.neoforged.neoforge.client.network.event.RegisterClientPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadHandler;

//?}

public class NeoForgeClientModLoader implements IClientModLoader {

	@Override
	public void registerOnClientStop(Runnable runnable) {
		NeoForge.EVENT_BUS.addListener(PatPatNeoForgeClientStoppingEvent.class, event -> runnable.run());
	}

	@Override
	public void registerClientCommands(Consumer<CommandDispatcher<CommandSourceStack>> consumer) {
		NeoForge.EVENT_BUS.addListener(RegisterClientCommandsEvent.class, (event) -> consumer.accept(event.getDispatcher()));
	}

	@Override
	public void registerAfterEntitiesRenderer(CustomRenderer renderer) {
		//? if >=1.21.6 && <=1.21.8 {
		/^NeoForge.EVENT_BUS.addListener(RenderLevelStageEvent.AfterEntities.class, (p) -> {
			renderer.render(Minecraft.getInstance().renderBuffers().bufferSource(), p.getPoseStack());
		});
		^///?}

		//? if <=1.21.5 {
		/^NeoForge.EVENT_BUS.addListener(RenderLevelStageEvent.class, (p) -> {
			if (p.getStage() != RenderLevelStageEvent.Stage.AFTER_ENTITIES) {
				return;
			}
			renderer.render(Minecraft.getInstance().renderBuffers().bufferSource(), p.getPoseStack());
		});
		^///?}
	}

	@Override
	public void registerAfterWorldTickListener(Consumer<ClientLevel> runnable) {
		//? if >=1.20.5 {
		NeoForge.EVENT_BUS.addListener(ClientTickEvent.Post.class, (p) -> {
			ClientLevel level = Minecraft.getInstance().level;
			if (level == null) {
				return;
			}
			runnable.accept(level);
		});
		//?} else {
		/^NeoForge.EVENT_BUS.addListener(net.neoforged.neoforge.event.TickEvent.ClientTickEvent.class, (p) -> {
			if (p.phase != net.neoforged.neoforge.event.TickEvent.Phase.END) {
				return;
			}
			ClientLevel level = Minecraft.getInstance().level;
			if (level == null) {
				return;
			}
			runnable.accept(level);
		});
		^///?}
	}

	@Override
	public void registerResourceReloadListener(AbstractResourceReloadListener listener) {
		//? if >=1.21.4 {
		NeoForgeClientEntrypoint.getEventBus().addListener(AddClientReloadListenersEvent.class, (e) -> e.addListener(listener.getId(), listener));
		 //?}

		//? if <=1.21.1 {
		/^NeoForgeClientEntrypoint.getEventBus().addListener(RegisterClientReloadListenersEvent.class, (e) -> e.registerReloadListener(listener));
		^///?}
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
	public void registerClientPlayerLogListener(ClientPlayerLogListener consumer) {
		NeoForge.EVENT_BUS.addListener(ClientPlayerNetworkEvent.LoggingOut.class, (e) -> consumer.onLog(false));
		NeoForge.EVENT_BUS.addListener(ClientPlayerNetworkEvent.LoggingIn.class, (e) -> consumer.onLog(true));
	}

	@Override
	public void registerClientPackets(Consumer<ClientPacketRegister> consumer) {
		//? if <=1.21.6 {
		/^ClientPacketRegister register = new ClientPacketRegister() {
			@Override
			public <P extends BasePatPatPacket<P>> void register(PatPatPacketType<P> type, PatPatClientPacketHandler<P> handler) {
				if (!(ServerMultiLoader.getInstance() instanceof NeoForgeServerModLoader serverLoader)) {
					return;
				}
				serverLoader.getClientHandlers().put(type.getId(), handler);
			}
		};
		consumer.accept(register);
		^///?}

		//? if >=1.21.7 {
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
		//?}
	}

	@Override
	public void sendPacketToServer(BasePatPatPacket<?> packet) {
		ClientPacketListener connection = Minecraft.getInstance().getConnection();
		if (connection == null) {
			return;
		}
		//? if >=1.20.5 {
		if (!connection.hasChannel(packet.type())) {
			return;
		}
		//?} elif >=1.20.4 {
		/^if (!NetworkRegistry.getInstance().isConnected(connection, packet.getPatPatType().getId())) {
			return;
		}
		^///?}
		if (packet instanceof PongPatPacket<?> pingPong && pingPong.canPong()) {
			pingPong.pong(packet);
		} else {
			//? if >=1.21.7 {
			ClientPacketDistributor.sendToServer(packet);
			//?} elif >=1.20.5 {
			/^PacketDistributor.sendToServer(packet);
			^///?} elif >=1.20.4 {
			/^PacketDistributor.SERVER.noArg().send(packet);
			^///?} else {
			/^if (ServerMultiLoader.getInstance() instanceof NeoForgeServerModLoader serverLoader && serverLoader.getNeoForgeChannelHandler() != null) {
				serverLoader.getNeoForgeChannelHandler().getChannel().sendToServer(packet);
			}
			^///?}
		}
	}

}
*///?}
