package net.lopymine.patpat.extension;

import com.google.gson.JsonElement;

import com.mojang.serialization.*;

//? if <1.20.5
import net.lopymine.patpat.PatPat;

public class GsonExtension {

	private GsonExtension() {
		throw new IllegalStateException("Extension class");
	}

	public static <A> JsonElement encode(Codec<A> codec, A config) {
		return codec.encode(config, JsonOps.INSTANCE, JsonOps.INSTANCE.empty())/*? if >=1.20.5 {*//*.getOrThrow();*//*?} else*/.getOrThrow(false, PatPat.LOGGER::error);
	}
}
