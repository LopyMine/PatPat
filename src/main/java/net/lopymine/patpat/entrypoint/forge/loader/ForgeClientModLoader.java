package net.lopymine.patpat.entrypoint.forge.loader;

//? if forge {

/*import com.mojang.brigadier.CommandDispatcher;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.*;
import lombok.Getter;
import net.lopymine.patpat.PatPat;
import net.lopymine.patpat.client.resourcepack.AbstractResourceReloadListener;
import net.lopymine.patpat.entrypoint.ServerMultiLoader;
import net.lopymine.patpat.entrypoint.forge.event.PatPatForgeClientStoppingEvent;
import net.lopymine.patpat.entrypoint.forge.loader.ForgeServerModLoader.*;
import net.lopymine.patpat.entrypoint.loader.client.IClientModLoader;
import net.lopymine.patpat.entrypoint.loader.client.IClientModLoader.ClientPacketRegister.PatPatClientPacketHandler;
import net.lopymine.patpat.packet.*;
import net.lopymine.patpat.utils.*;
import net.minecraft.client.*;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.client.event.*;
import net.minecraftforge.client.event.RenderLevelStageEvent.Stage;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent.*;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.*;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.registries.DeferredRegister;

public class ForgeClientModLoader implements IClientModLoader {

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
			if (event.side != LogicalSide.CLIENT || event.phase != Phase.END || !(event.level instanceof ClientLevel clientLevel)) {
				return;
			}
			consumer.accept(clientLevel);
		});
	}

	@Override
	public void registerResourceReloadListener(AbstractResourceReloadListener listener) {
		FMLJavaModLoadingContext.get().getModEventBus().<RegisterClientReloadListenersEvent>addListener((event) -> {
			event.registerReloadListener(listener);
		});
	}

	@Override
	public void registerKeybinding(KeyMapping keybinding) {
		FMLJavaModLoadingContext.get().getModEventBus().<RegisterKeyMappingsEvent>addListener((event) -> {
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
			this.register.register(FMLJavaModLoadingContext.get().getModEventBus());
		}

	}

	@Override
	public void registerClientPlayerLogListener(ClientPlayerLogListener consumer) {
		MinecraftForge.EVENT_BUS.<ClientPlayerNetworkEvent.LoggingOut>addListener((e) -> consumer.onLog(false));
		MinecraftForge.EVENT_BUS.<ClientPlayerNetworkEvent.LoggingIn>addListener((e) -> consumer.onLog(true));
	}

	@Override
	public void registerOnClientStop(Runnable runnable) {
		MinecraftForge.EVENT_BUS.<PatPatForgeClientStoppingEvent>addListener((event) -> runnable.run());
	}

	@Override
	public void registerClientPackets(Consumer<ClientPacketRegister> consumer) {
		if (!(ServerMultiLoader.getInstance() instanceof ForgeServerModLoader serverModLoader)) {
			return;
		}

		ForgeLazyClientPacketHandler packetHandler = serverModLoader.getForgeLazyClientPacketHandler();
		if (packetHandler == null) {
			return;
		}

		ClientPacketRegister register = new ClientPacketRegister() {
			@Override
			public <P extends BasePatPatPacket<P>> void register(PatPatPacketType<P> type, PatPatClientPacketHandler<P> handler) {
				Map<Identifier, PatPatClientPacketHandler<?>> lazyListeners = packetHandler.getLazyListeners();
				lazyListeners.put(type.getId(), handler);
			}
		};

		consumer.accept(register);
	}

	@Override
	public void sendPacketToServer(BasePatPatPacket<?> packet) {
		if (!(ServerMultiLoader.getInstance() instanceof ForgeServerModLoader serverModLoader)) {
			return;
		}

		ForgeServerModLoader.ForgePackerHandler packerHandler = serverModLoader.getPackerHandler();
		if (packerHandler == null) {
			return;
		}

		if (packet instanceof PongPatPacket<?> pingPong && pingPong.canPong()) {
			pingPong.pong(packet);
		} else {
			packerHandler.getPacketsChannel().sendToServer(packet);
		}
	}

	private static class LazyClientPacketHandler {

		private final Map<Identifier, PatPatClientPacketHandler<?>> lazyListeners = new HashMap<>();

	}

	@Getter
	private static class ForgePackerHandler {

		private final String packetVersion;
		private final SimpleChannel packetsChannel;
		private final AtomicInteger latestPacketId = new AtomicInteger(0);

		public ForgePackerHandler(String packetVersion, String channelName) {
			this.packetVersion  = packetVersion;
			this.packetsChannel = NetworkRegistry.newSimpleChannel(RLUtils.modId(channelName), () -> packetVersion, packetVersion::equals, packetVersion::equals);
		}
	}
}
*///?}
