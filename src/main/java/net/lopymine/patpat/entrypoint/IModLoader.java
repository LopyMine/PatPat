package net.lopymine.patpat.entrypoint;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.brigadier.CommandDispatcher;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.function.Consumer;
import net.lopymine.patpat.packet.*;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

public interface IModLoader {

	Path getConfigDir();

	boolean isDevelopmentEnvironment();

	boolean isModLoaded(String modid);

	void registerClientCommands(Consumer<CommandDispatcher<CommandSourceStack>> consumer);

	void registerServerCommands(Consumer<CommandDispatcher<CommandSourceStack>> consumer);

	void registerAfterEntitiesRenderer(CustomRenderer renderer);

	void registerAfterWorldTickListener(Consumer<ClientLevel> runnable);

	void registerResourceReloadListener(Identifier id, ResourceManagerReloadListener listener);

	void registerKeybinding(KeyMapping keybinding);

	void registerSounds(Consumer<SoundRegister> consumer);

	void registerServerPlayerLogListener(ServerPlayerLogListener consumer);

	void registerClientPlayerLogListener(ClientPlayerLogListener consumer);

	void registerOnServerStart(Runnable runnable);

	void registerOnServerStop(Runnable runnable);

	void registerOnClientStop(Runnable runnable);

	void registerClientPackets(Consumer<ClientPacketRegister> consumer);

	@Nullable
	InputStream loadModFile(String modId, String path);

	void registerServerPackets(Consumer<ServerPacketRegister> consumer);

	ModEnvironment getEnvironment();

	enum ModEnvironment {

		SERVER,
		CLIENT

	}

	interface ClientPacketRegister {

		<P extends BasePatPatPacket<P>> void register(PatPatPacketType<P> type, PatPatClientPacketHandler<P> handler);

		interface PatPatClientPacketHandler<T extends BasePatPatPacket<T>> {

			void handle(T packet);

		}

	}

	interface ServerPacketRegister {

		<P extends BasePatPatPacket<P>> void register(PatPatPacketType<P> type, PacketRegistrationSide registrationSide, PatPatServerPacketHandler<P> handler);

		enum PacketRegistrationSide {

			C2S,
			S2C,
			BOTH

		}

		interface PatPatServerPacketHandler<T extends BasePatPatPacket<T>> {

			void handle(ServerPlayer player, T packet);

		}

	}

	interface ServerPlayerLogListener {

		void onLog(boolean loggedIn, Player player);

	}

	interface ClientPlayerLogListener {

		void onLog(boolean loggedIn);

	}

	interface CustomRenderer {

		void render(MultiBufferSource source, PoseStack stack);

	}

	interface SoundRegister {

		SoundEvent registerSound(String id);

		void finish();

	}

}
