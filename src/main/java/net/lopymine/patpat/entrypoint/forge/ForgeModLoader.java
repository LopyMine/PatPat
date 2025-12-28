package net.lopymine.patpat.entrypoint.forge;

import com.mojang.brigadier.CommandDispatcher;
import java.io.*;
import java.nio.file.Path;
import java.util.function.Consumer;
import net.lopymine.patpat.PatPat;
import net.lopymine.patpat.entrypoint.IModLoader;
import net.lopymine.patpat.packet.*;
import net.lopymine.patpat.utils.*;
import net.minecraft.client.*;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import net.neoforged.fml.*;
import net.neoforged.fml.loading.*;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.event.lifecycle.ClientStoppingEvent;
import net.neoforged.neoforge.client.network.event.RegisterClientPayloadHandlersEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.server.*;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforgespi.language.IModFileInfo;

public class ForgeModLoader implements IModLoader {

	@Override
	public boolean isDevelopmentEnvironment() {
		return !FMLEnvironment.isProduction();
	}

	@Override
	public Path getConfigDir() {
		return FMLPaths.CONFIGDIR.get().resolve("patpat/");
	}

	@Override
	public boolean isModLoaded(String modid) {
		return ModList.get().isLoaded(modid);
	}

	@Override
	public void registerOnClientStop(Runnable runnable) {
		NeoForge.EVENT_BUS.addListener(ClientStoppingEvent.class, event -> runnable.run());
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
		/*NeoForge.EVENT_BUS.addListener(Post.class, (p) -> renderer.render(p.getMultiBufferSource(), p.getPoseStack()));
		*///?}
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
	public void registerResourceReloadListener(Identifier id, ResourceManagerReloadListener listener) {
		PatPatNeoForgeClientEntrypoint.getEventBus().addListener(AddClientReloadListenersEvent.class, (e) -> e.addListener(id, listener));
	}

	@Override
	public void registerKeybinding(KeyMapping keybinding) {
		PatPatNeoForgeClientEntrypoint.getEventBus().addListener(RegisterKeyMappingsEvent.class, (event) -> event.register(keybinding));
	}

	@Override
	public void registerSounds(Consumer<IModLoader.SoundRegister> consumer) {
		ForgeSoundRegister register = new ForgeSoundRegister();
		consumer.accept(register);
		register.finish();
	}

	public static class ForgeSoundRegister implements IModLoader.SoundRegister {

		private final DeferredRegister<SoundEvent> register;

		public ForgeSoundRegister() {
			this.register = DeferredRegister.create(VersionedThings.SOUND_EVENT.key(), "patpat");
		}

		public SoundEvent registerSound(String id) {
			SoundEvent event = SoundUtils.getSoundEvent(id);
			this.register.register(id, () -> event);
			return event;
		}

		public void finish() {
			this.register.register(PatPatNeoForgeClientEntrypoint.getEventBus());
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
		PatPatNeoForgeClientEntrypoint.getEventBus().addListener(RegisterClientPayloadHandlersEvent.class, (event) -> {
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
		PatPatNeoForgeClientEntrypoint.getEventBus().addListener(RegisterPayloadHandlersEvent.class, (e) -> {
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
	public InputStream loadModFile(String modId, String path) {
		IModFileInfo file = ModList.get().getModFileById(modId);
		if (file == null) {
			PatPat.LOGGER.error("Failed to load file at \"{}\", because \"{}\" mod container doesn't exits!", path, modId);
			return null;
		}
		try {
			return file.getFile().getContents().openFile(path);
		} catch (IOException e) {
			PatPat.LOGGER.error("Failed to open file at \"{}\", reason:", path, e);
			return null;
		}
	}

	@Override
	public ModEnvironment getEnvironment() {
		return switch (FMLEnvironment.getDist()) {
			case CLIENT -> ModEnvironment.CLIENT;
			case DEDICATED_SERVER -> ModEnvironment.SERVER;
		};
	}
}
