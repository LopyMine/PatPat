package net.lopymine.patpat.entrypoint.forge.loader;

import com.mojang.brigadier.CommandDispatcher;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.*;
import lombok.Getter;
import net.lopymine.patpat.PatPat;
import net.lopymine.patpat.client.resourcepack.AbstractResourceReloadListener;
import net.lopymine.patpat.entrypoint.forge.event.PatPatClientStoppingEvent;
import net.lopymine.patpat.entrypoint.loader.*;
import net.lopymine.patpat.entrypoint.loader.IModLoader.ClientPacketRegister.PatPatClientPacketHandler;
import net.lopymine.patpat.packet.*;
import net.lopymine.patpat.utils.*;
import net.minecraft.client.*;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.client.event.*;
import net.minecraftforge.client.event.RenderLevelStageEvent.Stage;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.*;
import net.minecraftforge.event.TickEvent.*;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.*;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.*;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.registries.DeferredRegister;
import org.jetbrains.annotations.Nullable;

public class ForgeModLoader implements IModLoader {

	@Nullable
	private ForgePackerHandler packerHandler;
	@Nullable
	private LazyClientPacketHandler lazyClientPacketHandler;

	@Override
	public void registerServerCommands(Consumer<CommandDispatcher<CommandSourceStack>> consumer) {
		MinecraftForge.EVENT_BUS.<RegisterCommandsEvent>addListener((event) -> {
			consumer.accept(event.getDispatcher());
		});
	}

	@Override
	public void registerClientCommands(Consumer<CommandDispatcher<CommandSourceStack>> consumer) {
		MinecraftForge.EVENT_BUS.<RegisterClientCommandsEvent>addListener((event) -> {
			consumer.accept(event.getDispatcher());
		});
	}

	@Override
	public void registerAfterEntitiesRenderer(CustomRenderer renderer) {
		MinecraftForge.EVENT_BUS.<RenderLevelStageEvent>addListener((event) -> {
			if (event.getStage() != Stage.AFTER_ENTITIES) {
				return;
			}
			renderer.render(Minecraft.getInstance().renderBuffers().bufferSource(), event.getPoseStack());
		});
	}

	@Override
	public void registerAfterWorldTickListener(Consumer<ClientLevel> consumer) {
		MinecraftForge.EVENT_BUS.<LevelTickEvent>addListener((event) -> {
			if (event.side != LogicalSide.CLIENT || !(event.level instanceof ClientLevel clientLevel)) {
				return;
			}
			consumer.accept(clientLevel);
		});
	}

	@Override
	public void registerResourceReloadListener(AbstractResourceReloadListener listener) {
		MinecraftForge.EVENT_BUS.<RegisterClientReloadListenersEvent>addListener((event) -> {
			event.registerReloadListener(listener);
		});
	}

	@Override
	public void registerKeybinding(KeyMapping keybinding) {
		MinecraftForge.EVENT_BUS.<RegisterKeyMappingsEvent>addListener((event) -> {
			event.register(keybinding);
		});
	}

	@Override
	public void registerSounds(Consumer<SoundRegister> consumer) {
		ForgeSoundRegister register = new ForgeSoundRegister();
		consumer.accept(register);
		register.finish();
	}

	public static class ForgeSoundRegister implements SoundRegister {

		private final DeferredRegister<SoundEvent> register;

		public ForgeSoundRegister() {
			this.register = DeferredRegister.create(VersionedThings.SOUND_EVENT.key(), PatPat.MOD_ID);
		}

		public SoundEvent registerSound(String id) {
			SoundEvent event = SoundUtils.getSoundEvent(id);
			this.register.register(id, () -> event);
			return event;
		}

		public void finish() {
			this.register.register(MinecraftForge.EVENT_BUS);
		}

	}

	@Override
	public void registerServerPlayerLogListener(ServerPlayerLogListener consumer) {
		MinecraftForge.EVENT_BUS.<PlayerEvent.PlayerLoggedOutEvent>addListener((event) -> consumer.onLog(false, event.getEntity()));
		MinecraftForge.EVENT_BUS.<PlayerEvent.PlayerLoggedInEvent>addListener((event) -> consumer.onLog(true, event.getEntity()));
	}

	@Override
	public void registerClientPlayerLogListener(ClientPlayerLogListener consumer) {
		MinecraftForge.EVENT_BUS.<ClientPlayerNetworkEvent.LoggingOut>addListener((e) -> consumer.onLog(false));
		MinecraftForge.EVENT_BUS.<ClientPlayerNetworkEvent.LoggingIn>addListener((e) -> consumer.onLog(true));
	}

	@Override
	public void registerOnServerStart(Runnable runnable) {
		MinecraftForge.EVENT_BUS.<ServerStartedEvent>addListener((event) -> runnable.run());
	}

	@Override
	public void registerOnServerStop(Runnable runnable) {
		MinecraftForge.EVENT_BUS.<ServerStoppingEvent>addListener((event) -> runnable.run());
	}

	@Override
	public void registerOnClientStop(Runnable runnable) {
		MinecraftForge.EVENT_BUS.<PatPatClientStoppingEvent>addListener((event) -> runnable.run());
	}

	@Override
	public void registerClientPackets(Consumer<ClientPacketRegister> consumer) {
		if (this.lazyClientPacketHandler == null) {
			return;
		}

		ClientPacketRegister register = new ClientPacketRegister() {
			@Override
			public <P extends BasePatPatPacket<P>> void register(PatPatPacketType<P> type, PatPatClientPacketHandler<P> handler) {
				Map<ResourceLocation, PatPatClientPacketHandler<?>> lazyListeners = ForgeModLoader.this.lazyClientPacketHandler.lazyListeners;
				lazyListeners.put(type.getId(), handler);
			}
		};

		consumer.accept(register);
	}

	@Override
	public void registerServerPackets(Consumer<ServerPacketRegister> consumer) {
		if (this.packerHandler == null) {
			this.packerHandler = new ForgePackerHandler("1", "packets_channel");
		}
		if (this.lazyClientPacketHandler == null) {
			this.lazyClientPacketHandler = new LazyClientPacketHandler();
		}

		SimpleChannel channel = this.packerHandler.packetsChannel;
		ServerPacketRegister register = new ServerPacketRegister() {
			@Override
			public <P extends BasePatPatPacket<P>> void register(PatPatPacketType<P> type, PacketRegistrationSide registrationSide, PatPatServerPacketHandler<P> handler) {
				BiConsumer<P, Supplier<NetworkEvent.Context>> handle = (p, context) -> {
					context.get().enqueueWork(() -> {
						if (p instanceof PingPatPacket<?, ?> pingPacket) {
							pingPacket.setPacketReply((packet) -> {
								channel.reply(packet, context.get());
							});
						}

						if (registrationSide == PacketRegistrationSide.S2C || registrationSide == PacketRegistrationSide.BOTH) {
							Map<ResourceLocation, PatPatClientPacketHandler<?>> lazyListeners = ForgeModLoader.this.lazyClientPacketHandler.lazyListeners;
							@SuppressWarnings("unchecked")
							PatPatClientPacketHandler<P> packetHandler = (PatPatClientPacketHandler<P>) lazyListeners.get(p.getPatPatType().getId());
							if (packetHandler != null) {
								packetHandler.handle(p);
							}
						}
						if (registrationSide == PacketRegistrationSide.C2S || registrationSide == PacketRegistrationSide.BOTH) {
							ServerPlayer sender = context.get().getSender();
							if (sender != null) {
								handler.handle(sender, p);
							}
						}
					});
					context.get().setPacketHandled(true);
				};

				channel.registerMessage(ForgeModLoader.this.packerHandler.latestPacketId.getAndIncrement(), type.getClazz(), BasePatPatPacket::write, type.getFactory(), handle);
			}
		};

		consumer.accept(register);
	}

	@Override
	public void sendPacketToPlayer(ServerPlayer player, BasePatPatPacket<?> packet) {
		if (this.packerHandler == null) {
			return;
		}

		if (packet instanceof PongPatPacket<?> pingPong && pingPong.canPong()) {
			pingPong.pong(packet);
		} else {
			this.packerHandler.getPacketsChannel().send(PacketDistributor.PLAYER.with(() -> player), packet);
		}
	}

	@Override
	public void sendPacketToServer(BasePatPatPacket<?> packet) {
		if (this.packerHandler == null) {
			return;
		}

		if (packet instanceof PongPatPacket<?> pingPong && pingPong.canPong()) {
			pingPong.pong(packet);
		} else {
			this.packerHandler.getPacketsChannel().sendToServer(packet);
		}
	}

	private static class LazyClientPacketHandler {

		private final Map<ResourceLocation, PatPatClientPacketHandler<?>> lazyListeners = new HashMap<>();

	}

	@Getter
	private static class ForgePackerHandler {

		private final String packetVersion;
		private final SimpleChannel packetsChannel;
		private final AtomicInteger latestPacketId = new AtomicInteger(0);

		public ForgePackerHandler(String packetVersion, String channelName) {
			this.packetVersion = packetVersion;
			this.packetsChannel = NetworkRegistry.newSimpleChannel(RLUtils.modId(channelName), () -> packetVersion, packetVersion::equals, packetVersion::equals);
		}
	}
}
