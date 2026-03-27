package net.lopymine.patpat.entrypoint.impl.neoforge.loader;

//? if neoforge {
import com.mojang.brigadier.CommandDispatcher;

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
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.registries.DeferredRegister;

//? if >=1.21.10 {

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
		//? if >=1.21.2 && <=1.21.8 {
		/*NeoForge.EVENT_BUS.addListener(Post.class, (p) -> renderer.render(p.getMultiBufferSource(), p.getPoseStack()));
		*///?}

		//? if <=1.21.1 {
		/*NeoForge.EVENT_BUS.addListener(RenderLevelStageEvent.class, (p) -> {
			renderer.render(Minecraft.getInstance().renderBuffers().bufferSource(), p.getPoseStack());
		});
		*///?}
	}

	@Override
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
		//? if >=1.21.10 {
		NeoForgeClientEntrypoint.getEventBus().addListener(AddClientReloadListenersEvent.class, (e) -> e.addListener(listener.getId(), listener));
		 //?}

		//? if <=1.21.1 {
		/*NeoForgeClientEntrypoint.getEventBus().addListener(RegisterClientReloadListenersEvent.class, (e) -> e.registerReloadListener(listener));
		*///?}
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
		//? if <=1.21.1 {
		/*ClientPacketRegister register = new ClientPacketRegister() {
			@Override
			public <P extends BasePatPatPacket<P>> void register(PatPatPacketType<P> type, PatPatClientPacketHandler<P> handler) {
				if (!(ServerMultiLoader.getInstance() instanceof NeoForgeServerModLoader serverLoader)) {
					return;
				}
				serverLoader.getClientHandlers().put(type.getId(), handler);
			}
		};
		consumer.accept(register);
		*///?}

		//? if >=1.21.10 {
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
		if (packet instanceof PongPatPacket<?> pingPong && pingPong.canPong()) {
			pingPong.pong(packet);
		} else {
			//? if >=1.21.10 {
			ClientPacketDistributor.sendToServer(packet);
			//?} else {
			/*PacketDistributor.sendToServer(packet);
			*///?}
		}
	}

}
//?}
