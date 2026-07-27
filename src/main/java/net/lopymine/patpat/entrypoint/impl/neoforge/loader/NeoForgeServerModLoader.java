package net.lopymine.patpat.entrypoint.impl.neoforge.loader;

//? if neoforge {
/*import com.mojang.brigadier.CommandDispatcher;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import lombok.Getter;
import net.lopymine.patpat.PatPat;
import net.lopymine.patpat.entrypoint.impl.neoforge.NeoForgeCommonEntrypoint;
import net.lopymine.patpat.entrypoint.loader.client.IClientModLoader.ClientPacketRegister.PatPatClientPacketHandler;
import net.lopymine.patpat.entrypoint.loader.server.IServerModLoader;
import net.lopymine.patpat.packet.*;
import net.lopymine.patpat.utils.*;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.server.*;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.server.permission.PermissionAPI;

//? if >=1.20.5 {
/^import net.neoforged.neoforge.network.handling.IPayloadHandler;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
^///?} elif >=1.20.4 {
/^import net.neoforged.neoforge.network.event.RegisterPayloadHandlerEvent;
import net.neoforged.neoforge.network.registration.IPayloadRegistrar;
import net.neoforged.neoforge.network.registration.NetworkRegistry;
^///?} else {
import java.util.concurrent.atomic.AtomicInteger;
import net.neoforged.neoforge.network.NetworkEvent;
import net.neoforged.neoforge.network.NetworkRegistry;
import net.neoforged.neoforge.network.simple.SimpleChannel;
//?}

import net.neoforged.neoforge.server.permission.nodes.*;
import net.neoforged.neoforge.server.permission.nodes.PermissionTypes;
import net.neoforged.neoforge.server.permission.events.PermissionGatherEvent.Nodes;

//? if >=1.21.11 {
/^import net.minecraft.server.permissions.*;
import net.minecraft.server.permissions.Permission.HasCommandLevel;
^///?}

//? if >=1.21.9 {
/^import net.minecraft.server.players.NameAndId;
^///?} else {

import com.mojang.authlib.GameProfile;

//?}

@Getter
public class NeoForgeServerModLoader implements IServerModLoader {

	private final Map<String, PermissionNode<Boolean>> permissionNodes = new HashMap<>();
	private final List<PermissionNode<?>> registrationNodes = new ArrayList<>();

	private final Map<Identifier, PatPatClientPacketHandler<?>> clientHandlers = new HashMap<>();

	//? if <=1.20.3 {
	private NeoForgeChannelHandler neoForgeChannelHandler;
	//?}

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

	//? if <=1.21.6 {
	@SuppressWarnings("unchecked")
	//?}
	@Override
	public void registerServerPackets(Consumer<ServerPacketRegister> consumer) {
		//? if >=1.20.5 {
		/^NeoForgeCommonEntrypoint.getEventBus().addListener(RegisterPayloadHandlersEvent.class, (e) -> {
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

					//? if <=1.21.6 {
					IPayloadHandler<P> clientPayloadHandler = (packet, context) -> {
						PatPatClientPacketHandler<P> clientHandler = (PatPatClientPacketHandler<P>) NeoForgeServerModLoader.this.clientHandlers.get(packet.getPatPatType().getId());
						if (clientHandler == null) {
							return;
						}

						clientHandler.handle(packet);
					};
					//?}

					switch (registrationSide) {
						case C2S -> registrar.playToServer(type.getPacketId(), type.getCodec(), payloadHandler);
						case S2C -> registrar.playToClient(type.getPacketId(), type.getCodec()
								//? if <=1.21.6 {
								, clientPayloadHandler
								//?}
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
		^///?} elif >=1.20.4 {
		/^NeoForgeCommonEntrypoint.getEventBus().addListener(RegisterPayloadHandlerEvent.class, (e) -> {
			IPayloadRegistrar registrar = e.registrar(PatPat.MOD_ID).optional();

			ServerPacketRegister register = new ServerPacketRegister() {
				@Override
				public <P extends BasePatPatPacket<P>> void register(PatPatPacketType<P> type, PacketRegistrationSide registrationSide, PatPatServerPacketHandler<P> handler) {
					net.neoforged.neoforge.network.handling.IPlayPayloadHandler<P> payloadHandler = (packet, context) -> {
						Player player = context.player().orElse(null);
						if (!(player instanceof ServerPlayer serverPlayer)) {
							return;
						}
						if (packet instanceof PingPatPacket<?, ?> pingPacket) {
							pingPacket.setPacketReply(context.replyHandler()::send);
						}
						handler.handle(serverPlayer, packet);
					};

					net.neoforged.neoforge.network.handling.IPlayPayloadHandler<P> clientPayloadHandler = (packet, context) -> {
						PatPatClientPacketHandler<P> clientHandler = (PatPatClientPacketHandler<P>) NeoForgeServerModLoader.this.clientHandlers.get(packet.getPatPatType().getId());
						if (clientHandler == null) {
							return;
						}
						if (packet instanceof PingPatPacket<?, ?> pingPacket) {
							pingPacket.setPacketReply(context.replyHandler()::send);
						}
						clientHandler.handle(packet);
					};

					net.minecraft.network.FriendlyByteBuf.Reader<P> reader = type.getFactory()::apply;
					switch (registrationSide) {
						case C2S -> registrar.play(type.getId(), reader, payloadHandler);
						case S2C -> registrar.play(type.getId(), reader, (builder) -> builder.client(clientPayloadHandler));
						case BOTH -> registrar.play(type.getId(), reader, (builder) -> builder.server(payloadHandler).client(payloadHandler));
					}
				}
			};
			consumer.accept(register);
		});
		^///?} else {
		if (this.neoForgeChannelHandler == null) {
			this.neoForgeChannelHandler = new NeoForgeChannelHandler("1", "patpat_packets_channel");
		}

		SimpleChannel channel = this.neoForgeChannelHandler.channel;
		ServerPacketRegister register = new ServerPacketRegister() {
			@Override
			public <P extends BasePatPatPacket<P>> void register(PatPatPacketType<P> type, PacketRegistrationSide registrationSide, PatPatServerPacketHandler<P> handler) {
				net.neoforged.neoforge.network.simple.MessageFunctions.MessageConsumer<P> handle = (p, context) -> {
					context.enqueueWork(() -> {
						if (p instanceof PingPatPacket<?, ?> pingPacket) {
							pingPacket.setPacketReply((packet) -> channel.reply(packet, context));
						}
						if (registrationSide == PacketRegistrationSide.S2C || registrationSide == PacketRegistrationSide.BOTH) {
							PatPatClientPacketHandler<P> clientHandler = (PatPatClientPacketHandler<P>) NeoForgeServerModLoader.this.clientHandlers.get(p.getPatPatType().getId());
							if (clientHandler != null) {
								clientHandler.handle(p);
							}
						}
						if (registrationSide == PacketRegistrationSide.C2S || registrationSide == PacketRegistrationSide.BOTH) {
							ServerPlayer sender = context.getSender();
							if (sender != null) {
								handler.handle(sender, p);
							}
						}
					});
					context.setPacketHandled(true);
				};

				channel.registerMessage(NeoForgeServerModLoader.this.neoForgeChannelHandler.latestPacketId.getAndIncrement(), type.getClazz(), BasePatPatPacket::write, type.getFactory()::apply, handle);
			}
		};
		consumer.accept(register);
		//?}
	}

	@Override
	public void sendPacketToPlayer(ServerPlayer player, BasePatPatPacket<?> packet) {
		//? if >=1.20.5 {
		/^if (!player.connection.hasChannel(packet.getPatPatType().getId())) {
			return;
		}
		^///?} elif >=1.20.4 {
		/^if (!NetworkRegistry.getInstance().isConnected(player.connection, packet.getPatPatType().getId())) {
			return;
		}
		^///?} else {
		if (this.neoForgeChannelHandler == null) {
			return;
		}
		//?}
		if (packet instanceof PongPatPacket<?> pingPong && pingPong.canPong()) {
			pingPong.pong(packet);
		} else {
			//? if >=1.20.5 {
			/^PacketDistributor.sendToPlayer(player, packet);
			^///?} elif >=1.20.4 {
			/^PacketDistributor.PLAYER.with(player).send(packet);
			^///?} else {
			this.neoForgeChannelHandler.channel.send(PacketDistributor.PLAYER.with(() -> player), packet);
			//?}
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
						//? if >=1.21.11 {
						/^serverPlayer.permissions().hasPermission(new HasCommandLevel(PermissionLevel.GAMEMASTERS))
						^///?} else {
						serverPlayer.hasPermissions(2)
						//?}
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
			/^? if >=1.21.9 {^/ /^NameAndId ^//^?} else {^/ GameProfile /^?}^/ profile,
			MinecraftServer server,
			String permission
	) {
		return CompletableFuture.supplyAsync(() -> {
			PermissionNode<Boolean> node = this.permissionNodes.get(permission);
			if (node == null) {
				return false;
			}
			return PermissionAPI.getOfflinePermission(
					/^? if >=1.21.9 {^/ /^profile.id() ^//^?} else {^/ profile.getId() /^?}^/,
					node
			);
		});
	}

	//? if <=1.20.3 {
	@Getter
	public static class NeoForgeChannelHandler {

		private final SimpleChannel channel;
		private final AtomicInteger latestPacketId = new AtomicInteger(0);

		public NeoForgeChannelHandler(String packetVersion, String channelName) {
			this.channel = NetworkRegistry.newSimpleChannel(
					RLUtils.modId(channelName),
					() -> packetVersion,
					NetworkRegistry.acceptMissingOr(packetVersion),
					NetworkRegistry.acceptMissingOr(packetVersion)
			);
		}
	}
	//?}

}
*///?}
