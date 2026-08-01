package net.lopymine.patpat.entrypoint.impl.forge.loader;

//? if forge {

/*import com.mojang.brigadier.CommandDispatcher;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.*;
import lombok.Getter;
import net.lopymine.patpat.PatPat;
import net.lopymine.patpat.client.resourcepack.AbstractResourceReloadListener;
import net.lopymine.patpat.entrypoint.ServerMultiLoader;
import net.lopymine.patpat.entrypoint.impl.forge.event.PatPatForgeClientStoppingEvent;
import net.lopymine.patpat.entrypoint.impl.forge.loader.ForgeServerModLoader.*;
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
//? if >=1.18.2 {
import net.minecraftforge.client.event.RenderLevelStageEvent.Stage;
//?}
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
		//? if >=1.18 {
		MinecraftForge.EVENT_BUS.<RegisterClientCommandsEvent>addListener((event) -> {
			consumer.accept(event.getDispatcher());
		});
		//?}
	}

	@Override
	public void registerAfterEntitiesRenderer(CustomRenderer renderer) {
		//? if >=1.18.2 {
		MinecraftForge.EVENT_BUS.<RenderLevelStageEvent>addListener((event) -> {
			//? if >=1.19.3 {
			if (event.getStage() != Stage.AFTER_ENTITIES) {
			//?} else {
			/^if (event.getStage() != Stage.AFTER_TRANSLUCENT_BLOCKS) {
			^///?}
				return;
			}
			renderer.render(Minecraft.getInstance().renderBuffers().bufferSource(), event.getPoseStack());
		});
		//?} else {
		/^MinecraftForge.EVENT_BUS.<net.minecraftforge.client.event.RenderWorldLastEvent>addListener((event) -> {
			renderer.render(Minecraft.getInstance().renderBuffers().bufferSource(), event.getMatrixStack());
		});
		^///?}
	}

	@Override
	public void registerAfterWorldTickListener(Consumer<ClientLevel> consumer) {
		//? if >=1.19 {
		MinecraftForge.EVENT_BUS.<LevelTickEvent>addListener((event) -> {
			if (event.side != LogicalSide.CLIENT || event.phase != Phase.END || !(event.level instanceof ClientLevel clientLevel)) {
				return;
			}
			consumer.accept(clientLevel);
		});
		//?} else {
		/^MinecraftForge.EVENT_BUS.<WorldTickEvent>addListener((event) -> {
			if (event.side != LogicalSide.CLIENT || event.phase != Phase.END || !(event.world instanceof ClientLevel clientLevel)) {
				return;
			}
			consumer.accept(clientLevel);
		});
		^///?}
	}

	@Override
	public void registerResourceReloadListener(AbstractResourceReloadListener listener) {
		FMLJavaModLoadingContext.get().getModEventBus().<RegisterClientReloadListenersEvent>addListener((event) -> {
			event.registerReloadListener(listener);
		});
	}

	@Override
	public void registerKeybinding(KeyMapping keybinding) {
		//? if >=1.19 {
		FMLJavaModLoadingContext.get().getModEventBus().<RegisterKeyMappingsEvent>addListener((event) -> {
			event.register(keybinding);
		});
		//?} else {
		/^FMLJavaModLoadingContext.get().getModEventBus().<net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent>addListener((event) -> {
			net.minecraftforge.client.ClientRegistry.registerKeyBinding(keybinding);
		});
		^///?}
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
			//? if >=1.18.2 {
			this.register = DeferredRegister.create(VersionedThings.SOUND_EVENT.key(), PatPat.MOD_ID);
			//?} else {
			/^this.register = DeferredRegister.create(net.minecraftforge.registries.ForgeRegistries.SOUND_EVENTS, PatPat.MOD_ID);
			^///?}
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
		//? if >=1.19 {
		MinecraftForge.EVENT_BUS.<ClientPlayerNetworkEvent.LoggingOut>addListener((e) -> consumer.onLog(false));
		MinecraftForge.EVENT_BUS.<ClientPlayerNetworkEvent.LoggingIn>addListener((e) -> consumer.onLog(true));
		//?} else {
		/^MinecraftForge.EVENT_BUS.<ClientPlayerNetworkEvent.LoggedOutEvent>addListener((e) -> consumer.onLog(false));
		MinecraftForge.EVENT_BUS.<ClientPlayerNetworkEvent.LoggedInEvent>addListener((e) -> consumer.onLog(true));
		^///?}
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
}
*///?}
