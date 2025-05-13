package net.lopymine.patpat.client.config.sub;

import lombok.*;
import net.minecraft.client.MinecraftClient;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.lopymine.patpat.client.config.resourcepack.ListMode;
import net.lopymine.patpat.client.packet.PatPatClientProxLibManager;
import net.lopymine.patpat.utils.*;

import java.util.*;

import static net.lopymine.patpat.utils.CodecUtils.option;

@Getter
@Setter
@AllArgsConstructor
public class PatPatClientServerConfig {

	// For tests, because you can't use it from VersionedThings in this case
	public static final Codec<UUID> UUID_CODEC =
			/*? >=1.19.3 {*/net.minecraft.util.Uuids
			/*?} else {*//*net.minecraft.util.dynamic.DynamicSerializableUuid*//*?}*/
			.CODEC;

	public static final Codec<PatPatClientServerConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			option("bypassServerResourcePackPriorityEnabled", false, Codec.BOOL, PatPatClientServerConfig::isBypassServerResourcePackPriorityEnabled),
			option("patMeEnabled", true, Codec.BOOL, PatPatClientServerConfig::isPatMeEnabled),
			option("listMode", ListMode.DISABLED, ListMode.CODEC, PatPatClientServerConfig::getListMode),
			option("proxLib", true, Codec.BOOL, PatPatClientServerConfig::isProxLibEnabled)
	).apply(instance, PatPatClientServerConfig::new));

	@Setter(value = AccessLevel.PRIVATE)
	private boolean bypassServerResourcePackPriorityEnabled;
	private boolean patMeEnabled;
	private ListMode listMode;
	private boolean proxLibEnabled;

	private PatPatClientServerConfig() {
		throw new IllegalArgumentException();
	}

	public static PatPatClientServerConfig getNewInstance() {
		return CodecUtils.parseNewInstanceHacky(CODEC);
	}

	public void setBypassServerResourcePackEnabled(boolean value) {
		this.bypassServerResourcePackPriorityEnabled = value;
		MinecraftClient client = MinecraftClient.getInstance();
		if (client == null) {
			return;
		}
		boolean bl = client.getResourcePackManager()
				/*? <=1.20.4 {*//*.getEnabledNames()
				 *//*?} else {*/.getEnabledIds()/*?}*/
				.stream().anyMatch(s -> s.startsWith("server/"));

		if (bl) {
			client.reloadResources();
		}
	}

	public void setProxLibEnabled(boolean value){
		this.proxLibEnabled = value;
		PatPatClientProxLibManager.setEnabled(value);
	}
}
