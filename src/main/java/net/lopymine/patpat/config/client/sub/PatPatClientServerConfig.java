package net.lopymine.patpat.config.client.sub;

import lombok.*;
import net.minecraft.client.MinecraftClient;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.lopymine.patpat.config.client.PatPatClientConfig;
import net.lopymine.patpat.config.resourcepack.ListMode;
import net.lopymine.patpat.utils.*;

import java.util.*;

import static net.lopymine.patpat.utils.CodecUtils.option;

@Getter
@Setter
@AllArgsConstructor
public class PatPatClientServerConfig {

	public static final Codec<PatPatClientServerConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			option("bypassServerResourcePackPriorityEnabled", false, Codec.BOOL, PatPatClientServerConfig::isBypassServerResourcePackPriorityEnabled),
			option("patMeEnabled", true, Codec.BOOL, PatPatClientServerConfig::isPatMeEnabled),
			option("list", new HashMap<>(), VersionedThings.UUID_CODEC, Codec.STRING, PatPatClientServerConfig::getPlayers),
			option("listMode", ListMode.DISABLED, ListMode.CODEC, PatPatClientServerConfig::getListMode)
	).apply(instance, PatPatClientServerConfig::new));

	private boolean bypassServerResourcePackPriorityEnabled;
	private boolean patMeEnabled;
	private final HashMap<UUID, String> players;
	private ListMode listMode;

	private PatPatClientServerConfig() {
		throw new IllegalArgumentException();
	}

	public static PatPatClientServerConfig getNewInstance() {
		return CodecUtils.parseNewInstanceHacky(CODEC);
	}

	public void setBypassServerResourcePackEnabled(boolean value) {
		this.bypassServerResourcePackPriorityEnabled = value;
		MinecraftClient client = MinecraftClient.getInstance();
		boolean bl = client.getResourcePackManager()
				/*? <=1.20.4 {*//*.getEnabledNames()
				 *//*?} else {*/.getEnabledIds()/*?}*/
				.stream().anyMatch(s -> s.startsWith("server/"));

		if (bl) {
			client.reloadResources();
		}
	}
}
