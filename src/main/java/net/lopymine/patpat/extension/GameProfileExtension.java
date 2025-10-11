package net.lopymine.patpat.extension;

import com.mojang.authlib.GameProfile;
import java.util.UUID;

public class GameProfileExtension {

	public static String getName(GameProfile profile) {
		//? if >=1.21.9 {
		/*return profile.name();
		*///?} else {
		return profile.getName();
		 //?}
	}

	public static UUID getUUID(GameProfile profile) {
		//? if >=1.21.9 {
		/*return profile.id();
		*///?} else {
		return profile.getId();
		//?}
	}

	//? if >=1.21.9 {
	/*public static String getName(net.minecraft.server.players.NameAndId profile) {
		return profile.name();
	}

	public static UUID getUUID(net.minecraft.server.players.NameAndId profile) {
		return profile.id();
	}
	*///?}

}
