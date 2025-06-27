package net.lopymine.patpat.client.config;

import lombok.*;
import net.lopymine.patpat.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.*;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;

import net.lopymine.patpat.client.PatPatClient;
import net.lopymine.patpat.common.config.PatPatConfigManager;
import net.lopymine.patpat.utils.*;

import java.io.File;
import java.util.*;
import java.util.concurrent.*;

import static net.lopymine.patpat.utils.CodecUtils.option;

@Getter
@Setter
@AllArgsConstructor
public class PatPatStatsConfig {

	public static final Codec<PatPatStatsConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			option("totalPatsCounter", new PatsCounter(0), PatsCounter.CODEC, PatPatStatsConfig::getTotalPatsCounter),
			option("entities", new HashMap<>(), Codec.STRING, PatsCounter.CODEC, PatPatStatsConfig::getPatsPerEntity)
	).apply(instance, PatPatStatsConfig::new));

	private static final PatLogger LOGGER = PatPatClient.LOGGER.extend("StatsConfig");
	private static final File CONFIG_FILE = PatPatConfigManager.CONFIG_PATH.resolve(PatPat.MOD_ID + "-client-stats.json5").toFile();
	private static PatPatStatsConfig instance;

	private PatsCounter totalPatsCounter;
	private HashMap<String, PatsCounter> patsPerEntity;

	private PatPatStatsConfig() {
		throw new IllegalArgumentException();
	}

	public void count(LivingEntity pattedEntity) {
		this.totalPatsCounter.totalPats++;
		ResourceLocation id = VersionedThings.ENTITY_TYPE.getKey(pattedEntity.getType());
		if (id == null) {
			return;
		}
		String entityId = id.toString();
		PatsCounter patsCounter = this.patsPerEntity.get(entityId);
		if (patsCounter == null) {
			this.patsPerEntity.put(entityId, new PatsCounter(0));
		} else {
			patsCounter.totalPats++;
		}
		AutoSaveManager.markToSave();
	}

	public static void registerSaveHooks() {
		AutoSaveManager.start();
		ClientLifecycleEvents.CLIENT_STOPPING.register((client) -> {
			PatPatStatsConfig.getInstance().save();
		});
	}

	public static PatPatStatsConfig getInstance() {
		return instance == null ? reload() : instance;
	}

	public static PatPatStatsConfig reload() {
		instance = PatPatStatsConfig.read();
		return instance;
	}

	public static PatPatStatsConfig getNewInstance() {
		return CodecUtils.parseNewInstanceHacky(CODEC);
	}

	private static PatPatStatsConfig read() {
		return ConfigUtils.readConfig(CODEC, CONFIG_FILE, LOGGER);
	}

	public void saveAsync() {
		CompletableFuture.runAsync(this::save);
	}

	public void save() {
		ConfigUtils.saveConfig(this, CODEC, CONFIG_FILE, LOGGER);
	}

	@Getter
	@AllArgsConstructor
	private static class PatsCounter {

		public static final Codec<PatsCounter> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				option("pats", 0L, Codec.LONG, PatsCounter::getTotalPats)
		).apply(instance, PatsCounter::new));

		public long totalPats;
	}

	private static class AutoSaveManager {

		private static final ScheduledExecutorService SERVICE = Executors.newScheduledThreadPool(1);
		private static boolean shouldSave = true;

		private static void start() {
			Runnable runnable = () -> {
				if (!AutoSaveManager.shouldSave || !PatPatClientConfig.getInstance().getMainConfig().isModEnabled()) {
					return;
				}
				PatPatStatsConfig.getInstance().save();
				AutoSaveManager.shouldSave = false;
			};

			SERVICE.scheduleAtFixedRate(
					runnable,
					5,
					5,
					TimeUnit.MINUTES
			);
		}

		private static void markToSave() {
			AutoSaveManager.shouldSave = true;
		}
	}
}
