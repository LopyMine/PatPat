package net.lopymine.patpat.client.config.sub;

import lombok.*;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.lopymine.patpat.client.config.resourcepack.ListMode;
import net.lopymine.patpat.client.packet.PatPatClientProxLibManager;
import net.lopymine.patpat.utils.*;
import net.minecraft.client.Minecraft;

import static net.lopymine.patpat.utils.CodecUtils.option;

@Getter
@Setter
@AllArgsConstructor
public class PatPatClientServerConfig {

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
		Minecraft client = Minecraft.getInstance();
		if (client == null) {
			return;
		}
		boolean bl = client.getResourcePackRepository()
				/*? <=1.20.4 {*//*.getEnabledNames()
				 *//*?} else {*/.getSelectedIds()/*?}*/
				.stream().anyMatch(s -> s.startsWith("server/"));

		if (bl) {
			client.reloadResourcePacks();
		}
	}

	public void setProxLibEnabled(boolean value){
		this.proxLibEnabled = value;
		PatPatClientProxLibManager.setEnabled(value);
	}
}
