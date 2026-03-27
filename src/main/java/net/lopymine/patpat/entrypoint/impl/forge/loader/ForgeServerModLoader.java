package net.lopymine.patpat.entrypoint.impl.forge.loader;

//? if forge {

/*import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.CommandDispatcher;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.*;
import lombok.Getter;
import net.lopymine.patpat.PatPat;
import net.lopymine.patpat.entrypoint.loader.client.IClientModLoader.ClientPacketRegister.PatPatClientPacketHandler;
import net.lopymine.patpat.entrypoint.loader.server.IServerModLoader;
import net.lopymine.patpat.packet.*;
import net.lopymine.patpat.utils.*;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.*;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.*;
import net.minecraftforge.network.*;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.server.permission.PermissionAPI;
import net.minecraftforge.server.permission.events.PermissionGatherEvent.Nodes;
import net.minecraftforge.server.permission.nodes.*;
import org.jetbrains.annotations.Nullable;

@Getter
public class ForgeServerModLoader implements IServerModLoader {

	private final Map<String, PermissionNode<Boolean>> permissionNodes = new HashMap<>();
	private final List<PermissionNode<?>> registrationNodes = new ArrayList<>();

	@Nullable
	private ForgePackerHandler packerHandler;
	@Nullable
	private ForgeServerModLoader.ForgeLazyClientPacketHandler forgeLazyClientPacketHandler;

	@Override
	public void registerServerCommands(Consumer<CommandDispatcher<CommandSourceStack>> consumer) {
		MinecraftForge.EVENT_BUS.<RegisterCommandsEvent>addListener((event) -> {
			consumer.accept(event.getDispatcher());
		});
		MinecraftForge.EVENT_BUS.<Nodes>addListener((nodes) -> {
			nodes.addNodes(this.registrationNodes);
		});
	}

	@Override
	public void registerServerPlayerLogListener(ServerPlayerLogListener consumer) {
		MinecraftForge.EVENT_BUS.<PlayerEvent.PlayerLoggedOutEvent>addListener((event) -> consumer.onLog(false, event.getEntity()));
		MinecraftForge.EVENT_BUS.<PlayerEvent.PlayerLoggedInEvent>addListener((event) -> consumer.onLog(true, event.getEntity()));
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
	public void registerServerPackets(Consumer<ServerPacketRegister> consumer) {
		if (this.packerHandler == null) {
			this.packerHandler = new ForgePackerHandler("1", "patpat_packets_channel");
		}
		if (this.forgeLazyClientPacketHandler == null) {
			this.forgeLazyClientPacketHandler = new ForgeLazyClientPacketHandler();
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
							Map<Identifier, PatPatClientPacketHandler<?>> lazyListeners = ForgeServerModLoader.this.forgeLazyClientPacketHandler.lazyListeners;
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

				channel.registerMessage(ForgeServerModLoader.this.packerHandler.latestPacketId.getAndIncrement(), type.getClazz(), BasePatPatPacket::write, type.getFactory(), handle);
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
				(serverPlayer, uuid, something) ->
						serverPlayer != null && serverPlayer.hasPermissions(2)
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
	public CompletableFuture<Boolean> hasOfflinePermission(
			GameProfile profile,
			MinecraftServer server,
			String permission
	) {
		UUID uuid = profile.getId();
		return CompletableFuture.supplyAsync(() -> {
			PermissionNode<Boolean> node = this.permissionNodes.get(permission);
			if (node == null) {
				return false;
			}
			return PermissionAPI.getOfflinePermission(uuid, node);
		});
	}

	@Getter
	public static class ForgeLazyClientPacketHandler {

		private final Map<Identifier, PatPatClientPacketHandler<?>> lazyListeners = new HashMap<>();

	}

	@Getter
	public static class ForgePackerHandler {

		private final String packetVersion;
		private final SimpleChannel packetsChannel;
		private final AtomicInteger latestPacketId = new AtomicInteger(0);

		public ForgePackerHandler(String packetVersion, String channelName) {
			this.packetVersion  = packetVersion;
			this.packetsChannel = NetworkRegistry.newSimpleChannel(
					RLUtils.modId(channelName),
					() -> packetVersion,
					NetworkRegistry.acceptMissingOr(packetVersion),
					NetworkRegistry.acceptMissingOr(packetVersion)
			);
		}
	}
}
*///?}
