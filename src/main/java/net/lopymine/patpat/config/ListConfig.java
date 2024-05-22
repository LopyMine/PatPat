package net.lopymine.patpat.config;

import lombok.*;
import net.minecraft.util.Uuids;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.*;

@Getter
public class ListConfig {
	public static final Codec<ListConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.unboundedMap(Uuids.CODEC, Codec.STRING).xmap(HashMap::new, HashMap::new).fieldOf("players").forGetter(ListConfig::getPlayers),
			Codec.BOOL.fieldOf("enable").forGetter(ListConfig::isEnable)
	).apply(instance, ListConfig::new));

	private final HashMap<UUID, String> players;
	@Setter
	private boolean enable;

	public ListConfig(HashMap<UUID, String> players, boolean enable) {
		this.players = players;
		this.enable = enable;
	}

	public static ListConfig empty() {
		return new ListConfig(new HashMap<>(), false);
	}
}
