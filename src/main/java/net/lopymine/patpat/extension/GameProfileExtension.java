package net.lopymine.patpat.extension;

import com.mojang.authlib.GameProfile;
import java.util.UUID;

public class GameProfileExtension {

	public static String getName(GameProfile profile) {
		return profile.name();
	}

	public static UUID getUUID(GameProfile profile) {
		return profile.id();
	}

	public static String getName(net.minecraft.server.players.NameAndId profile) {
		return profile.name();
	}

	public static UUID getUUID(net.minecraft.server.players.NameAndId profile) {
		return profile.id();
	}

}
