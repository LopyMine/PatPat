package net.lopymine.patpat.client.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.io.File;
import java.util.HashMap;
import java.util.concurrent.*;
import java.util.function.Supplier;
import lombok.*;
import net.lopymine.patpat.PatPat;
import net.lopymine.patpat.client.PatPatClient;
import net.lopymine.patpat.common.config.PatPatConfigManager;
import net.lopymine.patpat.entrypoint.ClientMultiLoader;
import net.lopymine.patpat.logger.PatLogger;
import net.lopymine.patpat.utils.*;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;
import static net.lopymine.patpat.utils.CodecUtils.option;

@Getter
@Setter
@AllArgsConstructor
public class PatPatClientStatsConfig {

	public static final Codec<PatPatClientStatsConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			option("totalPatsCounter", new PatsCounter(0), PatsCounter.CODEC, PatPatClientStatsConfig::getTotalPatsCounter),
			option("entities", new HashMap<>(), Codec.STRING, PatsCounter.CODEC, PatPatClientStatsConfig::getPatsPerEntity)
	).apply(instance, PatPatClientStatsConfig::new));

	private static final PatLogger LOGGER = PatPatClient.LOGGER.extend("StatsConfig");
	private static final File CONFIG_FILE = PatPatConfigManager.CONFIG_PATH.resolve(PatPat.MOD_ID + "-client-stats.json5").toFile();
	private static PatPatClientStatsConfig instance;

	private PatsCounter totalPatsCounter;
	private HashMap<String, PatsCounter> patsPerEntity;

	private PatPatClientStatsConfig() {
		throw new IllegalArgumentException();
	}

	public static void registerSaveHooks() {
		AutoSaveManager.start();
		ClientMultiLoader.getInstance().registerOnClientStop(() -> {
			PatPatClientStatsConfig.getInstance().save();
			AutoSaveManager.stop();
		});
	}

	private void stop() {

	}

	public static PatPatClientStatsConfig getInstance() {
		return instance == null ? reload() : instance;
	}

	public static PatPatClientStatsConfig reload() {
		instance = PatPatClientStatsConfig.read();
		return instance;
	}

	public static Supplier<PatPatClientStatsConfig> getNewInstance() {
		return () -> CodecUtils.parseNewInstanceHacky(CODEC);
	}

	private static PatPatClientStatsConfig read() {
		return ConfigUtils.readConfig(CODEC, CONFIG_FILE, LOGGER);
	}

	public void count(LivingEntity pattedEntity) {
		this.totalPatsCounter.totalPats++;
		Identifier id = VersionedThings.ENTITY_TYPE.getKey(pattedEntity.getType());
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

		private static final ScheduledExecutorService SERVICE = Executors.newScheduledThreadPool(1, (runnable) -> {
			Thread thread = new Thread(runnable);
			thread.setDaemon(true);
			thread.setName("PatPatClientStatsAutoSaveManager");
			return thread;
		});
		private static boolean shouldSave = true;

		private static void start() {
			Runnable runnable = () -> {
				if (!AutoSaveManager.shouldSave || !PatPatClientConfig.getInstance().getMainConfig().isModEnabled()) {
					return;
				}
				PatPatClientStatsConfig.getInstance().save();
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

		public static void stop() {
			SERVICE.shutdown();
		}
	}
}
