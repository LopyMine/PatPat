package net.lopymine.patpat.entrypoint.neoforge.loader;

//? if neoforge {
import com.mojang.brigadier.CommandDispatcher;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import lombok.Getter;
import net.lopymine.patpat.PatPat;
import net.lopymine.patpat.entrypoint.loader.client.IClientModLoader.ClientPacketRegister.PatPatClientPacketHandler;
import net.lopymine.patpat.entrypoint.loader.server.IServerModLoader;
import net.lopymine.patpat.entrypoint.neoforge.*;
import net.lopymine.patpat.packet.*;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.server.*;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.server.permission.PermissionAPI;
import net.neoforged.neoforge.server.permission.nodes.*;
import net.neoforged.neoforge.server.permission.nodes.PermissionTypes;
import net.neoforged.neoforge.server.permission.events.PermissionGatherEvent.Nodes;

//? if >=1.21.10 {
import net.minecraft.server.permissions.*;
import net.minecraft.server.permissions.Permission.HasCommandLevel;
//?}

@Getter
public class NeoForgeServerModLoader implements IServerModLoader {

	private final Map<String, PermissionNode<Boolean>> permissionNodes = new HashMap<>();
	private final List<PermissionNode<?>> registrationNodes = new ArrayList<>();

	private final Map<Identifier, PatPatClientPacketHandler<?>> clientHandlers = new HashMap<>();

	@Override
	public void registerServerCommands(Consumer<CommandDispatcher<CommandSourceStack>> consumer) {
		NeoForge.EVENT_BUS.addListener(RegisterCommandsEvent.class, (event) -> consumer.accept(event.getDispatcher()));
		NeoForge.EVENT_BUS.addListener(Nodes.class, (nodes) -> nodes.addNodes(this.registrationNodes));
	}

	@Override
	public void registerServerPlayerLogListener(ServerPlayerLogListener consumer) {
		NeoForge.EVENT_BUS.addListener(PlayerEvent.PlayerLoggedOutEvent.class, event -> consumer.onLog(false, event.getEntity()));
		NeoForge.EVENT_BUS.addListener(PlayerEvent.PlayerLoggedInEvent.class, event -> consumer.onLog(true, event.getEntity()));
	}

	@Override
	public void registerOnServerStart(Runnable runnable) {
		NeoForge.EVENT_BUS.addListener(ServerStartedEvent.class, (s) -> runnable.run());
	}

	@Override
	public void registerOnServerStop(Runnable runnable) {
		NeoForge.EVENT_BUS.addListener(ServerStoppingEvent.class, (s) -> runnable.run());
	}

	//? if <=1.21.1 {
	/*@SuppressWarnings("unchecked")
	*///?}
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

					//? if <=1.21.1 {
					/*IPayloadHandler<P> clientPayloadHandler = (packet, context) -> {
						PatPatClientPacketHandler<P> clientHandler = (PatPatClientPacketHandler<P>) NeoForgeServerModLoader.this.clientHandlers.get(packet.getPatPatType().getId());
						if (clientHandler == null) {
							return;
						}

						clientHandler.handle(packet);
					};
					*///?}

					switch (registrationSide) {
						case C2S -> registrar.playToServer(type.getPacketId(), type.getCodec(), payloadHandler);
						case S2C -> registrar.playToClient(type.getPacketId(), type.getCodec()
								//? if <=1.21.1 {
								/*, clientPayloadHandler
								*///?}
						);
						case BOTH -> {
							registrar.playToServer(type.getPacketId(), type.getCodec(), payloadHandler);
							registrar.playToClient(type.getPacketId(), type.getCodec(), payloadHandler);
						}
					}
				}
			};
			consumer.accept(register);
		});
	}

	@Override
	public void sendPacketToPlayer(ServerPlayer player, BasePatPatPacket<?> packet) {
		if (!player.connection.hasChannel(packet.getPatPatType().getId())) {
			return;
		}
		if (packet instanceof PongPatPacket<?> pingPong && pingPong.canPong()) {
			pingPong.pong(packet);
		} else {
			PacketDistributor.sendToPlayer(player, packet);
		}
	}

	@Override
	public void registerPermission(String name) {
		String permission = name.startsWith(PatPat.MOD_ID) ? name.replaceFirst(PatPat.MOD_ID + "\\.", "") : name;
		if (permission.isEmpty()) {
			return;
		}

		String permissionId = "%s.%s".formatted(PatPat.MOD_ID, permission);
		if (this.permissionNodes.get(permissionId) != null) {
			return;
		}

		PermissionNode<Boolean> node = new PermissionNode<>(
				PatPat.MOD_ID,
				permission,
				PermissionTypes.BOOLEAN,
				(serverPlayer, uuid, something) -> serverPlayer != null &&
						//? if >=1.21.10 {
						serverPlayer.permissions().hasPermission(new HasCommandLevel(PermissionLevel.GAMEMASTERS))
						//?} else {
						/*serverPlayer.hasPermissions(2)
						*///?}
		);

		this.permissionNodes.put(permissionId, node);
		this.registrationNodes.add(node);
	}

	@Override
	public boolean hasPermission(ServerPlayer player, String permission) {
		PermissionNode<Boolean> node = this.permissionNodes.get(permission);
		if (node == null) {
			return false;
		}
		return PermissionAPI.getPermission(player, node);
	}

	@Override
	public CompletableFuture<Boolean> hasOfflinePermission(UUID uuid, String permission) {
		return CompletableFuture.supplyAsync(() -> {
			PermissionNode<Boolean> node = this.permissionNodes.get(permission);
			if (node == null) {
				return false;
			}
			return PermissionAPI.getOfflinePermission(uuid, node);
		});
	}

}
//?}
