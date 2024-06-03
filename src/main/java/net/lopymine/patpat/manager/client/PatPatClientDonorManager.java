package net.lopymine.patpat.manager.client;

import com.google.gson.*;
import lombok.Getter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Uuids;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.datafixers.util.*;
import com.mojang.serialization.*;
import com.mojang.serialization.DataResult.PartialResult;

import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

import net.lopymine.patpat.client.PatPatClient;
import net.lopymine.patpat.utils.*;

import java.util.*;

@Getter
public class PatPatClientDonorManager {
	private static final PatPatClientDonorManager INSTANCE = new PatPatClientDonorManager();
	private static final Codec<Set<UUID>> CODEC = Uuids.CODEC.listOf().xmap(HashSet::new, ArrayList::new);
	// TODO
	//  Когда откроем гитхаб, нужно будет изменить ссылку в переменной, на эту:
	//  https://raw.githubusercontent.com/LopyMine/PatPat/dev/src/main/resources/donors/donors.json
	private static final String GITHUB_SOURCES_URL = "https://raw.githubusercontent.com/LopyMine/CollisionFix/master/src/main/resources/test.json";
	private static final String MOD_SOURCES_PATH = "donors/donors.json";

	private PatPatClientDonorManager() {
	}

	public static PatPatClientDonorManager getInstance() {
		return PatPatClientDonorManager.INSTANCE;
	}

	private Set<UUID> donors = new HashSet<>();

	public void loadDonors() {
		PatPatClient.LOGGER.info("Start loading donors...");
		JsonElement sources = GithubSourcesUtils.getSources(GITHUB_SOURCES_URL);
		if (sources == null) {
			PatPatClient.LOGGER.error("Failed to get donors from github, loading from mod sources");
			sources = ModSourcesUtils.getSources(MOD_SOURCES_PATH);
		}
		if (sources == null) {
			PatPatClient.LOGGER.error("Failed to get donors from mod sources");
			return;
		}
		Either<Pair<Set<UUID>, JsonElement>, PartialResult<Pair<Set<UUID>, JsonElement>>> donors1 = PatPatClientDonorManager.CODEC.decode(JsonOps.INSTANCE, sources).get();
		if (donors1.left().isEmpty()) {
			PatPatClient.LOGGER.error("Failed to parse donors json element");
			return;
		}
		this.donors = donors1.left().get().getFirst();
		PatPatClient.LOGGER.info("Donors successfully loaded");
	}

	public boolean isAmDonor() {
		UUID uuid = MinecraftClient.getInstance().getSession().getUuidOrNull();
		if (uuid == null) {
			return false;
		}
		return this.donors.contains(uuid);
	}
}
