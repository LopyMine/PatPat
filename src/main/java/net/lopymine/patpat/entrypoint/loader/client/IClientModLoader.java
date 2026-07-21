//~ client_fabric_commands

package net.lopymine.patpat.entrypoint.loader.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.brigadier.CommandDispatcher;
import java.util.function.Consumer;
//? if <1.19 {
/*import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
*///?} else {
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
 //?}
import net.lopymine.patpat.client.resourcepack.AbstractResourceReloadListener;
import net.lopymine.patpat.packet.*;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.sounds.SoundEvent;

public interface IClientModLoader {

	void registerClientCommands(Consumer<CommandDispatcher<FabricClientCommandSource>> consumer);

	void registerAfterEntitiesRenderer(CustomRenderer renderer);

	void registerAfterWorldTickListener(Consumer<ClientLevel> consumer);

	void registerResourceReloadListener(AbstractResourceReloadListener listener);

	void registerKeybinding(KeyMapping keybinding);

	void registerSounds(Consumer<SoundRegister> consumer);

	void registerClientPlayerLogListener(ClientPlayerLogListener consumer);

	void registerOnClientStop(Runnable runnable);

	void registerClientPackets(Consumer<ClientPacketRegister> consumer);

	void sendPacketToServer(BasePatPatPacket<?> packet);

	interface ClientPacketRegister {

		<P extends BasePatPatPacket<P>> void register(PatPatPacketType<P> type, PatPatClientPacketHandler<P> handler);

		interface PatPatClientPacketHandler<T extends BasePatPatPacket<T>> {

			void handle(T packet);

		}

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
